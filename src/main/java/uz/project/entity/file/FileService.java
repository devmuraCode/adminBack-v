package uz.project.entity.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import uz.project.common.constant.FileType;
import uz.project.common.constant.Status;
import uz.project.common.exception.ApiException;
import uz.project.common.response.status.FileStatus;
import uz.project.common.response.status.SystemStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FileService {
    private final FileRepository fileRepository;

    private static final Set<String> blackListTypes;

    static {
        blackListTypes = new HashSet<>();
        blackListTypes.add("JAR");
        blackListTypes.add("SQL");
        blackListTypes.add("TXT");
    }

    @Value("${upload.server.folder}")
    private String uploadFolderPath;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Optional<File> findById(Long id) {
        if (id == null) return Optional.empty();
        return fileRepository.findById(id);
    }

    public Optional<File> findByUuid(UUID uuid) {
        if (uuid == null) return Optional.empty();
        return fileRepository.findByUuid(uuid);
    }

    @Transactional(readOnly = true)
    public File getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(FileStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public File getByUuid(UUID uuid) {
        return findByUuid(uuid).orElseThrow(() -> new ApiException(FileStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<File> getByIds(List<Long> ids) {
        return fileRepository.findAllById(ids);
    }

    @Transactional(noRollbackFor = ApiException.class)
    public File upload(MultipartFile multipartFile, FileType type) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new ApiException(SystemStatus.MULTIPART_FILE_NOT_FOUND);
        }
        String extension = getExtension(multipartFile.getOriginalFilename());
        if (checkExtension(extension)) {
            throw new ApiException(FileStatus.TYPE_NOT_SUPPORTED);
        }
        Date date = new Date();
        Long time = date.getTime();

        String filename = time + "." + extension;
        String directory = getPathForUpload();
        Path filePath = Paths.get(directory, filename);
        try {
            Files.copy(multipartFile.getInputStream(), filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File();
        file.setName(multipartFile.getOriginalFilename());
        file.setType(type);
        file.setExtension(extension);
        file.setSize(multipartFile.getSize());
        file.setUploadPath(directory + "/" + filename);
        file.setStatus(Status.ACTIVE);
        return fileRepository.saveAndFlush(file);
    }

    @Transactional
    public File changeType(Long id, FileType type) {
        File file = getById(id);
        if (type.equals(file.getType())) {
            return file;
        }
        file.setType(type);
        return fileRepository.saveAndFlush(file);
    }

    @Transactional
    public List<File> changeType(List<Long> ids, FileType type) {
        List<File> files = getByIds(ids);
        for (File file : files) {
            file.setType(type);
        }
        return fileRepository.saveAllAndFlush(files);
    }

    public ResponseEntity<Resource> download(File file) {
        Resource fileAsResource = getFileAsResource(file);
        if (fileAsResource == null) return null;

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getSize()));

        if (file.getExtension() != null) {
            switch (file.getExtension().toLowerCase()) {
                case "png" -> headers.add(HttpHeaders.CONTENT_TYPE, "image/png");
                case "svg" -> headers.add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");
                case "jpeg", "jpg" -> headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
                case "gif" -> headers.add(HttpHeaders.CONTENT_TYPE, "image/gif");
                case "pdf" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
                case "zip" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
                case "rar" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/x-rar-compressed");
                case "doc" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/msword");
                case "docx" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                case "xls" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel");
                case "xlsx" -> headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                default -> headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            }
        }
        return new ResponseEntity<>(fileAsResource, headers, HttpStatus.OK);
    }

    @Transactional
    public void delete(Long id) {
        fileRepository.deleteById(id);
    }

    public Resource getFileAsResource(File file) {
        try {
            String path = file.getUploadPath();
            Path filePath = Paths.get(path);
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Could not read file: #{} ", path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("all")
    private String getPathForUpload() {
        LocalDate now = LocalDate.now();
        String path = new StringBuilder()
                .append(this.uploadFolderPath).append("/")
                .append(now.getYear()).append("_YEAR").append("/")
                .append(now.getMonthValue()).append("_MONTH").append("/")
                .append(now.getDayOfMonth()).append("_DAY").append("/")
                .toString();

        java.io.File root = new java.io.File(path);
        if (!root.exists() || !root.isDirectory()) {
            root.mkdirs();
        }
        return path;
    }

    public String getExtension(String filename) {
        String extension = null;
        if (filename != null && !filename.isEmpty()) {
            int dot = filename.lastIndexOf('.');
            if (dot > 0 && dot <= filename.length() - 2) {
                extension = filename.substring(dot + 1);
            }
        }
        return extension;
    }

    public Boolean checkExtension(String extension) {
        return blackListTypes.contains(extension.toUpperCase());
    }
}
