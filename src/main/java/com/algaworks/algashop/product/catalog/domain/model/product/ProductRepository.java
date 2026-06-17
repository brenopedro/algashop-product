
package com.algaworks.algashop.product.catalog.domain.model.product;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, UUID> {

}
