package uz.project.entity.pageinfo;

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
import uz.project.common.constant.PageName;

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
@Table(name = "page_infos")
public class PageInfo {

    @Id
    @SequenceGenerator(name = "page_infos_id_seq", sequenceName = "page_infos_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_infos_id_seq")
    private Integer id;

    @Type(type = "jsonb")
    @Column(name = "info", columnDefinition = "jsonb")
    private Name info;

    @Column(name = "views", columnDefinition = "integer default 0")
    private Integer views;

    @Column(name = "page_name", columnDefinition = "varchar(20) default 'AUTHOR'")
    @Enumerated(EnumType.STRING)
    private PageName pageName;
}
