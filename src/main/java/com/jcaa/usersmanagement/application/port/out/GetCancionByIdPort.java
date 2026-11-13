package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import java.util.Optional;

public interface GetCancionByIdPort {
  Optional<CancionModel> getById(CancionId cancionId);
}
