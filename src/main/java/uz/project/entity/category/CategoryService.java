package uz.project.entity.category;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.CategoryStatus;
import uz.project.entity.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public CategoryService(CategoryRepository categoryRepository, FileService fileService) {
        this.categoryRepository = categoryRepository;
        this.fileService = fileService;
    }

    public Optional<Category> findById(Long id) {
        if (id == null) return Optional.empty();
        return categoryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Category> getList(Status status) {
        return categoryRepository.findAllByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Category> getList(PageableRequest pageable) {
        return categoryRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(CategoryStatus.NOT_FOUND));
    }

    @Transactional
    public Category save(Category category, CategoryPayload payload) {
        category.setName(payload.getName());
        category.setDescription(payload.getDescription());
        category.setPhoto(fileService.changeType(payload.getPhotoId(), FileType.ACTIVE));
        category.setStatus(payload.getStatus());
        return categoryRepository.saveAndFlush(category);
    }

    public Category create(CategoryPayload payload) {
        Category category = new Category();
        return save(category, payload);
    }

    public Category update(Long id, CategoryPayload payload) {
        Category category = getById(id);
        return save(category, payload);
    }

    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        category.setStatus(Status.DELETED);
        categoryRepository.saveAndFlush(category);
    }
}
