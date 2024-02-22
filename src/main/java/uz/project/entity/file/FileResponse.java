package uz.project.entity.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.constant.FileType;
import uz.project.common.response.TechnicalFieldsResponse;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileResponse extends TechnicalFieldsResponse {
    private Long id;
    private String name;
    private String extension;
    private Long size;
    private String contentType;
    private UUID uuid;
    private FileType type;

    public FileResponse(File file) {
        this.id = file.getId();
        this.name = file.getName();
        this.extension = file.getExtension();
        this.size = file.getSize();
        this.contentType = file.getContentType();
        this.uuid = file.getUuid();
        this.type = file.getType();
        this.createdAt = file.getCreatedAt();
        this.updatedAt = file.getUpdatedAt();
        this.status = file.getStatus();
    }

    public static FileResponse minResponse(File file) {
        FileResponse fileResponse = new FileResponse();
        fileResponse.setId(file.getId());
        fileResponse.setName(file.getName());
        fileResponse.setUuid(file.getUuid());
        return fileResponse;
    }
}
