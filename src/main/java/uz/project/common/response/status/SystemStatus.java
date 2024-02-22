package uz.project.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.project.common.constant.MessageType;

@Getter
@AllArgsConstructor
public enum SystemStatus implements ResponseStatus {
    VALIDATION(-5, MessageType.ERROR, HttpStatus.BAD_REQUEST),
    FORBIDDEN(-4, MessageType.ERROR, HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(-3, MessageType.ERROR, HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(-2, MessageType.ERROR, HttpStatus.BAD_REQUEST),
    NO_CONTENT(-1, MessageType.ERROR, HttpStatus.BAD_REQUEST),
    OK(0, MessageType.INFO, HttpStatus.OK),
    INTERNAL_SERVER_ERROR(1, MessageType.ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
    MULTIPART_FILE_NOT_FOUND(2, MessageType.ERROR, HttpStatus.BAD_GATEWAY),
    DELETED(3, MessageType.INFO, HttpStatus.OK);

    private final Integer status;
    private final MessageType messageType;
    private final HttpStatus httpStatus;
}
