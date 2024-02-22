package uz.project.entity.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaGenericRepository<Blog> {

}
