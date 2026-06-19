package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import java.util.UUID;

import com.algaworks.algashop.product.catalog.application.product.query.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.algaworks.algashop.product.catalog.application.utility.Mapper;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return mapper.convert(product, ProductDetailOutput.class);
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(ProductFilter filter) {
        Page<Product> products = productRepository.findAll(PageRequest.of(filter.getPage(), filter.getSize()));
        Page<ProductSummaryOutput> productOutputs = products.map(p -> mapper.convert(p, ProductSummaryOutput.class));
        return PageModel.of(productOutputs);
    }
}
