package com.fiapx.video.config

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI
import java.time.Duration
import java.time.Duration.*

@Configuration
class SqsConfig {

    @Value("\${spring.cloud.aws.sqs.endpoint:defaultEndpoint}")
    private lateinit var endpoint: String

    @Value("\${spring.cloud.aws.credentials.access-key}")
    private lateinit var accessKey: String

    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private lateinit var secretKey: String

    @Value("\${spring.cloud.aws.region.static}")
    private lateinit var region: String

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient = SqsAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .endpointOverride(URI.create(endpoint))
        .region(Region.of(region))
        .build()

    @Bean
    fun defaultSqsListenerContainerFactory(sqsAsyncClient: SqsAsyncClient): SqsMessageListenerContainerFactory<Any> {
        return SqsMessageListenerContainerFactory.builder<Any>()
            .configure {
                it.acknowledgementMode(AcknowledgementMode.ALWAYS)
                it.pollTimeout(ofMinutes(1))
            }
            .sqsAsyncClient(sqsAsyncClient)
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
