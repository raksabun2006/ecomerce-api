package co.istad.iteecomerc.feature.category.dto;

import lombok.Builder;
import java.util.List;


@Builder
public record CategoryResponse(
        Integer id,
        String name,
        String description,
        String icon,
        boolean isDeleted,
        Integer parentCategory,
        List<Integer> subCategories
) {

}