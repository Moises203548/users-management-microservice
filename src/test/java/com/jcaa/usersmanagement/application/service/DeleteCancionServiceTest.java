package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.jcaa.usersmanagement.application.port.out.DeleteCancionPort;
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;

@DisplayName("DeleteCancionService")
@ExtendWith(MockitoExtension.class)
class DeleteCancionServiceTest {

  @Mock private DeleteCancionPort deleteCancionPort;
  @Mock private GetCancionByIdPort getCancionByIdPort;

  private DeleteCancionService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new DeleteCancionService(
              deleteCancionPort, getCancionByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() elimina la canción cuando existe")
  void shouldDeleteWhenCancionExists() {
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.of(buildModel()));

    service.execute(new DeleteCancionCommand("c-01"));

    verify(deleteCancionPort).delete(new CancionId("c-01"));
  }

  @Test
  @DisplayName("execute() lanza CancionNotFoundException cuando el id no existe")
  void shouldThrowWhenCancionDoesNotExist() {
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.empty());
    final DeleteCancionCommand command = new DeleteCancionCommand("no-existe");

    assertThrows(CancionNotFoundException.class, () -> service.execute(command));
    verify(deleteCancionPort, never()).delete(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenCommandIsInvalid() {
    final DeleteCancionCommand command = new DeleteCancionCommand("");

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(deleteCancionPort, getCancionByIdPort);
  }

  private static CancionModel buildModel() {
    return new CancionModel(
        new CancionId("c-01"),
        new CancionNombre("Yesterday"),
        "Balada",
        new CancionDuracion(125),
        "Help!",
        13,
        "The Beatles",
        "Paul McCartney",
        "Lennon-McCartney",
        LocalDate.of(1965, 8, 6));
  }
}
