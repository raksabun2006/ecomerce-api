package co.istad.iteecomerc.feature.file;


import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    @Value("${file.storage-location}")
    FileUplaodResponse Upload(MultipartFile file);
    List<FileUplaodResponse> UploadMultiFiles(MultipartFile[] files);
    void deleteByName(String name);

}
