package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.ProductoData;
import com.api.mercadeando.domain.dto.ProductoRequest;
import com.api.mercadeando.domain.dto.ProductoResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.FileStorage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

/**
 * @author cristianmarint
 * @Date 2021-01-19 8:45
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProductoService {
    @Autowired
    private ProductoData productoData;
    /**
     * Crea una respuesta Json mapeado los datos de ProductoData a una respuesta detallada
     * @param productoId Id de un producto existente
     * @return ProductoResponse con los datos en formato JSON
     * @throws ResourceNotFoundException cuando el producto no es encontrado
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @Transactional(readOnly = true)
    public ProductoResponse readProducto(Long productoId) throws ResourceNotFoundException, BadRequestException {
        return productoData.readProducto(productoId);
    }

    /**
     * Crea una respuesta Json mapeando los datos en una lista de productos
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ProductosResponse con datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @Transactional(readOnly = true)
    public ProductosResponse readProductos(int offset, int limit){
        return productoData.readProductos(offset, limit);
    }

    /**
     * Permite crear un producto especificando sus datos si se cuenta con el permiso
     * @param request Datos necesario para crear un producto
     * @return ProductoResponse con los datos en formato JSON
     * @throws BadRequestException cuando faltan datos necesarios
     */
    @PreAuthorize("hasAuthority('ADD_PRODUCTO')")
    public ProductoResponse addProducto(@Valid ProductoRequest request) throws BadRequestException {
        return productoData.addProducto(request);
    }

    /**
     * Permite actualizar los datos de un producto registrado si se cuenta con el permiso
     * @param productoId Id de un producto registrado
     * @param request ProductoRequest con los datos modificados
     * @throws BadRequestException cuando existen valores incorrectos
     * @throws ResourceNotFoundException cuando el recurso no existe
     */
    @PreAuthorize("hasAuthority('EDIT_PRODUCTO')")
    public void editProducto(Long productoId, ProductoRequest request) throws BadRequestException, ResourceNotFoundException {
        productoData.editProducto(productoId,request);
    }

    /**
     * Permite agregar una foto a un producto
     * @param productoId Id de un producto registrado
     * @param foto FileStorage (foto) con formato valido
     * @throws BadRequestException Cuando el Id del producto es Null
     * @throws ResourceNotFoundException Cuando el producto no es encontrado
     */
    @PreAuthorize("hasAuthority('EDIT_PRODUCTO')")
    public void addFoto(Long productoId, FileStorage foto) throws BadRequestException, ResourceNotFoundException {
        productoData.addFoto(productoId,foto);
    }

    /**
     * Permite borrar una foto de BD y sistema de archivos local si cuenta con el permiso
     * @param productoId Id de un producto registrado
     * @param nombreArchivo Nombre de una foto existente
     * @throws BadRequestException cuando productoId es NUll
     * @throws ResourceNotFoundException Cuando no se encuentra un recurso
     */
    @PreAuthorize("hasAuthority('EDIT_PRODUCTO')")
    public void deleteFoto(Long productoId, String nombreArchivo) throws BadRequestException, ResourceNotFoundException {
        productoData.deleteFoto(productoId, nombreArchivo);
    }

}
