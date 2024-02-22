package uz.project.entity.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPayload {

    @NotNull
    private Name name;
    @NotNull
    private Name description;
    @NotNull
    private Long photoId;
    @NotNull
    private Status status;
}
