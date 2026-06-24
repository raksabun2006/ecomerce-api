package co.istad.iteecomerc.feature.file;

import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUplaodServiceImpl implements FileUploadService {

    private final FileUploadRepository fileUploadRepository;
    @Value("${file.storage-location}")
    private String storageLocation;

    @Value("${file.base-uri}")
    private String baseUri;
    private String getFormattedBaseUri() {
        return baseUri.endsWith("/") ? baseUri : baseUri + "/";
    }
    @Override
    public FileUplaodResponse Upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file format");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String name = UUID.randomUUID().toString() + "." + ext;

        try {
            Path folderPath = Paths.get(storageLocation);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            Path targetPath = folderPath.resolve(name);


            Files.copy(file.getInputStream(), targetPath);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File failed to upload: " + e.getMessage());
        }


        return FileUplaodResponse.builder()
                .name(name)
                .size(file.getSize())
                .mediaType(file.getContentType())
                .uri(baseUri + name)
                .build();
    }



    @Override
    public List<FileUplaodResponse> UploadMultiFiles(MultipartFile[] files) {
        List<FileUplaodResponse> responses = new ArrayList<>();
        Path rootPath = Paths.get(storageLocation);

        if (files == null || files.length == 0) {
            return responses;
        }

        try {
            Path folderPath = rootPath;
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            for (MultipartFile file : files) {
                if (file == null) continue;

                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || !originalFilename.contains(".")) {

                    continue;
                }

                String ext = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
                String name = UUID.randomUUID().toString() + "." + ext;

                Path targetPath = folderPath.resolve(name);

                // copy file to disk
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                FileUplaodResponse resp = FileUplaodResponse.builder()
                        .name(name)
                        .size(file.getSize())
                        .mediaType(file.getContentType())
                        .uri(getFormattedBaseUri() + name)
                        .build();

                responses.add(resp);
            }

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File failed to upload: " + e.getMessage());
        }

        return responses;
    }

    @Override
    public void deleteByName(String name) {

        Path targetPath = Paths.get(storageLocation).resolve(name);

        try {

            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            } else {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cannot find file on disk with name: " + name);
            }


        } catch (IOException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to delete file: " + e.getMessage());
        }
    }


}