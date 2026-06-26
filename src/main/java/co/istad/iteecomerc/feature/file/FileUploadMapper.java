package co.istad.iteecomerc.feature.file;

import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUploadMapper {

    @Value("${file.base-uri}")
    private String baseUri;

    private String getFormattedBaseUri() {
        return baseUri.endsWith("/") ? baseUri : baseUri + "/";
    }

    public FileUplaodResponse mapFileUploadToFileUploadResponse(FileUplaod fileUplaod) {
        return FileUplaodResponse.builder()
                .name(fileUplaod.getName())
                .caption(fileUplaod.getCaption())
                .extension(fileUplaod.getExtension())
                .size(fileUplaod.getSize())
                .mediaType(fileUplaod.getMediaType())
                .uri(getFormattedBaseUri() + fileUplaod.getName())
                .downloadUri(getFormattedBaseUri() + fileUplaod.getName())
                .build();
    }
}