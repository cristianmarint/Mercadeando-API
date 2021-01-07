package com.api.mercadeando.excepciones;

public class MercadeandoException extends RuntimeException{
    public MercadeandoException(String mensajeException, Exception exception){
        super(mensajeException,exception);
    }

    public MercadeandoException(String mensajeException){
        super(mensajeException);
    }
}
