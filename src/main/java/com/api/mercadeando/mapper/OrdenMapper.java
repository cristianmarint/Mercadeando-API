package com.api.mercadeando.mapper;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.entity.Orden;
import com.api.mercadeando.utils.formatDates;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

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
     * Transforma la Orden Entity a un OrdenResponse para ser retornado por la API en formato JSON
     * @param orden entidad con datos de una orden
     * @param self
     * @param clienteLink Link a cliente
     * @param productosLinks Links a los productos de una orden
     * @return OrdenResponse con detalles de una orden y links de acceso a recursos
     */
    public OrdenResponse mapOrdenToOrdenResponse(Orden orden, Link self, Link clienteLink, Map<String, Link> productosLinks){
        OrdenResponse response = new OrdenResponse();
        if(orden.getId()!=null) response.setId(orden.getId());
        if(orden.getActivo()!=null) response.setActivo(orden.getActivo());
        if(orden.getEstado()!=null) response.setEstado(orden.getEstado());
        if(orden.getFecha()!=null) response.setFecha(instantToString(orden.getFecha()));
        if(orden.getPrecio()!=null) response.setPrecio(orden.getPrecio());
        if(orden.getPago()!=null) {
            response.getPago().setMetodo(String.valueOf(orden.getPago().getPagoMetodo()));
            response.getPago().setFecha(instantToString(orden.getPago().getFecha()));
        }
        if(orden.getCliente()!=null) response.setCliente(clienteLink);
        if(orden.getProductos()!=null) response.setProductos(productosLinks);
        if(self!=null) response.setSelf(self);
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
