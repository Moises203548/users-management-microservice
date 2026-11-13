package com.jcaa.usersmanagement.domain.exception;

public final class CancionNotFoundException extends DomainException {

  private static final String MESSAGE_BY_ID = "The cancion with id '%s' was not found.";

  private CancionNotFoundException(final String message) {
    super(message);
  }

  public static CancionNotFoundException becauseIdWasNotFound(final String cancionId) {
    return new CancionNotFoundException(String.format(MESSAGE_BY_ID, cancionId));
  }
}
