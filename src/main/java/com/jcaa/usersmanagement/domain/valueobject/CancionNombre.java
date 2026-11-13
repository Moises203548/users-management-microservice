package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCancionNombreException;
import java.util.Objects;

public record CancionNombre(String value) {

  private static final int MAXIMUM_LENGTH = 150;

  public CancionNombre {
    final String normalizedValue =
        Objects.requireNonNull(value, "CancionNombre cannot be null").trim();
    validateNotEmpty(normalizedValue);
    validateMaximumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidCancionNombreException.becauseValueIsEmpty();
    }
  }

  private static void validateMaximumLength(final String normalizedValue) {
    if (normalizedValue.length() > MAXIMUM_LENGTH) {
      throw InvalidCancionNombreException.becauseLengthIsTooLong(MAXIMUM_LENGTH);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
