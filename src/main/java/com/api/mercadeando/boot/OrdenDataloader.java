package com.api.mercadeando.boot;

import com.api.mercadeando.entity.*;
import com.api.mercadeando.repository.*;
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
@Order(4)
@AllArgsConstructor
@Slf4j
public class OrdenDataloader implements CommandLineRunner {
    private OrdenRepository ordenRepository;
    private OrdenProductoRepository ordenProductoRepository;
    private ProductoRepository productoRepository;
    private PagoRepository pagoRepository;
    private ClienteRepository clienteRepository;
    private UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        List<Producto> productos = productoRepository.findAll();

        log.info("---------- 4 - CARGANDO ORDENES ----------");
        List<Orden> ordenes = new ArrayList<>();
        List<Pago> pagos = new ArrayList<>();
        List<OrdenProducto> ordenProductos = new ArrayList<>();

        Pago pago1 = new Pago().builder().fecha(Instant.parse("2021-01-08T08:48:04Z")).pagoMetodo(PagoMetodo.EFECTIVO).build();
        Pago pago2 = new Pago().builder().fecha(Instant.parse("2021-01-01T01:01:01Z")).pagoMetodo(PagoMetodo.TARJETA_CREDITO).build();
        pagos.add(pago1);
        pagos.add(pago2);
        pagoRepository.saveAll(pagos);

        Orden orden1 = new Orden().builder()
                .estado(OrdenEstado.PENDIENTE)
                .fecha(Instant.now())
                .precio(BigDecimal.valueOf(15.490))
                .pago(pago1)
                .cliente(clienteRepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .user(userRepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .build();

        Orden orden2 = new Orden().builder()
                .estado(OrdenEstado.PAGADO)
                .fecha(Instant.parse("2021-01-01T01:01:01Z"))
                .precio(BigDecimal.valueOf(7.680))
                .pago(pago2)
                .cliente(clienteRepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .user(userRepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .build();

        ordenes.add(orden1);
        ordenes.add(orden2);
        ordenRepository.saveAll(ordenes);

        log.info("---------- 4.5 - CARGANDO PRODUCTO A ORDENES ----------");

        OrdenProducto orden1Producto1 = new OrdenProducto().builder()
                .cantidad(1)
                .producto(productos.get(0))
                .ordenx(orden1)
                .build();

        OrdenProducto orden2Producto4 = new OrdenProducto().builder()
                .cantidad(3)
                .producto(productos.get(3))
                .ordenx(orden2)
                .build();

        ordenProductos.add(orden1Producto1);
        ordenProductos.add(orden2Producto4);
        ordenProductoRepository.saveAll(ordenProductos);
    }
}
