package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.CancionId;

public interface DeleteCancionPort {
  void delete(CancionId cancionId);
}
