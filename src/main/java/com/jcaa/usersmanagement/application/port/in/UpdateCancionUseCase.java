package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateCancionUseCase {
  CancionModel execute(@NotNull @Valid UpdateCancionCommand command);
}
