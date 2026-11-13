package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidCancionDuracionException;

/** Duración de una canción expresada en segundos. */
public record CancionDuracion(int segundos) {

  public CancionDuracion {
    if (segundos <= 0) {
      throw InvalidCancionDuracionException.becauseValueIsNotPositive(segundos);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(segundos);
  }
}
