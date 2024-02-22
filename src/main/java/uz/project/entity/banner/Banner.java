package uz.project.entity.banner;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import uz.project.common.constant.Name;
import uz.project.common.constant.Status;
import uz.project.entity.file.File;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = IntArrayType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),

})
@Table(name = "banners")
public class Banner {

    @Id
    @SequenceGenerator(name = "banners_id_seq", sequenceName = "banners_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banners_id_seq")
    private Long id;

    @Type(type = "jsonb")
    @Column(name = "title", columnDefinition = "jsonb", nullable = false)
    private Name title;

    @Type(type = "jsonb")
    @Column(name = "description", columnDefinition = "jsonb", nullable = false)
    private Name description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "id")
    private File photo;

    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;
}
