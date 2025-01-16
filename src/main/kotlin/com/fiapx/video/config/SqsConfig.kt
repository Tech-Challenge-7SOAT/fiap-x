package com.fiapx.video.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class SqsConfig {

    @Value("\${spring.cloud.aws.sqs.endpoint:defaultEndpoint}")
    private lateinit var endpoint: String

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.US_EAST_1)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build()
    }

    @Bean
    fun messageConverter(): MappingJackson2MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.isStrictContentTypeMatch = false
        converter.serializedPayloadClass = String::class.java

        return converter
    }
}
