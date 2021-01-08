package com.api.mercadeando.excepciones;

public class ResourceNotFoundException  extends Exception{
    public ResourceNotFoundException(){}
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(Long id,String recurso){
        super(recurso+" con ID "+id+" no fue encontrado");
    }
}