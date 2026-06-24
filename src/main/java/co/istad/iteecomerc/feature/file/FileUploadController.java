package co.istad.iteecomerc.feature.file;

import co.istad.iteecomerc.feature.file.dto.FileUplaodResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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


}