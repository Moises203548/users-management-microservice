package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CancionModel;

public interface UpdateCancionPort {
  CancionModel update(CancionModel cancion);
}
