package co.istad.iteecomerc.feature.file;

import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public FileUplaodResponse upload(@RequestPart MultipartFile file) {
        return fileUploadService.Upload(file);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/multiple")
    public List<FileUplaodResponse> uploadMultiple(@RequestParam("files") MultipartFile[] files) {
        return fileUploadService.UploadMultiFiles(files);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{name}")
    public void deleteByName(@PathVariable String name){
        fileUploadService.deleteByName(name);
    }

    @GetMapping("{name}")
    public FileUplaodResponse getByName(@PathVariable String name){
        return fileUploadService.findByName(name);
    }

    @GetMapping
    public Page<FileUplaodResponse> findAll(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "25") int pageSize
    ) {
        return fileUploadService.findAll(pageNumber, pageSize);
    }
}