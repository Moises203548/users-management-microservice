package com.jcaa.usersmanagement.application.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateCancionCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetCancionByIdQuery;
import com.jcaa.usersmanagement.domain.model.CancionModel;
import com.jcaa.usersmanagement.domain.valueobject.CancionId;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CancionApplicationMapper")
class CancionApplicationMapperTest {

  @Test
  @DisplayName("fromCreateCommandToModel() mapea y normaliza todos los campos")
  void shouldMapCreateCommand() {
    final CreateCancionCommand command =
        new CreateCancionCommand(
            " c-01 ", "  Yesterday ", " Balada ", 125, " Help! ", 13, " The Beatles ",
            " Paul McCartney ", " Lennon-McCartney ", LocalDate.of(1965, 8, 6));

    final CancionModel model = CancionApplicationMapper.fromCreateCommandToModel(command);

    assertAll(
        () -> assertEquals("c-01", model.getId().value()),
        () -> assertEquals("Yesterday", model.getNombre().value()),
        () -> assertEquals("Balada", model.getRitmo()),
        () -> assertEquals(125, model.getDuracion().segundos()),
        () -> assertEquals("Help!", model.getAlbum()),
        () -> assertEquals(13, model.getPosicionEnAlbum()),
        () -> assertEquals("The Beatles", model.getBanda()),
        () -> assertEquals("Paul McCartney", model.getInterprete()),
        () -> assertEquals("Lennon-McCartney", model.getAutor()),
        () -> assertEquals(LocalDate.of(1965, 8, 6), model.getFechaLanzamiento()));
  }

  @Test
  @DisplayName("fromUpdateCommandToModel() conserva el id y mapea los campos")
  void shouldMapUpdateCommand() {
    final UpdateCancionCommand command =
        new UpdateCancionCommand(
            "c-02", "Hey Jude", "Rock", 431, "Single", 1, "The Beatles", "Paul McCartney",
            "Lennon-McCartney", LocalDate.of(1968, 8, 26));

    final CancionModel model = CancionApplicationMapper.fromUpdateCommandToModel(command);

    assertEquals("c-02", model.getId().value());
    assertEquals("Hey Jude", model.getNombre().value());
    assertEquals(431, model.getDuracion().segundos());
  }

  @Test
  @DisplayName("Los mappers de id convierten query y command a CancionId")
  void shouldMapIds() {
    assertEquals(
        new CancionId("c-01"),
        CancionApplicationMapper.fromGetByIdQueryToCancionId(new GetCancionByIdQuery("c-01")));
    assertEquals(
        new CancionId("c-01"),
        CancionApplicationMapper.fromDeleteCommandToCancionId(new DeleteCancionCommand("c-01")));
  }
}
