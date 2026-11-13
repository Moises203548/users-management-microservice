package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateCancionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateCancionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CancionRestResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CancionRestMapper {

  public CreateCancionCommand toCreateCommand(final CreateCancionRestRequest request) {
    return new CreateCancionCommand(
        request.id(),
        request.nombre(),
        request.ritmo(),
        request.duracionSegundos(),
        request.album(),
        request.posicionEnAlbum(),
        request.banda(),
        request.interprete(),
        request.autor(),
        request.fechaLanzamiento());
  }

  public UpdateCancionCommand toUpdateCommand(
      final String id, final UpdateCancionRestRequest request) {
    return new UpdateCancionCommand(
        id,
        request.nombre(),
        request.ritmo(),
        request.duracionSegundos(),
        request.album(),
        request.posicionEnAlbum(),
        request.banda(),
        request.interprete(),
        request.autor(),
        request.fechaLanzamiento());
  }

  public GetCancionByIdQuery toGetByIdQuery(final String id) {
    return new GetCancionByIdQuery(id);
  }

  public DeleteCancionCommand toDeleteCommand(final String id) {
    return new DeleteCancionCommand(id);
  }

  public CancionRestResponse toResponse(final CancionModel cancion) {
    return new CancionRestResponse(
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

  public List<CancionRestResponse> toResponseList(final List<CancionModel> canciones) {
    return canciones.stream().map(CancionRestMapper::toResponse).toList();
  }
}
