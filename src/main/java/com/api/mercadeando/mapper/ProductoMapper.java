package com.api.mercadeando.mapper;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.ProductoResponse;
import com.api.mercadeando.dto.ProductosResponse;
import com.api.mercadeando.dto.UploadFileResponse;
import com.api.mercadeando.entity.FileStorage;
import com.api.mercadeando.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.api.mercadeando.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-19 9:03
 */
@Component
@AllArgsConstructor
@Slf4j
public class ProductoMapper {
    /**
     * Crea una respuesta Json mapeado los datos de Producto a una respuesta
     * @param producto datos de una producto
     * @return ProductoResponse
     */
    public ProductoResponse mapProductoToProductoResponse(Producto producto){
        ProductoResponse response = new ProductoResponse();
        if (producto.getId()!=null) response.setId(producto.getId());
        response.setSelf(new Link("self",URL_PRODUCTOS_V1+"/"+producto.getId()));
        if (producto.getActivo()!=null) response.setActivo(producto.getActivo());
        if (producto.getNombre()!=null) response.setNombre(producto.getNombre());
        if (producto.getDescripcion()!=null) response.setDescripcion(producto.getDescripcion());
        if (producto.getPeso()!=null) response.setPeso(producto.getPeso());
        if (producto.getUnidades()!=null) response.setUnidades(producto.getUnidades());
        if (producto.getPrecio()!=null) response.setPrecio(producto.getPrecio());
        if (producto.getFotos()!=null) {
            for (FileStorage foto:producto.getFotos()
                 ) {
                response.getFotos().add(new UploadFileResponse(foto.getFileName(),foto.getFileUrl(),foto.getDocumentFormat(),foto.getFileSize()));
            }
        }
        if (producto.getEstado()!=null) response.setEstado(producto.getEstado());
        return response;
    }

    /**
     * Mapea una lista de ordenes a OrdenesResponse
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @param productos Listado de productos con sus datos
     * @return ProductosResponse con datos de cada orden mapeado
     */
    public ProductosResponse mapProductosToProductosResponse(int offset, int limit, List<Producto> productos) {
        ProductosResponse response = new ProductosResponse();
        if(productos!=null & !productos.isEmpty()){
            response.setCount(productos.size());
            productos.forEach(producto -> response.getProductos().add(this.mapProductoToProductoResponse(producto)));
            if (offset>0){
                response.getLinks().add(
                        new Link("prev",
                                String.format(URL_PRODUCTOS_V1+"?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
            }
            response.getLinks().add(
                    new Link("next",
                            String.format(URL_PRODUCTOS_V1+"?offset=%s&limit=%s", offset + limit, limit)));
        }
        return response;
    }
}
