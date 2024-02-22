package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.CategoryStatus;
import uz.project.entity.category.CategoryPayload;
import uz.project.entity.category.CategoryResponse;
import uz.project.entity.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_CATEGORIES')")
    @PostMapping("/pageable")
    public SuccessDataIterable<CategoryResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<CategoryResponse> response = categoryService.getList(pageable).map(CategoryResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_CATEGORY')")
    @GetMapping("/{id}")
    public SuccessfulResponse<CategoryResponse> getById(@PathVariable Long id) {
        CategoryResponse response = new CategoryResponse(categoryService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('CREATE_CATEGORY')")
    @PostMapping
    public SuccessfulResponse<CategoryResponse> create(@Valid @RequestBody CategoryPayload payload) {
        CategoryResponse response = new CategoryResponse(categoryService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('UPDATE_CATEGORY')")
    @PutMapping("/{id}")
    public SuccessfulResponse<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryPayload payload) {
        CategoryResponse response = new CategoryResponse(categoryService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('DELETE_CATEGORY')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        throw new ApiException(CategoryStatus.DELETED);
    }
}
