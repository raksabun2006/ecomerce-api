package co.istad.iteecomerc.feature.order.dto;

import java.time.LocalDateTime;
import java.util.UUID;


public record OrderResponse(
        UUID uuid,
        String customerId,
        Float discount ,
        String remark,
        String address,
        boolean status,
        LocalDateTime orderDate,
        boolean isDelete
) {
}
