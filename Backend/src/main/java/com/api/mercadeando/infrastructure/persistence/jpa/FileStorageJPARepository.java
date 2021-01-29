package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author cristianmarint
 * @Date 2021-01-19 11:43
 */
@Repository
public interface FileStorageJPARepository extends JpaRepository<FileStorage, Long> {
    /**
     * Busca un archivo por nombre.
     *
     * @param fileName nombre de un archivo
     * @return Boolean true si el archivo es encontrado, false si no.
     */
    @Query(
            value = "Select count(*) from file_storage where file_name =:fileName",
            nativeQuery = true
    )
    Integer findByFileNameAndCount(String fileName);

    /**
     * Busca un archivo por nombre.
     *
     * @param fileName nombre de un archivo
     * @return Optional<FileStorage> datos archivo encontrado
     */
    @Query(
            value = "Select * from file_storage where file_name =:fileName",
            nativeQuery = true
    )
    Optional<FileStorage> findByFileName(String fileName);
}
