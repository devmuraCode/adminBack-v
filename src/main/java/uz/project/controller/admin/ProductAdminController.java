package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.ProductStatus;
import uz.project.entity.product.ProductPayload;
import uz.project.entity.product.ProductResponse;
import uz.project.entity.product.ProductService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/pageable")
    public SuccessDataIterable<ProductResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<ProductResponse> response = productService.getList(pageable).map(ProductResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/{id}")
    public SuccessfulResponse<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse response = new ProductResponse(productService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping
    public SuccessfulResponse<ProductResponse> create(@Valid @RequestBody ProductPayload payload) {
        ProductResponse response = new ProductResponse(productService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PutMapping("/{id}")
    public SuccessfulResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductPayload payload) {
        ProductResponse response = new ProductResponse(productService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        productService.delete(id);
        throw new ApiException(ProductStatus.DELETED);
    }
}
