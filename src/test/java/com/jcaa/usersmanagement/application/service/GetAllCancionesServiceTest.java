package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllCancionesPort;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("GetAllCancionesService")
@ExtendWith(MockitoExtension.class)
class GetAllCancionesServiceTest {

  @Mock private GetAllCancionesPort getAllCancionesPort;

  @InjectMocks private GetAllCancionesService service;

  @Test
  @DisplayName("execute() retorna todas las canciones del puerto")
  void shouldReturnAllCanciones() {
    final List<CancionModel> expected = List.of(buildModel());
    when(getAllCancionesPort.getAll()).thenReturn(expected);

    assertSame(expected, service.execute());
  }

  @Test
  @DisplayName("execute() retorna lista vacía cuando no hay canciones")
  void shouldReturnEmptyList() {
    when(getAllCancionesPort.getAll()).thenReturn(List.of());

    assertTrue(service.execute().isEmpty());
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
