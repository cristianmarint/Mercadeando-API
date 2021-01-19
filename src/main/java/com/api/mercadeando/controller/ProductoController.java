package com.api.mercadeando.controller;

import com.api.mercadeando.dto.ProductoResponse;
import com.api.mercadeando.dto.ProductosResponse;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.service.ProductoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static com.api.mercadeando.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-19 9:16
 */
@RestController
@RequestMapping(URL_PRODUCTOS_V1)
@AllArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    /**
     * Encuentra un producto especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     * @param productoId Id de un producto registrado
     * @return ResponseEntity<ProductoResponse> cuando el producto es encontrado y sus datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @GetMapping(value = "/{productoId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductoResponse> getProducto(@PathVariable("productoId") @Min(1) Long productoId){
        try{
            if (productoId==null) throw new BadRequestException();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productoService.getProducto(productoId));
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
    public ResponseEntity<ProductosResponse> getProductos(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ){
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(productoService.getProductos(offset,limit));
    }
}
