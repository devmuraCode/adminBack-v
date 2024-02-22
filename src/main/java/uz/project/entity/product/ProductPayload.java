package uz.project.entity.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPayload {

    @NotNull
    private Name name;
    @NotNull
    private BigDecimal price;
    private Name description;
    @NotNull
    private List<Long> photoIds;
    @NotNull
    private Long categoryId;
    @NotNull
    private Status status;
}
