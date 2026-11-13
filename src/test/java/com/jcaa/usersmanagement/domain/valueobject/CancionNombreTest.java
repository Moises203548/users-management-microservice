package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCancionNombreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CancionNombreTest {

  @ParameterizedTest
  @ValueSource(strings = {"Yesterday", "   Yesterday   ", "Yesterday \t"})
  @DisplayName("Normaliza el nombre quitando espacios al inicio y al final")
  void shouldNormalizeNombre(final String nombre) {
    assertEquals("Yesterday", new CancionNombre(nombre).toString());
  }

  @Test
  @DisplayName("Valida que el nombre no sea nulo")
  void shouldRejectNullNombre() {
    assertThrows(NullPointerException.class, () -> new CancionNombre(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  ", "\t", "\n"})
  @DisplayName("Valida que el nombre no sea vacío")
  void shouldRejectEmptyNombre(final String nombre) {
    assertThrows(InvalidCancionNombreException.class, () -> new CancionNombre(nombre));
  }

  @Test
  @DisplayName("Valida que el nombre no supere 150 caracteres")
  void shouldRejectTooLongNombre() {
    final String tooLong = "a".repeat(151);
    assertThrows(InvalidCancionNombreException.class, () -> new CancionNombre(tooLong));
  }

  @Test
  @DisplayName("Acepta un nombre de exactamente 150 caracteres")
  void shouldAcceptMaximumLengthNombre() {
    final String max = "a".repeat(150);
    assertEquals(max, new CancionNombre(max).value());
  }
}
