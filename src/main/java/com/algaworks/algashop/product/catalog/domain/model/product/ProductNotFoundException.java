package com.algaworks.algashop.product.catalog.domain.model.product;

import java.util.UUID;

import com.algaworks.algashop.product.catalog.domain.model.DomainEntityNotFoundException;

public class ProductNotFoundException extends DomainEntityNotFoundException {

  public ProductNotFoundException(UUID productId) {
    super(String.format("Product with id %s was not found", productId));
  }
}
