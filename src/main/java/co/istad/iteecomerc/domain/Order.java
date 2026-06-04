package co.istad.iteecomerc.domain;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false , length = 255)
    private String address;

    @Column(nullable = true)
    private Float discount ;

    @Column(nullable = true, length = 255)
    private String remake;

    private Boolean status ;

    private LocalDate orderDate;

    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;
}
