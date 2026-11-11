package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetCancionByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetCancionByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
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
public class GetCancionByIdService implements GetCancionByIdUseCase {

  private final GetCancionByIdPort getCancionByIdPort;
  private final Validator validator;

  @Override
  public CancionModel execute(final GetCancionByIdQuery query) {
    validateQuery(query);

    final CancionId cancionId = CancionApplicationMapper.fromGetByIdQueryToCancionId(query);
    return getCancionByIdPort
        .getById(cancionId)
        .orElseThrow(() -> CancionNotFoundException.becauseIdWasNotFound(cancionId.value()));
  }

  private void validateQuery(final GetCancionByIdQuery query) {
    final Set<ConstraintViolation<GetCancionByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
