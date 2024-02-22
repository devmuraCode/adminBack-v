package uz.project.common.request;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class PageableRequestUtil {
    public static Pageable toPageable(PageableRequest pageable) {
        return PageRequest.of(
                pageable.getPage(),
                pageable.getPerPage(),
                Sort.Direction.fromString(pageable.getSort().getDirection()),
                pageable.getSort().getName()
        );
    }
}
