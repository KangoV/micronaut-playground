package io.belldj.pg.sessions.session;

import io.belldj.pg.sessions.domain.DomainEntity;
import io.belldj.pg.sessions.session.api.SessionStatus;
import io.belldj.pg.sessions.util.ImmutableStyle;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
@ImmutableStyle
public
interface SessionSpec extends DomainEntity {
  @Value.Default default UUID getId() { return UUID.randomUUID(); }
  @Value.Default default Integer getVersion() { return Integer.valueOf(0); }
  @Value.Default default LocalDateTime getCreated() { return LocalDateTime.now(); }
  @Value.Default default Optional<LocalDateTime> getUpdated() { return Optional.empty(); }
  UUID getClientId();
  LocalDateTime getStart();
  LocalDateTime getFinish();
  SessionStatus getStatus();
}
