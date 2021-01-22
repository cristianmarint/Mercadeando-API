package com.api.mercadeando.domain.data;

import com.api.mercadeando.domain.dto.CategoriaRequest;
import com.api.mercadeando.domain.dto.CategoriaResponse;
import com.api.mercadeando.domain.dto.CategoriasResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:39
 */
public interface CategoriaData {
    CategoriasResponse getCategorias(int offset, int limit);

    CategoriaResponse getCategoria(Long categoriaId) throws ResourceNotFoundException, BadRequestException;

    CategoriaResponse addCategoria(CategoriaRequest request) throws BadRequestException;

    void editCategoria(Long categoriaId, CategoriaRequest request) throws ResourceNotFoundException, BadRequestException;

    void deleteCategoria(Long categoriaId) throws BadRequestException, ResourceNotFoundException;

    ProductosResponse getCategoriaProductos(Long categoriaId) throws BadRequestException, ResourceNotFoundException;
}
