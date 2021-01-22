package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-22 10:22
 */
@Repository
public interface CategoriaJPARepository extends JpaRepository<Categoria, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT categoria.* FROM categoria ORDER BY categoria.created_at ASC LIMIT :limit OFFSET :offset"
    )
    List<Categoria> getCategorias(@Param("offset") int offset, @Param("limit") int limit);
}
