package uz.project.entity.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.project.common.constant.FileType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByUuid(UUID uuid);

    List<File> findAllByType(FileType type);
}
