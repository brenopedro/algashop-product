package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Document(collection = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private String brand;
    private String name;
    private String description;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    private Integer quantityInStock;
    private Boolean enabled;

    @Version
    private Long version;

    @CreatedDate
    private OffsetDateTime addedAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @CreatedBy
    private UUID createdByUserId;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Builder
    public Product(String brand, String name, String description, BigDecimal regularPrice,
            BigDecimal salePrice, Boolean enabled) {
        this.id = IdGenerator.generateTimeBasedUUID();
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.regularPrice = regularPrice;
        this.salePrice = salePrice;
        this.enabled = enabled;
    }
}
