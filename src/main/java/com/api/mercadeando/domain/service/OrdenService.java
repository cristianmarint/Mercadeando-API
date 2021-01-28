package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.OrdenData;
import com.api.mercadeando.domain.dto.OrdenRequest;
import com.api.mercadeando.domain.dto.OrdenResponse;
import com.api.mercadeando.domain.dto.OrdenesResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

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
    private OrdenData ordenData;

    /**
     * Crea una respuesta Json mapeado los datos de Orden, Productos y OrdenProductos a una respuesta detallada
     *
     * @param ordenId Id de una orden registrada
     * @return OrdenResponse con los detos en formato JSON
     * @throws ResourceNotFoundException Cuando la orden no es encontrada
     */
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    public OrdenResponse readOrden(Long ordenId) throws ResourceNotFoundException, BadRequestException {
        return ordenData.readOrden(ordenId);
    }

    /**
     * Crea una respuesta en JSON mapeado los datos de Orden, ProductoData y OrdenProducto a una respues
     *
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit  Cantidad de valores a entontrar menor a cien
     * @return OrdenesResponse con datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @Transactional(readOnly = true)
    public OrdenesResponse readOrdenes(int offset, int limit) {
        return ordenData.readOrdenes(offset, limit);
    }

    /**
     * Permite crear una orden especificando sus datos si se cuenta con el permiso
     * verifica si existe suficientes productos para la orden y descuenta
     * la cantidad pedida
     *
     * @param ordenRequest Datos necesarios para crear orden
     * @return OrdenResponse con los datos en formato JSON
     * @throws BadRequestException       cuando faltan datos necesario
     * @throws ResourceNotFoundException cuando no se encuentra un recurso
     */
    @PreAuthorize("hasAuthority('ADD_ORDEN')")
    public OrdenResponse addOrden(@Valid OrdenRequest ordenRequest) throws BadRequestException, ResourceNotFoundException {
        return ordenData.addOrden(ordenRequest);
    }

    @PreAuthorize("hasAuthority('EDIT_ORDEN')")
    public void completarOrden(Long ordenId) throws BadRequestException, ResourceNotFoundException {
        OrdenResponse orden = ordenData.readOrden(ordenId);
        if (!orden.getEstado().equals(OrdenEstado.PAGADO)) throw new MercadeandoException("La orden no ha sido pagada");
        ordenData.completarOrden(orden.getId());
    }

    /**
     * Cambia el estado de una Orden a false (softdelete)
     *
     * @param ordenId Id de una orden registrada
     * @param estado  Boolean
     * @throws ResourceNotFoundException Cuando no se encuentra la Orden
     * @throws BadRequestException       Cuando existen valores necesarios Null
     */
    @PreAuthorize("hasAuthority('DELETE_ORDEN')")
    public void softDeleteOrden(Long ordenId, Boolean estado) throws ResourceNotFoundException, BadRequestException {
        ordenData.softDeleteOrden(ordenId, estado);
    }
}
