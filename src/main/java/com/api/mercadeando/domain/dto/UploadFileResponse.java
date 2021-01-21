package com.api.mercadeando.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cristianmarint
 * @Date 2021-01-19 4:27
 */

/**
 * Api Response cuando un archivo es subido de forma exitosa
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileUrl;
    private String fileType;
    /**
     * Tamaño del archivo en bytes
     */
    private long size;
}
