package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.CancionPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.CancionEntity;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CancionPersistenceMapper {

  public CancionPersistenceDto fromModelToDto(final CancionModel cancion) {
    return new CancionPersistenceDto(
        cancion.getId().value(),
        cancion.getNombre().value(),
        cancion.getRitmo(),
        cancion.getDuracion().segundos(),
        cancion.getAlbum(),
        cancion.getPosicionEnAlbum(),
        cancion.getBanda(),
        cancion.getInterprete(),
        cancion.getAutor(),
        cancion.getFechaLanzamiento());
  }

  public CancionEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    return new CancionEntity(
        resultSet.getString("id"),
        resultSet.getString("nombre"),
        resultSet.getString("ritmo"),
        resultSet.getInt("duracion_segundos"),
        resultSet.getString("album"),
        resultSet.getInt("posicion_en_album"),
        resultSet.getString("banda"),
        resultSet.getString("interprete"),
        resultSet.getString("autor"),
        resultSet.getDate("fecha_lanzamiento").toLocalDate());
  }

  public CancionModel fromEntityToModel(final CancionEntity entity) {
    return new CancionModel(
        new CancionId(entity.id()),
        new CancionNombre(entity.nombre()),
        entity.ritmo(),
        new CancionDuracion(entity.duracionSegundos()),
        entity.album(),
        entity.posicionEnAlbum(),
        entity.banda(),
        entity.interprete(),
        entity.autor(),
        entity.fechaLanzamiento());
  }

  public CancionModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public List<CancionModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<CancionModel> canciones = new ArrayList<>();
    while (resultSet.next()) {
      canciones.add(fromResultSetToModel(resultSet));
    }
    return canciones;
  }
}
