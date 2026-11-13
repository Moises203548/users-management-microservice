package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetCancionByIdUseCase {
  CancionModel execute(@NotNull @Valid GetCancionByIdQuery query);
}
