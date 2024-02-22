package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.TranslationStatus;
import uz.project.entity.translation.TranslationPayload;
import uz.project.entity.translation.TranslationResponse;
import uz.project.entity.translation.TranslationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/translations")
public class TranslationAdminController {

    private final TranslationService translationService;

    public TranslationAdminController(TranslationService translationService) {
        this.translationService = translationService;
    }

//    @PreAuthorize(value = "hasRole('VIEW_TRANSLATIONS')")
    @PostMapping("/pageable")
    public SuccessDataIterable<TranslationResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<TranslationResponse> response = translationService.getList(pageable).map(TranslationResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasRole('VIEW_TRANSLATION')")
    @GetMapping("/{id}")
    public SuccessfulResponse<TranslationResponse> getById(@PathVariable Long id) {
        TranslationResponse response = new TranslationResponse(translationService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasRole('CREATE_TRANSLATION')")
    @PostMapping
    public SuccessfulResponse<TranslationResponse> create(@Valid @RequestBody TranslationPayload payload) {
        TranslationResponse response = new TranslationResponse(translationService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasRole('UPDATE_TRANSLATION')")
    @PutMapping("/{id}")
    public SuccessfulResponse<TranslationResponse> update(@PathVariable Long id, @Valid @RequestBody TranslationPayload payload) {
        TranslationResponse response = new TranslationResponse(translationService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasRole('DELETE_TRANSLATION')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) {
        translationService.delete(id);
        throw new ApiException(TranslationStatus.DELETED);
    }
}
