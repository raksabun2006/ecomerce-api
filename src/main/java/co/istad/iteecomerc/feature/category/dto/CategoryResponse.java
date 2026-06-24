package co.istad.iteecomerc.feature.category.dto;

import lombok.Builder;


@Builder
public record CategoryResponse(
        Integer id,
        String name,
        String description,
        String icon,
        boolean isDeleted,
        Integer parentCategory
) {}