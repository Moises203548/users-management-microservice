package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateCancionUseCase;
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateCancionPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.application.service.mapper.CancionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateCancionService implements UpdateCancionUseCase {

  private final UpdateCancionPort updateCancionPort;
  private final GetCancionByIdPort getCancionByIdPort;
  private final Validator validator;

  @Override
  public CancionModel execute(final UpdateCancionCommand command) {
    validateCommand(command);

    final CancionId cancionId = new CancionId(command.id());
    ensureCancionExists(cancionId);

    final CancionModel cancionToUpdate =
        CancionApplicationMapper.fromUpdateCommandToModel(command);
    return updateCancionPort.update(cancionToUpdate);
  }

  private void validateCommand(final UpdateCancionCommand command) {
    final Set<ConstraintViolation<UpdateCancionCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureCancionExists(final CancionId cancionId) {
    getCancionByIdPort
        .getById(cancionId)
        .orElseThrow(() -> CancionNotFoundException.becauseIdWasNotFound(cancionId.value()));
  }
}
