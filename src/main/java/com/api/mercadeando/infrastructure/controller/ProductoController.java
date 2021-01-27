package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.ProductoRequest;
import com.api.mercadeando.domain.dto.ProductoResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.dto.UploadFileResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.DocumentStorageException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.domain.service.AuthService;
import com.api.mercadeando.domain.service.FileStorageService;
import com.api.mercadeando.domain.service.ProductoService;
import com.api.mercadeando.infrastructure.persistence.entity.FileStorage;
import com.api.mercadeando.infrastructure.persistence.entity.FileStorageDocumentType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-19 9:16
 */
@RestController
@RequestMapping(URL_PRODUCTOS_V1)
@AllArgsConstructor
@Slf4j
public class ProductoController {
    private final ProductoService productoService;
    private final FileStorageService fileStorageService;
    private final AuthService authService;

    /**
     * Encuentra un producto especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     * @param productoId Id de un producto registrado
     * @return ResponseEntity<ProductoResponse> cuando el producto es encontrado y sus datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @GetMapping(value = "/{productoId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoResponse> readProducto(@PathVariable("productoId") @Min(1) Long productoId){
        try{
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productoService.readProducto(productoId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Encuentra todos los producto y responde en JSON si se cuenta con el permiso
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ResposeEntity<ProductosResponse> Con los productos en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductosResponse> readProductos(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ){
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(productoService.readProductos(offset,limit));
    }

    /**
     * Permite crear un producto especificando sus datos si se cuenta con el permiso
     * @param request ProductoRequest con datos necesarios para crear un producto
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('ADD_PRODUCTO')")
    @PostMapping
    public ResponseEntity addProducto(@RequestBody @Valid ProductoRequest request){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(productoService.addProducto(request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Permite actualizar un producto registrado si se cuenta con el permiso
     * @param productoId Id de un producto registrado
     * @param request ProductoRequest con los datos nuevos
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('EDIT_PRODUCTO')")
    @PutMapping(value = "/{productoId}")
    public ResponseEntity<String> editProducto(@PathVariable("productoId") @Min(1) Long productoId, @RequestBody @Valid ProductoRequest request){
        try{
            productoService.editProducto(productoId,request);
            return ResponseEntity.ok().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Permite subir una foto a un producto si cuenta con el permiso
     * @param productoId Id de un producto existente
     * @param uploadedFile (MIME) foto
     * @return ResponseEntity<> JSON con los datos de la imagen subida
     * @throws DocumentStorageException Cuando existe un problema almacenando la foto
     * @throws BadRequestException Cuando el formato de la foto no es permitido
     * @throws ResourceNotFoundException Cuando el producto no existe
     */
    @PreAuthorize("hasAuthority('EDIT_PRODUCTO')")
    @PostMapping("/{productoId}/foto")
    public ResponseEntity addFoto(@PathVariable("productoId") @Min(1) Long productoId, @RequestParam("file") MultipartFile uploadedFile) throws DocumentStorageException, BadRequestException, ResourceNotFoundException {
        if (productoId==null) throw new BadRequestException("ProductoId no puede ser Null");
        List<String> allowedMimeTypes = new ArrayList<>();
        allowedMimeTypes.add("image/jpg");
        allowedMimeTypes.add("image/jpeg");
        allowedMimeTypes.add("image/png");

        boolean permitido=false;
        for (String l:allowedMimeTypes
             ) {
            if(uploadedFile.getContentType().equalsIgnoreCase(l)){
                permitido = true;
                break;
            }
        }

        if (permitido){
            FileStorage file = fileStorageService.storeFileLocally(uploadedFile, FileStorageDocumentType.FOTO_PRODUCTO);
            productoService.addFoto(productoId,file);
            return ResponseEntity.ok().body(new UploadFileResponse(file.getFileName(), file.getFileUrl(), uploadedFile.getContentType(), uploadedFile.getSize()));
        }else {
            return ResponseEntity.badRequest().body("La foto del producto solo puede ser: "+allowedMimeTypes);
        }
    }

    /**
     * Permite borrar una foto de BD y sistema de archivos local si cuenta con el permiso
     * @param productoId Id de un producto registrado
     * @param nombreArchivo Nombre de una foto existente
     * @param request HttpServletRequest
     * @return ResponseEntity con estado Http y mensaje segun corresponda
     */
    @PreAuthorize("hasAuthority('EDIT_PRODUCTO')")
    @DeleteMapping("/{productoId}/foto/{nombreArchivo}")
    public ResponseEntity deleteFoto(@PathVariable("productoId") @Min(1) Long productoId, @PathVariable("nombreArchivo") String nombreArchivo, HttpServletRequest request) {
        if (productoId==null) return ResponseEntity.badRequest().body("ProductoId no puede ser Null");
        if (nombreArchivo==null || nombreArchivo.isEmpty()) return ResponseEntity.badRequest().body("nombreArchivo no puede ser Null");
        String uriFileName = request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/') + 1);
        try {
            productoService.deleteFoto(productoId,uriFileName);
            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
