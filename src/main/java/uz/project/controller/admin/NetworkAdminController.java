package uz.project.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.response.SuccessDataIterable;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.NetworkStatus;
import uz.project.entity.network.NetworkPayload;
import uz.project.entity.network.NetworkResponse;
import uz.project.entity.network.NetworkService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/networks")
public class NetworkAdminController {

    private final NetworkService networkService;

    public NetworkAdminController(NetworkService networkService) {
        this.networkService = networkService;
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_NETWORKS')")
    @PostMapping("/pageable")
    public SuccessDataIterable<NetworkResponse> getList(@Valid @RequestBody PageableRequest pageable) {
        Page<NetworkResponse> response = networkService.getList(pageable).map(NetworkResponse::new);
        return new SuccessDataIterable<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('VIEW_NETWORK')")
    @GetMapping("/{id}")
    public SuccessfulResponse<NetworkResponse> getById(@PathVariable Integer id) {
        NetworkResponse response = new NetworkResponse(networkService.getById(id));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('CREATE_NETWORK')")
    @PostMapping
    public SuccessfulResponse<NetworkResponse> create(@Valid @RequestBody NetworkPayload payload) {
        NetworkResponse response = new NetworkResponse(networkService.create(payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('UPDATE_NETWORK')")
    @PutMapping("/{id}")
    public SuccessfulResponse<NetworkResponse> update(@PathVariable Integer id, @Valid @RequestBody NetworkPayload payload) {
        NetworkResponse response = new NetworkResponse(networkService.update(id, payload));
        return new SuccessfulResponse<>(response);
    }

//    @PreAuthorize(value = "hasAuthority('DELETE_NETWORK')")
    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Integer id) {
        networkService.delete(id);
        throw new ApiException(NetworkStatus.DELETED);
    }
}
