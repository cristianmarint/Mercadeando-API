package com.api.mercadeando.boot;

import com.api.mercadeando.entity.Permiso;
import com.api.mercadeando.entity.Rol;
import com.api.mercadeando.repository.PermisoRepository;
import com.api.mercadeando.repository.RolRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cristianmarint
 * @Date 2021-01-11 10:00
 */
@Component
@AllArgsConstructor
@Order(0)
@Slf4j
public class RolesYPermisosDataloader implements CommandLineRunner {
    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private RolRepository rolRepository;



    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("---------> 0 - CARGANDO PERMISOS");
        permisoRepository.deleteAllInBatch();
        rolRepository.deleteAllInBatch();
        Rol adminRol = new Rol().builder().name("ADMIN").build();
        Rol usuarioRol = new Rol().builder().name("USUARIO").build();
        Rol cajeroRol = new Rol().builder().name("CAJERO").build();

        Permiso BROWSE_CLIENTE = new Permiso().builder().name("BROWSE_CLIENTE").build();
        Permiso READ_CLIENTE = new Permiso().builder().name("READ_CLIENTE").build();
        Permiso EDIT_CLIENTE = new Permiso().builder().name("EDIT_CLIENTE").build();
        Permiso ADD_CLIENTE = new Permiso().builder().name("ADD_CLIENTE").build();
        Permiso DELETE_CLIENTE = new Permiso().builder().name("DELETE_CLIENTE").build();
        permisoRepository.save(BROWSE_CLIENTE);
        permisoRepository.save(READ_CLIENTE);
        permisoRepository.save(EDIT_CLIENTE);
        permisoRepository.save(ADD_CLIENTE);
        permisoRepository.save(DELETE_CLIENTE);

        adminRol.addPermiso(BROWSE_CLIENTE);
        adminRol.addPermiso(READ_CLIENTE);
        adminRol.addPermiso(EDIT_CLIENTE);
        adminRol.addPermiso(ADD_CLIENTE);
        adminRol.addPermiso(DELETE_CLIENTE);

        cajeroRol.addPermiso(READ_CLIENTE);
        cajeroRol.addPermiso(EDIT_CLIENTE);
        cajeroRol.addPermiso(ADD_CLIENTE);

        Permiso BROWSE_PRODUCTO = new Permiso().builder().name("BROWSE_PRODUCTO").build();
        Permiso READ_PRODUCTO = new Permiso().builder().name("READ_PRODUCTO").build();
        Permiso EDIT_PRODUCTO = new Permiso().builder().name("EDIT_PRODUCTO").build();
        Permiso ADD_PRODUCTO = new Permiso().builder().name("ADD_PRODUCTO").build();
        Permiso DELETE_PRODUCTO = new Permiso().builder().name("DELETE_PRODUCTO").build();
        permisoRepository.save(BROWSE_PRODUCTO);
        permisoRepository.save(READ_PRODUCTO);
        permisoRepository.save(EDIT_PRODUCTO);
        permisoRepository.save(ADD_PRODUCTO);
        permisoRepository.save(DELETE_PRODUCTO);

        adminRol.addPermiso(BROWSE_PRODUCTO);
        adminRol.addPermiso(READ_PRODUCTO);
        adminRol.addPermiso(EDIT_PRODUCTO);
        adminRol.addPermiso(ADD_PRODUCTO);
        adminRol.addPermiso(DELETE_PRODUCTO);

        cajeroRol.addPermiso(READ_PRODUCTO);
        cajeroRol.addPermiso(EDIT_PRODUCTO);
        cajeroRol.addPermiso(ADD_PRODUCTO);

        Permiso BROWSE_ORDEN = new Permiso().builder().name("BROWSE_ORDEN").build();
        Permiso READ_ORDEN = new Permiso().builder().name("READ_ORDEN").build();
        Permiso EDIT_ORDEN = new Permiso().builder().name("EDIT_ORDEN").build();
        Permiso ADD_ORDEN = new Permiso().builder().name("ADD_ORDEN").build();
        Permiso DELETE_ORDEN = new Permiso().builder().name("DELETE_ORDEN").build();
        permisoRepository.save(BROWSE_ORDEN);
        permisoRepository.save(READ_ORDEN);
        permisoRepository.save(EDIT_ORDEN);
        permisoRepository.save(ADD_ORDEN);
        permisoRepository.save(DELETE_ORDEN);

        adminRol.addPermiso(BROWSE_ORDEN);
        adminRol.addPermiso(READ_ORDEN);
        adminRol.addPermiso(EDIT_ORDEN);
        adminRol.addPermiso(ADD_ORDEN);
        adminRol.addPermiso(DELETE_ORDEN);

        cajeroRol.addPermiso(READ_ORDEN);
        cajeroRol.addPermiso(EDIT_ORDEN);
        cajeroRol.addPermiso(ADD_ORDEN);

        rolRepository.save(adminRol);
        rolRepository.save(usuarioRol);
        rolRepository.save(cajeroRol);
    }
}
