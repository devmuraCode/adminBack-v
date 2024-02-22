package uz.project.entity.pageinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.PageName;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfoPayload {

    private Name info;
    @NotNull
    private PageName pageName;
}
