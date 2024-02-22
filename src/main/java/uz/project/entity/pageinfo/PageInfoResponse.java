package uz.project.entity.pageinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.PageName;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfoResponse {
    private Integer id;
    private Name info;
    private PageName pageName;
    private Integer views;

    public PageInfoResponse(PageInfo pageInfo) {
        this.id = pageInfo.getId();
        this.info = pageInfo.getInfo();
        this.pageName = pageInfo.getPageName();
        this.views = pageInfo.getViews();
    }
}
