package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCancionNombreException extends DomainException {

  private static final String MESSAGE_EMPTY = "The cancion name must not be empty.";
  private static final String MESSAGE_TOO_LONG =
      "The cancion name must have at most %d characters.";

  private InvalidCancionNombreException(final String message) {
    super(message);
  }

  public static InvalidCancionNombreException becauseValueIsEmpty() {
    return new InvalidCancionNombreException(MESSAGE_EMPTY);
  }

  public static InvalidCancionNombreException becauseLengthIsTooLong(final int maximumLength) {
    return new InvalidCancionNombreException(String.format(MESSAGE_TOO_LONG, maximumLength));
  }
}
