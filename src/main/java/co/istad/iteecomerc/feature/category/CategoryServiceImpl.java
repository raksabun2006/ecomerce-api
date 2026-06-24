package co.istad.iteecomerc.feature.category;

import co.istad.iteecomerc.feature.category.dto.CategoryRequest;
import co.istad.iteecomerc.feature.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createNew(CategoryRequest createCategoryRequest) {
        log.info("Creating new category: {}", createCategoryRequest);

        // Fixed: Use boolean validation to avoid HTTP 500 query exception
        if (categoryRepository.existsByName(createCategoryRequest.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category name already exists"
            );
        }

        Category category = categoryMapper.mapCategoryRequestToCategory(createCategoryRequest);

        // Safely map parent category if ID is provided
        if (createCategoryRequest.parentCategory() != null) {
            Category parentCategory = categoryRepository.findById(createCategoryRequest.parentCategory())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Parent category not found with ID: " + createCategoryRequest.parentCategory()
                    ));
            category.setParentCategory(parentCategory);
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.mapCategoryToCategoryResponse(saved);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(categoryMapper::mapCategoryToCategoryResponse);
    }

    @Override
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        return categoryMapper.mapCategoryToCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> getSubcategoriesByMainCategoryId(Integer id) {
        // Ensure parent category exists first
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent category not found");
        }

        // Optimised to filter parent mapping safely
        List<Category> subcategories = categoryRepository.findAll().stream()
                .filter(c -> c.getParentCategory() != null && c.getParentCategory().getId().equals(id))
                .toList();

        return subcategories.stream()
                .map(categoryMapper::mapCategoryToCategoryResponse)
                .toList();
    }

    @Override
    public void hardDeleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse softDeleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        category.setIsDeleted(true);

        Category saved = categoryRepository.save(category);
        return categoryMapper.mapCategoryToCategoryResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest updateRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (updateRequest.name() != null) {
            category.setName(updateRequest.name());
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.mapCategoryToCategoryResponse(updatedCategory);
    }

    @Override
    public List<CategoryResponse> findByName(String name) {
        // Safe fallback streaming for name search queries
        return categoryRepository.findAll().stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(name.toLowerCase()))
                .map(categoryMapper::mapCategoryToCategoryResponse)
                .toList();
    }
}