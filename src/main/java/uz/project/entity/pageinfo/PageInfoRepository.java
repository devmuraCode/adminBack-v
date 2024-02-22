package uz.project.entity.pageinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.JpaGenericRepository;
import uz.project.common.constant.PageName;

import java.util.Optional;

@Repository
public interface PageInfoRepository extends JpaRepository<PageInfo, Integer>, JpaGenericRepository<PageInfo> {

    Optional<PageInfo> findByPageName(PageName pageName);
}
