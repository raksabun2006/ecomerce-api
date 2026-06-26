package co.istad.iteecomerc.feature.file.dto;

import lombok.Builder;

@Builder
public record FileUplaodResponse(
        String name,
        String caption,
        Long size,
        String mediaType,
        String extension,
        String uri,
        String downloadUri
) {
}
