package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.TestContainerMongoDBConfig;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.MongoConfig;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoadProperties;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoader;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.product.QuantityInStockAdjustmentMongoDBImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Import({
        MongoConfig.class,
        QuantityInStockAdjustmentMongoDBImpl.class,
        DataLoader.class,
        DataLoadProperties.class,
        TestContainerMongoDBConfig.class
})
@DataMongoTest
class QuantityInStockAdjustmentIT {

    @Autowired
    private QuantityInStockAdjustment quantityInStockAdjustment;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DataLoader dataLoader;

    private static final UUID existingProduct = UUID.fromString("946cea3b-d11d-4f11-b88d-3089b4e74087");

    @BeforeEach
    void setUp() throws Exception {
        dataLoader.run(new DefaultApplicationArguments());
    }

    @Test
    void shouldIncreaseQuantity() {
        Product product = productRepository.findById(existingProduct).orElseThrow();

        quantityInStockAdjustment.increase(existingProduct, 25);
        quantityInStockAdjustment.increase(existingProduct, 25);

        Product productUpdated = productRepository.findById(existingProduct).orElseThrow();

        assertThat(product.getQuantityInStock()).isEqualTo(50);
        assertThat(productUpdated.getQuantityInStock()).isEqualTo(100);
    }

    @Test
    void shouldDecreaseQuantity() {
        Product product = productRepository.findById(existingProduct).orElseThrow();

        quantityInStockAdjustment.decrease(existingProduct, 25);
        quantityInStockAdjustment.decrease(existingProduct, 25);

        Product productUpdated = productRepository.findById(existingProduct).orElseThrow();

        assertThat(product.getQuantityInStock()).isEqualTo(50);
        assertThat(productUpdated.getQuantityInStock()).isEqualTo(0);
    }

    @Test
    void shouldNotDecreaseQuantity() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> quantityInStockAdjustment.decrease(existingProduct, 100));

        Product product = productRepository.findById(existingProduct).orElseThrow();

        assertThat(product.getQuantityInStock()).isEqualTo(50);
    }

    @Test
    void shouldCalculateResult() {

        Product product = productRepository.findById(existingProduct).orElseThrow();

        QuantityInStockAdjustment.Result result = quantityInStockAdjustment.decrease(existingProduct, 25);

        Product productUpdated = productRepository.findById(existingProduct).orElseThrow();

        assertThat(result.newQuantity()).isEqualTo(25);
        assertThat(result.previousQuantity()).isEqualTo(50);
    }
}