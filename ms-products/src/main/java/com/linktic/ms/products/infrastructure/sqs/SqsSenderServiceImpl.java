package com.linktic.ms.products.infrastructure.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linktic.ms.products.application.event.ProductCreatedEvent;
import com.linktic.ms.products.application.port.out.ISqsSenderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Log4j2
@Service
@RequiredArgsConstructor
public class SqsSenderServiceImpl implements ISqsSenderService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${aws.sqs.product-created-queue-url}")
    private String queueUrl;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.sqs.delete-queue-url}")
    private String queueUrlProductDeleted;

    private SqsClient sqsClient;

    @PostConstruct
    public void init() {
        log.info("Initializing SQS Client...");
        this.sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
        log.info("SQS Client initialized successfully with region: {}", region);
    }

    @Override
    public void sendProductCreatedEvent(ProductCreatedEvent event) {
        try {
            log.info("Preparing to send ProductCreatedEvent to queue: {}", queueUrl);
            String message = objectMapper.writeValueAsString(event);

            log.info("Serialized message: {}", message);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();

            log.info("Sending message to SQS: {}", request);

            sqsClient.sendMessage(request);
            log.info("ProductCreatedEvent sent successfully for productId: {}", event.getProductId());
        }  catch (Exception e) {
            throw new RuntimeException("Exception when try to send message SQS create product ", e);
        }
    }

    @Override
    public void sendProductDeleteEvent(long productId) {
        try {
            log.info("Preparing to send ProductDeletedEvent to queue: {}", queueUrlProductDeleted);
            String message = String.valueOf(productId);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrlProductDeleted)
                    .messageBody(message)
                    .build();

            sqsClient.sendMessage(request);
            log.info("ProductDeletedEvent sent successfully for productId: {}", productId);
        } catch (Exception e) {
            log.error("Unexpected error sending ProductDeletedEvent: {}", e.getMessage(), e);
            throw new RuntimeException("Error enviando evento de eliminación de producto", e);
        }
    }
}