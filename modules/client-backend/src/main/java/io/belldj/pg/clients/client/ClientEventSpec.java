package io.belldj.pg.clients.client;

import io.belldj.pg.clients.domain.event.DomainEvent;
import io.belldj.pg.common.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface ClientEventSpec extends DomainEvent {
  Client getClient();
}
