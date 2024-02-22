package uz.project.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.BlogType;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPayload {

    @NotNull
    private Name name;
    @NotNull
    private Name description;
    private Long photoId;
    @NotNull
    private BlogType type;
    @NotNull
    private Status status;
}
