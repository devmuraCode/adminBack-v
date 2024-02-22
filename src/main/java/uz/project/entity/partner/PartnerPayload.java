package uz.project.entity.partner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerPayload {

    @NotNull
    private Name name;
    @NotBlank
    private String url;
    @NotNull
    private Long photoId;
    @NotNull
    private Status status;
}
