package uz.project.entity.vacancy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacancyPayload {

    @NotNull
    private Name name;
    @NotNull
    private BigDecimal salary;
    private Name description;
    @NotNull
    private Long photoId;
    @NotNull
    private Status status;
}
