package uz.project.entity.partner;

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
@Table(name = "partners")
public class Partner {

    @Id
    @SequenceGenerator(name = "partners_id_seq", sequenceName = "partners_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partners_id_seq")
    private Integer id;

    @Type(type = "jsonb")
    @Column(name = "name", columnDefinition = "jsonb", nullable = false)
    private Name name;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", nullable = false, referencedColumnName = "id")
    private File photo;

    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private Status status;
}
