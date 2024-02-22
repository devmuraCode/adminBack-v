package uz.project.entity.network;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.Status;

import java.util.List;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Integer>, JpaGenericRepository<Network> {

    List<Network> findAllByStatus(Status status);
}
