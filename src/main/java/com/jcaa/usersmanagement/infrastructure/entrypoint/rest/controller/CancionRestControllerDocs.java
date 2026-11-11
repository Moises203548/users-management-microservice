package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import com.jcaa.usersmanagement.infrastructure.config.OpenApiConfig;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.CreateCancionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request.UpdateCancionRestRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.ApiErrorResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response.CancionRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Contrato OpenAPI / Swagger para {@link CancionRestController}.
 *
 * <p>Toda la documentación de la API vive aquí para mantener el controlador limpio.
 */
@Tag(name = "Canciones", description = "Gestión de canciones: crear, consultar, actualizar y eliminar.")
public interface CancionRestControllerDocs {

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Crear canción",
      description = "Registra una nueva canción. El ID debe ser único. Requiere rol ADMIN.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Canción creada exitosamente.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CancionRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Token ausente o inválido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permisos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "409",
        description = "Ya existe una canción con ese ID.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  CancionRestResponse create(@Valid @RequestBody CreateCancionRestRequest request);

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Listar canciones",
      description = "Retorna todas las canciones ordenadas alfabéticamente por nombre.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Lista de canciones obtenida exitosamente. Puede ser vacía.",
        content =
            @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = CancionRestResponse.class)))),
    @ApiResponse(
        responseCode = "401",
        description = "Token ausente o inválido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permisos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  @SuppressWarnings("unused") // Invocado por Spring MVC y leído por springdoc mediante reflexión.
  List<CancionRestResponse> getAll();

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Obtener canción por ID",
      description = "Retorna los datos de una canción identificada por su ID único.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Canción encontrada.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CancionRestResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Token ausente o inválido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permisos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna canción con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  CancionRestResponse getById(
      @Parameter(description = "ID único de la canción.", example = "can-001", required = true)
          @PathVariable
          String id);

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Actualizar canción",
      description = "Actualiza los datos de una canción existente. Requiere rol ADMIN.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Canción actualizada exitosamente.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CancionRestResponse.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Token ausente o inválido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permisos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna canción con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  CancionRestResponse update(
      @Parameter(description = "ID único de la canción a actualizar.", example = "can-001", required = true)
          @PathVariable
          String id,
      @Valid @RequestBody UpdateCancionRestRequest request);

  @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
  @Operation(
      summary = "Eliminar canción",
      description = "Elimina permanentemente una canción por su ID. Requiere rol ADMIN.")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Canción eliminada exitosamente."),
    @ApiResponse(
        responseCode = "401",
        description = "Token ausente o inválido.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "403",
        description = "El rol no tiene permisos.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "404",
        description = "No existe ninguna canción con el ID proporcionado.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor.",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)))
  })
  void delete(
      @Parameter(description = "ID único de la canción a eliminar.", example = "can-001", required = true)
          @PathVariable
          String id);
}
