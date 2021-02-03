package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.CategoriaData;
import com.api.mercadeando.domain.dto.CategoriaResponse;
import com.api.mercadeando.domain.dto.CategoriasResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.testdata.CategoriaTestdata;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author cristianmarint
 * @Date 2021-02-03 10:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CategoriaServiceTest {
    private final CategoriaResponse categoriaTD = new CategoriaTestdata().generarCategoria();
    private final CategoriasResponse categoriasTD = new CategoriaTestdata().generarCategorias(0, 5);

    @InjectMocks
    private CategoriaService categoriaService;
    @Mock
    private CategoriaData categoriaData;

    @Test
    public void readCategoria() {
        try {
            Mockito.when(categoriaService.readCategoria(categoriaTD.getId())).thenReturn(categoriaTD);
        } catch (ResourceNotFoundException e) {
            log.info("Readcategoria no encontrada");
        } catch (BadRequestException e) {
            log.info("Readcategoria mala peticion");
        }

        CategoriaResponse saved;
        try {
            saved = categoriaService.readCategoria(categoriaTD.getId());
        } catch (ResourceNotFoundException e) {
            log.info("Readcategoria no encontrada");
            return;
        } catch (BadRequestException e) {
            log.info("Readcategoria mala peticion");
            return;
        }

        assertEquals(categoriaTD.getId(), saved.getId());
        assertEquals(categoriaTD.getSelf(), saved.getSelf());
        assertEquals(categoriaTD.getNombre(), saved.getNombre());
        assertEquals(categoriaTD.getDescripcion(), saved.getDescripcion());
        assertEquals(categoriaTD.getProductos(), saved.getProductos());
    }

    @Test
    public void readCategorias_offset_limit() {
        Mockito.when(categoriaData.getCategorias(0, 5)).thenReturn(categoriasTD);

        CategoriasResponse saved = categoriaService.readCategorias(0, 5);

        assertEquals(categoriasTD.getCount(), saved.getCount());
        assertEquals(categoriasTD.getCategorias(), saved.getCategorias());
        assertEquals(categoriasTD.getLinks(), saved.getLinks());
    }
}
