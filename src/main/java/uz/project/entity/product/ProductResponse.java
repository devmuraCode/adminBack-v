package uz.project.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.Name;
import uz.project.common.response.TechnicalFieldsResponse;
import uz.project.entity.category.CategoryResponse;
import uz.project.entity.file.FileResponse;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse extends TechnicalFieldsResponse {
    private Long id;
    private Name name;
    private BigDecimal price;
    private Name description;
    private List<FileResponse> photos;
    private CategoryResponse category;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.photos = product.getPhotos()
                .stream()
                .map(FileResponse::minResponse)
                .toList();
        this.category = CategoryResponse.minResponseToAdmin(product.getCategory());
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
        this.status = product.getStatus();
    }

    public static ProductResponse responseToClient(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setDescription(product.getDescription());
        productResponse.setPhotos(
                product.getPhotos()
                        .stream()
                        .map(FileResponse::minResponse)
                        .toList()
        );
        productResponse.setCategory(CategoryResponse.minResponseToClient(product.getCategory()));
        return productResponse;
    }
}
