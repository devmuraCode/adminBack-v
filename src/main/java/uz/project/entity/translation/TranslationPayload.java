package uz.project.entity.translation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;
import uz.project.common.constant.TranslationType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranslationPayload {

    @NotNull
    private Name name;
    @NotBlank
    private String tag;
    @NotNull
    private List<TranslationType> types;
    @NotNull
    private Status status;
}
