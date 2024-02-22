package uz.project.controller.client;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uz.project.common.constant.Status;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.SearchCriteria;
import uz.project.common.request.TypeSearch;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.entity.vacancy.VacancyResponse;
import uz.project.entity.vacancy.VacancyService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/client/vacancies")
public class VacancyClientController {

    private final VacancyService vacancyService;

    public VacancyClientController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping("/pageable")
    public SuccessDataIterable<VacancyResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        pageable.getSearch().add(new SearchCriteria("status", "=", Status.ACTIVE, TypeSearch.STRING));
        Page<VacancyResponse> response = vacancyService.getList(pageable).map(VacancyResponse::responseToClient);
        return new SuccessDataIterable<>(response);
    }

    @GetMapping("/{id}")
    public SuccessfulResponse<VacancyResponse> getById(@PathVariable Long id) {
        return new SuccessfulResponse<>(
                VacancyResponse.responseToClient(
                        vacancyService.getById(id)
                )
        );
    }
}
