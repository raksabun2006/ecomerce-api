package co.istad.iteecomerc.feature.category;

import co.istad.iteecomerc.feature.category.dto.CategoryRequest;
import co.istad.iteecomerc.feature.category.dto.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createNew(@Valid @RequestBody CategoryRequest categoryRequest) {
        return categoryService.createNew(categoryRequest);
    }

    @GetMapping
    public Page<CategoryResponse> getAllCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "25") int pageSize
    ) {
        return categoryService.getAllCategories(pageNumber, pageSize);
    }

    @GetMapping("/search")
    public List<CategoryResponse> searchCategoryByName(@RequestParam String name) {
        return categoryService.findByName(name);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{id}/subcategories")
    public List<CategoryResponse> getSubcategoriesByMainCategoryId(@PathVariable Integer id) {
        return categoryService.getSubcategoriesByMainCategoryId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hardDeleteCategory(@PathVariable Integer id) {
        categoryService.hardDeleteCategory(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse softDeleteCategory(@PathVariable Integer id) {
        return categoryService.softDeleteCategory(id);
    }

    @PatchMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryRequest updateRequest
    ) {
        return categoryService.updateCategory(id, updateRequest);
    }
}