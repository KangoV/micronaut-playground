package io.belldj.pg.clients.domain;

import io.belldj.pg.clients.client.ClientEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class DomainListener {

  private final EventClient eventClient;

  private static final Logger log = LoggerFactory.getLogger(DomainListener.class);

  public DomainListener(EventClient eventClient) {
    this.eventClient = eventClient;
  }

  @EventListener
  public void onClientSaved(ClientEvent event) {
    log.debug("Received event: {}", event);
    eventClient.notify(event.getClient().getId().toString(), event.toString());
  }

}
