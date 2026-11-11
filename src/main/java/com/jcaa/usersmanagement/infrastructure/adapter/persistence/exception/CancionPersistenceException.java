package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class CancionPersistenceException extends RuntimeException {

  private static final String MESSAGE_SAVE = "Failed to save cancion with ID: '%s'.";
  private static final String MESSAGE_UPDATE = "Failed to update cancion with ID: '%s'.";
  private static final String MESSAGE_FIND = "Failed to find cancion with ID: '%s'.";
  private static final String MESSAGE_ALL = "Failed to retrieve all canciones.";
  private static final String MESSAGE_DELETE = "Failed to delete cancion with ID: '%s'.";

  private CancionPersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static CancionPersistenceException becauseSaveFailed(
      final String cancionId, final Throwable cause) {
    return new CancionPersistenceException(String.format(MESSAGE_SAVE, cancionId), cause);
  }

  public static CancionPersistenceException becauseUpdateFailed(
      final String cancionId, final Throwable cause) {
    return new CancionPersistenceException(String.format(MESSAGE_UPDATE, cancionId), cause);
  }

  public static CancionPersistenceException becauseFindByIdFailed(
      final String cancionId, final Throwable cause) {
    return new CancionPersistenceException(String.format(MESSAGE_FIND, cancionId), cause);
  }

  public static CancionPersistenceException becauseFindAllFailed(final Throwable cause) {
    return new CancionPersistenceException(MESSAGE_ALL, cause);
  }

  public static CancionPersistenceException becauseDeleteFailed(
      final String cancionId, final Throwable cause) {
    return new CancionPersistenceException(String.format(MESSAGE_DELETE, cancionId), cause);
  }
}
