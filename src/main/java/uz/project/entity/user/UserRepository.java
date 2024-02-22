package uz.project.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaGenericRepository<User> {

    @Query("select case when count(u) > 0 then true else false end from User u where lower(u.username) = lower(?1)")
    boolean existsByUsername(String username);

    @Query("select case when count(u) > 0 then true else false end from User u where u.id <> ?1 and lower(u.username) = lower(?2)")
    boolean existsByIdNotAndUsername(Long id, String username);

    Optional<User> findByUsername(String username);
}
