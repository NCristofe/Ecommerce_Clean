package com.dm.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload-dir:uploads/produtos}")
    private String uploadDir;

    @Value("${file.max-size:5242880}")
    private long maxFileSize;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    public FileUploadService() {
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio. Por favor, selecione um arquivo.");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 5MB");
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);

        if (!isAllowedExtension(fileExtension)) {
            throw new IllegalArgumentException("Tipo de arquivo não permitido. Use: jpg, jpeg, png, gif, webp");
        }

        if (!isAllowedMimeType(file.getContentType())) {
            throw new IllegalArgumentException("Tipo MIME não permitido.");
        }

        // Create upload directory if not exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String uniqueFileName = UUID.randomUUID() + "." + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFileName);

        // Save file
        Files.copy(file.getInputStream(), filePath);

        return uniqueFileName;
    }

    public void deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("Arquivo não encontrado: " + fileName);
        }

        return Files.readAllBytes(filePath);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("Arquivo sem extensão válida.");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private boolean isAllowedMimeType(String mimeType) {
        return mimeType != null && ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase());
    }
}