package co.istad.iteecomerc.feature.order;


import co.istad.iteecomerc.feature.order.dto.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse mapOrderToOrderResponse(Order order);
}
