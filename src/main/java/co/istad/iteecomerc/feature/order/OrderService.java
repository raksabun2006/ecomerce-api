package co.istad.iteecomerc.feature.order;

import co.istad.iteecomerc.feature.order.dto.OrderRequest;
import co.istad.iteecomerc.feature.order.dto.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {

    OrderResponse create(OrderRequest request);
    Page<OrderResponse> findAll(int page, int size);
    OrderResponse findById(UUID id);
    void softDelete(UUID id);
    void hardDelete(UUID id);
    void updatePaymentStatus(UUID id, boolean status);
}