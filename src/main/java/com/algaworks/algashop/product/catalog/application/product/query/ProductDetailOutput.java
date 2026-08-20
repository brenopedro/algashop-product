package com.algaworks.algashop.product.catalog.application.product.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailOutput implements Serializable {

    private UUID id;
    private OffsetDateTime addedAt;
    private String name;
    private String brand;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    private Boolean inStock;
    private Boolean enabled;
    private UUID categoryId;
    private CategoryMinimalOutput category;
    private String description;

    private String slug;
    private Boolean hasDiscount;
    private Integer quantityInStock;
    private Integer discountPercentageRounded;

    private ImageOutput mainImage;

    private Long version;
    private OffsetDateTime updatedAt;
}
