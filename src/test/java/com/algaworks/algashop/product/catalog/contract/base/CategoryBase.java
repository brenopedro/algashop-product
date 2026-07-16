package com.algaworks.algashop.product.catalog.contract.base;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.category.management.CategoryInput;
import com.algaworks.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryFilter;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryOutputTestDataBuilder;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.presentation.CategoryController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CategoryQueryService categoryQueryService;

    @MockitoBean
    private CategoryManagementApplicationService categoryManagementApplicationService;

    private static final UUID VALID_CATEGORY_ID = UUID.fromString("fffee8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f");
    private static final UUID INVALID_CATEGORY_ID = UUID.fromString("2165e8b1-9c3a-4c5b-8f1e-2d9a7b6c8e9f");

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build());

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockValidCategoryFindById();
        mockCreateCategory();
        mockInvalidCategoryFindById();
        mockFilterCategories();
    }

    private void mockValidCategoryFindById() {
        when(categoryQueryService.findById(VALID_CATEGORY_ID))
                .thenReturn(CategoryOutputTestDataBuilder.aCategory()
                        .id(VALID_CATEGORY_ID)
                        .build());
    }

    private void mockCreateCategory() {
        when(categoryManagementApplicationService.create(any(CategoryInput.class)))
                .thenReturn(VALID_CATEGORY_ID);
    }

    private void mockInvalidCategoryFindById() {
        when(categoryQueryService.findById(INVALID_CATEGORY_ID))
                .thenThrow(new ResourceNotFoundException());
    }

    private void mockFilterCategories() {
        when(categoryQueryService.filter(any()))
                .then(answer -> {
                    CategoryFilter filter = answer.getArgument(0);
                    return PageModel.<CategoryDetailOutput>builder()
                            .number(0)
                            .size(filter.getSize())
                            .totalPages(1)
                            .totalElements(2)
                            .content(List.of(
                                            CategoryOutputTestDataBuilder.aCategory().build(),
                                            CategoryOutputTestDataBuilder.aDisabledCategory().build()
                                    )
                            ).build();
                });
    }
}
