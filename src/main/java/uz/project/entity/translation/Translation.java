package uz.project.entity.translation;

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
import uz.project.common.constant.TranslationType;

import javax.persistence.*;
import java.util.List;

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
@Table(name = "translations")
public class Translation {

    @Id
    @SequenceGenerator(name = "translations_id_seq", sequenceName = "translations_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "translations_id_seq")
    private Long id;

    @Column(name = "tag", nullable = false, unique = true)
    private String tag;

    @Type(type = "jsonb")
    @Column(name = "name", columnDefinition = "jsonb", nullable = false)
    private Name name;

    @Type(type = "jsonb")
    @Column(name = "types", columnDefinition = "jsonb", nullable = false)
    private List<TranslationType> types;

    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;
}
