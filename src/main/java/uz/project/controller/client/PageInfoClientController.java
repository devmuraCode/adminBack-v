package uz.project.controller.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.project.common.constant.PageName;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.pageinfo.PageInfoResponse;
import uz.project.entity.pageinfo.PageInfoService;

@RestController
@RequestMapping("/api/v1/client/page-infos")
public class PageInfoClientController {

    private final PageInfoService pageInfoService;

    public PageInfoClientController(PageInfoService pageInfoService) {
        this.pageInfoService = pageInfoService;
    }

    @GetMapping("/{pageName}")
    public SuccessfulResponse<PageInfoResponse> getById(@PathVariable PageName pageName) {
        PageInfoResponse response = new PageInfoResponse(pageInfoService.changeViews(pageName));
        return new SuccessfulResponse<>(response);
    }
}
