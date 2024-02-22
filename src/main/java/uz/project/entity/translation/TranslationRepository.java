package uz.project.entity.translation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long>, JpaGenericRepository<Translation> {

    @Query("select case when count(e) > 0 then true else false end from Translation e where lower(e.tag) = lower(?1)")
    boolean existsByTag(String tag);

    @Query("select case when count(e) > 0 then true else false end from Translation e where e.id <> ?1 and lower(e.tag) = lower(?2)")
    boolean existsByIdNotAndTag(@Param("id") Long id, @Param("tag") String tag);
}
