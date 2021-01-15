package com.api.mercadeando.service;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.entity.Orden;
import com.api.mercadeando.entity.OrdenProducto;
import com.api.mercadeando.entity.Producto;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.mapper.OrdenMapper;
import com.api.mercadeando.repository.OrdenProductoRepository;
import com.api.mercadeando.repository.OrdenRepository;
import com.api.mercadeando.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cristianmarint
 * @Date 2021-01-14 9:41
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private OrdenProductoRepository ordenProductoRepository;
    @Autowired
    private OrdenMapper ordenMapper;

    /**
     * Crea una respuesta Json mapeado los datos de Orden, Productos y OrdenProductos a una respuesta
     * @param ordenId Id de una orden registrada
     * @return OrdenResponse con los detos en formato JSON
     * @throws ResourceNotFoundException
     */
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    public OrdenResponse getCliente(Long ordenId) throws ResourceNotFoundException {
        if (ordenId==null) throw new MercadeandoException("OrdenId cannot be Null");
        Orden orden = ordenRepository.findById(ordenId).orElseThrow(()->new ResourceNotFoundException(ordenId,"Orden"));
        Map<String, Link> productosLinks=new HashMap<>();
        List<OrdenProducto> ordenProductosDetalles = ordenProductoRepository.getOrdenProductoDetalles(ordenId);
        List<Producto> ordenProductos = productoRepository.getOrdenProductos(ordenId);
        return ordenMapper.mapOrdenToOrdenResponse(orden,ordenProductos,ordenProductosDetalles);
    }

//    @PreAuthorize("hasAuthority('READ_ORDEN')")
//    @Transactional(readOnly = true)
//    public OrdenesResponse getOrdenes(int offset, int limit){
//        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
//        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
//        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
//        List<Orden> ordenes=ordenRepository.getOrdenes(offset,limit);
//        return ordenMapper.mapOrdenesToOrdenResponse(ordenes,offset,limit);
//    }
}
