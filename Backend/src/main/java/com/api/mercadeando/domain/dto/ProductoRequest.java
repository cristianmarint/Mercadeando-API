package com.api.mercadeando.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cristianmarint
 * @Date 2021-01-21 10:07
 */

/**
 * API request para cuando se crea un solo ProductoData.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoRequest {
    @ApiModelProperty(hidden = true)
    private Long id;
    private String codigo;
    @NotBlank(message = "El nombre del pruducto ser no puede estar Blank o Null")
    @NotNull
    private String nombre;
    private String descripcion;
    /**
     * En gramos
     */
    private Double peso;
    private Integer unidades = 0;
    /**
     * En Peso Colombiano CO
     */
    @NotNull
    @DecimalMin(value = "0.00", message = "El precio debe de ser un valor mayor a cero (0)")
    private BigDecimal precio = BigDecimal.valueOf(0);
    private Set<CategoriaResponse> categorias = new HashSet<>();
}
