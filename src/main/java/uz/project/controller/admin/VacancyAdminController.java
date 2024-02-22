package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.VacancyStatus;
import uz.project.entity.vacancy.VacancyPayload;
import uz.project.entity.vacancy.VacancyResponse;
import uz.project.entity.vacancy.VacancyService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/vacancies")
public class VacancyAdminController {

    private final VacancyService vacancyService;

    public VacancyAdminController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/pageable")
    public SuccessDataIterable<VacancyResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<VacancyResponse> response = vacancyService.getList(pageable).map(VacancyResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/{id}")
    public SuccessfulResponse<VacancyResponse> getById(@PathVariable Long id) {
        VacancyResponse response = new VacancyResponse(vacancyService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping
    public SuccessfulResponse<VacancyResponse> create(@Valid @RequestBody VacancyPayload payload) {
        VacancyResponse response = new VacancyResponse(vacancyService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @PutMapping("/{id}")
    public SuccessfulResponse<VacancyResponse> update(@PathVariable Long id, @Valid @RequestBody VacancyPayload payload) {
        VacancyResponse response = new VacancyResponse(vacancyService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAnyRole('ADMIN', 'MODERATOR')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        vacancyService.delete(id);
        throw new ApiException(VacancyStatus.DELETED);
    }
}
