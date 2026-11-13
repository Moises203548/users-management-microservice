package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateCancionUseCase {
  CancionModel execute(@NotNull @Valid CreateCancionCommand command);
}
