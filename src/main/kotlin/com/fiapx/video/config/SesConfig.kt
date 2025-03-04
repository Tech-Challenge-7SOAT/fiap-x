package com.fiapx.video.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesAsyncClient
import java.net.URI

@Configuration
class SesConfig {

    @Value("\${spring.cloud.aws.sqs.endpoint:defaultEndpoint}")
    private lateinit var endpoint: String

    @Value("\${spring.cloud.aws.credentials.access-key}")
    private lateinit var accessKey: String

    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private lateinit var secretKey: String

    @Value("\${spring.cloud.aws.region.static}")
    private lateinit var region: String

    @Bean
    fun sesAsyncClient(): SesAsyncClient = SesAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .endpointOverride(URI.create(endpoint))
        .region(Region.of(region))
        .build()
}
