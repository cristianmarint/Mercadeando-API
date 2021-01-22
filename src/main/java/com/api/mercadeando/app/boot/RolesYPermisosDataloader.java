package com.api.mercadeando.app.boot;

import com.api.mercadeando.infrastructure.persistence.entity.Permiso;
import com.api.mercadeando.infrastructure.persistence.entity.Rol;
import com.api.mercadeando.infrastructure.persistence.jpa.PermisoJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.RolJPARepository;
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
    private PermisoJPARepository permisoJPARepository;
    @Autowired
    private RolJPARepository rolJPARepository;



    @Transactional
    @Override
    public void run(String... args) throws Exception {
        log.info("---------> 0 - CARGANDO PERMISOS");
        permisoJPARepository.deleteAllInBatch();
        rolJPARepository.deleteAllInBatch();
        Rol adminRol = new Rol().builder().name("ADMIN").build();
        Rol usuarioRol = new Rol().builder().name("USUARIO").build();
        Rol cajeroRol = new Rol().builder().name("CAJERO").build();

        Permiso BROWSE_CLIENTE = new Permiso().builder().name("BROWSE_CLIENTE").build();
        Permiso READ_CLIENTE = new Permiso().builder().name("READ_CLIENTE").build();
        Permiso EDIT_CLIENTE = new Permiso().builder().name("EDIT_CLIENTE").build();
        Permiso ADD_CLIENTE = new Permiso().builder().name("ADD_CLIENTE").build();
        Permiso DELETE_CLIENTE = new Permiso().builder().name("DELETE_CLIENTE").build();
        permisoJPARepository.save(BROWSE_CLIENTE);
        permisoJPARepository.save(READ_CLIENTE);
        permisoJPARepository.save(EDIT_CLIENTE);
        permisoJPARepository.save(ADD_CLIENTE);
        permisoJPARepository.save(DELETE_CLIENTE);

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
        permisoJPARepository.save(BROWSE_PRODUCTO);
        permisoJPARepository.save(READ_PRODUCTO);
        permisoJPARepository.save(EDIT_PRODUCTO);
        permisoJPARepository.save(ADD_PRODUCTO);
        permisoJPARepository.save(DELETE_PRODUCTO);

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
        permisoJPARepository.save(BROWSE_ORDEN);
        permisoJPARepository.save(READ_ORDEN);
        permisoJPARepository.save(EDIT_ORDEN);
        permisoJPARepository.save(ADD_ORDEN);
        permisoJPARepository.save(DELETE_ORDEN);

        adminRol.addPermiso(BROWSE_ORDEN);
        adminRol.addPermiso(READ_ORDEN);
        adminRol.addPermiso(EDIT_ORDEN);
        adminRol.addPermiso(ADD_ORDEN);
        adminRol.addPermiso(DELETE_ORDEN);

        cajeroRol.addPermiso(READ_ORDEN);
        cajeroRol.addPermiso(EDIT_ORDEN);
        cajeroRol.addPermiso(ADD_ORDEN);

        Permiso BROWSE_CATEGORIA = new Permiso().builder().name("BROWSE_CATEGORIA").build();
        Permiso READ_CATEGORIA = new Permiso().builder().name("READ_CATEGORIA").build();
        Permiso EDIT_CATEGORIA = new Permiso().builder().name("EDIT_CATEGORIA").build();
        Permiso ADD_CATEGORIA = new Permiso().builder().name("ADD_CATEGORIA").build();
        Permiso DELETE_CATEGORIA = new Permiso().builder().name("DELETE_CATEGORIA").build();
        permisoJPARepository.save(BROWSE_CATEGORIA);
        permisoJPARepository.save(READ_CATEGORIA);
        permisoJPARepository.save(EDIT_CATEGORIA);
        permisoJPARepository.save(ADD_CATEGORIA);
        permisoJPARepository.save(DELETE_CATEGORIA);

        adminRol.addPermiso(BROWSE_CATEGORIA);
        adminRol.addPermiso(READ_CATEGORIA);
        adminRol.addPermiso(EDIT_CATEGORIA);
        adminRol.addPermiso(ADD_CATEGORIA);
        adminRol.addPermiso(DELETE_CATEGORIA);

        cajeroRol.addPermiso(READ_CATEGORIA);
        cajeroRol.addPermiso(EDIT_CATEGORIA);
        cajeroRol.addPermiso(ADD_CATEGORIA);

        rolJPARepository.save(adminRol);
        rolJPARepository.save(usuarioRol);
        rolJPARepository.save(cajeroRol);
    }
}
