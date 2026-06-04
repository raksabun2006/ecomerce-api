package co.istad.iteecomerc.repository;

import co.istad.iteecomerc.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
