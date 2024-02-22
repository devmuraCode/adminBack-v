package uz.project.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageableRequest {
    @Schema(name = "per_page", description = "Page size", example = "10")
    private int perPage = 10;

    @Schema(description = "Page", example = "1")
    private int page = 0;

    @Schema(description = "Sort data")
    private Sort sort = new Sort();

    @Valid
    private List<SearchCriteria> search = new ArrayList<>();
}
