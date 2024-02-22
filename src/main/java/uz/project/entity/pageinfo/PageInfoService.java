package uz.project.entity.pageinfo;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.PageName;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.PageInfoStatus;

import java.util.Optional;

@Service
public class PageInfoService {

    private final PageInfoRepository pageInfoRepository;

    public PageInfoService(PageInfoRepository pageInfoRepository) {
        this.pageInfoRepository = pageInfoRepository;
    }

    @Transactional(readOnly = true)
    public Page<PageInfo> getList(PageableRequest pageable) {
        return pageInfoRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    public Optional<PageInfo> findByPageName(PageName pageName) {
        if (pageName == null) return Optional.empty();
        return pageInfoRepository.findByPageName(pageName);
    }

    @Transactional(readOnly = true)
    public PageInfo getByPageName(PageName pageName) {
        return findByPageName(pageName).orElseThrow(() -> new ApiException(PageInfoStatus.NOT_FOUND));
    }

    public PageInfo changeViews(PageName pageName) {
        PageInfo pageInfo = getByPageName(pageName);
        pageInfo.setViews(pageInfo.getViews() + 1);
        return pageInfoRepository.saveAndFlush(pageInfo);
    }

    @Transactional
    public PageInfo save(PageInfoPayload payload) {
        PageInfo pageInfo = findByPageName(payload.getPageName()).orElseGet(PageInfo::new);
        pageInfo.setInfo(payload.getInfo());
        pageInfo.setPageName(payload.getPageName());
        return pageInfoRepository.saveAndFlush(pageInfo);
    }
}
