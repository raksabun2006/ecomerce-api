package co.istad.iteecomerc.feature.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;


@Builder
public record OrderLineDto(
        @NotBlank(message = "Product code is required")
        String code,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer qty
) {
}