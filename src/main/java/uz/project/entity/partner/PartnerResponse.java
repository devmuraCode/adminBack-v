package uz.project.entity.partner;

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
public class PartnerResponse {
    private Integer id;
    private Name name;
    private String url;
    private FileResponse photo;
    private Status status;

    public PartnerResponse(Partner partner) {
        this.id = partner.getId();
        this.name = partner.getName();
        this.url = partner.getUrl();
        this.photo = FileResponse.minResponse(partner.getPhoto());
        this.status = partner.getStatus();
    }

    public static PartnerResponse minResponse(Partner partner) {
        PartnerResponse partnerResponse = new PartnerResponse();
        partnerResponse.setId(partner.getId());
        partnerResponse.setName(partner.getName());
        partnerResponse.setUrl(partner.getUrl());
        partnerResponse.setPhoto(FileResponse.minResponse(partner.getPhoto()));
        return partnerResponse;
    }
}
