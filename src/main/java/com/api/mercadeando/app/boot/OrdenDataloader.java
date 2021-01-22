package com.api.mercadeando.app.boot;

import com.api.mercadeando.infrastructure.persistence.entity.*;
import com.api.mercadeando.infrastructure.persistence.jpa.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Crea valores por defecto para ordenes en la BD
 * Relacionando la orden con producto, cliente y usuario.
 */
@Component
@Order(5)
@AllArgsConstructor
@Slf4j
public class OrdenDataloader implements CommandLineRunner {
    private OrdenJPARepository ordenJPARepository;
    private OrdenProductoJPARepository ordenProductoJPARepository;
    private ProductoJPARepository productoJPARepository;
    private PagoJPARepository pagoJPARepository;
    private ClienteJPARepository clienteJPARepository;
    private UserJPARepository userJPARepository;


    @Override
    public void run(String... args) throws Exception {
        List<Producto> productos = productoJPARepository.findAll();

        log.info("---------> 5 - CARGANDO ORDENES");
        List<Orden> ordenes = new ArrayList<>();
        List<Pago> pagos = new ArrayList<>();
        List<OrdenProducto> ordenProductos = new ArrayList<>();

        Pago pago1 = new Pago().builder().fecha(Instant.parse("2021-01-08T08:48:04Z")).pagoMetodo(PagoMetodo.EFECTIVO).build();
        Pago pago2 = new Pago().builder().fecha(Instant.parse("2021-01-01T01:01:01Z")).pagoMetodo(PagoMetodo.TARJETA_CREDITO).build();
        pagos.add(pago1);
        pagos.add(pago2);
        pagoJPARepository.saveAll(pagos);

        Orden orden1 = new Orden().builder()
                .estado(OrdenEstado.PENDIENTE)
                .fecha(Instant.now())
                .total(BigDecimal.valueOf(15.490))
                .pago(pago1)
                .cliente(clienteJPARepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .user(userJPARepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .build();

        Orden orden2 = new Orden().builder()
                .estado(OrdenEstado.PAGADO)
                .fecha(Instant.parse("2021-01-01T01:01:01Z"))
                .total(BigDecimal.valueOf(38.200))
                .pago(pago2)
                .cliente(clienteJPARepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .user(userJPARepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .build();

        ordenes.add(orden1);
        ordenes.add(orden2);
        ordenJPARepository.saveAll(ordenes);

        log.info("---------> 4.5 - CARGANDO PRODUCTO A ORDENES");

        OrdenProducto orden1Producto1 = new OrdenProducto().builder()
                .cantidad(1)
                .precio(BigDecimal.valueOf(15.490))
                .producto(productos.get(0))
                .ordenx(orden1)
                .build();

        OrdenProducto orden2Producto4 = new OrdenProducto().builder()
                .cantidad(3)
                .precio(BigDecimal.valueOf(2.59))
                .producto(productos.get(3))
                .ordenx(orden2)
                .build();

        OrdenProducto orden2Producto3 = new OrdenProducto().builder()
                .cantidad(3)
                .precio(BigDecimal.valueOf(8.99))
                .producto(productos.get(2))
                .ordenx(orden2)
                .build();

        OrdenProducto orden2Producto2 = new OrdenProducto().builder()
                .cantidad(2)
                .precio(BigDecimal.valueOf(1.73))
                .producto(productos.get(1))
                .ordenx(orden2)
                .build();

        ordenProductos.add(orden1Producto1);
        ordenProductos.add(orden2Producto4);
        ordenProductos.add(orden2Producto3);
        ordenProductos.add(orden2Producto2);
        ordenProductoJPARepository.saveAll(ordenProductos);
    }
}
