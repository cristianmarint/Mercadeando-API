package com.api.mercadeando.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"rel","type","href"})
public class Link {
    private String rel;
    @Enumerated(EnumType.STRING)
    private static HttpMethods type = HttpMethods.GET;
    private String href;

}
