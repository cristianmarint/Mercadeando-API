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

        log.info("---------- 3 - CARGANDO PRODUCTOS ----------");
        List<Orden> ordenes = new ArrayList<>();

        Pago pago1 = new Pago().builder().fecha(Instant.parse("2021-01-08T08:48:04Z")).metodo(Metodo.EFECTIVO).build();
        pagoRepository.save(pago1);

        Orden orden1 = new Orden().builder()
                .estado(OrdenEstado.PENDIENTE)
                .fecha(Instant.now())
                .precio(BigDecimal.valueOf(15.490))
                .pago(pago1)
                .cliente(clienteRepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .user(userRepository.findById(1L).orElseThrow(ChangeSetPersister.NotFoundException::new))
                .build();

        OrdenProducto ordenProducto1 = new OrdenProducto().builder()
                .cantidad(1)
                .producto(productos.get(1))
                .ordenx(orden1)
                .build();

        ordenes.add(orden1);
        ordenRepository.saveAll(ordenes);
    }
}
