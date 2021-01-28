package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.CategoriaRequest;
import com.api.mercadeando.domain.dto.CategoriaResponse;
import com.api.mercadeando.domain.dto.CategoriasResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.domain.service.CategoriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CATEGORIAS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:53
 */
@RestController
@RequestMapping(URL_CATEGORIAS_V1)
@AllArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    /**
     * Encuentra todos los clientes y responde en JSON si se cuenta con el permiso
     *
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit  Cantidad de valores a entontrar menor a cien
     * @return ResposeEntity<CategoriasResponse> Con los clientes en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_CATEGORIA')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriasResponse> readCategorias(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ) {
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(categoriaService.readCategorias(offset, limit));
    }

    /**
     * Encuentra un Categoria especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     *
     * @param categoriaId Id de un Categoria registrado
     * @return ResponseEntity<CategoriaResponse> cuando el Categoria es encontrado y sus datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_CATEGORIA')")
    @GetMapping(value = "/{categoriaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriaResponse> readCategoria(@PathVariable(value = "categoriaId") @Min(1) Long categoriaId) {
        try {
            if (categoriaId == null) throw new BadRequestException();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(categoriaService.readCategoria(categoriaId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Permite crear un Categoria especificando sus datos si se cuenta con el permiso
     *
     * @param request CategoriaRequest con datos necesarios para crear un Categoria
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('ADD_CATEGORIA')")
    @PostMapping
    public ResponseEntity<?> addCategoria(@RequestBody @Valid CategoriaRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.addCategoria(request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Permite actualizar un Categoria registrado si se cuenta con el permiso
     *
     * @param categoriaId Id de un Categoria registrado
     * @param request     CategoriaRequest con los datos nuevos
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('EDIT_CATEGORIA')")
    @PutMapping(value = "/{categoriaId}")
    public ResponseEntity<String> editCategoria(@PathVariable("categoriaId") @Min(1) Long categoriaId, @RequestBody @Valid CategoriaRequest request) {
        try {
            categoriaService.editCategoria(categoriaId, request);
            return ResponseEntity.ok().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Permite actializar el estado de un Categoria (Softdelete) si se cuenta con el permiso
     *
     * @param categoriaId Id de un Categoria registrado
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('DELETE_CATEGORIA')")
    @DeleteMapping(value = "/{categoriaId}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable("categoriaId") @Min(1) Long categoriaId) {
        try {
            categoriaService.deleteCategoria(categoriaId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Encuentra todos los producto de una categoria y responde en JSON si se cuenta con el permiso
     *
     * @param categoriaId Id de una categoria con productos
     * @return ResposeEntity<ProductosResponse> Con los productos en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @GetMapping(value = "/{categoriaId}/productos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductosResponse> getCategoriaProductos(@PathVariable("categoriaId") Long categoriaId) {
        try {
            return ResponseEntity.ok().body(categoriaService.getCategoriaProductos(categoriaId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
