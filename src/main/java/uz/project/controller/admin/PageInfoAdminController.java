package uz.project.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.constant.PageName;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.pageinfo.PageInfoPayload;
import uz.project.entity.pageinfo.PageInfoResponse;
import uz.project.entity.pageinfo.PageInfoService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/page-infos")
public class PageInfoAdminController {

    private final PageInfoService pageInfoService;

    public PageInfoAdminController(PageInfoService pageInfoService) {
        this.pageInfoService = pageInfoService;
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_PAGE_INFOS')")
    @GetMapping("/{pageName}")
    public SuccessfulResponse<PageInfoResponse> getById(@PathVariable PageName pageName) {
        PageInfoResponse response = new PageInfoResponse(pageInfoService.getByPageName(pageName));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('CREATE_PAGE_INFO')")
    @PostMapping
    public SuccessfulResponse<PageInfoResponse> create(@Valid @RequestBody PageInfoPayload payload) {
        PageInfoResponse response = new PageInfoResponse(pageInfoService.save(payload));
        return new SuccessfulResponse<>(response);
    }
}
