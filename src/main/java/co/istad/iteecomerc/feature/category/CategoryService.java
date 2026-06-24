package co.istad.iteecomerc.feature.category;

import co.istad.iteecomerc.feature.category.dto.CategoryRequest;
import co.istad.iteecomerc.feature.category.dto.CategoryResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CategoryService {
    CategoryResponse createNew(CategoryRequest createCategoryRequest); // Already done

    Page<CategoryResponse> getAllCategories(int pageNumber, int pageSize);
    CategoryResponse getCategoryById(Integer id);
    List<CategoryResponse> getSubcategoriesByMainCategoryId(Integer id);
    void hardDeleteCategory(Integer id);
    CategoryResponse softDeleteCategory(Integer id);
    CategoryResponse updateCategory(Integer id, CategoryRequest updateRequest);
    List<CategoryResponse> findByName(String name);


}