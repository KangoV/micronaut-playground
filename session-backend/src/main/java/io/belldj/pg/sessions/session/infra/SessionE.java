package io.belldj.pg.sessions.session.infra;

import io.belldj.pg.sessions.session.api.SessionStatus;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Introspected
@MappedEntity("sess")
public record SessionE(
  // standard entity fields
  @Id UUID id,
  @Version Integer version,
  @DateCreated LocalDateTime created,
  @Nullable @DateUpdated LocalDateTime updated,
  // instance fields
  UUID clientId,
  LocalDateTime start,
  LocalDateTime finish,
  SessionStatus status
) {}
