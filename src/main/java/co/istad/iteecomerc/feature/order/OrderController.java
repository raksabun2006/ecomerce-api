package co.istad.iteecomerc.feature.order;

import co.istad.iteecomerc.feature.order.dto.OrderRequest;
import co.istad.iteecomerc.feature.order.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createNew(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping
    public Page<OrderResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return orderService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable UUID id) {
        return orderService.findById(id);
    }


    @PutMapping("/{id}/soft-delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDelete(@PathVariable UUID id) {
        orderService.softDelete(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void hardDelete(@PathVariable UUID id) {
        orderService.hardDelete(id);
    }

    @PutMapping("/{id}/status")
    public void updatePaymentStatus(
            @PathVariable UUID id,
            @RequestParam boolean status
    ) {
        orderService.updatePaymentStatus(id, status);
    }
}