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
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;

@DisplayName("GetCancionByIdService")
@ExtendWith(MockitoExtension.class)
class GetCancionByIdServiceTest {

  @Mock private GetCancionByIdPort getCancionByIdPort;

  private GetCancionByIdService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service = new GetCancionByIdService(getCancionByIdPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() retorna la canción cuando el id existe")
  void shouldReturnCancionWhenFound() {
    final CancionModel expected = buildModel();
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.of(expected));

    assertSame(expected, service.execute(new GetCancionByIdQuery("c-01")));
  }

  @Test
  @DisplayName("execute() lanza CancionNotFoundException cuando el id no existe")
  void shouldThrowWhenNotFound() {
    when(getCancionByIdPort.getById(any())).thenReturn(Optional.empty());
    final GetCancionByIdQuery query = new GetCancionByIdQuery("no-existe");

    assertThrows(CancionNotFoundException.class, () -> service.execute(query));
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenQueryIsInvalid() {
    final GetCancionByIdQuery query = new GetCancionByIdQuery("");

    assertThrows(ConstraintViolationException.class, () -> service.execute(query));
    verifyNoInteractions(getCancionByIdPort);
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
