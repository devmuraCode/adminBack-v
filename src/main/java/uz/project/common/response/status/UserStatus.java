package uz.project.common.response.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.project.common.constant.MessageType;

@Getter
@AllArgsConstructor
public enum UserStatus implements ResponseStatus {
    NOT_FOUND(1, MessageType.ERROR, HttpStatus.NOT_FOUND),
    DELETED(2, MessageType.INFO, HttpStatus.NO_CONTENT),
    NOT_ALLOWED(3, MessageType.ERROR, HttpStatus.FORBIDDEN),
    INVALID_TOKEN(4, MessageType.ERROR, HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXIST(5, MessageType.ERROR, HttpStatus.CONFLICT),
    USERNAME_OR_PASSWORD_INCORRECT(7, MessageType.ERROR, HttpStatus.BAD_REQUEST);

    private final Integer status;
    private final MessageType messageType;
    private final HttpStatus httpStatus;
}
