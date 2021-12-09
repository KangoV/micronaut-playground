package io.belldj.pg.clients.domain.event;

public interface DomainEvent {
  EventAction getAction();
}
