package com.algaworks.algashop.product.catalog.contract.base;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.product.management.ProductInput;
import com.algaworks.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutputTestDataBuilder;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.presentation.ProductController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@WebMvcTest(controllers = ProductController.class)
@ExtendWith(RestDocumentationExtension.class)
class ProductBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ProductQueryService productQueryService;

    @MockitoBean
    private ProductManagementApplicationService productManagementApplicationService;

    private static final UUID VALID_PRODUCT_ID = UUID.fromString("fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f");
    private static final UUID INVALID_PRODUCT_ID = UUID.fromString("2165e8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f");

    @BeforeEach
    void setUp(RestDocumentationContextProvider documentationContextProvider) {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(documentationContextProvider)
                        .snippets().withTemplateFormat(TemplateFormats.asciidoctor())
                        .and().operationPreprocessors()
                        .withResponseDefaults(Preprocessors.prettyPrint()))
                .alwaysDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockValidProductFindById();
        mockFilterProducts();
        mockCreateProduct();
        mockInvalidProductFindById();
    }

    private void mockInvalidProductFindById() {
        when(productQueryService.findById(INVALID_PRODUCT_ID))
                .thenThrow(new ResourceNotFoundException());
    }

    private void mockCreateProduct() {
        when(productManagementApplicationService.create(any(ProductInput.class)))
                .thenReturn(VALID_PRODUCT_ID);
    }

    private void mockFilterProducts() {
        when(productQueryService.filter(anyInt(), anyInt()))
                .then(answer -> {
                    Integer size = answer.getArgument(0);
                    return PageModel.<ProductDetailOutput>builder()
                            .number(0)
                            .size(size)
                            .totalPages(1)
                            .totalElements(2)
                            .content(List.of(
                                            ProductDetailOutputTestDataBuilder.aProduct().build(),
                                            ProductDetailOutputTestDataBuilder.aProductAlt().build()
                                    )
                            ).build();
                });
    }

    private void mockValidProductFindById() {
        when(productQueryService.findById(VALID_PRODUCT_ID))
                .thenReturn(ProductDetailOutputTestDataBuilder.aProduct()
                        .id(VALID_PRODUCT_ID)
                        .build());
    }
}
