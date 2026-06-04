package co.istad.iteecomerc.repository;

import co.istad.iteecomerc.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {
}
