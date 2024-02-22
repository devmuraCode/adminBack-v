package uz.project.entity.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.project.common.TechnicalFields;
import uz.project.common.constant.FileType;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class File extends TechnicalFields {

    @Id
    @SequenceGenerator(name = "files_id_seq", sequenceName = "files_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "type", columnDefinition = "varchar(20) default 'DRAFT'")
    @Enumerated(EnumType.STRING)
    private FileType type;

    @PreRemove
    public void preRemove() {
        try {
            Files.deleteIfExists(Paths.get(this.uploadPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID();
    }
}
