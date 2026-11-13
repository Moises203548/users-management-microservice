package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCancionIdException extends DomainException {

  private static final String MESSAGE_EMPTY = "The cancion id must not be empty.";

  private InvalidCancionIdException(final String message) {
    super(message);
  }

  public static InvalidCancionIdException becauseValueIsEmpty() {
    return new InvalidCancionIdException(MESSAGE_EMPTY);
  }
}
