package com.api.mercadeando.controller;

import com.api.mercadeando.service.AuthService;
import com.api.mercadeando.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.api.mercadeando.controller.Mappings.URL_BASE_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-19 12:10
 */
//TODO:
//    * CREAR PERMISO PARA SUBIR ARCHIVOS Y ASIGNAR AL ROL
//    * O USAR EL PERMISO DE WRITE_TABLE ?
@RestController
@RequestMapping(URL_BASE_V1)
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class FileStorageController {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private AuthService authService;

//    @PostMapping("/upload")
//    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("docType") String docType) throws DocumentStorageException {
//        String fileName = fileStorageService.storeFileLocally(file, authService.getCurrentUser().getId(), docType);
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/static/")
//                .path(fileName)
//                .toUriString();
//        return new UploadFileResponse(fileName, fileDownloadUri,file.getContentType(), file.getSize());
//    }

    /**
     * Permite acceder a un recuurso y descargarlo.
     * @param pathFileName nombre de un archivo existe
     * @param request HttpServletRequest peticion Http
     * @return ResponseEntity<Resource> archivo
     */
    @GetMapping("/static/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String pathFileName, HttpServletRequest request){
        String uriFileName = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/') + 1);;
        Resource resource = null;
        if(fileStorageService.doesItExists(uriFileName)) {
            try {
                resource = fileStorageService.loadFileAsResource(uriFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                //logger.info("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
