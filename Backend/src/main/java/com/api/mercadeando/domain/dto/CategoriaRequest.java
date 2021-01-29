package com.api.mercadeando.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaRequest {
    @ApiModelProperty(hidden = true)
    private Long id;
    private String nombre;
    private String descripcion;
}
