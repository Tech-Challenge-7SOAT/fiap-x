package com.fiapx.video.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class SqsConfig {

    @Value("\${spring.cloud.aws.sqs.endpoint:defaultEndpoint}")
    private lateinit var endpoint: String

    @Value("\${spring.cloud.aws.credentials.access-key}")
    private lateinit var accessKey: String

    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private lateinit var secretKey: String

    @Bean
    fun sqsClient(): SqsClient = SqsClient.builder()
        .overrideConfiguration(ClientOverrideConfiguration.builder().build())
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .endpointOverride(URI.create(endpoint))
        .region(Region.of("\${spring.cloud.aws.region.static}"))
        .build()

    @Bean
    fun messageConverter(): MappingJackson2MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.isStrictContentTypeMatch = false
        converter.serializedPayloadClass = String::class.java

        return converter
    }
}
