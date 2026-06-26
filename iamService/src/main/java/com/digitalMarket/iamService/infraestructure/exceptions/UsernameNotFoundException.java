package com.digitalMarket.iamService.infraestructure.exceptions;

public class UsernameNotFoundException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Usuario no existe en %s";

  public UsernameNotFoundException(String tableName) {
    super(String.format(ERROR_MESSAGE, tableName));
  }
}
