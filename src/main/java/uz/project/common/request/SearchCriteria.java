package uz.project.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    @NotNull
    private String key;
    @Pattern(regexp = "^(^(!=)?|^(<=)?|^(>=)?|^(=)?|^(<)?|^(>)?|^(in)?|(%_)?|^(_%)?|^(%_%)?)$")
    private String operation;
    private Object value;
    @NotNull
    private TypeSearch type;
}
