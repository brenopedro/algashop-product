package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Document(collection = "products")
@CompoundIndex(name = "idx_product_by_category_enabledTrue_salePrice", def = "{'category.id': 1, 'salePrice': 1}", partialFilter = "{'enabled': true}")
@CompoundIndex(name = "idx_product_by_category_enabledTrue_addedAt", def = "{'category.id': 1, 'addedAt': -1}", partialFilter = "{'enabled': true}")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Indexed(name = "idx_product_by_brand")
    private String brand;

    @TextIndexed(weight = 1)
    private String name;

    @TextIndexed(weight = 5)
    private String description;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    private Integer quantityInStock = 0;
    private Boolean enabled;

    private ProductCategory category;

    private Integer discountPercentageRounded;

    @TextScore
    private Float score;

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
            BigDecimal salePrice, Boolean enabled, Category category) {
        this.setId(IdGenerator.generateTimeBasedUUID());
        this.setBrand(brand);
        this.setName(name);
        this.setDescription(description);
        this.setRegularPrice(regularPrice);
        this.setSalePrice(salePrice);
        this.setEnabled(enabled);
        this.setCategory(category);
    }

    public void setName(String name) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException();
        this.name = name;
    }

    public void setBrand(String brand) {
        if (StringUtils.isBlank(brand))
            throw new IllegalArgumentException();
        this.brand = brand;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegularPrice(BigDecimal regularPrice) {
        Objects.requireNonNull(regularPrice);
        if (regularPrice.signum() == -1)
            throw new IllegalArgumentException();

        if (this.salePrice == null)
            this.salePrice = regularPrice;
        else if (regularPrice.compareTo(this.salePrice) < 0)
            throw new DomainException("Sale price cannot be greater than regular price");

        this.regularPrice = regularPrice;
        this.calculateDiscountPercentage();
    }

    public void setSalePrice(BigDecimal salePrice) {
        Objects.requireNonNull(salePrice);
        if (regularPrice.signum() == -1)
            throw new IllegalArgumentException();

        if (this.regularPrice == null)
            this.regularPrice = salePrice;
        else if (this.regularPrice.compareTo(salePrice) < 0)
            throw new DomainException("Sale price cannot be greater than regular price");

        this.salePrice = salePrice;
        this.calculateDiscountPercentage();
    }

    public void setEnabled(Boolean enabled) {
        Objects.requireNonNull(enabled);
        this.enabled = enabled;
    }

    public void setCategory(Category category) {
        Objects.requireNonNull(category);
        this.category = ProductCategory.of(category);
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    public boolean isInStock() {
        return this.getQuantityInStock() != null && this.getQuantityInStock() > 0;
    }

    public boolean getHasDiscount() {
        return getDiscountPercentageRounded() != null && this.getDiscountPercentageRounded() > 0;
    }

    private void setId(UUID id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setQuantityInStock(Integer quantityInStock) {
        Objects.requireNonNull(quantityInStock);
        if (quantityInStock < 0)
            throw new IllegalArgumentException();
        this.quantityInStock = quantityInStock;

    }

    private void calculateDiscountPercentage() {
        if (regularPrice == null || salePrice == null || regularPrice.signum() == 0) {
            discountPercentageRounded = 0;
            return;
        }

        discountPercentageRounded = BigDecimal.ONE
                .subtract(salePrice.divide(regularPrice, 4, RoundingMode.HALF_UP))
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
    }
}
