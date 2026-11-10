package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCancionDuracionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CancionDuracionTest {

  @Test
  @DisplayName("Acepta una duración positiva")
  void shouldAcceptPositiveDuration() {
    final CancionDuracion duracion = new CancionDuracion(125);
    assertEquals(125, duracion.segundos());
    assertEquals("125", duracion.toString());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -300})
  @DisplayName("Rechaza duraciones menores o iguales a cero")
  void shouldRejectNonPositiveDuration(final int segundos) {
    assertThrows(InvalidCancionDuracionException.class, () -> new CancionDuracion(segundos));
  }
}
