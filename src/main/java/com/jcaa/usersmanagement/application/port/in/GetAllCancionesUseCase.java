package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.CancionModel;
import java.util.List;

public interface GetAllCancionesUseCase {
  List<CancionModel> execute();
}
