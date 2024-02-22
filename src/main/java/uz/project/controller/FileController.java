package uz.project.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.project.common.constant.FileType;
import uz.project.common.exception.ApiException;
import uz.project.common.response.SuccessfulResponse;
import uz.project.common.response.status.FileStatus;
import uz.project.entity.file.FileResponse;
import uz.project.entity.file.FileService;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{id}")
    public SuccessfulResponse<FileResponse> getById(@PathVariable Long id) {
        FileResponse response = new FileResponse(fileService.getById(id));
        return new SuccessfulResponse<>(response);
    }

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public SuccessfulResponse<FileResponse> upload(@RequestParam("file") MultipartFile multipartFile) {
        FileResponse response = new FileResponse(fileService.upload(multipartFile, FileType.DRAFT));
        return new SuccessfulResponse<>(response);
    }

    @DeleteMapping("/{id}")
    public SuccessfulResponse<?> delete(@PathVariable Long id) throws Exception {
        fileService.delete(id);
        throw new ApiException(FileStatus.DELETED);
    }
}
