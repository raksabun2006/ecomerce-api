package co.istad.iteecomerc.feature.order;

import co.istad.iteecomerc.feature.order.dto.OrderLineDto;
import co.istad.iteecomerc.feature.order.dto.OrderRequest;
import co.istad.iteecomerc.feature.order.dto.OrderResponse;
import co.istad.iteecomerc.feature.product.Product;
import co.istad.iteecomerc.feature.product.ProductRepository;
import co.istad.iteecomerc.security.SecurityUtils;
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
    OrderLine orderLine = new OrderLine();
    @Override
    public OrderResponse create(OrderRequest request) {
        if (request.orderLineList() == null || request.orderLineList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order lines cannot be empty");
        }


        Order order = new Order();
        order.setCustomerId(SecurityUtils.extractUserId());
        order.setAddress(request.address());
        order.setDiscount(request.discount());
        order.setRemake(request.remark());
        order.setOrderDate(LocalDate.now());
        order.setStatus(false);

        List<OrderLine> orderLineList = new ArrayList<>();

        for (OrderLineDto dto : request.orderLineList()) {
            Product product = productRepository.findByCode(dto.code())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Product not found with code: " + dto.code()
                    ));

            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setCode(dto.code());
            orderLine.setQty(dto.qty());

            // Take price dynamically from Product table to prevent fraud/null constraint violations
            orderLine.setUnitPrice(product.getUnitPrice());

            // CRITICAL FIX: Link the line back to the parent order to populate order_uuid
            orderLine.setOrder(order);

            orderLineList.add(orderLine);
        }

        // 3. Link the populated list to your order object
        order.setOrderLines(orderLineList);

        // 4. Persist to Database safely
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