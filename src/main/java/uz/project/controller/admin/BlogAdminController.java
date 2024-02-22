package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.BlogStatus;
import uz.project.entity.blog.BlogPayload;
import uz.project.entity.blog.BlogResponse;
import uz.project.entity.blog.BlogService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/blogs")
public class BlogAdminController {

    private final BlogService blogService;

    public BlogAdminController(BlogService blogService) {
        this.blogService = blogService;
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_BLOGS')")
    @PostMapping("/pageable")
    public SuccessDataIterable<BlogResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<BlogResponse> response = blogService.getList(pageable).map(BlogResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_BLOG')")
    @GetMapping("/{id}")
    public SuccessfulResponse<BlogResponse> getById(@PathVariable Long id) {
        BlogResponse response = new BlogResponse(blogService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('CREATE_BLOG')")
    @PostMapping
    public SuccessfulResponse<BlogResponse> create(@Valid @RequestBody BlogPayload payload) {
        BlogResponse response = new BlogResponse(blogService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('UPDATE_BLOG')")
    @PutMapping("/{id}")
    public SuccessfulResponse<BlogResponse> update(@PathVariable Long id, @Valid @RequestBody BlogPayload payload) {
        BlogResponse response = new BlogResponse(blogService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('DELETE_BLOG')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        blogService.delete(id);
        throw new ApiException(BlogStatus.DELETED);
    }
}
