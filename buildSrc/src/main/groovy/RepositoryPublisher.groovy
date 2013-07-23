/*
 * Cloud Foundry Java Buildpack Support
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class RepositoryPublisher extends DefaultTask {

	String repositoryRoot

	@TaskAction
	void publish() {
		validate()

		def file = getFile()

		def amazonS3 = getAmazonS3()
		def (bucket, rootPath) = getRepositoryParts()
		def artifactKey = getArtifactKey(rootPath, file)

		publishArtifact(amazonS3, bucket, artifactKey, file)
		updateIndex(amazonS3, bucket, rootPath, artifactKey)
	}

	def getAmazonS3() {
		AWSCredentials credentials = new BasicAWSCredentials(project.s3AccessKey, project.s3SecretKey)
		return new AmazonS3Client(credentials)
	}

	def getArtifactKey(rootPath, file) {
		return "${rootPath}${file.name}"
	}

	def getExistingIndex(amazonS3, bucket, indexKey) {
		def yaml = getYaml()

		def index
		try {
			index = getYaml().load(amazonS3.getObject(bucket, indexKey).objectContent)
		} catch (AmazonServiceException e) {
			if (!e.getErrorCode().equals("NoSuchKey")) {
				throw e;
			}

			index = [:]
		}

		return index
	}

	def getFile() {
		return project.jar.outputs.files.singleFile
	}

	def getIndexKey(rootPath) {
		return "${rootPath}index.yml"
	}

	def getRepositoryParts() {
		def uri = URI.create(this.repositoryRoot)
		return [uri.host, getRootPath(uri)]
	}

	def getRootPath(uri) {
		def sb = new StringBuilder(uri.path).deleteCharAt(0);

		if ((sb.length() != 0) && (sb.charAt(sb.length() - 1) != '/')) {
			sb.append('/');
		}

		return sb.toString();
	}

	def getUri(bucket, artifactKey) {
		return "http://${bucket}.s3.amazonaws.com/${artifactKey}".toString()
	}

	def getVersion() {
		return project.version
	}

	def getYaml() {
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		return new Yaml(options)
	}

	def publishArtifact(amazonS3, bucket, artifactKey, file) {
		amazonS3.putObject(bucket, artifactKey, file)
	}

	def publishIndex(amazonS3, bucket, indexKey, index) {
		def content = getYaml().dump(index).getBytes("UTF-8")

		def metadata = new ObjectMetadata()
		metadata.contentLength = content.length

		amazonS3.putObject(bucket, indexKey, new ByteArrayInputStream(content), metadata)
	}

	def updateIndex(amazonS3, bucket, rootPath, artifactKey) {
		def indexKey = getIndexKey(rootPath)

		def index = getExistingIndex(amazonS3, bucket, indexKey)
		index[getVersion()] = getUri(bucket, artifactKey)
		publishIndex(amazonS3, bucket, indexKey, index)
	}

	def validate() {
		if (!project.hasProperty("s3AccessKey")) {
			throw new InvalidUserDataException("The property 's3AccessKey' must be defined")
		}

		if (!project.hasProperty("s3SecretKey")) {
			throw new InvalidUserDataException("The property 's3SecretKey' must be defined")
		}
	}
}
