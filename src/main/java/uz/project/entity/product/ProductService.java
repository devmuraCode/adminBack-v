package uz.project.entity.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.ProductStatus;
import uz.project.entity.category.CategoryService;
import uz.project.entity.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, FileService fileService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.categoryService = categoryService;
    }

    public Optional<Product> findById(Long id) {
        if (id == null) return Optional.empty();
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> getList(PageableRequest pageable) {
        return productRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public List<Product> getList(Long categoryId) {
        return productRepository.findAllByCategoryIdAndStatus(categoryId, Status.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(ProductStatus.NOT_FOUND));
    }

    @Transactional
    public Product save(Product product, ProductPayload payload) {
        product.setName(payload.getName());
        product.setPrice(payload.getPrice());
        product.setDescription(payload.getDescription());
        product.setPhotos(fileService.changeType(payload.getPhotoIds(), FileType.ACTIVE));
        product.setCategoryId(payload.getCategoryId());
        product.setCategory(categoryService.getById(payload.getCategoryId()));
        product.setStatus(payload.getStatus());
        return productRepository.saveAndFlush(product);
    }

    public Product create(ProductPayload payload) {
        Product product = new Product();
        return save(product, payload);
    }

    public Product update(Long id, ProductPayload payload) {
        Product product = getById(id);
        return save(product, payload);
    }

    @Transactional
    public void delete(Long id) {
        Product product = getById(id);
        product.setStatus(Status.DELETED);
        productRepository.saveAndFlush(product);
    }
}
