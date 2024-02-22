package uz.project.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.project.common.constant.MessageType;

@Getter
@AllArgsConstructor
public enum VacancyStatus implements ResponseStatus {
    NOT_FOUND(1, MessageType.ERROR, HttpStatus.NOT_FOUND),
    DELETED(2, MessageType.INFO, HttpStatus.NO_CONTENT);

    private final Integer status;
    private final MessageType messageType;
    private final HttpStatus httpStatus;
}
