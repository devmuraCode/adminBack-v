package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.BannerStatus;
import uz.project.entity.banner.BannerPayload;
import uz.project.entity.banner.BannerResponse;
import uz.project.entity.banner.BannerService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/banners")
public class BannerAdminController {

    private final BannerService bannerService;

    public BannerAdminController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_BANNERS')")
    @PostMapping("/pageable")
    public SuccessDataIterable<BannerResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<BannerResponse> response = bannerService.getList(pageable).map(BannerResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_BANNER')")
    @GetMapping("/{id}")
    public SuccessfulResponse<BannerResponse> getById(@PathVariable Long id) {
        BannerResponse response = new BannerResponse(bannerService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('CREATE_BANNER')")
    @PostMapping
    public SuccessfulResponse<BannerResponse> create(@Valid @RequestBody BannerPayload payload) {
        BannerResponse response = new BannerResponse(bannerService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('UPDATE_BANNER')")
    @PutMapping("/{id}")
    public SuccessfulResponse<BannerResponse> update(@PathVariable Long id, @Valid @RequestBody BannerPayload payload) {
        BannerResponse response = new BannerResponse(bannerService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('DELETE_BANNER')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        bannerService.delete(id);
        throw new ApiException(BannerStatus.DELETED);
    }
}
