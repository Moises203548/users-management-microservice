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
import com.jcaa.usersmanagement.application.port.out.SaveCancionPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.domain.exception.CancionAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;

@DisplayName("CreateCancionService")
@ExtendWith(MockitoExtension.class)
class CreateCancionServiceTest {

  @Mock private SaveCancionPort saveCancionPort;
  @Mock private GetCancionByIdPort getCancionByIdPort;

  private CreateCancionService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new CreateCancionService(
              saveCancionPort, getCancionByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() guarda la canción cuando el id no existe")
  void shouldSaveCancionWhenIdIsNew() {
    final CreateCancionCommand command = new CreateCancionCommand("c-01", "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney", "Lennon-McCartney", LocalDate.of(1965, 8, 6));
    final CancionModel saved = buildModel();
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.empty());
    when(saveCancionPort.save(any())).thenReturn(saved);

    final CancionModel result = service.execute(command);

    assertSame(saved, result);
    verify(saveCancionPort).save(any(CancionModel.class));
  }

  @Test
  @DisplayName("execute() lanza CancionAlreadyExistsException cuando el id ya existe")
  void shouldThrowWhenIdAlreadyExists() {
    final CreateCancionCommand command = new CreateCancionCommand("c-01", "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney", "Lennon-McCartney", LocalDate.of(1965, 8, 6));
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.of(buildModel()));

    assertThrows(CancionAlreadyExistsException.class, () -> service.execute(command));
    verify(saveCancionPort, never()).save(any());
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command es inválido")
  void shouldThrowWhenCommandIsInvalid() {
    final CreateCancionCommand command = new CreateCancionCommand("", "", "", 0, "", 0, "", "", "", null);

    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveCancionPort, getCancionByIdPort);
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
