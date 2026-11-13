package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jcaa.usersmanagement.application.port.in.CreateCancionUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteCancionUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllCancionesUseCase;
import com.jcaa.usersmanagement.application.port.in.GetCancionByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateCancionUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.exception.CancionNotFoundException;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionDuracion;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import com.jcaa.usersmanagement.domain.valueobject.CancionNombre;
import java.time.LocalDate;
import com.jcaa.usersmanagement.infrastructure.security.JwtAuthenticationFilter;
import com.jcaa.usersmanagement.infrastructure.security.JwtTokenService;
import com.jcaa.usersmanagement.infrastructure.security.RestAccessDeniedHandler;
import com.jcaa.usersmanagement.infrastructure.security.RestAuthenticationEntryPoint;
import com.jcaa.usersmanagement.infrastructure.security.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = CancionRestController.class,
    properties = "spring.main.web-application-type=servlet")
@Import({
  SecurityConfig.class,
  JwtAuthenticationFilter.class,
  RestAuthenticationEntryPoint.class,
  RestAccessDeniedHandler.class
})
@DisplayName("CancionRestController")
class CancionRestControllerTest {

  private static final String ID = "c-01";

  private static final String VALID_BODY =
      """
      {
        "id": "c-01",
        "nombre": "Yesterday",
        "ritmo": "Balada",
        "duracionSegundos": 125,
        "album": "Help!",
        "posicionEnAlbum": 13,
        "banda": "The Beatles",
        "interprete": "Paul McCartney",
        "autor": "Lennon-McCartney",
        "fechaLanzamiento": "1965-08-06"
      }
      """;

  private static final String VALID_UPDATE_BODY =
      """
      {
        "nombre": "Yesterday",
        "ritmo": "Balada",
        "duracionSegundos": 125,
        "album": "Help!",
        "posicionEnAlbum": 13,
        "banda": "The Beatles",
        "interprete": "Paul McCartney",
        "autor": "Lennon-McCartney",
        "fechaLanzamiento": "1965-08-06"
      }
      """;

  @Autowired private MockMvc mockMvc;

  @MockBean private CreateCancionUseCase createCancionUseCase;
  @MockBean private UpdateCancionUseCase updateCancionUseCase;
  @MockBean private DeleteCancionUseCase deleteCancionUseCase;
  @MockBean private GetCancionByIdUseCase getCancionByIdUseCase;
  @MockBean private GetAllCancionesUseCase getAllCancionesUseCase;
  @MockBean private JwtTokenService jwtTokenService;

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("GET /api/canciones retorna todas las canciones")
  void shouldGetAllCanciones() throws Exception {
    when(getAllCancionesUseCase.execute()).thenReturn(List.of(buildModel()));

    mockMvc
        .perform(get("/api/canciones"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ID))
        .andExpect(jsonPath("$[0].nombre").value("Yesterday"))
        .andExpect(jsonPath("$[0].duracionSegundos").value(125))
        .andExpect(jsonPath("$[0].fechaLanzamiento").value("1965-08-06"));
  }

  @Test
  @WithMockUser(roles = "REVIEWER")
  @DisplayName("GET /api/canciones/{id} retorna la canción")
  void shouldGetCancionById() throws Exception {
    when(getCancionByIdUseCase.execute(new GetCancionByIdQuery(ID))).thenReturn(buildModel());

    mockMvc
        .perform(get("/api/canciones/{id}", ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ID))
        .andExpect(jsonPath("$.banda").value("The Beatles"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("GET /api/canciones/{id} responde 404 cuando no existe")
  void shouldReturnNotFoundWhenCancionDoesNotExist() throws Exception {
    when(getCancionByIdUseCase.execute(new GetCancionByIdQuery("x")))
        .thenThrow(CancionNotFoundException.becauseIdWasNotFound("x"));

    mockMvc
        .perform(get("/api/canciones/{id}", "x"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/canciones crea una canción válida")
  void shouldCreateCancion() throws Exception {
    final CreateCancionCommand command =
        new CreateCancionCommand(
            ID, "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney",
            "Lennon-McCartney", LocalDate.of(1965, 8, 6));
    when(createCancionUseCase.execute(command)).thenReturn(buildModel());

    mockMvc
        .perform(post("/api/canciones").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ID));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("POST /api/canciones rechaza una solicitud inválida antes del caso de uso")
  void shouldRejectInvalidCreateRequest() throws Exception {
    final String invalid =
        """
        { "id": "", "nombre": "", "duracionSegundos": 0 }
        """;

    mockMvc
        .perform(post("/api/canciones").contentType(MediaType.APPLICATION_JSON).content(invalid))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(createCancionUseCase);
  }

  @Test
  @WithMockUser(roles = "MEMBER")
  @DisplayName("POST /api/canciones responde 403 para un rol sin permisos")
  void shouldForbidCreateForNonAdmin() throws Exception {
    mockMvc
        .perform(post("/api/canciones").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
        .andExpect(status().isForbidden());

    verifyNoInteractions(createCancionUseCase);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("PUT /api/canciones/{id} actualiza una canción")
  void shouldUpdateCancion() throws Exception {
    final UpdateCancionCommand command =
        new UpdateCancionCommand(
            ID, "Yesterday", "Balada", 125, "Help!", 13, "The Beatles", "Paul McCartney",
            "Lennon-McCartney", LocalDate.of(1965, 8, 6));
    when(updateCancionUseCase.execute(command)).thenReturn(buildModel());

    mockMvc
        .perform(
            put("/api/canciones/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_UPDATE_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Yesterday"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("DELETE /api/canciones/{id} elimina una canción")
  void shouldDeleteCancion() throws Exception {
    mockMvc.perform(delete("/api/canciones/{id}", ID)).andExpect(status().isNoContent());

    verify(deleteCancionUseCase).execute(new DeleteCancionCommand(ID));
  }

  private static CancionModel buildModel() {
    return new CancionModel(
        new CancionId("c-01"),
        new CancionNombre("Yesterday"),
        "Balada",
        new CancionDuracion(125),
        "Help!",
        13,
        "The Beatles",
        "Paul McCartney",
        "Lennon-McCartney",
        LocalDate.of(1965, 8, 6));
  }
}
