package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;
import lombok.Value;

@Value
public class CancionModel {

  CancionId id;
  CancionNombre nombre;
  String ritmo;
  CancionDuracion duracion;
  String album;
  int posicionEnAlbum;
  String banda;
  String interprete;
  String autor;
  LocalDate fechaLanzamiento;
}
