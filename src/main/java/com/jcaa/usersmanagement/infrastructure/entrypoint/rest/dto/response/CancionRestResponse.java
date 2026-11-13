package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

import java.time.LocalDate;

public record CancionRestResponse(
    String id,
    String nombre,
    String ritmo,
    int duracionSegundos,
    String album,
    int posicionEnAlbum,
    String banda,
    String interprete,
    String autor,
    LocalDate fechaLanzamiento) {}
