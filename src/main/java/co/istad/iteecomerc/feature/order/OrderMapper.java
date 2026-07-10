package co.istad.iteecomerc.feature.order;


import co.istad.iteecomerc.feature.order.dto.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "remark", source = "remake")
    @Mapping(target = "isDelete", ignore = true)
    OrderResponse mapOrderToOrderResponse(Order order);
}
