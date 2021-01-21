package com.api.mercadeando.service;

import com.api.mercadeando.dto.Link;
import com.api.mercadeando.dto.OrdenRequest;
import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.dto.OrdenesResponse;
import com.api.mercadeando.entity.*;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.mapper.OrdenMapper;
import com.api.mercadeando.repository.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.api.mercadeando.entity.OrdenEstado.PAGADO;
import static com.api.mercadeando.entity.OrdenEstado.PENDIENTE;
import static com.api.mercadeando.entity.PagoMetodo.*;

/**
 * @author cristianmarint
 * @Date 2021-01-14 9:41
 */

/**
 * Brinda acceso a modificación y creación de
 * recursos
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
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PagoRepository pagoRepository;

    /**
     * Crea una respuesta Json mapeado los datos de Orden, Productos y OrdenProductos a una respuesta detallada
     * @param ordenId Id de una orden registrada
     * @return OrdenResponse con los detos en formato JSON
     * @throws ResourceNotFoundException Cuando la orden no es encontrada
     */
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    public OrdenResponse readOrden(Long ordenId) throws ResourceNotFoundException {
        if (ordenId==null) throw new MercadeandoException("OrdenId cannot be Null");
        Orden orden = ordenRepository.findById(ordenId).orElseThrow(()->new ResourceNotFoundException(ordenId,"Orden"));
        Map<String, Link> productosLinks=new HashMap<>();
        List<OrdenProducto> ordenProductosDetalles = ordenProductoRepository.getOrdenProductoDetalles(ordenId);
        List<Producto> ordenProductos = productoRepository.getOrdenProductos(ordenId);
        return ordenMapper.mapOrdenToOrdenResponse(orden,ordenProductos,ordenProductosDetalles);
    }

    /**
     * Crea una respuesta en JSON mapeado los datos de Orden, Producto y OrdenProducto a una respues
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return OrdenesResponse con datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @Transactional(readOnly = true)
    public OrdenesResponse readOrdenes(int offset, int limit){
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Orden> ordenes=ordenRepository.getOrdenes(offset,limit);
        return ordenMapper.mapOrdenesToOrdenResponse(ordenes,offset,limit);
    }

    /**
     * Permite crear una orden especificando sus datos si se cuenta con el permiso
     * verifica si existe suficientes productos para la orden y descuenta
     * la cantidad pedida
     *
     * @param ordenRequest Datos necesarios para crear orden
     * @throws BadRequestException cuando faltan datos necesario
     * @throws ResourceNotFoundException cuando no se encuentra un recurso
     */
    @PreAuthorize("hasAuthority('ADD_ORDEN')")
    public void addOrden(@Valid OrdenRequest ordenRequest) throws BadRequestException, ResourceNotFoundException {
        validateOrden(ordenRequest);
        if (ordenRequest.getCliente_id()!=null){
            Cliente cliente = clienteRepository.findById(ordenRequest.getCliente_id()).orElseThrow(()-> new ResourceNotFoundException(ordenRequest.getCliente_id(), "Cliente"));
            ordenRequest.setCliente(cliente);
            ordenRequest.setCliente_id(cliente.getId());
        }else{
            ordenRequest.setCliente(null);
            ordenRequest.setCliente_id(null);
        }
        Orden orden=ordenRepository.save(ordenMapper.mapOrdenRequestToOrden(ordenRequest, null));

        for (OrdenRequest.ordenProductos o: ordenRequest.getOrdenDetalle()
             ) {
            Producto producto = productoRepository.findById(o.getProducto_id()).get();
            if (o.getCantidad()>producto.getUnidades())
                throw new BadRequestException("Unidades insuficientes para el producto " + producto.getNombre() + " producto_id: " + producto.getId());
        }


        if (ordenRequest.getPago().getMetodo().equals(EFECTIVO)) orden.setEstado(PAGADO);
        if (ordenRequest.getPago().getMetodo().equals(TARJETA_DEBITO)) orden.setEstado(PAGADO);
        if (ordenRequest.getPago().getMetodo().equals(TARJETA_CREDITO)) orden.setEstado(PENDIENTE);
        if (ordenRequest.getPago().getMetodo().equals(CHECK)) orden.setEstado(PENDIENTE);

        Pago pago = new Pago(null, ordenRequest.getPago().getFecha(), ordenRequest.getPago().getMetodo());
        pagoRepository.save(pago);

        BigDecimal total = getTotalYActualizarOrdenProductoDetalle(ordenRequest, orden);
        orden.setTotal(total);
        orden.setPago(pago);

        ordenRepository.save(orden);
    }

    /**
     * Permite cambiar el metodo de pago de una orden por medio del metodo de pago
     * @param ordenId Id de una orden registrada
     * @param pagoMetodo Metodo de pago valido
     * @throws BadRequestException Cuando valores necesario no son asignados
     * @throws ResourceNotFoundException Cuando la orden requerida no es encontrada
     */
    @PreAuthorize("hasAuthority('EDIT_ORDEN')")
    public void editOrden(Long ordenId, PagoMetodo pagoMetodo) throws BadRequestException, ResourceNotFoundException {
        if (pagoMetodo==null) throw new BadRequestException("Metodo de pago no puede ser Null");
        if (ordenId==null) throw new BadRequestException("OrdenId Cannot be Null");

        OrdenRequest ordenRequestMetodoPago = new OrdenRequest();
        Pago pago = new Pago().builder().pagoMetodo(pagoMetodo).fecha(Instant.now()).build();
        pagoRepository.save(pago);
        Optional<Orden> ordenAlmacenda = ordenRepository.findById(ordenId);

        if (ordenAlmacenda.isPresent()){
            if (!ordenAlmacenda.get().getEstado().equals(PAGADO)){

                ordenAlmacenda.get().setPago(pago);
                if (pagoMetodo.equals(EFECTIVO)) ordenAlmacenda.get().setEstado(PAGADO);
                if (pagoMetodo.equals(TARJETA_DEBITO)) ordenAlmacenda.get().setEstado(PAGADO);
                if (pagoMetodo.equals(TARJETA_CREDITO)) ordenAlmacenda.get().setEstado(PENDIENTE);
                if (pagoMetodo.equals(CHECK)) ordenAlmacenda.get().setEstado(PENDIENTE);

                ordenRepository.save(ordenAlmacenda.get());
            }else{
                throw new BadRequestException("La orden no puede ser modificada, el pago ya fue procesado");
            }
        }else{
            throw new ResourceNotFoundException(ordenId,"Orden");
        }
    }

    /**
     * Cambia el estado de una Orden a false (softdelete)
     * @param ordenId Id de una orden registrada
     * @param estado Boolean
     * @throws ResourceNotFoundException Cuando no se encuentra la Orden
     * @throws BadRequestException Cuando existen valores necesarios Null
     */
    @PreAuthorize("hasAuthority('DELETE_ORDEN')")
    public void softDeleteOrden(Long ordenId,Boolean estado) throws ResourceNotFoundException, BadRequestException {
        if (ordenId==null) throw new BadRequestException("OrdenId cannot be Null");
        if (ordenRepository.findById(ordenId).isPresent()){
           ordenRepository.updateOrdenEstado(ordenId,estado);
        }else {
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
            Producto producto = productoRepository.findById(detalleOrdenProducto.getProducto_id()).orElseThrow(()-> new ResourceNotFoundException(detalleOrdenProducto.getProducto_id(),"No se encontro el producto"));
            if (detalleOrdenProducto.getCantidad()>producto.getUnidades()){
                throw new BadRequestException("No existe suficientes productos de "+producto.getNombre()+" producto_id: "+producto.getId());
            }
        }
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
            Producto producto = productoRepository.findById(o.getProducto_id()).get();

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

            productoRepository.save(producto);
            ordenProductoRepository.save(ordenProducto);
        }
        return total;
    }
}
