package co.istad.iteecomerc.feature.product.dto;

import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ProductResponse(

        String code,
        String slug ,
        String name ,
        String description,
        String thumbnail,
        BigDecimal unitPrice,
        Integer qty,
        boolean isAvailable,
        CategorySnippetResponse parentCategory
) {
}
