package uz.project.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.util.List;

@SuppressWarnings("all")
@NoRepositoryBean
public interface JpaGenericRepository<T> extends JpaSpecificationExecutor<T> {
    Page<T> findAll(@Nullable Pageable pageable);

    List<T> findAll(Specification<T> specification);

    Page<T> findAll(Specification<T> specification, @Nullable Pageable pageable);
}
