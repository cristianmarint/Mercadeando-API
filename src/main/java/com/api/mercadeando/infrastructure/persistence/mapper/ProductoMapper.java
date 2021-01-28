package com.api.mercadeando.infrastructure.persistence.mapper;

import com.api.mercadeando.domain.dto.*;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.service.AuthService;
import com.api.mercadeando.infrastructure.persistence.entity.Categoria;
import com.api.mercadeando.infrastructure.persistence.entity.FileStorage;
import com.api.mercadeando.infrastructure.persistence.entity.Producto;
import com.api.mercadeando.infrastructure.persistence.entity.ProductoEstado;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CATEGORIAS_V1;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-19 9:03
 */
@Component
@AllArgsConstructor
@Slf4j
public class ProductoMapper {
    private final AuthService authService;

    /**
     * Crea una respuesta Json mapeado los datos de ProductoData a una respuesta
     *
     * @param producto datos de una producto
     * @return ProductoResponse
     */
    public ProductoResponse mapProductoToProductoResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        if (producto.getId() != null) response.setId(producto.getId());
        response.setSelf(new Link("self", URL_PRODUCTOS_V1 + "/" + producto.getId()));
        if (producto.getActivo() != null) response.setActivo(producto.getActivo());
        if (producto.getCodigo() != null) response.setCodigo(producto.getCodigo());
        if (producto.getNombre() != null) response.setNombre(producto.getNombre());
        if (producto.getDescripcion() != null) response.setDescripcion(producto.getDescripcion());
        if (producto.getPeso() != null) response.setPeso(producto.getPeso());
        if (producto.getUnidades() != null) response.setUnidades(producto.getUnidades());
        if (producto.getPrecio() != null) response.setPrecio(producto.getPrecio());
        if (producto.getCodigo() != null) response.setCodigo(producto.getCodigo());
        if (producto.getFotos() != null) {
            for (FileStorage foto : producto.getFotos()) {
                response.getFotos().add(new UploadFileResponse(foto.getFileName(), foto.getFileUrl(), foto.getDocumentFormat(), foto.getFileSize()));
            }
        }
        if (producto.getEstado() != null) response.setEstado(producto.getEstado());
        if (producto.getCategorias() != null) {
            for (Categoria c : producto.getCategorias()
            ) {
                response.getCategorias().add(
                        new CategoriaResponse().builder()
                                .id(c.getId())
                                .self(new Link("self", URL_CATEGORIAS_V1 + "/" + c.getId()))
                                .nombre(c.getNombre())
                                .descripcion(c.getDescripcion())
                                .build()
                );
            }
        }
        return response;
    }

    /**
     * Mapea una lista de ordenes a OrdenesResponse
     *
     * @param offset    Punto de partida mayor a cero para buscar nuevos valores
     * @param limit     Cantidad de valores a entontrar menor a cien
     * @param productos Listado de productos con sus datos
     * @return ProductosResponse con datos de cada orden mapeado
     */
    public ProductosResponse mapProductosToProductosResponse(int offset, int limit, List<Producto> productos) {
        ProductosResponse response = new ProductosResponse();
        if (productos != null & !productos.isEmpty()) {
            response.setCount(productos.size());
            productos.forEach(producto -> response.getProductos().add(this.mapProductoToProductoResponse(producto)));
            if (offset > 0) {
                response.getLinks().add(
                        new Link("prev",
                                String.format(URL_PRODUCTOS_V1 + "?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
            }
            response.getLinks().add(
                    new Link("next",
                            String.format(URL_PRODUCTOS_V1 + "?offset=%s&limit=%s", offset + limit, limit)));
        }
        return response;
    }

    public Producto mapProductoRequestToProducto(ProductoRequest request, Producto producto) throws BadRequestException {
        if (request == null) throw new BadRequestException("Productorequest no puede ser Null");
        if (producto == null) producto = new Producto();

        if (request.getId() != null) producto.setId(request.getId());
        if (request.getCodigo() != null) producto.setCodigo(request.getCodigo());
        if (request.getNombre() != null) producto.setNombre(request.getNombre());
        if (request.getDescripcion() != null) producto.setDescripcion(request.getDescripcion());
        if (request.getPeso() != null) producto.setPeso(request.getPeso());
        if (request.getUnidades() != null) {
            producto.setUnidades(request.getUnidades());
            if (producto.getUnidades() > 10) {
                producto.setEstado(ProductoEstado.DISPONIBLE);
            } else if (producto.getUnidades() <= 10 & producto.getUnidades() > 1) {
                producto.setEstado(ProductoEstado.POCAS_UNIDADES);
            } else {
                producto.setEstado(ProductoEstado.AGOTADO);
            }
        } else {
            producto.setUnidades(0);
            producto.setEstado(ProductoEstado.AGOTADO);
        }
        if (request.getPrecio() != null) producto.setPrecio(request.getPrecio());
        if (producto.getCreatedAt() != null) producto.setCreatedAt(Instant.now());

        return producto;
    }
}
