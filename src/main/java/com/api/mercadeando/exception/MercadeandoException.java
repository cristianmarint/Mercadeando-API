package com.api.mercadeando.exception;

public class MercadeandoException extends RuntimeException {
    public MercadeandoException(){};

    public MercadeandoException(String mensajeException, Exception exception){
        super(mensajeException,exception);
    }

    public MercadeandoException(String mensajeException){
        super(mensajeException);
    }
}
