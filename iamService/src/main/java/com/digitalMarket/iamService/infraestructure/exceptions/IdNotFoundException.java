package com.digitalMarket.iamService.infraestructure.exceptions;

public class IdNotFoundException extends RuntimeException {
    private static final String ERRORMESSAGE ="Registro no se encuentra en %s";

    public IdNotFoundException(String tableName) {
        super(String.format(ERRORMESSAGE, tableName));
    }
}
