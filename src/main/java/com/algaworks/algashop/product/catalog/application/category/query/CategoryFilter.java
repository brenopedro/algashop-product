package com.algaworks.algashop.product.catalog.application.category.query;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.algaworks.algashop.product.catalog.application.utility.SortablePageFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryFilter extends SortablePageFilter<CategoryFilter.SortType> {

  @Override
  public SortType getSortByPropertyOrDefault() {
    return getSortByProperty() == null ? SortType.NAME : getSortByProperty();
  }

  @Override
  public Sort.Direction getSortDirectionOrDefault() {
    return getSortDirection() == null ? Sort.Direction.ASC : getSortDirection();
  }

  private String name;
  private Boolean enabled;

  @Getter
  @RequiredArgsConstructor
  public enum SortType {
    NAME("name");

    private final String propertyName;

  }

}
