package uz.project.controller.client;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.project.common.constant.Status;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.SearchCriteria;
import uz.project.common.request.TypeSearch;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.product.ProductResponse;
import uz.project.entity.product.ProductService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/client/products")
public class ProductClientController {

    private final ProductService productService;

    public ProductClientController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/pageable")
    public SuccessDataIterable<ProductResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        pageable.getSearch().add(new SearchCriteria("status", "=", Status.ACTIVE, TypeSearch.STRING));
        Page<ProductResponse> response = productService.getList(pageable).map(ProductResponse::responseToClient);
        return new SuccessDataIterable<>(response);
    }

    @GetMapping("/list")
    public List<ProductResponse> getList(@RequestParam Long categoryId) {
        return productService.getList(categoryId)
                .stream()
                .map(ProductResponse::responseToClient)
                .toList();
    }

    @GetMapping("/{id}")
    public SuccessfulResponse<ProductResponse> getById(@PathVariable Long id) {
        return new SuccessfulResponse<>(
                ProductResponse.responseToClient(
                        productService.getById(id)
                )
        );
    }
}
