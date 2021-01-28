package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.CategoriaData;
import com.api.mercadeando.domain.dto.CategoriaRequest;
import com.api.mercadeando.domain.dto.CategoriaResponse;
import com.api.mercadeando.domain.dto.CategoriasResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.jpa.CategoriaJPARepository;
import com.api.mercadeando.infrastructure.persistence.mapper.CategoriaMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:38
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaService {
    @Autowired
    private CategoriaData categoriaData;
    @Autowired
    private CategoriaJPARepository categoriaJPARepository;
    @Autowired
    private CategoriaMapper categoriaMapper;

    /**
     * Encuentra todos los categorias y responde en JSON si se cuenta con el permiso
     *
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit  Cantidad de valores a entontrar menor a cien
     * @return CategoriasResponse Con los categorias en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_CATEGORIA')")
    @Transactional(readOnly = true)
    public CategoriasResponse readCategorias(int offset, int limit) {
        if (offset < 0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit < 0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit > 100) throw new MercadeandoException("Offset must be less than one hundred 100");
        return categoriaData.getCategorias(offset, limit);
    }

    /**
     * Encuentra un categoria especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     *
     * @param categoriaId Id de un categoria registrado
     * @return CategoriaResponse con datos correspondientes
     * @throws ResourceNotFoundException cuando el categoria no es encontrado
     */
    @PreAuthorize("hasAuthority('READ_CATEGORIA')")
    public CategoriaResponse readCategoria(Long categoriaId) throws ResourceNotFoundException, BadRequestException {
        if (categoriaId == null) throw new MercadeandoException("CategoriaId no puede ser Null");
        return categoriaData.getCategoria(categoriaId);
    }

    /**
     * Permite ver los productos vinculados a una categoria
     *
     * @param categoriaId Id de un categoria registrado
     * @return ProductosResponse productos vinculadosa una categoria
     * @throws BadRequestException       cuando CategoriaId es Null
     * @throws ResourceNotFoundException cuando el Categoria no esta registrado
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    public ProductosResponse getCategoriaProductos(Long categoriaId) throws BadRequestException, ResourceNotFoundException {
        return categoriaData.getCategoriaProductos(categoriaId);
    }

    /**
     * Permite crear un categoria especificando sus datos si se cuenta con el permiso
     *
     * @param request Datos necesarios para crear categoria
     * @return CategoriaResponse con los datos en formato JSON
     * @throws BadRequestException cuando faltan datos necesario
     */
    @PreAuthorize("hasAuthority('ADD_CATEGORIA')")
    public CategoriaResponse addCategoria(@Valid CategoriaRequest request) throws BadRequestException {
        validarCategoria(request);
        return categoriaData.addCategoria(request);
    }

    /**
     * Permite actualizar los datos de un categoria registrado si se cuenta con el permiso
     *
     * @param categoriaId Id de un categoria registrado
     * @param request     CategoriaRequest con los datos modificados
     * @throws ResourceNotFoundException cuando el recuerso no existe
     * @throws BadRequestException       cuando existen valores incorrectos.
     */
    @PreAuthorize("hasAuthority('EDIT_CATEGORIA')")
    public void editCategoria(Long categoriaId, CategoriaRequest request) throws ResourceNotFoundException, BadRequestException {
        validarCategoria(request);
        categoriaData.editCategoria(categoriaId, request);
    }

    /**
     * Actualiza el estado de un categoria registrado si se cuenta con el permiso
     *
     * @param categoriaId Id de un categoria registrado
     * @throws BadRequestException       cuando CategoriaId es Null
     * @throws ResourceNotFoundException cuando el Categoria no esta registrado
     */
    @PreAuthorize("hasAuthority('DELETE_CATEGORIA')")
    public void deleteCategoria(Long categoriaId) throws BadRequestException, ResourceNotFoundException {
        categoriaData.deleteCategoria(categoriaId);
    }

    /**
     * Permite validar campos necesarios
     *
     * @param request entidad a verificar
     * @throws BadRequestException cuando existen valores en NUll
     */
    private void validarCategoria(CategoriaRequest request) throws BadRequestException {
        if (request.getNombre() == null) throw new BadRequestException("El nombre de la categoria no puede ser Null");
        if (request.getDescripcion() == null)
            throw new BadRequestException("La descripción de la categoria no puede ser Null");
    }
}