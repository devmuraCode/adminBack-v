package uz.project.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.project.common.response.status.ResponseStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException {
    private ResponseStatus responseStatus;
}
