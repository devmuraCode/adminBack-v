package uz.project.controller.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.project.common.constant.Status;
import uz.project.entity.network.NetworkResponse;
import uz.project.entity.network.NetworkService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client/networks")
public class NetworkClientController {

    private final NetworkService networkService;

    public NetworkClientController(NetworkService networkService) {
        this.networkService = networkService;
    }

    @GetMapping("/list")
    public List<NetworkResponse> getList() {
        return networkService.getList(Status.ACTIVE)
                .stream()
                .map(NetworkResponse::minResponse)
                .toList();
    }
}
