package co.istad.iteecomerc.service.Impl;

import co.istad.iteecomerc.domain.Category;
import co.istad.iteecomerc.dto.CategoryRequest;
import co.istad.iteecomerc.dto.CategoryResponse;
import co.istad.iteecomerc.mapper.CategoryMapper;
import co.istad.iteecomerc.repository.CategoryRepository;
import co.istad.iteecomerc.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createNew(CategoryRequest createCategoryRequest) {
        // Your existing create code...
        return null;
    }

    // 1. [GET] Get all categories by pagination
    @Override
    public Page<CategoryResponse> getAllCategories(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        // Assuming your repository filters out soft-deleted ones or gets all
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(categoryMapper::mapCategoryToCategoryResponse);
    }

    // 2. [GET] Get category by ID
    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return categoryMapper.mapCategoryToCategoryResponse(category);
    }

    // 3. [GET] Get subcategories by main category ID
    @Override
    public List<CategoryResponse> getSubcategoriesByMainCategoryId(Integer id) {
        // Make sure you have 'findByParentCategory_Id' declared in your CategoryRepository
        List<Category> subcategories = categoryRepository.findByParentCategory_Id(id);
        return subcategories.stream()
                .map(categoryMapper::mapCategoryToCategoryResponse)
                .toList();
    }

    // 4. [DELETE] Hard delete category by ID
    @Override
    public void hardDeleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }

    // 5. [PUT] Soft delete category by ID
    @Override
    public CategoryResponse softDeleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Assuming your Category entity has an 'isDeleted' or 'isActived' attribute
        category.setIsDeleted(true);

        Category saved = categoryRepository.save(category);
        return categoryMapper.mapCategoryToCategoryResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest updateRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        // Check and partially update only provided fields
        if (updateRequest.name() != null) category.setName(updateRequest.name());
        if (updateRequest.icon() != null) category.setIcon(updateRequest.icon());
        if (updateRequest.description() != null) category.setDescription(updateRequest.description());

        Category saved = categoryRepository.save(category);
        return categoryMapper.mapCategoryToCategoryResponse(saved);
    }
}