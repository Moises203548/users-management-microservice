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
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateCancionPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;

@DisplayName("UpdateCancionService")
@ExtendWith(MockitoExtension.class)
class UpdateCancionServiceTest {

  @Mock private UpdateCancionPort updateCancionPort;
  @Mock private GetCancionByIdPort getCancionByIdPort;

  private UpdateCancionService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new UpdateCancionService(
              updateCancionPort, getCancionByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() actualiza la canción cuando existe")
  void shouldUpdateWhenCancionExists() {
    final UpdateCancionCommand command = new UpdateCancionCommand("c-01", "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney", "Lennon-McCartney", LocalDate.of(1965, 8, 6));
    final CancionModel updated = buildModel();
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.of(buildModel()));
    when(updateCancionPort.update(any())).thenReturn(updated);

    final CancionModel result = service.execute(command);

    assertSame(updated, result);
    verify(updateCancionPort).update(any(CancionModel.class));
  }

  @Test
  @DisplayName("execute() lanza CancionNotFoundException cuando el id no existe")
  void shouldThrowWhenCancionDoesNotExist() {
    final UpdateCancionCommand command = new UpdateCancionCommand("c-01", "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney", "Lennon-McCartney", LocalDate.of(1965, 8, 6));
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.empty());

    assertThrows(CancionNotFoundException.class, () -> service.execute(command));
    verify(updateCancionPort, never()).update(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command es inválido")
  void shouldThrowWhenCommandIsInvalid() {
    final UpdateCancionCommand command = new UpdateCancionCommand("", "", "", 0, "", 0, "", "", "", null);

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(updateCancionPort, getCancionByIdPort);
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
