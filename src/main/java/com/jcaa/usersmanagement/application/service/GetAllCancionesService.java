package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllCancionesUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllCancionesPort;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCancionesService implements GetAllCancionesUseCase {

  private final GetAllCancionesPort getAllCancionesPort;

  @Override
  public List<CancionModel> execute() {
    return getAllCancionesPort.getAll();
  }
}
