package co.istad.iteecomerc.feature.product.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "please input name")
        @Size(max = 255)
        String name,
        @NotBlank(message = "Please input the des")
        @Size(max = 500)
        String description,
        @Size(max = 255)
        @NotBlank(message = "Input thumbnail")
        String thumbnail,
        @Positive
        @NotNull(message = "is require")
        BigDecimal unitPrice,
        @Min(0)
        @NotNull(message = "input qty")
        Integer qty,
        @Positive
        @NotNull(message = "category is required")
        Integer parentCategory

){
}
