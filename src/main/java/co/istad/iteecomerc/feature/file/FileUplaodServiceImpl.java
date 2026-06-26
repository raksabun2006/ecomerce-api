package co.istad.iteecomerc.feature.file;

import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final FileUploadMapper fileUplaodMapper;
    @Value("${file.storage-location}")
    private String storageLocation;

    @Value("${file.base-uri}")
    private String baseUri;
    private String getFormattedBaseUri() {
        return baseUri.endsWith("/") ? baseUri : baseUri + "/";
    }
    @Override
    public FileUplaodResponse Upload(MultipartFile files) {
        String originalFilename = files.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file format");
        }

        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String name = UUID.randomUUID().toString() ;

        FileUplaod fileUplaod = new FileUplaod();
        fileUplaod.setName(name);
        fileUplaod.setExtension(ext);
        fileUplaod.setCaption("Hello Spring boot from junior");
        fileUplaod.setMediaType(files.getContentType());
        fileUplaod.setSize(files.getSize());
        fileUploadRepository.save(fileUplaod);


        try {
            Path folderPath = Paths.get(storageLocation);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            Path targetPath = folderPath.resolve(name);


            Files.copy(files.getInputStream(), targetPath);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "File failed to upload: " + e.getMessage());
        }

        return FileUplaodResponse.builder()
                .name(name)
                .size(files.getSize())
                .mediaType(files.getContentType())
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
                String name = UUID.randomUUID().toString();
                FileUplaod fileUplaod = new FileUplaod();
                fileUplaod.setName(name);
                fileUplaod.setExtension(ext);
                fileUplaod.setCaption("Hello Spring boot from junior");
                fileUplaod.setMediaType(file.getContentType());
                fileUplaod.setSize(file.getSize());
                fileUploadRepository.save(fileUplaod);
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

    @Override
    public FileUplaodResponse findByName(String name) {
        return fileUploadRepository.findByName(name)
                .map(fileUplaodMapper::mapFileUploadToFileUploadResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File has not been found"));
    }


    @Override
    public Page<FileUplaodResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<FileUplaod> fileUploadResponses = fileUploadRepository.findAll(pageRequest);

        return fileUploadResponses.map(fileUplaodMapper::mapFileUploadToFileUploadResponse);
    }


}