package uz.project.entity.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.Status;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaGenericRepository<Category> {

    List<Category> findAllByStatus(Status status);

    long countByStatus(Status status);
}
