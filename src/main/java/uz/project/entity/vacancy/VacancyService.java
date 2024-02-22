package uz.project.entity.vacancy;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.VacancyStatus;
import uz.project.entity.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final FileService fileService;

    public VacancyService(VacancyRepository vacancyRepository, FileService fileService) {
        this.vacancyRepository = vacancyRepository;
        this.fileService = fileService;
    }

    public Optional<Vacancy> findById(Long id) {
        if (id == null) return Optional.empty();
        return vacancyRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Vacancy> getList(PageableRequest pageable) {
        return vacancyRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public List<Vacancy> getList() {
        return vacancyRepository.findAllByStatus(Status.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Vacancy getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(VacancyStatus.NOT_FOUND));
    }

    @Transactional
    public Vacancy save(Vacancy vacancy, VacancyPayload payload) {
        vacancy.setName(payload.getName());
        vacancy.setSalary(payload.getSalary());
        vacancy.setDescription(payload.getDescription());
        vacancy.setPhoto(fileService.changeType(payload.getPhotoId(), FileType.ACTIVE));
        vacancy.setStatus(payload.getStatus());
        return vacancyRepository.saveAndFlush(vacancy);
    }

    public Vacancy create(VacancyPayload payload) {
        Vacancy vacancy = new Vacancy();
        return save(vacancy, payload);
    }

    public Vacancy update(Long id, VacancyPayload payload) {
        Vacancy vacancy = getById(id);
        return save(vacancy, payload);
    }

    @Transactional
    public void delete(Long id) {
        Vacancy vacancy = getById(id);
        vacancy.setStatus(Status.DELETED);
        vacancyRepository.saveAndFlush(vacancy);
    }
}
