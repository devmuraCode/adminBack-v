package uz.project.entity.blog;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.BlogStatus;
import uz.project.entity.file.FileService;

import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final FileService fileService;

    public BlogService(BlogRepository blogRepository, FileService fileService) {
        this.blogRepository = blogRepository;
        this.fileService = fileService;
    }

    public Optional<Blog> findById(Long id) {
        if (id == null) return Optional.empty();
        return blogRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Blog> getList(PageableRequest pageable) {
        return blogRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public Blog getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(BlogStatus.NOT_FOUND));
    }

    public Blog changeViews(Long id) {
        Blog blog = getById(id);
        blog.setViews(blog.getViews() + 1);
        return blogRepository.saveAndFlush(blog);
    }

    @Transactional
    public Blog save(Blog blog, BlogPayload payload) {
        blog.setName(payload.getName());
        blog.setDescription(payload.getDescription());
        if (payload.getPhotoId() != null) {
            blog.setPhoto(fileService.changeType(payload.getPhotoId(), FileType.ACTIVE));
        }
        blog.setType(payload.getType());
        blog.setStatus(payload.getStatus());
        return blogRepository.saveAndFlush(blog);
    }

    public Blog create(BlogPayload payload) {
        Blog blog = new Blog();
        return save(blog, payload);
    }

    public Blog update(Long id, BlogPayload payload) {
        Blog blog = getById(id);
        return save(blog, payload);
    }

    public void delete(Long id) {
        Blog blog = getById(id);
        blogRepository.delete(blog);
    }
}
