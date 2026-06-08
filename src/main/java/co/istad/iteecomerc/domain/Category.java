package co.istad.iteecomerc.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Category {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 50)

    private String name;

    private String description;
    private String icon;
    private boolean IsDeleted;
    @ManyToOne
    private Category parentCategory;

    @OneToMany(mappedBy = "categories")
    private List<Product> products;
}