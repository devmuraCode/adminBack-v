package uz.project.controller.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.project.common.constant.Status;
import uz.project.entity.banner.BannerResponse;
import uz.project.entity.banner.BannerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client/banners")
public class BannerClientController {

    private final BannerService bannerService;

    public BannerClientController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/list")
    public List<BannerResponse> getList() {
        return bannerService.getList(Status.ACTIVE)
                .stream()
                .map(BannerResponse::minResponse)
                .toList();
    }
}
