package com.algaworks.algashop.product.catalog.domain.model.category;

import java.util.UUID;

import com.algaworks.algashop.product.catalog.domain.model.DomainEntityNotFoundException;

public class CategoryNotFoundException extends DomainEntityNotFoundException {

  public CategoryNotFoundException(UUID categoryId) {
    super(String.format("Category with id %s was not found", categoryId));
  }
}
