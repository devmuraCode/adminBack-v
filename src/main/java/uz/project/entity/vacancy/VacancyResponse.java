package uz.project.entity.vacancy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.response.TechnicalFieldsResponse;
import uz.project.entity.file.FileResponse;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacancyResponse extends TechnicalFieldsResponse {
    private Long id;
    private Name name;
    private BigDecimal salary;
    private Name description;
    private FileResponse photo;

    public VacancyResponse(Vacancy vacancy) {
        this.id = vacancy.getId();
        this.name = vacancy.getName();
        this.salary = vacancy.getSalary();
        this.description = vacancy.getDescription();
        this.photo = FileResponse.minResponse(vacancy.getPhoto());
        this.createdAt = vacancy.getCreatedAt();
        this.updatedAt = vacancy.getUpdatedAt();
        this.status = vacancy.getStatus();
    }

    public static VacancyResponse responseToClient(Vacancy vacancy) {
        VacancyResponse vacancyResponse = new VacancyResponse();
        vacancyResponse.setId(vacancy.getId());
        vacancyResponse.setName(vacancy.getName());
        vacancyResponse.setSalary(vacancy.getSalary());
        vacancyResponse.setDescription(vacancy.getDescription());
        vacancyResponse.setPhoto(FileResponse.minResponse(vacancy.getPhoto()));
        return vacancyResponse;
    }
}
