package uz.project.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SuccessfulResponse<T> {
    private Integer status;
    private ResponseMessage message;
    private T data;

    public SuccessfulResponse(T data) {
        this.data = data;
        this.status = 0;
    }
}
