package uz.project.entity.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Status;
import uz.project.entity.file.FileResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkResponse {
    private Integer id;
    private String url;
    private FileResponse photo;
    private Status status;

    public NetworkResponse(Network network) {
        this.id = network.getId();
        this.url = network.getUrl();
        this.photo = FileResponse.minResponse(network.getPhoto());
        this.status = network.getStatus();
    }

    public static NetworkResponse minResponse(Network network) {
        NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setId(network.getId());
        networkResponse.setUrl(network.getUrl());
        networkResponse.setPhoto(FileResponse.minResponse(network.getPhoto()));
        return networkResponse;
    }
}
