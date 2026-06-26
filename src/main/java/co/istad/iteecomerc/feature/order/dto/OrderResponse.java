package co.istad.iteecomerc.feature.order.dto;
//import co.istad.iteecomerc.feature.order.OrderLine;

import java.time.LocalDateTime;

//import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customer_id,
        Float discount ,
        String remark,
        String address,
        boolean status,
        LocalDateTime orderDate,
        boolean isDelete
//        List<OrderLine> orderLineList
) {
}
