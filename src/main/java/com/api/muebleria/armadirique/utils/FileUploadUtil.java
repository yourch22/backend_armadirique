package com.api.muebleria.armadirique.utils;

import org.springframework.stereotype.Component; // <-- ¡Añade esta importación!
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component // <-- ¡Añade esta anotación!
public class FileUploadUtil {

    // Tu método saveFile está perfecto como no estático para la inyección
    public String saveFile(String uploadDir,String subFolder, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir,subFolder);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + fileExtension; // Generate unique file name

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return Paths.get(subFolder, fileName).toString().replace("\\", "/");
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    // Este método es estático y no será mockeado por @Mock fileUploadUtil
    // Si necesitas testearlo o verificar llamadas, tendrías que usar PowerMock (desaconsejado)
    // o refactorizarlo también a no estático si es parte de la lógica de negocio del bean.
    public static boolean deleteFile(String uploadDir, String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        return Files.deleteIfExists(filePath);
    }
}