package uz.project.entity.banner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;
import uz.project.entity.file.FileResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BannerResponse {
    private Long id;
    private Name title;
    private Name description;
    private FileResponse photo;
    private Status status;

    public BannerResponse(Banner banner) {
        this.id = banner.getId();
        this.title = banner.getTitle();
        this.description = banner.getDescription();
        this.photo = FileResponse.minResponse(banner.getPhoto());
        this.status = banner.getStatus();
    }

    public static BannerResponse minResponse(Banner banner) {
        BannerResponse bannerResponse = new BannerResponse();
        bannerResponse.setId(banner.getId());
        bannerResponse.setTitle(banner.getTitle());
        bannerResponse.setDescription(banner.getDescription());
        bannerResponse.setPhoto(FileResponse.minResponse(banner.getPhoto()));
        return bannerResponse;
    }
}
