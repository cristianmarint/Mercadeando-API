package com.api.mercadeando.boot;

import com.api.mercadeando.entity.Producto;
import com.api.mercadeando.entity.ProductoEstado;
import com.api.mercadeando.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Crea valores por defecto para productos en la BD
 */
@Component
@AllArgsConstructor
@Order(3)
@Slf4j
public class ProductoDataloader implements CommandLineRunner {
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("---------- 3 - CARGANDO PRODUCTOS ----------");
        List<Producto> productos = new ArrayList<>();

        Producto producto1 = new Producto().builder()
                .nombre("Arroz Diana")
                .foto("https://i.blogs.es/ebc3b9/arroz-blanco-rehacer-pakus/1366_2000.jpg")
                .descripcion("El arroz es la semilla de la planta Oryza sativa o de Oryza glaberrima. Se trata de un cereal considerado alimento básico en muchas culturas culinarias, así como en algunas partes de América Latina.\u200BEl arroz es el segundo cereal más producido en el mundo, detrás del maíz y por delante del trigo.")
                .peso(5000.0)
                .unidades(50)
                .precio(BigDecimal.valueOf(15.490))
                .estado(ProductoEstado.DISPONIBLE)
                .activo(true)
                .build();
        productos.add(producto1);

        Producto producto2 = new Producto().builder()
                .nombre("Lenteja Diana")
                .foto("https://jumbocolombiafood.vteximg.com.br/arquivos/ids/3510553-1000-1000/7702511002933.jpg")
                .descripcion("Lens culinaris, la lenteja es una planta anual herbácea de la familia fabaceae, con tallos de 30 a 40 cm, endebles, ramosos y estriados, hojas oblongas, estípulas lanceoladas, zarcillos poco arrollados")
                .peso(500.0)
                .unidades(10)
                .precio(BigDecimal.valueOf(1.727))
                .estado(ProductoEstado.POCAS_UNIDADES)
                .activo(true)
                .build();
        productos.add(producto2);

        Producto producto3 = new Producto().builder()
                .nombre("Frijol bola roja Maritza premium")
                .foto("https://jumbocolombiafood.vteximg.com.br/arquivos/ids/3406420-1000-1000/7707335283445.jpg?v=636904183744570000")
                .descripcion("Phaseolus vulgaris es la especie más conocida del género Phaseolus en la familia Fabaceae. Sus semillas, y por extensión la propia planta, reciben diversos nombres según la región; entre los más comunes están frejol, frijol\u200B\u200B o fríjol, \u200B\u200B\u200B habichuela, \u200B\u200B caraota, \u200B poroto, \u200B\u200B judía\u200B y alubia.")
                .peso(1000.0)
                .unidades(80)
                .precio(BigDecimal.valueOf(8.990))
                .estado(ProductoEstado.DISPONIBLE)
                .activo(true)
                .build();
        productos.add(producto3);

        Producto producto4 = new Producto().builder()
                .nombre("Garbanzo Maritza premium")
                .foto("https://jumbocolombiafood.vteximg.com.br/arquivos/ids/3406423-1000-1000/7707335280819.jpg?v=636904183748470000")
                .descripcion("Maritza, te trae deliciosos, nutritivos, económicos y muy fáciles de preparar con diversos ingredientes.")
                .peso(500.0)
                .unidades(30)
                .precio(BigDecimal.valueOf(2.590))
                .estado(ProductoEstado.DISPONIBLE)
                .activo(true)
                .build();
        productos.add(producto4);

        Producto producto5 = new Producto().builder()
                .nombre("Maíz pira Diana crispetas alta calidad")
                .foto("https://jumbocolombiafood.vteximg.com.br/arquivos/ids/3529361-1000-1000/7702206036007.jpg?v=637304248950100000")
                .descripcion("¿Ya probaste el Maíz Pira para crispetas Diana? Disfruta en familia de momentos únicos y llenos de sabor.")
                .peso(500.0)
                .unidades(25)
                .precio(BigDecimal.valueOf(1.500))
                .estado(ProductoEstado.DISPONIBLE)
                .activo(true)
                .build();
        productos.add(producto5);

        productoRepository.saveAll(productos);
    }
}
