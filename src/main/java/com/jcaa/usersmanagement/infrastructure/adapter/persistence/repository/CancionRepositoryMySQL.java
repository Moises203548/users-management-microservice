package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteCancionPort;
import com.jcaa.usersmanagement.application.port.out.GetAllCancionesPort;
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveCancionPort;
import com.jcaa.usersmanagement.application.port.out.UpdateCancionPort;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.CancionPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.CancionPersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.CancionPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CancionRepositoryMySQL
    implements SaveCancionPort,
        UpdateCancionPort,
        GetCancionByIdPort,
        GetAllCancionesPort,
        DeleteCancionPort {

  private static final String SQL_INSERT =
      "INSERT INTO canciones "
      + "(id, nombre, ritmo, duracion_segundos, album, posicion_en_album, "
      + "banda, interprete, autor, fecha_lanzamiento) "
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_UPDATE =
      "UPDATE canciones SET nombre = ?, ritmo = ?, duracion_segundos = ?, album = ?, "
      + "posicion_en_album = ?, banda = ?, interprete = ?, autor = ?, fecha_lanzamiento = ? "
      + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT id, nombre, ritmo, duracion_segundos, album, posicion_en_album, "
      + "banda, interprete, autor, fecha_lanzamiento "
      + "FROM canciones "
      + "WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT id, nombre, ritmo, duracion_segundos, album, posicion_en_album, "
      + "banda, interprete, autor, fecha_lanzamiento "
      + "FROM canciones "
      + "ORDER BY nombre ASC";

  private static final String SQL_DELETE = "DELETE FROM canciones WHERE id = ?";

  private final DataSource dataSource;

  @Override
  public CancionModel save(final CancionModel cancion) {
    final CancionPersistenceDto dto = CancionPersistenceMapper.fromModelToDto(cancion);
    executeSave(dto);
    return findByIdOrFail(cancion.getId());
  }

  @Override
  public CancionModel update(final CancionModel cancion) {
    final CancionPersistenceDto dto = CancionPersistenceMapper.fromModelToDto(cancion);
    executeUpdate(dto);
    return findByIdOrFail(cancion.getId());
  }

  @Override
  public Optional<CancionModel> getById(final CancionId cancionId) {
    try (final Connection connection = dataSource.getConnection();
         final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, cancionId.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(CancionPersistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw CancionPersistenceException.becauseFindByIdFailed(cancionId.value(), exception);
    }
  }

  @Override
  public List<CancionModel> getAll() {
    try (final Connection connection = dataSource.getConnection();
         final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
      final ResultSet resultSet = statement.executeQuery();
      return CancionPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw CancionPersistenceException.becauseFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final CancionId cancionId) {
    try (final Connection connection = dataSource.getConnection();
         final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, cancionId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw CancionPersistenceException.becauseDeleteFailed(cancionId.value(), exception);
    }
  }

  private void executeSave(final CancionPersistenceDto dto) {
    try (final Connection connection = dataSource.getConnection();
         final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.nombre());
      statement.setString(3, dto.ritmo());
      statement.setInt(4, dto.duracionSegundos());
      statement.setString(5, dto.album());
      statement.setInt(6, dto.posicionEnAlbum());
      statement.setString(7, dto.banda());
      statement.setString(8, dto.interprete());
      statement.setString(9, dto.autor());
      statement.setDate(10, Date.valueOf(dto.fechaLanzamiento()));
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw CancionPersistenceException.becauseSaveFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final CancionPersistenceDto dto) {
    try (final Connection connection = dataSource.getConnection();
         final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.nombre());
      statement.setString(2, dto.ritmo());
      statement.setInt(3, dto.duracionSegundos());
      statement.setString(4, dto.album());
      statement.setInt(5, dto.posicionEnAlbum());
      statement.setString(6, dto.banda());
      statement.setString(7, dto.interprete());
      statement.setString(8, dto.autor());
      statement.setDate(9, Date.valueOf(dto.fechaLanzamiento()));
      statement.setString(10, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw CancionPersistenceException.becauseUpdateFailed(dto.id(), exception);
    }
  }

  private CancionModel findByIdOrFail(final CancionId cancionId) {
    return getById(cancionId)
        .orElseThrow(() -> CancionNotFoundException.becauseIdWasNotFound(cancionId.value()));
  }
}
