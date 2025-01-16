package com.fiapx.video.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class S3Config {

    @Value("\${spring.cloud.aws.s3.endpoint}")
    private lateinit var endpoint: String

    @Bean
    fun s3Client(): S3Client = S3Client.builder()
        .overrideConfiguration(ClientOverrideConfiguration.builder().build())
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
        .endpointOverride(URI.create(endpoint))
        .region(Region.US_EAST_1)
        .forcePathStyle(true)
        .build()
}
