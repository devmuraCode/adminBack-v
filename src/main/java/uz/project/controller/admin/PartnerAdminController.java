package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.PartnerStatus;
import uz.project.entity.partner.PartnerPayload;
import uz.project.entity.partner.PartnerResponse;
import uz.project.entity.partner.PartnerService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/partners")
public class PartnerAdminController {

    private final PartnerService partnerService;

    public PartnerAdminController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/pageable")
    public SuccessDataIterable<PartnerResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<PartnerResponse> response = partnerService.getList(pageable).map(PartnerResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/{id}")
    public SuccessfulResponse<PartnerResponse> getById(@PathVariable Integer id) {
        PartnerResponse response = new PartnerResponse(partnerService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping
    public SuccessfulResponse<PartnerResponse> create(@Valid @RequestBody PartnerPayload payload) {
        PartnerResponse response = new PartnerResponse(partnerService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PutMapping("/{id}")
    public SuccessfulResponse<PartnerResponse> update(@PathVariable Integer id, @Valid @RequestBody PartnerPayload payload) {
        PartnerResponse response = new PartnerResponse(partnerService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Integer id) {
        partnerService.delete(id);
        throw new ApiException(PartnerStatus.DELETED);
    }
}
