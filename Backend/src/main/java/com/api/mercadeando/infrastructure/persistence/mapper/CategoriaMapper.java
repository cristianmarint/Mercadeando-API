package com.api.mercadeando.infrastructure.persistence.mapper;

import com.api.mercadeando.domain.dto.CategoriaRequest;
import com.api.mercadeando.domain.dto.CategoriaResponse;
import com.api.mercadeando.domain.dto.CategoriasResponse;
import com.api.mercadeando.domain.dto.Link;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.infrastructure.persistence.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CATEGORIAS_V1;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PRODUCTOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:18
 */
@Component
@AllArgsConstructor
@Slf4j
public class CategoriaMapper {
    /**
     * @param categoria      entidad con datos de una categoria
     * @param productosLinks Links a productos
     * @return CategoriaResponse con detalles de categoria y links a productos
     */
    public CategoriaResponse mapCategoriaToCategoriaResponse(Categoria categoria, List<Link> productosLinks) {
        CategoriaResponse response = new CategoriaResponse();
        if (categoria.getId() != null) response.setId(categoria.getId());
        response.setSelf(new Link("self", String.format(URL_CATEGORIAS_V1 + "/%s", categoria.getId())));
        if (categoria.getNombre() != null) response.setNombre(categoria.getNombre());
        if (categoria.getDescripcion() != null) response.setDescripcion(categoria.getDescripcion());
        if (productosLinks != null) response.setProductos(productosLinks);
        return response;
    }

    /**
     * Transforma una lista de Categoria Entities a CategoriaResponse para ser retornado por la API al frontend
     *
     * @param categorias Lista de categorias con sus datos
     * @param offset     Punto de partida mayor a cero para buscar nuevos valores
     * @param limit      Cantidad de valores a entontrar menor a cien
     * @return CategoriasResponse con categorias y sus datos correspondientes
     */
    public CategoriasResponse mapCategoriasToCategoriaResponse(List<Categoria> categorias, int offset, int limit) {
        CategoriasResponse response = new CategoriasResponse();
        if (categorias != null & !categorias.isEmpty()) {
            response.setCount(categorias.size());
            categorias.forEach(categoria -> response.getCategorias().add(
                    this.mapCategoriaToCategoriaResponse(
                            categoria,
                            Collections.singletonList(new Link("producto", URL_PRODUCTOS_V1 + "/" + categoria.getId()))
                    )));
            if (offset > 0) {
                response.getLinks().add(
                        new Link("prev",
                                String.format("/api/v1/clientes?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
            }
            response.getLinks().add(
                    new Link("next",
                            String.format("/api/v1/clientes?offset=%s&limit=%s", offset + limit, limit)));
        }
        return response;
    }

    /**
     * Mapea un CategoriaRequest a Categoria, si categoria es Null, creará una nueva instancia
     *
     * @param request   CategoriaRequest
     * @param categoria Categoria entity
     * @return Categoria datos de cliente mapeado
     */
    public Categoria mapCategoriaRequestToCategoria(CategoriaRequest request, Categoria categoria) throws BadRequestException {
        if (request == null) throw new BadRequestException("CategoriaRequest no puede ser Null");
        if (categoria == null) categoria = new Categoria();

        if (request.getId() != null) categoria.setId(request.getId());
        if (request.getNombre() != null) categoria.setNombre(request.getNombre());
        if (request.getDescripcion() != null) categoria.setDescripcion(request.getDescripcion());

        return categoria;
    }
}
