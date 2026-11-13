package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidCancionIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CancionIdTest {

  @Test
  @DisplayName("Normaliza el id quitando espacios")
  void shouldNormalizeId() {
    assertEquals("c-01", new CancionId("  c-01  ").value());
  }

  @Test
  @DisplayName("Valida que el id no sea nulo")
  void shouldRejectNullId() {
    assertThrows(NullPointerException.class, () -> new CancionId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t"})
  @DisplayName("Valida que el id no sea vacío")
  void shouldRejectEmptyId(final String id) {
    assertThrows(InvalidCancionIdException.class, () -> new CancionId(id));
  }
}
