package co.istad.iteecomerc.mapper;

import co.istad.iteecomerc.domain.Category;
import co.istad.iteecomerc.dto.CategoryRequest;
import co.istad.iteecomerc.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "parentCategory", ignore = true)
  Category mapCategoryRequestToCategory(CategoryRequest createCategoryRequest);

  @Mapping(target = "parentCategory", source = "parentCategory.id")
  CategoryResponse mapCategoryToCategoryResponse(Category category);
}