package io.belldj.pg.clients.domain;

import io.micronaut.core.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface DomainEntity {
  @Nullable UUID getId();
  @Value.Default default Integer getVersion() { return 0; }
  @Nullable LocalDateTime getCreated();
  Optional<LocalDateTime> getUpdated();
}
