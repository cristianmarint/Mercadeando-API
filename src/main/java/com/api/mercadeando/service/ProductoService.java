package com.api.mercadeando.service;

import com.api.mercadeando.dto.ProductoResponse;
import com.api.mercadeando.dto.ProductosResponse;
import com.api.mercadeando.entity.FileStorage;
import com.api.mercadeando.entity.Producto;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.mapper.ProductoMapper;
import com.api.mercadeando.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoMapper productoMapper;

    /**
     * Crea una respuesta Json mapeado los datos de Producto a una respuesta detallada
     * @param productoId
     * @return ProductoResponse con los datos en formate JSON
     * @throws ResourceNotFoundException cuando el producto no es encontrado
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @Transactional(readOnly = true)
    public ProductoResponse getProducto(Long productoId) throws ResourceNotFoundException {
        if (productoId==null) throw new MercadeandoException("ProductoId cannot be null");
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException(productoId, "Producto"));
        return productoMapper.mapProductoToProductoResponse(producto);
    }

    /**
     * Crea una respuesta Json mapeando los datos en una lista de productos
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ProductosResponse con datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_PRODUCTO')")
    @Transactional(readOnly = true)
    public ProductosResponse getProductos(int offset, int limit){
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Producto> productos = productoRepository.getProductos(offset,limit);
        return productoMapper.mapProductosToProductosResponse(offset,limit,productos);
    }

    /**
     * Permite agregar una foto a un producto
     * @param productoId Id de un producto registrado
     * @param foto FileStorage (foto) con formato valido
     * @throws BadRequestException Cuando el Id del producto es Null
     * @throws ResourceNotFoundException Cuando el producto no es encontrado
     */
    public void addFoto(Long productoId, FileStorage foto) throws BadRequestException, ResourceNotFoundException {
        if (productoId==null) throw new BadRequestException("Producto Id cannot be Null");
        Producto producto = productoRepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException(productoId, "Producto"));
        producto.getFotos().add(foto);
        productoRepository.save(producto);
    }
}
