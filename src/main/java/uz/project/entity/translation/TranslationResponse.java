package uz.project.entity.translation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;
import uz.project.common.constant.TranslationType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslationResponse {
    private Long id;
    private Name name;
    private String tag;
    private List<TranslationType> types;
    private Status status;

    public TranslationResponse(Translation translation) {
        this.id = translation.getId();
        this.name = translation.getName();
        this.tag = translation.getTag();
        this.types = translation.getTypes();
        this.status = translation.getStatus();
    }
}
