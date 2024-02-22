package uz.project.entity.banner;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.BannerStatus;
import uz.project.entity.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
public class BannerService {

    private final BannerRepository bannerRepository;
    private final FileService fileService;

    public BannerService(BannerRepository bannerRepository, FileService fileService) {
        this.bannerRepository = bannerRepository;
        this.fileService = fileService;
    }

    public Optional<Banner> findById(Long id) {
        if (id == null) return Optional.empty();
        return bannerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Banner> getList(Status status) {
        return bannerRepository.findAllByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Banner> getList(PageableRequest pageable) {
        return bannerRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public Banner getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(BannerStatus.NOT_FOUND));
    }

    @Transactional
    public Banner save(Banner banner, BannerPayload payload) {
        banner.setTitle(payload.getTitle());
        banner.setDescription(payload.getDescription());
        banner.setPhoto(fileService.changeType(payload.getPhotoId(), FileType.ACTIVE));
        banner.setStatus(payload.getStatus());
        return bannerRepository.saveAndFlush(banner);
    }

    public Banner create(BannerPayload payload) {
        Banner banner = new Banner();
        return save(banner, payload);
    }

    public Banner update(Long id, BannerPayload payload) {
        Banner banner = getById(id);
        return save(banner, payload);
    }

    public void delete(Long id) {
        Banner banner = getById(id);
        bannerRepository.delete(banner);
    }
}
