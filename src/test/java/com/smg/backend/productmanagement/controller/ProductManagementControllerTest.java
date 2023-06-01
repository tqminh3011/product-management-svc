package com.smg.backend.productmanagement.controller;

import com.smg.backend.productmanagement.messaging.Event;
import com.smg.backend.productmanagement.messaging.EventProducer;
import com.smg.backend.productmanagement.repository.ProductRepository;
import com.smg.backend.productmanagement.repository.ProductTrackingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.converter.ConversionException;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Import(TestChannelBinderConfiguration.class)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductManagementControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTrackingRepository productTrackingRepository;

    @Autowired
    private OutputDestination outputDestination;

    @SpyBean
    private EventProducer eventProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        productRepository.deleteAll().block();
        productTrackingRepository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll().block();
        productTrackingRepository.deleteAll().block();
    }

    @Test
    void When_createProduct_Given_Valid_Payload_Then_Return_200_With_New_Product() throws IOException {
        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        """
                                 {
                                   "name": "Denim Jeans - XL",
                                   "productOwner": "owner1",
                                   "type": "FASHION",
                                   "price": 10000.99
                                 }
                                """
                ))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Denim Jeans - XL")
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.createdDate").isNotEmpty()
                .jsonPath("$.price").isEqualTo("10000.99")
                .jsonPath("$.type").isEqualTo("FASHION")
                ;

        // Verify product persistence
        assertEquals(1, productRepository.count().block());

        // Verify messaging payload
        Message<byte[]> message = outputDestination.receive(30, "test-product-events");
        assertNotNull(message);
        Event actualEvent = objectMapper.readValue(message.getPayload(), Event.class);

        assertFalse(actualEvent.getId().isBlank());
        assertNotNull(actualEvent.getTime());
        assertEquals("1", actualEvent.getVersion());
        assertEquals("product-management-svc", actualEvent.getSource());
        LinkedHashMap<String, Object> actualEventDetail = (LinkedHashMap) actualEvent.getDetail();
        assertEquals("Denim Jeans - XL", actualEventDetail.get("name"));
        assertEquals(10000.99, actualEventDetail.get("price"));
    }

    @Test
    void When_createProduct_Given_Invalid_Owner_Then_Return_400() {
        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        """
                                 {
                                   "name": "Denim Jeans - XL",
                                   "productOwner": "owner-invalid",
                                   "type": "FASHION",
                                   "price": 10000.99
                                 }
                                """
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("""
                                 {
                                   "errors": [
                                       {
                                            "errorCode": "301102",
                                            "errorMessage": "Invalid Product Owner"
                                       }
                                   ]
                                 }
                                """
                )
        ;

        assertEquals(0, productRepository.count().block());
    }

    @Test
    void When_createProduct_Given_Missing_Name_Then_Return_400() {
        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        """
                                 {
                                   "productOwner": "owner-invalid",
                                   "type": "FASHION",
                                   "price": 10000.99
                                 }
                                """
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("""
                                 {
                                   "errors": [
                                       {
                                            "errorCode": "301101",
                                            "errorMessage": "Invalid Parameter"
                                       }
                                   ]
                                 }
                                """
                )
        ;

        assertEquals(0, productRepository.count().block());
    }

    @Test
    void When_createProduct_Given_Invalid_Price_Then_Return_400() {
        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        """
                                 {
                                   "name": "Denim Jeans - XL",
                                   "productOwner": "owner-invalid",
                                   "type": "FASHION",
                                   "price": 0.00
                                 }
                                """
                ))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .json("""
                                 {
                                   "errors": [
                                       {
                                            "errorCode": "301101",
                                            "errorMessage": "Invalid Parameter"
                                       }
                                   ]
                                 }
                                """
                )
        ;

        assertEquals(0, productRepository.count().block());
    }

    @Test
    void When_createProduct_Given_Valid_Payload_But_PublishMessage_Failed_Then_Return_500_And_Should_Rollback_Transaction() {
        doThrow(new ConversionException("test")).when(eventProducer).publishMessage(anyString(), any());

        webTestClient.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                        """
                                 {
                                   "name": "Denim Jeans - XL",
                                   "productOwner": "owner1",
                                   "type": "FASHION",
                                   "price": 10000.99
                                 }
                                """
                ))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .json("""
                                 {
                                   "errors": [
                                       {
                                            "errorCode": "301100",
                                            "errorMessage": "Internal Server Error"
                                       }
                                   ]
                                 }
                                """
                )
        ;

        assertEquals(0, productRepository.count().block());
    }
}