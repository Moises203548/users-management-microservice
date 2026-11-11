package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateCancionRestRequest(
    @NotBlank(message = "nombre must not be blank")
        @Size(max = 150, message = "nombre must have at most 150 characters")
        String nombre,
    @NotBlank(message = "ritmo must not be blank")
        @Size(max = 80, message = "ritmo must have at most 80 characters")
        String ritmo,
    @Positive(message = "duracionSegundos must be greater than 0") int duracionSegundos,
    @NotBlank(message = "album must not be blank")
        @Size(max = 150, message = "album must have at most 150 characters")
        String album,
    @Positive(message = "posicionEnAlbum must be greater than 0") int posicionEnAlbum,
    @NotBlank(message = "banda must not be blank")
        @Size(max = 150, message = "banda must have at most 150 characters")
        String banda,
    @NotBlank(message = "interprete must not be blank")
        @Size(max = 150, message = "interprete must have at most 150 characters")
        String interprete,
    @NotBlank(message = "autor must not be blank")
        @Size(max = 150, message = "autor must have at most 150 characters")
        String autor,
    @NotNull(message = "fechaLanzamiento must not be null")
        @PastOrPresent(message = "fechaLanzamiento must not be in the future")
        LocalDate fechaLanzamiento) {}
