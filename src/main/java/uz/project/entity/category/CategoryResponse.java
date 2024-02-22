package uz.project.entity.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.response.TechnicalFieldsResponse;
import uz.project.entity.file.FileResponse;
import uz.project.entity.product.ProductResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryResponse extends TechnicalFieldsResponse {
    private Long id;
    private Name name;
    private Name description;
    private FileResponse photo;
    private Integer productCount;
    private List<ProductResponse> products;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.photo = FileResponse.minResponse(category.getPhoto());
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
        this.status = category.getStatus();
    }

    public static CategoryResponse minResponseToAdmin(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }

    public static CategoryResponse responseToClient(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setDescription(category.getDescription());
        categoryResponse.setPhoto(FileResponse.minResponse(category.getPhoto()));
        categoryResponse.setProductCount(category.getProducts().size());
        return categoryResponse;
    }

    public static CategoryResponse minResponseToClient(Category category) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        categoryResponse.setPhoto(FileResponse.minResponse(category.getPhoto()));
        categoryResponse.setProductCount(category.getProducts().size());
        return categoryResponse;
    }
}
