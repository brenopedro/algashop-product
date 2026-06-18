package com.algaworks.algashop.product.catalog.domain.model.product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        this.setId(IdGenerator.generateTimeBasedUUID());
        this.setBrand(brand);
        this.setName(name);
        this.setDescription(description);
        this.setRegularPrice(regularPrice);
        this.setSalePrice(salePrice);
        this.setEnabled(enabled);
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
    }

    public void setEnabled(Boolean enabled) {
        Objects.requireNonNull(enabled);
        this.enabled = enabled;
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
}
