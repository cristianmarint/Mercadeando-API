package com.api.mercadeando.mapper;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.dto.OrdenesResponse;
import com.api.mercadeando.entity.Orden;
import com.api.mercadeando.entity.OrdenProducto;
import com.api.mercadeando.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_V1;
import static com.api.mercadeando.controller.Mappings.URL_ORDENES_V1;
import static com.api.mercadeando.utils.formatDates.instantToString;
/**
 * @author cristianmarint
 * @Date 2021-01-14 9:42
 */

/**
 * Permite mapear datos internos de una Orden
 * para mostrarlos de manera segura por medio de la API
 */
@Component
@AllArgsConstructor
@Slf4j
public class OrdenMapper {

    /**
     * Crea una respuesta Json mapeado los datos de Orden, Productos y OrdenProductos a una respuesta
     * @param orden entidad con datos de una orden
     * @param ordenProductos productos asociados a la orden
     * @param ordenProductosDetalles detalles de los productos asociados a la orden
     * @return OrdenResponse con los datos detallados en formato JSON
     */
    public OrdenResponse mapOrdenToOrdenResponse(
            Orden orden,
            List<Producto> ordenProductos,
            List<OrdenProducto> ordenProductosDetalles
    ){
        OrdenResponse response = new OrdenResponse();
        if(orden.getId()!=null) {
            response.setId(orden.getId());
            response.setCliente(new Link("cliente",String.format(URL_CLIENTES_V1+"/%s",orden.getCliente().getId())));
            response.setSelf(new Link("self",String.format(URL_ORDENES_V1+"/%s",orden.getId())));
        }
        if(orden.getActivo()!=null) response.setActivo(orden.getActivo());
        if(orden.getEstado()!=null) response.setEstado(orden.getEstado());
        if(orden.getFecha()!=null) response.setFecha(instantToString(orden.getFecha()));
        if(orden.getTotal()!=null) response.setTotal(orden.getTotal());
        if(orden.getPago()!=null) {
            response.getPago().setMetodo(String.valueOf(orden.getPago().getPagoMetodo()));
            response.getPago().setFecha(instantToString(orden.getPago().getFecha()));
        }
        for (OrdenProducto d:ordenProductosDetalles
             ) {
            List<Producto> p = ordenProductos.stream().
                    filter(a -> Objects.equals(a.getId(), d.getProducto().getId()))
                    .collect(Collectors.toList());
            response.addProductoDetalle(p.get(0).getId(),p.get(0).getNombre(),d.getCantidad(),d.getPrecio());
        }
        return response;
    }

    /**
     * Mapea una Orden Entity a una OrdenResponse
     * @param orden datos de una Orden Entity
     * @return OrdenesDetalle datos de una orden en mapeados con detalles
     */
    private OrdenesResponse.OrdenesDetalle asignarOrdenAOrdenes(Orden orden){
        OrdenesResponse.OrdenesDetalle response = new OrdenesResponse.OrdenesDetalle();
        if (orden.getId()!=null) response.setId(orden.getId());
        if (orden.getActivo()!=null) response.setActivo(orden.getActivo());
        if (orden.getFecha()!=null) response.setFecha(instantToString(orden.getFecha()));
        if (orden.getTotal()!=null) response.setTotal(orden.getTotal());
        if(orden.getPago()!=null) {
            log.info(String.valueOf(orden.getPago().getPagoMetodo()));
            log.info(String.valueOf(orden.getPago().getFecha()));
            response.getPago().setMetodo(String.valueOf(orden.getPago().getPagoMetodo()));
            response.getPago().setFecha(instantToString(orden.getPago().getFecha()));
        }
        response.setSelf(new Link("self",URL_ORDENES_V1+"/"+orden.getId()));
        response.setCliente(new Link("cliente",URL_CLIENTES_V1+"/"+orden.getCliente().getId()));

        return response;
    }

    /**
     * Mapea una lista de ordenes a OrdenesResponse
     * @param ordenes Lista de ordenes Entity
     * @param offset
     * @param limit
     * @return OrdenesResponse con datos de cada orden mapeado
     */
    public OrdenesResponse mapOrdenesToOrdenResponse(List<Orden> ordenes, int offset, int limit) {
        OrdenesResponse response = new OrdenesResponse();
        if (ordenes!=null & !ordenes.isEmpty()){
            response.setCount(ordenes.size());
            ordenes.forEach(orden -> response.getOrdenes().add(
                    this.asignarOrdenAOrdenes(orden)
            ));
            if (offset>0){
                response.getLinks().add(
                        new Link("prev",
                                String.format("/api/v1/clientes?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
            }
            response.getLinks().add(
                    new Link("next",
                            String.format("/api/v1/clientes?offset=%s&limit=%s", offset + limit, limit)));
        }
        return response;
    }
}
