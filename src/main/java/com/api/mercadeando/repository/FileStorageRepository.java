package com.api.mercadeando.repository;

import com.api.mercadeando.entity.FileStorage;
import com.api.mercadeando.entity.FileStorageDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author cristianmarint
 * @Date 2021-01-19 11:43
 */
@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    /**
     * Busca un archivo por nombre.
     * @param fileName nombre de un archivo
     * @return Boolean true si el archivo es encontrado, false si no.
     */
    @Query(
            value = "Select case when(count (*) > 0) then true else false end from file_storage where file_name =:fileName",
            nativeQuery = true
    )
    Boolean findByFileName(String fileName);
}
