package uz.project.common.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SuccessDataIterable<T> {
    private Integer status;
    private ResponseMessage message;
    private List<T> data;
    private int totalPages;
    private long totalCount;
    private int size;
    private int page;

    public SuccessDataIterable(Page<T> objects) {
        this.status = 0;
        this.message = new ResponseMessage();
        this.data = objects.getContent();
        this.totalPages = objects.getTotalPages();
        this.totalCount = objects.getTotalElements();
        this.size = objects.getSize();
        this.page = objects.getNumber();
    }

    @SuppressWarnings("all")
    public SuccessDataIterable(Iterable<T> objects) {
        this.data = new ArrayList<>();
        objects.forEach(this.data::add);
        if (objects instanceof Page<?> page) {
            this.page = page.getNumber();
            this.size = page.getSize();
            this.totalCount = page.getTotalElements();
            this.page = page.getTotalPages();
        }
    }
}
