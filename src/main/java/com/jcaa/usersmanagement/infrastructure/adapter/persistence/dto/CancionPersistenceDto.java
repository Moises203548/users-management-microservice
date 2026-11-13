package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

import java.time.LocalDate;

public record CancionPersistenceDto(
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
