package uz.project.entity.partner;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.PartnerStatus;
import uz.project.entity.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final FileService fileService;

    public PartnerService(PartnerRepository partnerRepository, FileService fileService) {
        this.partnerRepository = partnerRepository;
        this.fileService = fileService;
    }

    public Optional<Partner> findById(Integer id) {
        if (id == null) return Optional.empty();
        return partnerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Partner> getList(Status status) {
        return partnerRepository.findAllByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Partner> getList(PageableRequest pageable) {
        return partnerRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public Partner getById(Integer id) {
        return findById(id).orElseThrow(() -> new ApiException(PartnerStatus.NOT_FOUND));
    }

    @Transactional
    public Partner save(Partner partner, PartnerPayload payload) {
        partner.setName(payload.getName());
        partner.setUrl(payload.getUrl());
        partner.setPhoto(fileService.changeType(payload.getPhotoId(), FileType.ACTIVE));
        partner.setStatus(payload.getStatus());
        return partnerRepository.saveAndFlush(partner);
    }

    public Partner create(PartnerPayload payload) {
        Partner partner = new Partner();
        return save(partner, payload);
    }

    public Partner update(Integer id, PartnerPayload payload) {
        Partner partner = getById(id);
        return save(partner, payload);
    }

    public void delete(Integer id) {
        Partner partner = getById(id);
        partnerRepository.delete(partner);
    }
}
