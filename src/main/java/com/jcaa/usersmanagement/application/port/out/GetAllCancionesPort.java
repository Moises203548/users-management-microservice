package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.CancionModel;
import java.util.List;

public interface GetAllCancionesPort {
  List<CancionModel> getAll();
}
