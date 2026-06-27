package co.istad.iteecomerc.feature.order.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

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

        @NotEmpty(message = "Order lines cannot be empty")
        @Valid
        List<OrderLineDto> orderLineList
) {
}