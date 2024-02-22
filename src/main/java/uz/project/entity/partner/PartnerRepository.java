package uz.project.entity.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.Status;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer>, JpaGenericRepository<Partner> {

    List<Partner> findAllByStatus(Status status);
}
