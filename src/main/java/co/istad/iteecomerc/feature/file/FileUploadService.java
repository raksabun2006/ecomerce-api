package co.istad.iteecomerc.feature.file;
import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface FileUploadService {
    FileUplaodResponse Upload(MultipartFile file);

    List<FileUplaodResponse> UploadMultiFiles(MultipartFile[] files);

    void deleteByName(String name);
    FileUplaodResponse findByName(String name);

    Page<FileUplaodResponse> findAll(int pageNumber, int pageSize);

}
