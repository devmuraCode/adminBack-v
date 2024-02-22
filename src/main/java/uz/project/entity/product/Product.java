package uz.project.entity.product;

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
import uz.project.common.constant.Name;
import uz.project.entity.category.Category;
import uz.project.entity.file.File;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Table(name = "products")
public class Product extends TechnicalFields {

    @Id
    @SequenceGenerator(name = "products_id_seq", sequenceName = "products_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_id_seq")
    private Long id;

    @Type(type = "jsonb")
    @Column(name = "name", columnDefinition = "jsonb", nullable = false)
    private Name name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Type(type = "jsonb")
    @Column(name = "description", columnDefinition = "jsonb")
    private Name description;

    @OneToMany(targetEntity = File.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_photos",
            joinColumns = {@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_product_id"))},
            inverseJoinColumns = {@JoinColumn(name = "photo_id", foreignKey = @ForeignKey(name = "fk_photo_id"))}
    )
    private List<File> photos = new ArrayList<>();

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            insertable = false,
            updatable = false,
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "product_category_fk")
    )
    private Category category;
}
