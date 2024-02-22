package uz.project.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.Status;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaGenericRepository<Product> {

    List<Product> findAllByCategoryIdAndStatus(Long category_id, Status status);
}
