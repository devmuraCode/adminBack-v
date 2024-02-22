package uz.project.controller.client;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.project.common.constant.Status;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.SearchCriteria;
import uz.project.common.request.TypeSearch;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.blog.BlogResponse;
import uz.project.entity.blog.BlogService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/client/blogs")
public class BlogClientController {

    private final BlogService blogService;

    public BlogClientController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping("/pageable")
    public SuccessDataIterable<BlogResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        pageable.getSearch().add(new SearchCriteria("status", "=", Status.ACTIVE, TypeSearch.STRING));
        Page<BlogResponse> response = blogService.getList(pageable).map(BlogResponse::minResponse);
        return new SuccessDataIterable<>(response);
    }

    @GetMapping("/{id}")
    public SuccessfulResponse<BlogResponse> getById(@PathVariable Long id) {
        BlogResponse response = BlogResponse.minResponse(blogService.changeViews(id));
        return new SuccessfulResponse<>(response);
    }
}
