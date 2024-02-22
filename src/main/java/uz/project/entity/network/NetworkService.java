package uz.project.entity.network;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.NetworkStatus;
import uz.project.entity.file.FileService;

import java.util.List;
import java.util.Optional;

@Service
public class NetworkService {

    private final NetworkRepository networkRepository;
    private final FileService fileService;

    public NetworkService(NetworkRepository networkRepository, FileService fileService) {
        this.networkRepository = networkRepository;
        this.fileService = fileService;
    }

    public Optional<Network> findById(Integer id) {
        if (id == null) return Optional.empty();
        return networkRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Network> getList(Status status) {
        return networkRepository.findAllByStatus(status);
    }

    @Transactional(readOnly = true)
    public Page<Network> getList(PageableRequest pageable) {
        return networkRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    @Transactional(readOnly = true)
    public Network getById(Integer id) {
        return findById(id).orElseThrow(() -> new ApiException(NetworkStatus.NOT_FOUND));
    }

    @Transactional
    public Network save(Network network, NetworkPayload payload) {
        network.setUrl(payload.getUrl());
        network.setPhoto(fileService.changeType(payload.getPhotoId(), FileType.ACTIVE));
        network.setStatus(payload.getStatus());
        return networkRepository.saveAndFlush(network);
    }

    public Network create(NetworkPayload payload) {
        Network network = new Network();
        return save(network, payload);
    }

    public Network update(Integer id, NetworkPayload payload) {
        Network network = getById(id);
        return save(network, payload);
    }

    public void delete(Integer id) {
        Network network = getById(id);
        networkRepository.delete(network);
    }
}
