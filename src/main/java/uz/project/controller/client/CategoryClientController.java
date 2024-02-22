package uz.project.controller.client;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.project.common.constant.Status;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.SearchCriteria;
import uz.project.common.request.TypeSearch;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.category.CategoryResponse;
import uz.project.entity.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/client/categories")
public class CategoryClientController {

    private final CategoryService categoryService;

    public CategoryClientController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/pageable")
    public SuccessDataIterable<CategoryResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        pageable.getSearch().add(new SearchCriteria("status", "=", Status.ACTIVE, TypeSearch.STRING));
        Page<CategoryResponse> response = categoryService.getList(pageable).map(CategoryResponse::minResponseToClient);
        return new SuccessDataIterable<>(response);
    }

    @GetMapping("/{id}")
    public SuccessfulResponse<CategoryResponse> getById(@PathVariable Long id) {
        return new SuccessfulResponse<>(
                CategoryResponse.responseToClient(
                        categoryService.getById(id)
                )
        );
    }
}
