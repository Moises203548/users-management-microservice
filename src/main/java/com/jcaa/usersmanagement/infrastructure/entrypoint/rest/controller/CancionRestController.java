package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.application.port.in.CreateCancionUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteCancionUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllCancionesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetCancionByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateCancionUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateCancionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateCancionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CancionRestResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.mapper.CancionRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/canciones")
@RequiredArgsConstructor
public class CancionRestController implements CancionRestControllerDocs {

  private final CreateCancionUseCase createCancionUseCase;
  private final UpdateCancionUseCase updateCancionUseCase;
  private final DeleteCancionUseCase deleteCancionUseCase;
  private final GetCancionByIdUseCase getCancionByIdUseCase;
  private final GetAllCancionesUseCase getAllCancionesUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CancionRestResponse create(@Valid @RequestBody final CreateCancionRestRequest request) {
    final CreateCancionCommand command = CancionRestMapper.toCreateCommand(request);
    final CancionModel cancion = createCancionUseCase.execute(command);
    return CancionRestMapper.toResponse(cancion);
  }

  @GetMapping
  public List<CancionRestResponse> getAll() {
    return CancionRestMapper.toResponseList(getAllCancionesUseCase.execute());
  }

  @GetMapping("/{id}")
  public CancionRestResponse getById(@PathVariable final String id) {
    final GetCancionByIdQuery query = CancionRestMapper.toGetByIdQuery(id);
    final CancionModel cancion = getCancionByIdUseCase.execute(query);
    return CancionRestMapper.toResponse(cancion);
  }

  @PutMapping("/{id}")
  public CancionRestResponse update(
      @PathVariable final String id,
      @Valid @RequestBody final UpdateCancionRestRequest request) {
    final UpdateCancionCommand command = CancionRestMapper.toUpdateCommand(id, request);
    final CancionModel cancion = updateCancionUseCase.execute(command);
    return CancionRestMapper.toResponse(cancion);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable final String id) {
    final DeleteCancionCommand command = CancionRestMapper.toDeleteCommand(id);
    deleteCancionUseCase.execute(command);
  }
}
