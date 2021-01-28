package com.api.mercadeando.domain.data;

import com.api.mercadeando.domain.dto.ProductoRequest;
import com.api.mercadeando.domain.dto.ProductoResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.FileStorage;

/**
 * @author cristianmarint
 * @Date 2021-01-21 3:28
 */
public interface ProductoData {
    ProductoResponse readProducto(Long productoId) throws BadRequestException, ResourceNotFoundException;

    ProductosResponse readProductos(int offset, int limit);

    ProductoResponse addProducto(ProductoRequest request) throws BadRequestException;

    void editProducto(Long productoId, ProductoRequest request) throws BadRequestException, ResourceNotFoundException;

    void deleteFoto(Long productoId, String nombreArchivo) throws BadRequestException, ResourceNotFoundException;

    void addFoto(Long productoId, FileStorage foto) throws BadRequestException, ResourceNotFoundException;
}
