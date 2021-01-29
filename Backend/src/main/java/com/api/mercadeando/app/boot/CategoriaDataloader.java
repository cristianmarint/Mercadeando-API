package com.api.mercadeando.app.boot;

import com.api.mercadeando.infrastructure.persistence.entity.Categoria;
import com.api.mercadeando.infrastructure.persistence.jpa.CategoriaJPARepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-22 10:22
 */

/**
 * Crea valores por defecto para productos en la BD
 */
@Component
@AllArgsConstructor
@Order(3)
@Slf4j
public class CategoriaDataloader implements CommandLineRunner {
    @Autowired
    private final CategoriaJPARepository categoriaJPARepository;

    @Override
    public void run(String... args) {
        log.info("---------> 3 - CARGANDO CATEGORIAS");
        List<Categoria> categorias = new ArrayList<>();

        Categoria categoria1 = new Categoria().builder()
                .nombre("Cuidado Personal")
                .descripcion("La autoprotección, velar por el bienestar propio y la imagen que transmitimos a los demás, hacen parte del cuidado personal. Muchos lo asocian con aseo e higiene que permite que el cuerpo y la mente se encuentren saludables.\n")
                .createdAt(Instant.parse("2021-01-22T10:27:01Z"))
                .build();
        categorias.add(categoria1);

        Categoria categoria2 = new Categoria().builder()
                .nombre("Frutas y Verduras")
                .descripcion("Lleva a casa frutas y verduras del campo colombiano. Encuentra hortalizas, hierbas aromáticas orgánicas, cebolla, tomates, papas, plátanos, manzanas, bananos y naranjas. Elije lo que quieras y nosotros nos encargamos llevártelo donde lo necesites.")
                .createdAt(Instant.parse("2021-01-22T10:27:01Z"))
                .build();
        categorias.add(categoria2);

        Categoria categoria3 = new Categoria().builder()
                .nombre("Pollo, Carne y Pescado")
                .descripcion("Llevamos hasta tu casa el pollo, las carnes y el pescado de la mejor calidad. Encuentra las ofertas que tenemos para ti.")
                .createdAt(Instant.parse("2021-01-22T10:27:01Z"))
                .build();
        categorias.add(categoria3);

        Categoria categoria4 = new Categoria().builder()
                .nombre("Arroz, Granos y Pastas")
                .descripcion("En esta categoría encuentra arroz blanco, arroz integral, pastas, lentejas, frijoles, sal, granolas, cereales, garbanzos, maíz, quinua, entre otros granos para tu despensa. Disfruta de las mejores marcas y productos.")
                .createdAt(Instant.parse("2021-01-22T10:27:01Z"))
                .build();
        categorias.add(categoria4);

        categoriaJPARepository.saveAll(categorias);
    }
}
