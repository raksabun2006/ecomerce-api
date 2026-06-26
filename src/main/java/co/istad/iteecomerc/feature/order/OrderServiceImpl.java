package co.istad.iteecomerc.feature.order;

import co.istad.iteecomerc.feature.order.dto.OrderRequest;
import co.istad.iteecomerc.feature.order.dto.OrderResponse;
import co.istad.iteecomerc.feature.product.Product;
import co.istad.iteecomerc.feature.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse create(OrderRequest request) {
        if (request.orderLineList() == null || request.orderLineList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order lines cannot be empty");
        }

        List<OrderLine> orderLineList = new ArrayList<>();

        boolean isValidOrder = request.orderLineList().stream()
                .allMatch(orderLine -> {
                    java.util.Optional<Product> productOptional = productRepository.findByCode(orderLine.getCode());

                    if (productOptional.isPresent()) {
                        orderLine.setProduct(productOptional.get());
                        orderLineList.add(orderLine);
                        return true;
                    }
                    return false;
                });

        if (!isValidOrder) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more products are invalid");
        }

        Order order = new Order();
        order.setCustomerId("ISTAD");
        order.setAddress(request.address());
        order.setDiscount(request.discount());
        order.setRemake(request.remark()); // Matches the 'remake' property name from your Order entity
        order.setOrderLines(orderLineList);
        order.setOrderDate(LocalDate.now());
        order.setStatus(false);

        order = orderRepository.save(order);

        return orderMapper.mapOrderToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        return ordersPage.map(orderMapper::mapOrderToOrderResponse);
    }

    @Override
    public OrderResponse findById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
        return orderMapper.mapOrderToOrderResponse(order);
    }

    @Override
    public void softDelete(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));

        // Note: Make sure an 'isDelete' or similar boolean property exists on your Order entity
        // order.setIsDelete(true);
        orderRepository.save(order);
    }

    @Override
    public void hardDelete(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public void updatePaymentStatus(UUID id, boolean status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));

        order.setStatus(status);
        orderRepository.save(order);
    }
}