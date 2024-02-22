package uz.project.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.project.common.constant.MessageType;

@Getter
@AllArgsConstructor
public enum PageInfoStatus implements ResponseStatus {
    NOT_FOUND(1, MessageType.ERROR, HttpStatus.NOT_FOUND);

    private final Integer status;
    private final MessageType messageType;
    private final HttpStatus httpStatus;
}
