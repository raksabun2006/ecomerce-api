package co.istad.iteecomerc.feature.order.dto;

import co.istad.iteecomerc.feature.order.OrderLine;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderLineDto (
        String code ,
        Integer qty,
        BigDecimal unitPrice
){
}
