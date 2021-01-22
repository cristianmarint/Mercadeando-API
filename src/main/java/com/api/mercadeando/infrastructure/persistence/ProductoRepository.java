package com.api.mercadeando.infrastructure.persistence;

import com.api.mercadeando.domain.data.ProductoData;
import com.api.mercadeando.domain.dto.ProductoRequest;
import com.api.mercadeando.domain.dto.ProductoResponse;
import com.api.mercadeando.domain.dto.ProductosResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.domain.service.FileStorageService;
import com.api.mercadeando.infrastructure.persistence.entity.FileStorage;
import com.api.mercadeando.infrastructure.persistence.entity.Producto;
import com.api.mercadeando.infrastructure.persistence.jpa.FileStorageJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.ProductoJPARepository;
import com.api.mercadeando.infrastructure.persistence.mapper.ProductoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author cristianmarint
 * @Date 2021-01-21 3:29
 */
@Repository
@Slf4j
public class ProductoRepository implements ProductoData {
    @Autowired
    private ProductoJPARepository productoJPARepository;
    @Autowired
    private FileStorageJPARepository fileStorageJPARepository;
    @Autowired
    private ProductoMapper productoMapper;
    @Autowired
    private FileStorageService fileStorageService;

    public ProductoResponse readProducto(Long productoId) throws BadRequestException, ResourceNotFoundException {
        if (productoId==null) throw new BadRequestException("ProductoId cannot be null");
        Producto producto = productoJPARepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException(productoId, "Producto"));
        return productoMapper.mapProductoToProductoResponse(producto);
    }

    public ProductosResponse readProductos(int offset, int limit){
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Producto> productos = productoJPARepository.getProductos(offset,limit);
        return productoMapper.mapProductosToProductosResponse(offset,limit,productos);
    }

    public ProductoResponse addProducto(@Valid ProductoRequest request) throws BadRequestException {
        validarProducto(request);
        Producto nuevo = productoJPARepository.save(productoMapper.mapProductoRequestToProducto(request, null));
        return productoMapper.mapProductoToProductoResponse(nuevo);
    }

    public void editProducto(Long productoId, ProductoRequest request) throws BadRequestException, ResourceNotFoundException {
        validarProducto(request);
        if (productoId==null) throw new BadRequestException("ProductoId cannot be Null");
        Optional<Producto> actual = productoJPARepository.findById(productoId);
        if (actual.isPresent()){
            request.setId(productoId);
            productoJPARepository.save(productoMapper.mapProductoRequestToProducto(request, actual.get()));
        }else {
            throw new ResourceNotFoundException(productoId,"ProductoData");
        }
    }

    public void addFoto(Long productoId, FileStorage foto) throws BadRequestException, ResourceNotFoundException {
        if (productoId==null) throw new BadRequestException("ProductoData Id cannot be Null");
        Producto producto = productoJPARepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException(productoId, "ProductoData"));
        producto.getFotos().add(foto);
        productoJPARepository.save(producto);
    }

    public void deleteFoto(Long productoId, String nombreArchivo) throws BadRequestException, ResourceNotFoundException {
        if (productoId==null) throw new BadRequestException("ProductoData Id cannot be Null");
        Producto producto = productoJPARepository.findById(productoId).orElseThrow(() -> new ResourceNotFoundException(productoId, "ProductoData"));
        FileStorage foto = fileStorageJPARepository.findByFileName(nombreArchivo).orElseThrow(()-> new ResourceNotFoundException("Foto no registrada."));
        producto.getFotos().remove(foto);
        productoJPARepository.save(producto);
        fileStorageJPARepository.delete(foto);
        try {
            fileStorageService.deleteFileLocally(nombreArchivo);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Foto no encontrada.");
        }
    }

    /**
     * Permite validar campos necesarios
     * @param request entidad para verificar
     * @throws BadRequestException
     */
    private void validarProducto(ProductoRequest request) throws BadRequestException {
        if (request.getNombre()==null) throw new BadRequestException("El nombre no puede ser Null");
        if (request.getPrecio()==null) throw new BadRequestException("El precio no puede ser Null");
    }
}
