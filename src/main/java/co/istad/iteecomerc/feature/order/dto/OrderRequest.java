package co.istad.iteecomerc.feature.order.dto;

import co.istad.iteecomerc.feature.order.OrderLine;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotBlank(message = "is require")
        String customer_id,
        @NotNull(message = "discount is require")
        @Min(0)
        @Max((100))
        Float discount ,
        @NotNull(message = "is require")
        String address,
        @NotBlank(message = "is require")
        String remark,
        List<OrderLine> orderLineList

) {
}
