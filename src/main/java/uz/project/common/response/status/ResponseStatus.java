package uz.project.common.response.status;

import org.springframework.http.HttpStatus;
import uz.project.common.constant.MessageType;

public interface ResponseStatus {
    Integer getStatus();

    MessageType getMessageType();

    HttpStatus getHttpStatus();

    String name();
}
