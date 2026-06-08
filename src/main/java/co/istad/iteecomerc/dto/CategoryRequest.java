package co.istad.iteecomerc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

//        @NotBlank(message = "Icon is required")
        @Size(max=255)
        String icon,
        @Positive
        Integer parentCategory

) {}