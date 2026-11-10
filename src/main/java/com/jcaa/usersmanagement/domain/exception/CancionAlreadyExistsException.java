package com.jcaa.usersmanagement.domain.exception;

public final class CancionAlreadyExistsException extends DomainException {

  private static final String MESSAGE_ID_EXISTS = "A cancion with id '%s' already exists.";

  private CancionAlreadyExistsException(final String message) {
    super(message);
  }

  public static CancionAlreadyExistsException becauseIdAlreadyExists(final String cancionId) {
    return new CancionAlreadyExistsException(String.format(MESSAGE_ID_EXISTS, cancionId));
  }
}
