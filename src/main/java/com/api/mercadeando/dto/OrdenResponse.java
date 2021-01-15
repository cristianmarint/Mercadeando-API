package com.api.mercadeando.dto;

import com.api.mercadeando.entity.OrdenEstado;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.api.mercadeando.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-14 9:43
 */

/**
 *  API response para cuando se solicita un solo Cliente.
 */
@JsonPropertyOrder({
        "id",
        "self",
        "activo",
        "fecha",
        "precio",
        "estado",
        "pago",
        "cliente",
        "productos",
})
@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OrdenResponse {
    private Long id;
    private Link self;
    private Boolean activo;
    private OrdenEstado estado;
    private String fecha;
    private BigDecimal precio;
    private PagoResponse pago= new PagoResponse();
    private Link cliente;
    private List<productosDetalle> productos=new ArrayList<>();


    /**
     * Permite vincular todos los producto de una orden con sus datos
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @JsonSerialize
    public class productosDetalle{
        private Long id;
        private String nombre;
        private Integer cantidad;
        private BigDecimal precioUnidad;
        private BigDecimal precioTotal;
        private Link self;
    }
    public void addProductoDetalle(long productoId, String productoNombre, int cantidad, BigDecimal precioUnidad) {
        Link link = new Link("self", String.format(URL_PRODUCTOS_V1 + "/%s",productoId));
        productos.add(new productosDetalle(
                productoId,
                productoNombre,
                cantidad,
                precioUnidad,
                precioUnidad.multiply(BigDecimal.valueOf(cantidad)),
                link
        ));
    }
}
