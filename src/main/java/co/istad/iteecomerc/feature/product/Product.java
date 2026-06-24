package co.istad.iteecomerc.feature.product;



import co.istad.iteecomerc.feature.category.Category;
import co.istad.iteecomerc.feature.order.OrderLine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false , length = 100, unique = true)
    private String code;
    @Column(nullable = false , unique = true)
    private String slug;
    @Column(nullable = false, unique = true , length = 150)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(unique = false)

    private String thumbnail;
    @Column(unique = false)
    private BigDecimal unitPrice;
    @Column(unique = false)
    private Integer qty;

    @Column(nullable = false)
    private Boolean isAvailable;
    @Column(nullable = false)
    private Boolean isDelete;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderLine> orderLines;


}
