package com.api.mercadeando.infrastructure.persistence;

import com.api.mercadeando.domain.data.CategoriaData;
import com.api.mercadeando.domain.dto.*;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.Categoria;
import com.api.mercadeando.infrastructure.persistence.entity.Producto;
import com.api.mercadeando.infrastructure.persistence.jpa.CategoriaJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.ProductoJPARepository;
import com.api.mercadeando.infrastructure.persistence.mapper.CategoriaMapper;
import com.api.mercadeando.infrastructure.persistence.mapper.ProductoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:59
 */
@Repository
@AllArgsConstructor
@Slf4j
public class CategoriaRepository implements CategoriaData {
    @Autowired
    private final CategoriaJPARepository categoriaJPARepository;
    @Autowired
    private final ProductoJPARepository productoJPARepository;
    @Autowired
    private final CategoriaMapper categoriaMapper;
    @Autowired
    private final ProductoMapper productoMapper;

    @Override
    public CategoriasResponse getCategorias(int offset, int limit) {
        if (offset < 0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit < 0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit > 100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Categoria> categorias = categoriaJPARepository.getCategorias(offset, limit);
        return categoriaMapper.mapCategoriasToCategoriaResponse(categorias, offset, limit);
    }

    @Override
    public CategoriaResponse getCategoria(Long categoriaId) throws ResourceNotFoundException, BadRequestException {
        if (categoriaId == null) throw new BadRequestException("CategoriaId no puede ser Null");
        Categoria categoria = categoriaJPARepository.findById(categoriaId).orElseThrow(() -> new ResourceNotFoundException(categoriaId, "Categoria"));

        List<Link> productosLinks = new ArrayList<>();
        for (Producto producto : productoJPARepository.getCategoriaProductos(categoriaId)) {
            productosLinks.add(new Link("producto", URL_PRODUCTOS_V1 + "/" + producto.getId()));
        }
        return categoriaMapper.mapCategoriaToCategoriaResponse(categoria, productosLinks);
    }

    @Override
    public CategoriaResponse addCategoria(CategoriaRequest request) throws BadRequestException {
        Categoria categoria = categoriaJPARepository.save(categoriaMapper.mapCategoriaRequestToCategoria(request, null));
        return categoriaMapper.mapCategoriaToCategoriaResponse(categoria, null);
    }

    @Override
    public void editCategoria(Long categoriaId, CategoriaRequest request) throws ResourceNotFoundException, BadRequestException {
        if (categoriaId == null) throw new BadRequestException("CategoriaId no puede ser Null");
        Optional<Categoria> actual = categoriaJPARepository.findById(categoriaId);
        if (actual.isPresent()) {
            request.setId(categoriaId);
            categoriaJPARepository.save(categoriaMapper.mapCategoriaRequestToCategoria(request, actual.get()));
        } else {
            throw new ResourceNotFoundException(categoriaId, "Categoria");
        }
    }

    @Override
    public void deleteCategoria(Long categoriaId) throws BadRequestException, ResourceNotFoundException {
        if (categoriaId == null) throw new BadRequestException("CategoriaId no puede ser Null");
        if (categoriaJPARepository.findById(categoriaId).isPresent()) {
            categoriaJPARepository.deleteById(categoriaId);
        } else {
            throw new ResourceNotFoundException(categoriaId, "Categoria");
        }
    }

    @Override
    public ProductosResponse getCategoriaProductos(Long categoriaId) throws BadRequestException, ResourceNotFoundException {
        if (categoriaId == null) throw new BadRequestException("CategoriaId no puede ser Null");
        categoriaJPARepository.findById(categoriaId).orElseThrow(() -> new ResourceNotFoundException(categoriaId, "Categoria"));
        List<Producto> productos = productoJPARepository.getCategoriaProductos(categoriaId);
        return productoMapper.mapProductosToProductosResponse(0, 0, productos);
    }
}
