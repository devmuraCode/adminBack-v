package uz.project.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import uz.project.common.constant.Status;
import uz.project.entity.user.User;

import java.time.LocalDateTime;

@Data
public class TechnicalFieldsResponse {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime updatedAt;

    public User createdBy;

    public Integer createdById;

    public User updatedBy;

    public Integer updatedById;

    private String message;

    public Status status;
}
