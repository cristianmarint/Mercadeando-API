package com.api.mercadeando.mapper;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.OrdenResponse;
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
@Component
@AllArgsConstructor
@Slf4j
public class OrdenMapper {

    /**
     * Crea una respuesta Json mapeado los datos de Orden, Productos y OrdenProductos a una respuesta
     * @param orden entidad con datos de una orden
     * @param ordenProductos productos asociados a la orden
     * @param ordenProductosDetalles detalles de los productos asociados a la orden
     * @return OrdenResponse con los detos en formato JSON
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
        if(orden.getPrecio()!=null) response.setPrecio(orden.getPrecio());
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

//    public OrdenesResponse mapOrdenesToOrdenResponse(List<Orden> ordenes, int offset, int limit) {
//        OrdenesResponse response = new OrdenesResponse();
//        if (ordenes!=null & !ordenes.isEmpty()){
//            response.setCount(ordenes.size());
//
//            ordenes.forEach(orden -> response.getOrdenes().add(
//                    orden,
////                    Collections.singletonMap("_link",new Link("oi",URL_CLIENTE))
//            ));
//        }
//        return response;
//    }
}
