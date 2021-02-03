package com.api.mercadeando.testdata;

import com.api.mercadeando.domain.dto.CategoriaResponse;
import com.api.mercadeando.domain.dto.CategoriasResponse;
import com.api.mercadeando.domain.dto.Link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CATEGORIAS_V1;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-02-03 12:00
 */
public class CategoriaTestdata {
    public static final String NOMBRE = "Cuidado Personal";
    public static final String DESCRIPCION = "La autoprotección, velar por el bienestar propio y la imagen que transmitimos a los demás, hacen parte del cuidado personal. Muchos lo asocian con aseo e higiene que permite que el cuerpo y la mente se encuentren saludables.\n";
    public static final int CATEGORIAID_NO_EXISTENTE = 36894631;
    public static final int CATEGORIAID_EDIT_READ = 2;
    public static final int CATEGORIAID_DELETE = 3;
    public static final String BODY_VALID = "{\n  \"nombre\": \"nombre\",\n  \"descripcion\": \"descripcion\"\n}";
    public static final String BODY_VALID_EDIT = "{\"nombre\": \"[EDIT]\", \"descripcion\": \"[EDIT]\"}";
    public static final String BODY_INVALID = "{\"descripcion\": \"[EDIT] \"}";
    private static final Long ID = Long.valueOf(4);
    private static final int COUNT = 0;
    private static final Link SELF = new Link("self", URL_CATEGORIAS_V1 + "/" + ID);
    private static final Long PRODUCTOID = Long.valueOf(1);
    private static final List<Link> PRODUCTOS = Collections.singletonList(new Link("producto", URL_PRODUCTOS_V1 + "/" + PRODUCTOID));
    private CategoriaResponse categoria = new CategoriaResponse();
    private CategoriasResponse categorias = new CategoriasResponse();

    public CategoriaResponse generarCategoria() {
        categoria = new CategoriaResponse().builder()
                .id(ID)
                .self(SELF)
                .nombre(NOMBRE)
                .descripcion(DESCRIPCION)
                .productos(PRODUCTOS)
                .build();
        return categoria;
    }

    public CategoriasResponse generarCategorias(int offset, int limit) {
        categorias = new CategoriasResponse().builder()
                .count(4)
                .categorias(Collections.singletonList(categoria))
                .build();
        List<Link> links = new ArrayList<>();

        links.add(new Link("prev", String.format(URL_CATEGORIAS_V1 + "/?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
        links.add(new Link("next", String.format(URL_CATEGORIAS_V1 + "/?offset=%s&limit=%s", offset + limit, limit)));

        categorias.setLinks(links);
        return categorias;
    }
}
