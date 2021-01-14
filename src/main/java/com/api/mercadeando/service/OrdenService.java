package com.api.mercadeando.service;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.entity.Orden;
import com.api.mercadeando.entity.Producto;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.mapper.OrdenMapper;
import com.api.mercadeando.repository.ClienteRepository;
import com.api.mercadeando.repository.OrdenRepository;
import com.api.mercadeando.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_V1;
import static com.api.mercadeando.controller.Mappings.URL_ORDENES_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-14 9:41
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private OrdenMapper ordenMapper;

    public OrdenResponse getCliente(Long ordenId) throws ResourceNotFoundException {
        if (ordenId==null) throw new MercadeandoException("OrdenId cannot be Null");

        Orden orden = ordenRepository.findById(ordenId).orElseThrow(()->new ResourceNotFoundException(ordenId,"Orden"));
        Map<String, Link> productosLinks=new HashMap<>();
        for (Producto producto:productoRepository.getOrdenProductos(ordenId)) {
            productosLinks.put("producto-"+producto.getId(), new Link("producto",String.format(URL_ORDENES_V1+"/"+ordenId+"/productos/%s",producto.getId())));
        }
        Link clienteLink = new Link("cliente",String.format(URL_CLIENTES_V1+"/%s",orden.getCliente().getId()));
        Link self = new Link("self",String.format(URL_ORDENES_V1+"/%s",orden.getId()));
        return ordenMapper.mapOrdenToOrdenResponse(orden,self,clienteLink,productosLinks);
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
