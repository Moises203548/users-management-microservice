package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCancionDuracionException extends DomainException {

  private static final String MESSAGE_NOT_POSITIVE =
      "The cancion duration must be greater than 0 seconds but was %d.";

  private InvalidCancionDuracionException(final String message) {
    super(message);
  }

  public static InvalidCancionDuracionException becauseValueIsNotPositive(final int segundos) {
    return new InvalidCancionDuracionException(String.format(MESSAGE_NOT_POSITIVE, segundos));
  }
}
