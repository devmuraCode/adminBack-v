package uz.project.controller.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.project.common.constant.Status;
import uz.project.entity.partner.PartnerResponse;
import uz.project.entity.partner.PartnerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client/partners")
public class PartnerClientController {

    private final PartnerService partnerService;

    public PartnerClientController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping("/list")
    public List<PartnerResponse> getList() {
        return partnerService.getList(Status.ACTIVE)
                .stream()
                .map(PartnerResponse::minResponse)
                .toList();
    }
}
