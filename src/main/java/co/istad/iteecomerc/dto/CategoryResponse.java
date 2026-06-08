package co.istad.iteecomerc.dto;

import lombok.Builder;
import org.mapstruct.Mapper;

@Builder
public record CategoryResponse(
        Integer id,
        String name,
        String description,
        String icon,
        boolean isDeleted,
        Integer parentCategory
) {}