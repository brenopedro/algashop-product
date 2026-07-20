package com.algaworks.algashop.product.catalog.infrastructure.listener.product;

import com.algaworks.algashop.product.catalog.domain.model.product.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEventListener {

    @Async
    @EventListener(ProductPriceChangedEvent.class)
    public void handle(ProductPriceChangedEvent event) {
        log.info("Product price change event received" + event);
    }

    @Async
    @EventListener(ProductPlacedOnSaleEvent.class)
    public void handle(ProductPlacedOnSaleEvent event) {
        log.info("Product placed on Sale event received " + event);
    }

    @Async
    @EventListener(ProductAddedEvent.class)
    public void handle(ProductAddedEvent event) {
        log.info("Product added event received " + event);
    }

    @Async
    @EventListener(ProductListedEvent.class)
    public void handle(ProductListedEvent event) {
        log.info("Product listed event received " + event);
    }

    @Async
    @EventListener(ProductDelistedEvent.class)
    public void handle(ProductDelistedEvent event) {
        log.info("Product delisted event received " + event);
    }

    @Async
    @EventListener(ProductRestockedEvent.class)
    public void handle(ProductRestockedEvent event) {
        log.info("Product restocked event received " + event);
    }

    @Async
    @EventListener(ProductSoldOutEvent.class)
    public void handle(ProductSoldOutEvent event) {
        log.info("Product soldOut event received " + event);
    }

}
