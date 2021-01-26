package com.api.mercadeando.infrastructure.persistence;

import com.api.mercadeando.domain.data.OrdenData;
import com.api.mercadeando.domain.dto.Link;
import com.api.mercadeando.domain.dto.OrdenRequest;
import com.api.mercadeando.domain.dto.OrdenResponse;
import com.api.mercadeando.domain.dto.OrdenesResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.*;
import com.api.mercadeando.infrastructure.persistence.jpa.*;
import com.api.mercadeando.infrastructure.persistence.mapper.OrdenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado.PAGADO;

/**
 * @author cristianmarint
 * @Date 2021-01-21 3:15
 */
@Repository
public class OrdenRepository implements OrdenData {
    @Autowired
    private OrdenJPARepository ordenJPARepository;
    @Autowired
    private ProductoJPARepository productoJPARepository;
    @Autowired
    private OrdenProductoJPARepository ordenProductoJPARepository;
    @Autowired
    private OrdenMapper ordenMapper;
    @Autowired
    private ClienteJPARepository clienteJPARepository;
    @Autowired
    private PagoJPARepository pagoJPARepository;

    @Override
    public OrdenResponse readOrden(Long ordenId) throws ResourceNotFoundException, BadRequestException {
        if (ordenId==null) throw new BadRequestException("OrdenId no puede ser Null");
        Orden orden = ordenJPARepository.findById(ordenId).orElseThrow(()->new ResourceNotFoundException(ordenId,"Orden"));
        Map<String, Link> productosLinks=new HashMap<>();
        List<OrdenProducto> ordenProductosDetalles = ordenProductoJPARepository.getOrdenProductoDetalles(ordenId);
        List<Producto> ordenProductos = productoJPARepository.getOrdenProductos(ordenId);
        return ordenMapper.mapOrdenToOrdenResponse(orden,ordenProductos,ordenProductosDetalles);
    }

    @Override
    public OrdenesResponse readOrdenes(int offset, int limit) {
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Orden> ordenes= ordenJPARepository.getOrdenes(offset,limit);
        return ordenMapper.mapOrdenesToOrdenResponse(ordenes,offset,limit);
    }

    @Override
    public OrdenResponse addOrden(OrdenRequest ordenRequest) throws ResourceNotFoundException, BadRequestException {
        validateOrden(ordenRequest);
        if (ordenRequest.getCliente_id()!=null){
            Cliente cliente = clienteJPARepository.findById(ordenRequest.getCliente_id()).orElseThrow(()-> new ResourceNotFoundException(ordenRequest.getCliente_id(), "Cliente"));
            ordenRequest.setCliente(cliente);
            ordenRequest.setCliente_id(cliente.getId());
        }else{
            ordenRequest.setCliente(null);
            ordenRequest.setCliente_id(null);
        }
        Orden orden= ordenJPARepository.save(ordenMapper.mapOrdenRequestToOrden(ordenRequest, null));

        for (OrdenRequest.ordenProductos o: ordenRequest.getOrdenDetalle()
        ) {
            Producto producto = productoJPARepository.findById(o.getProducto_id()).get();
            if (o.getCantidad()>producto.getUnidades())
                throw new BadRequestException("Unidades insuficientes para el producto " + producto.getNombre() + " producto_id: " + producto.getId());
        }

        BigDecimal total = getTotalYActualizarOrdenProductoDetalle(ordenRequest, orden);
        orden.setTotal(total);

        Orden save = ordenJPARepository.save(orden);
        List<OrdenProducto> ordenProductosDetalles = ordenProductoJPARepository.getOrdenProductoDetalles(save.getId());
        List<Producto> ordenProductos = productoJPARepository.getOrdenProductos(save.getId());

        return ordenMapper.mapOrdenToOrdenResponse(save,ordenProductos,ordenProductosDetalles);
    }

    /**
     * Para una OrdenRequest Crea o actualizar una orden y le asigna el total
     * 1) Solo se descuentan los producto si orden.estado=PAGADO
     * @param ordenRequest Petición para crear una nueva orden
     * @param orden Orden almacenada, si no se provee se crea una.
     * @return total BigDecimal con el total de la orden.
     */
    private BigDecimal getTotalYActualizarOrdenProductoDetalle(OrdenRequest ordenRequest, Orden orden) {
        BigDecimal total = BigDecimal.ZERO;
        if (orden==null) orden = new Orden();

        for (OrdenRequest.ordenProductos o: ordenRequest.getOrdenDetalle()
        ) {
            Producto producto = productoJPARepository.findById(o.getProducto_id()).get();

            if (orden.getEstado().equals(PAGADO)){
                producto.setUnidades(producto.getUnidades()-o.getCantidad());
            }
            OrdenProducto ordenProducto = new OrdenProducto(
                    null,
                    o.getCantidad(),
                    producto.getPrecio(),
                    orden,
                    producto);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(o.getCantidad())));

            if (producto.getUnidades()>10){
                producto.setEstado(ProductoEstado.DISPONIBLE);
            }else if (producto.getUnidades()<=10 & producto.getUnidades()>1){
                producto.setEstado(ProductoEstado.POCAS_UNIDADES);
            }else{
                producto.setEstado(ProductoEstado.AGOTADO);
            }

            productoJPARepository.save(producto);
            ordenProductoJPARepository.save(ordenProducto);
        }
        return total;
    }

//    @Override
//    public void editOrden(Long ordenId, PagoMetodo pagoMetodo) throws BadRequestException, ResourceNotFoundException {
//
//
//        OrdenRequest ordenRequestMetodoPago = new OrdenRequest();
//        Pago pago = new Pago().builder().metodo(pagoMetodo).fecha(Instant.now()).build();
//        pagoJPARepository.save(pago);
//        Optional<Orden> ordenAlmacenda = ordenJPARepository.findById(ordenId);
//
//        if (ordenAlmacenda.isPresent()){
//            if (!ordenAlmacenda.get().getEstado().equals(PAGADO)){
//
//                ordenAlmacenda.get().setPagox(pago);
//
//                ordenJPARepository.save(ordenAlmacenda.get());
//            }else{
//                throw new BadRequestException("La orden no puede ser modificada, el pago ya fue procesado");
//            }
//        }else{
//            throw new ResourceNotFoundException(ordenId,"Orden");
//        }
//    }

    public void softDeleteOrden(Long ordenId, Boolean estado) throws BadRequestException, ResourceNotFoundException {
        if (ordenId==null) throw new BadRequestException("OrdenId no puede ser Null");
        if (ordenJPARepository.findById(ordenId).isPresent()){
            ordenJPARepository.updateOrdenEstado(ordenId,estado);
        }else {
            throw new ResourceNotFoundException(ordenId,"Orden");
        }
    }

    @Override
    public void addPagoId(Long ordenId, Long pagoId) throws ResourceNotFoundException {
        if (ordenJPARepository.findById(ordenId).isPresent()){
            ordenJPARepository.editPagoId(ordenId,pagoId);
        } else{
            throw new ResourceNotFoundException(ordenId,"Orden");
        }
    }

    /**
     * Valida datos necesario en una OrdenRequest
     * @param ordenRequest Datos necesarios para crear orden
     * @throws BadRequestException cuando faltan datos necesario
     * @throws ResourceNotFoundException cuando no se encuentra un recurso
     */
    private void validateOrden(OrdenRequest ordenRequest) throws BadRequestException, ResourceNotFoundException {
        if (ordenRequest.getOrdenDetalle()==null) throw new BadRequestException("La orden debe tener uno o mas productos");
        if (ordenRequest.getFecha()==null) throw new BadRequestException("La fecha de la orden no puede ser Null");

        for (OrdenRequest.ordenProductos detalleOrdenProducto:ordenRequest.getOrdenDetalle()
        ) {
            Producto producto = productoJPARepository.findById(detalleOrdenProducto.getProducto_id()).orElseThrow(()-> new ResourceNotFoundException(detalleOrdenProducto.getProducto_id(),"No se encontro el producto"));
            if (detalleOrdenProducto.getCantidad()>producto.getUnidades()){
                throw new BadRequestException("No existe suficientes productos de "+producto.getNombre()+" producto_id: "+producto.getId());
            }
        }
    }
}
