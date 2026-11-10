package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CancionApplicationMapper {

  public CancionModel fromCreateCommandToModel(final CreateCancionCommand command) {
    return new CancionModel(
        new CancionId(command.id()),
        new CancionNombre(command.nombre()),
        command.ritmo().trim(),
        new CancionDuracion(command.duracionSegundos()),
        command.album().trim(),
        command.posicionEnAlbum(),
        command.banda().trim(),
        command.interprete().trim(),
        command.autor().trim(),
        command.fechaLanzamiento());
  }

  public CancionModel fromUpdateCommandToModel(final UpdateCancionCommand command) {
    return new CancionModel(
        new CancionId(command.id()),
        new CancionNombre(command.nombre()),
        command.ritmo().trim(),
        new CancionDuracion(command.duracionSegundos()),
        command.album().trim(),
        command.posicionEnAlbum(),
        command.banda().trim(),
        command.interprete().trim(),
        command.autor().trim(),
        command.fechaLanzamiento());
  }

  public CancionId fromGetByIdQueryToCancionId(final GetCancionByIdQuery query) {
    return new CancionId(query.id());
  }

  public CancionId fromDeleteCommandToCancionId(final DeleteCancionCommand command) {
    return new CancionId(command.id());
  }
}
