package com.api.mercadeando.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * API response para enlaces
 */
@Setter
@Getter
@JsonPropertyOrder({"rel","type","href"})
public class Link {
    public Link(String rel, String href) {
        this.href=href;
        this.rel=rel;
        this.type=HttpMethods.GET;
    }
    public Link(String rel, String href, HttpMethods type) {
        this.href=href;
        this.rel=rel;
        this.type=type;
    }
    enum HttpMethods {
        GET,
        POST,
        PUT,
        DELETE
    }
    private String rel;
    @Enumerated(EnumType.STRING)
    private HttpMethods type = HttpMethods.GET;
    private String href;

}
