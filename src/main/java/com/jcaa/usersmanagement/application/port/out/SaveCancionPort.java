package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CancionModel;

public interface SaveCancionPort {
  CancionModel save(CancionModel cancion);
}
