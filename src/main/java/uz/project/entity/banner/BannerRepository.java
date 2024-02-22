package uz.project.entity.banner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.Status;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>, JpaGenericRepository<Banner> {

    List<Banner> findAllByStatus(Status status);
}
