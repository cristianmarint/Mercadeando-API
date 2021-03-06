package com.api.mercadeando.app.boot;

import com.api.mercadeando.infrastructure.persistence.entity.Cliente;
import com.api.mercadeando.infrastructure.persistence.entity.ColombiaDepartamentos;
import com.api.mercadeando.infrastructure.persistence.jpa.ClienteJPARepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Crea valores por defecto para clientes en la BD
 */
@Component
@AllArgsConstructor
@Order(2)
@Slf4j
public class ClienteDataloader implements CommandLineRunner {
    private final ClienteJPARepository clienteJPARepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("---------> 2 - CARGANDO CLIENTES");
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente1 = new Cliente().builder()
                .cedula("112777001")
                .nombres("Fernando")
                .apellidos("Lopez")
                .direccion("Crra 1 N # 54-26")
                .ciudad("Cartago")
                .departamento(ColombiaDepartamentos.VALLE_DEL_CAUCA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:01:01Z"))
                .build();
        clientes.add(cliente1);

        Cliente cliente2 = new Cliente().builder()
                .cedula("112777002")
                .nombres("Carlos")
                .apellidos("Castaño")
                .direccion("Crra 2 N # 54-26")
                .ciudad("Amalfi")
                .departamento(ColombiaDepartamentos.ANTIOQUIA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:02:01Z"))
                .build();
        clientes.add(cliente2);

        Cliente cliente3 = new Cliente().builder()
                .cedula("112777003")
                .nombres("Fabio")
                .apellidos("Ochoa Vásquez")
                .direccion("Crra 3 N # 54-26")
                .ciudad("Rionegro")
                .departamento(ColombiaDepartamentos.ANTIOQUIA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:03:01Z"))
                .build();
        clientes.add(cliente3);

        Cliente cliente4 = new Cliente().builder()
                .cedula("112777004")
                .nombres("Gonzalo")
                .apellidos("Rodríguez Gacha")
                .direccion("Crra 4 N # 54-26")
                .ciudad("Pacho")
                .departamento(ColombiaDepartamentos.CUNDINAMARCA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:04:01Z"))
                .build();
        clientes.add(cliente4);

        Cliente cliente5 = new Cliente().builder()
                .cedula("112777005")
                .nombres("Carlos")
                .apellidos("Lehder")
                .direccion("Crra 5 N # 54-26")
                .ciudad("Armenia")
                .departamento(ColombiaDepartamentos.QUINDIO)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:05:01Z"))
                .build();
        clientes.add(cliente5);

        Cliente cliente6 = new Cliente().builder()
                .cedula("112777006")
                .nombres("Gustavo")
                .apellidos("Gaviria")
                .direccion("Crra 6 N # 54-26")
                .ciudad("Pereira")
                .departamento(ColombiaDepartamentos.RISARALDA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:06:01Z"))
                .build();
        clientes.add(cliente6);

        Cliente cliente7 = new Cliente().builder()
                .cedula("112777007")
                .nombres("Jorge Luis")
                .apellidos("Ochoa Vásquez")
                .direccion("Crra 7 N # 54-26")
                .ciudad("Medellin")
                .departamento(ColombiaDepartamentos.ANTIOQUIA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:07:01Z"))
                .build();
        clientes.add(cliente7);

        Cliente cliente8 = new Cliente().builder()
                .cedula("112777008")
                .nombres("Juan David")
                .apellidos("Ochoa Vásquez")
                .direccion("Crra 8 N # 54-26")
                .ciudad("Medellin")
                .departamento(ColombiaDepartamentos.ANTIOQUIA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:08:01Z"))
                .build();
        clientes.add(cliente8);

        Cliente cliente9 = new Cliente().builder()
                .cedula("112777009")
                .nombres("Fabio")
                .apellidos("Ochoa Vásquez")
                .direccion("Crra 9 N # 54-26")
                .ciudad("Medellin")
                .departamento(ColombiaDepartamentos.ANTIOQUIA)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:09:01Z"))
                .build();
        clientes.add(cliente9);

        Cliente cliente10 = new Cliente().builder()
                .cedula("112777010")
                .nombres("Ana Griselda")
                .apellidos("Blanco Restrepo")
                .direccion("Crra 10 N # 54-26")
                .ciudad("Cartagena")
                .departamento(ColombiaDepartamentos.BOLIVAR)
                .activo(true)
                .createdAt(Instant.parse("2021-01-09T18:10:01Z"))
                .build();
        clientes.add(cliente10);

        clienteJPARepository.saveAll(clientes);
    }
}
