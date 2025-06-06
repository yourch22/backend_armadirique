package com.api.muebleria.armadirique.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    // Puedes configurar la ruta de subida en application.properties
    // Ejemplo: file.upload-dir=./uploads
    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio donde se almacenarán los archivos subidos.", ex);
        }
    }

    /**
     * Almacena un archivo.
     * @param file Archivo MultipartFile.
     * @param prefix Prefijo para el nombre del archivo (ej. "producto_").
     * @return El nombre del archivo generado y almacenado.
     * @throws IOException Si ocurre un error al guardar el archivo.
     */
    public String storeFile(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío.");
        }

        // Normalizar nombre de archivo y generar uno único
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            // No hacer nada si no hay extensión o hay error
        }

        // Generar un nombre de archivo único para evitar colisiones y problemas con caracteres especiales.
        String generatedFileName = (prefix != null ? prefix : "") + UUID.randomUUID().toString() + fileExtension;

        Path targetLocation = this.fileStorageLocation.resolve(generatedFileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("No se pudo almacenar el archivo " + generatedFileName + ". Por favor, inténtelo de nuevo.", ex);
        }

        return generatedFileName;
    }

    /**
     * Carga un archivo como recurso.
     * (No implementado aquí, pero necesario si quieres servir los archivos directamente)
     * public Resource loadFileAsResource(String fileName) { ... }
     */

    /**
     * Elimina un archivo.
     * @param fileName Nombre del archivo a eliminar.
     * @return true si se eliminó, false en caso contrario.
     */
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            System.err.println("Error al eliminar el archivo: " + fileName + " - " + ex.getMessage());
            return false;
        }
    }
}
