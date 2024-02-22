package uz.project.entity.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NetworkPayload {

    @NotBlank
    private String url;
    @NotNull
    private Long photoId;
    @NotNull
    private Status status;
}
