package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.domain.exception.InvalidCancionDuracionException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.CancionPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.CancionEntity;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CancionPersistenceMapper")
class CancionPersistenceMapperTest {

  @Test
  @DisplayName("fromModelToDto() convierte el modelo a DTO de persistencia")
  void shouldMapModelToDto() {
    final CancionPersistenceDto dto = CancionPersistenceMapper.fromModelToDto(buildModel());

    assertAll(
        () -> assertEquals("c-01", dto.id()),
        () -> assertEquals("Yesterday", dto.nombre()),
        () -> assertEquals("Balada", dto.ritmo()),
        () -> assertEquals(125, dto.duracionSegundos()),
        () -> assertEquals("Help!", dto.album()),
        () -> assertEquals(13, dto.posicionEnAlbum()),
        () -> assertEquals(LocalDate.of(1965, 8, 6), dto.fechaLanzamiento()));
  }

  @Test
  @DisplayName("fromEntityToModel() reconstruye el modelo de dominio")
  void shouldMapEntityToModel() {
    final CancionEntity entity =
        new CancionEntity(
            "c-01", "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney",
            "Lennon-McCartney", LocalDate.of(1965, 8, 6));

    assertEquals(buildModel(), CancionPersistenceMapper.fromEntityToModel(entity));
  }

  @Test
  @DisplayName("fromEntityToModel() rechaza una duración inválida guardada en la BD")
  void shouldRejectInvalidDurationFromEntity() {
    final CancionEntity entity =
        new CancionEntity(
            "c-01", "Yesterday", "Balada", 0, "Help!", 13, "The Beatles", "Paul McCartney",
            "Lennon-McCartney", LocalDate.of(1965, 8, 6));

    assertThrows(
        InvalidCancionDuracionException.class,
        () -> CancionPersistenceMapper.fromEntityToModel(entity));
  }

  @Test
  @DisplayName("fromResultSetToModelList() mapea todas las filas")
  void shouldMapResultSetToModelList() throws SQLException {
    final ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.next()).thenReturn(true, true, false);
    when(resultSet.getString("id")).thenReturn("c-01");
    when(resultSet.getString("nombre")).thenReturn("Yesterday");
    when(resultSet.getString("ritmo")).thenReturn("Balada");
    when(resultSet.getInt("duracion_segundos")).thenReturn(125);
    when(resultSet.getString("album")).thenReturn("Help!");
    when(resultSet.getInt("posicion_en_album")).thenReturn(13);
    when(resultSet.getString("banda")).thenReturn("The Beatles");
    when(resultSet.getString("interprete")).thenReturn("Paul McCartney");
    when(resultSet.getString("autor")).thenReturn("Lennon-McCartney");
    when(resultSet.getDate("fecha_lanzamiento")).thenReturn(Date.valueOf("1965-08-06"));

    final List<CancionModel> result = CancionPersistenceMapper.fromResultSetToModelList(resultSet);

    assertEquals(2, result.size());
    assertEquals(buildModel(), result.get(0));
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
