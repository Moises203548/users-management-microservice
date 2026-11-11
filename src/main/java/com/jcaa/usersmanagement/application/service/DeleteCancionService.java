package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteCancionUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteCancionPort;
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.application.service.mapper.CancionApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeleteCancionService implements DeleteCancionUseCase {

  private final DeleteCancionPort deleteCancionPort;
  private final GetCancionByIdPort getCancionByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteCancionCommand command) {
    validateCommand(command);

    final CancionId cancionId = CancionApplicationMapper.fromDeleteCommandToCancionId(command);
    ensureCancionExists(cancionId);
    deleteCancionPort.delete(cancionId);
  }

  private void validateCommand(final DeleteCancionCommand command) {
    final Set<ConstraintViolation<DeleteCancionCommand>> violations = validator.validate(command);
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
