package uz.project.entity.vacancy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.Status;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaGenericRepository<Vacancy> {

    List<Vacancy> findAllByStatus(Status status);
}
