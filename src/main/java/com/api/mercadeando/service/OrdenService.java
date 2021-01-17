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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    /**
     * Crea una respuesta en JSON mapeado los datos de Orden, Producto y OrdenProducto a una respues
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return OrdenesResponse con datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @Transactional(readOnly = true)
    public OrdenesResponse getOrdenes(int offset, int limit){
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
    public void createOrden(@Valid OrdenRequest ordenRequest) throws BadRequestException, ResourceNotFoundException {
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
        BigDecimal total = BigDecimal.ZERO;

        for (OrdenRequest.ordenProductos o: ordenRequest.getOrdenDetalle()
             ) {
            Producto producto = productoRepository.findById(o.getProducto_id()).get();
            if (o.getCantidad()>producto.getUnidades())
                throw new BadRequestException("Unidades insuficientes para el producto " + producto.getNombre() + " producto_id: " + producto.getId());
        }

        for (OrdenRequest.ordenProductos o: ordenRequest.getOrdenDetalle()
             ) {
            Producto producto = productoRepository.findById(o.getProducto_id()).get();

            producto.setUnidades(producto.getUnidades()-o.getCantidad());

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

        Pago pago = new Pago(null, ordenRequest.getPago().getFecha(), ordenRequest.getPago().getMetodo());
        pagoRepository.save(pago);
        orden.setTotal(total);
        orden.setPago(pago);

        ordenRepository.save(orden);
    }

    /**
     * Actualiza los datos de una orden registrada si se cuenta con el permiso
     * @param ordenId Id de una orden registrado
     * @param request OrdenRequest con los datos nuevos
     * @throws ResourceNotFoundException cuando el recuerso no existe
     * @throws BadRequestException cuando existen valores incorrectos.
     */
    @PreAuthorize("hasAuthority('EDIT_ORDEN')")
    public void editOrden(Long ordenId, OrdenRequest request) throws BadRequestException, ResourceNotFoundException {
        validateOrden(request);
        if (ordenId==null) throw new BadRequestException("OrdenId Cannot be Null");
        Optional<Orden> actual = ordenRepository.findById(ordenId);
        if (actual.isPresent()){
//          TODO: Actualizar la cantidad de productos y evaluar los cambios
            request.setId(ordenId);
            ordenRepository.save(ordenMapper.mapOrdenRequestToOrden(request,actual.get()));
        }else{
            throw new ResourceNotFoundException(ordenId,"Orden");
        }
    }

    @PreAuthorize("hasAuthority('DELETE_ORDEN')")
    public void softDeleteOrden(Long ordenId, boolean estado) throws ResourceNotFoundException, BadRequestException {
        if (ordenId==null) throw new BadRequestException("OrdenId cannot be Null");
        if (ordenRepository.findById(ordenId).isPresent()){
            //          TODO: Actualizar la cantidad de productos y evaluar los cambios
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
}
