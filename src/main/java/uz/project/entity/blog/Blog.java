package uz.project.entity.blog;

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
import uz.project.common.TechnicalFields;
import uz.project.common.constant.BlogType;
import uz.project.common.constant.Name;
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
@Table(name = "blogs")
public class Blog extends TechnicalFields {

    @Id
    @SequenceGenerator(name = "videos_id_seq", sequenceName = "videos_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "videos_id_seq")
    private Long id;

    @Type(type = "jsonb")
    @Column(name = "name", columnDefinition = "jsonb", nullable = false)
    private Name name;

    @Type(type = "jsonb")
    @Column(name = "description", columnDefinition = "jsonb", nullable = false)
    private Name description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", referencedColumnName = "id")
    private File photo;

    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "type", columnDefinition = "varchar(20) default 'NEW'")
    @Enumerated(EnumType.STRING)
    private BlogType type;
}
