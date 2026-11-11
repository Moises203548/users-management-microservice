package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateCancionUseCase;
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveCancionPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.application.service.mapper.CancionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.CancionAlreadyExistsException;
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
public class CreateCancionService implements CreateCancionUseCase {

  private final SaveCancionPort saveCancionPort;
  private final GetCancionByIdPort getCancionByIdPort;
  private final Validator validator;

  @Override
  public CancionModel execute(final CreateCancionCommand command) {
    validateCommand(command);

    final CancionId cancionId = new CancionId(command.id());
    ensureIdIsNotTaken(cancionId);

    final CancionModel cancionToSave = CancionApplicationMapper.fromCreateCommandToModel(command);
    return saveCancionPort.save(cancionToSave);
  }

  private void validateCommand(final CreateCancionCommand command) {
    final Set<ConstraintViolation<CreateCancionCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureIdIsNotTaken(final CancionId cancionId) {
    getCancionByIdPort
        .getById(cancionId)
        .ifPresent(
            ignored -> {
              throw CancionAlreadyExistsException.becauseIdAlreadyExists(cancionId.value());
            });
  }
}
