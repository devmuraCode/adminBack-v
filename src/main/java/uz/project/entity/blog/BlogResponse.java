package uz.project.entity.blog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.BlogType;
import uz.project.common.constant.Name;
import uz.project.common.response.TechnicalFieldsResponse;
import uz.project.entity.file.FileResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogResponse extends TechnicalFieldsResponse {
    private Long id;
    private Name name;
    private Name description;
    private FileResponse photo;
    private Integer views;
    private BlogType type;

    public BlogResponse(Blog blog) {
        this.id = blog.getId();
        this.name = blog.getName();
        this.description = blog.getDescription();
        if (blog.getPhoto() != null) {
            this.photo = FileResponse.minResponse(blog.getPhoto());
        }
        this.views = blog.getViews();
        this.type = blog.getType();
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.status = blog.getStatus();
    }

    public static BlogResponse minResponse(Blog blog) {
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setId(blog.getId());
        blogResponse.setName(blog.getName());
        blogResponse.setDescription(blog.getDescription());
        blogResponse.setPhoto(FileResponse.minResponse(blog.getPhoto()));
        blogResponse.setCreatedAt(blog.getCreatedAt());
        blogResponse.setViews(blog.getViews());
        return blogResponse;
    }
}
