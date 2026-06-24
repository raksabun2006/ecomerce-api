package co.istad.iteecomerc.feature.product.dto;

import lombok.Builder;

@Builder
public record CategorySnippetResponse(
        Integer id ,
        String name
) {
}
