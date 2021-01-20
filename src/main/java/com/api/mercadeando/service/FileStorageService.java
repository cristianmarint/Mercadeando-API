package com.api.mercadeando.service;

import com.api.mercadeando.entity.FileStorage;
import com.api.mercadeando.entity.FileStorageDocumentType;
import com.api.mercadeando.exception.DocumentStorageException;
import com.api.mercadeando.repository.FileStorageRepository;
import com.api.mercadeando.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;


/**
 * @author cristianmarint
 * @Date 2021-01-19 11:48
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FileStorageService {
    private Path fileStorageLocation;

    @Autowired
    private FileStorageRepository fileStorageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @Autowired
    public FileStorageService(FileStorage fileStorage) throws DocumentStorageException {
        this.fileStorageLocation = Paths.get(fileStorage.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new DocumentStorageException("No se pudo crear el directorio donde los archivos seran almacenadaos", ex);
        }
    }

    /**
     * Permite almacenar un archivo de forma local
     * la ruta se define en application.properties
     * 
     * @param file MultipartFile 
     * @param documentType tipo de documento, información para metadatos
     * @return FileStorage archivo creado
     * @throws DocumentStorageException
     */
    public FileStorage storeFileLocally(MultipartFile file, FileStorageDocumentType documentType) throws DocumentStorageException {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = "";
        try {
            // Check if the file's name contains invalid characters
            if(originalFileName.contains("..")) {
                throw new DocumentStorageException("El nombre de archivo contiene una secuencia de ruta no válida" + originalFileName);
            }
            String fileExtension;
            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch(Exception e) {
                fileExtension = "";
            }

            fileName = UUID.randomUUID().toString()+""+ fileExtension;
            //Crea la url para acceder
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/static/").path(fileName).toUriString();

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileStorage doc = new FileStorage();
            doc.setUserId(authService.getCurrentUser().getId());
            doc.setDocumentFormat(file.getContentType());
            doc.setFileName(fileName);
            doc.setDocumentType(documentType);
            doc.setFileUrl(url);
            doc.setFileSize(file.getSize());

            fileStorageRepository.save(doc);

            return doc;
        } catch (IOException | DocumentStorageException ex) {
            throw new DocumentStorageException("No se pudo almacenar el archivo " + fileName + ". Vuelve a intentarlo!", ex);
        }
    }

    public Boolean deleteFileLocally(String fileName) throws Exception {
        Resource file = this.loadFileAsResource(fileName);
        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        log.info(String.valueOf(targetLocation));

        Files.delete(targetLocation);

        if(this.loadFileAsResource(fileName).exists()){
            return true;
        }
        return false;
    }


    /**
     * Permite cargar un archivo guardado
     * @param fileName nombre del archivo
     * @return Resource archivo encontrado
     * @throws Exception
     */
    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("Archivo no encontrado " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Archivo no encontrado" + fileName);
        }
    }

    /**
     * Busca un archivo por nombre.
     * @param fileName nombre de un archivo
     * @return Boolean true si el archivo es encontrado, false si no.
     */
    public Boolean doesItExists(String fileName) {
        return fileStorageRepository.findByFileName(fileName);
    }
}
