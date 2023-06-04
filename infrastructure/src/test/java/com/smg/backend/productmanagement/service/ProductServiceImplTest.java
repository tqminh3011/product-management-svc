package com.smg.backend.productmanagement.service;

import com.smg.backend.productmanagement.core.enums.ProductActivity;
import com.smg.backend.productmanagement.core.enums.ProductType;
import com.smg.backend.productmanagement.core.model.CreateProductRequest;
import com.smg.backend.productmanagement.core.model.Product;
import com.smg.backend.productmanagement.core.model.ProductOwner;
import com.smg.backend.productmanagement.messaging.Event;
import com.smg.backend.productmanagement.messaging.EventProducer;
import com.smg.backend.productmanagement.repository.ProductOwnerRepository;
import com.smg.backend.productmanagement.repository.ProductRepository;
import com.smg.backend.productmanagement.repository.entity.ProductEntity;
import io.r2dbc.pool.ConnectionPoolException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.converter.ConversionException;
import org.springframework.messaging.Message;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductOwnerRepository productOwnerRepository;

    @Mock
    private EventProducer eventProducer;

    @Mock
    private ProductTrackingService productTrackingService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<ProductEntity> productEntityArgCaptor;

    @Captor
    private ArgumentCaptor<Message<Event<Object>>> messageArgCaptor;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");

    @Test
    void When_createProduct_Given_SaveProduct_And_PublishMessage_Success_Then_Return_Product() {
        // Mock dependencies
        ProductEntity mockProductEntity = mockProductEntity();
        when(productRepository.save(any())).thenReturn(Mono.just(mockProductEntity));

        // Execute test method
        CreateProductRequest request = new CreateProductRequest(
                "Denim Jacket - XL",
                "owner1",
                ProductType.FASHION,
                BigDecimal.valueOf(10000.99)
        );
        ProductOwner owner = new ProductOwner("1", "owner1");
        Mono<Product> result = productService.createProduct(request, owner);

        // Verify actual compared to expected
        StepVerifier.create(result)
                .consumeNextWith(actualProduct -> {

                    // Verify actual result production
                    assertEquals("b843fcc5-7331-41b9-8bcb-1e98681fb2f0", actualProduct.id());
                    assertEquals("Denim Jacket - XL", actualProduct.name());
                    assertEquals("owner1", actualProduct.productOwner());
                    assertEquals(ProductType.FASHION, actualProduct.type());
                    assertEquals(BigDecimal.valueOf(10000.99), actualProduct.price());
                    assertEquals("2023-06-04 01:02:18.259", actualProduct.createdDate().format(dateFormatter));
                    assertEquals("2023-06-04 01:02:18.259", actualProduct.updatedDate().format(dateFormatter));

                    // Verify product persistence invocation
                    verify(productRepository).save(productEntityArgCaptor.capture());
                    ProductEntity actualDBPayload = productEntityArgCaptor.getValue();
                    assertEquals(1L, actualDBPayload.getProductOwnerId());
                    assertEquals("Denim Jacket - XL", actualDBPayload.getName());
                    assertEquals(ProductType.FASHION, actualDBPayload.getType());
                    assertEquals(BigDecimal.valueOf(10000.99), actualDBPayload.getPrice());

                    // Verify actual messaging invocation
                    verify(eventProducer).publishMessage(
                            eq("product-events-channel-out-0"),
                            messageArgCaptor.capture()
                    );
                    Event<Object> actualEvent = messageArgCaptor.getValue().getPayload();
                    assertEquals("1", actualEvent.getVersion());
                    assertEquals("product-management-svc", actualEvent.getSource());
                    assertEquals(actualProduct, actualEvent.getDetail());

                    // Verify product tracking invocation
                    verify(productTrackingService).asyncTrackProductSuccess(
                            "b843fcc5-7331-41b9-8bcb-1e98681fb2f0",
                            "1",
                            ProductActivity.CREATE
                    );

                })
                .verifyComplete();
    }

    @Test
    void When_createProduct_Given_SaveProduct_Failed_Then_ThrowError() {
        // Mock dependencies
        when(productRepository.save(any())).thenReturn(Mono.error(new ConnectionPoolException("test")));

        // Execute test method
        CreateProductRequest request = new CreateProductRequest(
                "Denim Jacket - XL",
                "owner1",
                ProductType.FASHION,
                BigDecimal.valueOf(10000.99)
        );
        ProductOwner owner = new ProductOwner("1", "owner1");
        Mono<Product> result = productService.createProduct(request, owner);

        // Verify actual compared to expected
        StepVerifier.create(result)
                .consumeErrorWith(error -> {
                    assertEquals(ConnectionPoolException.class ,error.getClass());
                    assertEquals("test" ,error.getMessage());

                    // Verify fail-fast logic therefore no further invocation
                    verifyNoInteractions(eventProducer);

                    // Verify product tracking invocation
                    verify(productTrackingService).asyncTrackProductFailure(
                            "1",
                            ProductActivity.CREATE,
                            error
                    );

                })
                .verify();
    }

    @Test
    void When_createProduct_Given_PublishMessage_Failed_Then_ThrowError() {
        // Mock dependencies
        ProductEntity mockProductEntity = mockProductEntity();
        when(productRepository.save(any())).thenReturn(Mono.just(mockProductEntity));
        doThrow(new ConversionException("test")).when(eventProducer).publishMessage(anyString(), any());

        // Execute test method
        CreateProductRequest request = new CreateProductRequest(
                "Denim Jacket - XL",
                "owner1",
                ProductType.FASHION,
                BigDecimal.valueOf(10000.99)
        );
        ProductOwner owner = new ProductOwner("1", "owner1");
        Mono<Product> result = productService.createProduct(request, owner);

        // Verify actual compared to expected
        StepVerifier.create(result)
                .consumeErrorWith(error -> {
                    assertEquals(ConversionException.class ,error.getClass());
                    assertEquals("test" ,error.getMessage());

                    // Verify product tracking invocation
                    verify(productTrackingService).asyncTrackProductFailure(
                            "1",
                            ProductActivity.CREATE,
                            error
                    );

                })
                .verify();
    }

    @Test
    void getProductOwner() {
    }

    private ProductEntity mockProductEntity() {
        return ProductEntity.builder()
                .productOwnerId(1L)
                .externalId("b843fcc5-7331-41b9-8bcb-1e98681fb2f0")
                .name("Denim Jacket - XL")
                .type(ProductType.FASHION)
                .price(BigDecimal.valueOf(10000.99))
                .createdDate(LocalDateTime.parse("2023-06-04 01:02:18.259", dateFormatter))
                .updatedDate(LocalDateTime.parse("2023-06-04 01:02:18.259", dateFormatter))
                .build();
    }
}