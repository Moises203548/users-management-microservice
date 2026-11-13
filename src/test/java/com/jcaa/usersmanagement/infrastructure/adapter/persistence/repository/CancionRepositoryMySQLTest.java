package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.CancionPersistenceException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("CancionRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class CancionRepositoryMySQLTest {

  private static final String ID = "c-01";

  @Mock private DataSource dataSource;
  @Mock private Connection connection;
  @Mock private PreparedStatement statement;
  @Mock private ResultSet resultSet;

  private CancionRepositoryMySQL repository;

  @BeforeEach
  void setUp() {
    repository = new CancionRepositoryMySQL(dataSource);
  }

  private void configureConnection() throws SQLException {
    when(dataSource.getConnection()).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(statement);
  }

  private void configureQuery() throws SQLException {
    configureConnection();
    when(statement.executeQuery()).thenReturn(resultSet);
  }

  private void configureRow() throws SQLException {
    when(resultSet.getString("id")).thenReturn(ID);
    when(resultSet.getString("nombre")).thenReturn("Yesterday");
    when(resultSet.getString("ritmo")).thenReturn("Balada");
    when(resultSet.getInt("duracion_segundos")).thenReturn(125);
    when(resultSet.getString("album")).thenReturn("Help!");
    when(resultSet.getInt("posicion_en_album")).thenReturn(13);
    when(resultSet.getString("banda")).thenReturn("The Beatles");
    when(resultSet.getString("interprete")).thenReturn("Paul McCartney");
    when(resultSet.getString("autor")).thenReturn("Lennon-McCartney");
    when(resultSet.getDate("fecha_lanzamiento")).thenReturn(Date.valueOf("1965-08-06"));
  }

  @Test
  @DisplayName("getById() retorna la canción cuando existe")
  void shouldReturnCancionWhenFound() throws SQLException {
    configureQuery();
    when(resultSet.next()).thenReturn(true);
    configureRow();

    final Optional<CancionModel> result = repository.getById(new CancionId(ID));

    assertTrue(result.isPresent());
    assertEquals(buildModel(), result.get());
    verify(statement).setString(1, ID);
  }

  @Test
  @DisplayName("getById() retorna vacío cuando no existe")
  void shouldReturnEmptyWhenNotFound() throws SQLException {
    configureQuery();
    when(resultSet.next()).thenReturn(false);

    assertTrue(repository.getById(new CancionId(ID)).isEmpty());
  }

  @Test
  @DisplayName("getById() lanza CancionPersistenceException ante SQLException")
  void shouldWrapSqlExceptionOnGetById() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));
    final CancionId id = new CancionId(ID);

    assertThrows(CancionPersistenceException.class, () -> repository.getById(id));
  }

  @Test
  @DisplayName("getAll() retorna todas las canciones")
  void shouldReturnAllCanciones() throws SQLException {
    configureQuery();
    when(resultSet.next()).thenReturn(true, false);
    configureRow();

    final List<CancionModel> result = repository.getAll();

    assertEquals(1, result.size());
    assertEquals(buildModel(), result.get(0));
  }

  @Test
  @DisplayName("getAll() lanza CancionPersistenceException ante SQLException")
  void shouldWrapSqlExceptionOnGetAll() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));

    assertThrows(CancionPersistenceException.class, () -> repository.getAll());
  }

  @Test
  @DisplayName("save() inserta y retorna la canción guardada")
  void shouldSaveAndReturnCancion() throws SQLException {
    configureQuery();
    when(resultSet.next()).thenReturn(true);
    configureRow();

    final CancionModel result = repository.save(buildModel());

    assertEquals(buildModel(), result);
    verify(statement).setInt(4, 125);
    verify(statement).setDate(10, Date.valueOf("1965-08-06"));
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("save() lanza CancionPersistenceException cuando falla el INSERT")
  void shouldWrapSqlExceptionOnSave() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));
    final CancionModel model = buildModel();

    assertThrows(CancionPersistenceException.class, () -> repository.save(model));
  }

  @Test
  @DisplayName("save() lanza CancionNotFoundException si no se encuentra tras insertar")
  void shouldThrowWhenCancionIsMissingAfterSave() throws SQLException {
    configureQuery();
    when(resultSet.next()).thenReturn(false);
    final CancionModel model = buildModel();

    assertThrows(CancionNotFoundException.class, () -> repository.save(model));
  }

  @Test
  @DisplayName("update() actualiza y retorna la canción")
  void shouldUpdateAndReturnCancion() throws SQLException {
    configureQuery();
    when(resultSet.next()).thenReturn(true);
    configureRow();

    final CancionModel result = repository.update(buildModel());

    assertEquals(buildModel(), result);
    verify(statement).setString(10, ID);
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("update() lanza CancionPersistenceException cuando falla el UPDATE")
  void shouldWrapSqlExceptionOnUpdate() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));
    final CancionModel model = buildModel();

    assertThrows(CancionPersistenceException.class, () -> repository.update(model));
  }

  @Test
  @DisplayName("delete() ejecuta el DELETE con el id")
  void shouldDeleteCancion() throws SQLException {
    configureConnection();

    repository.delete(new CancionId(ID));

    verify(statement).setString(1, ID);
    verify(statement).executeUpdate();
  }

  @Test
  @DisplayName("delete() lanza CancionPersistenceException ante SQLException")
  void shouldWrapSqlExceptionOnDelete() throws SQLException {
    when(dataSource.getConnection()).thenThrow(new SQLException("boom"));
    final CancionId id = new CancionId(ID);

    assertThrows(CancionPersistenceException.class, () -> repository.delete(id));
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
