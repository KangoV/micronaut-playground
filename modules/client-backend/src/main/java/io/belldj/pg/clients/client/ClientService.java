package io.belldj.pg.clients.client;

import io.belldj.pg.clients.domain.event.EventAction;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class ClientService {

  private static final Logger log = LoggerFactory.getLogger(ClientService.class);

  private final ClientStore store;
  private final ApplicationEventPublisher<ClientEvent> eventPublisher;

  @Inject
  public ClientService(ClientStore store, ApplicationEventPublisher<ClientEvent> eventPublisher) {
    this.store = store;
    this.eventPublisher = eventPublisher;
  }

  public Client addClient(Client client) {
    var savedClient = store.add(client);
    eventPublisher.publishEvent(ClientEvent.builder().action(EventAction.ADD).client(savedClient).build());
    return savedClient;
  }

  public Client saveClient(Client client) {
    var savedClient = store.add(client);
    eventPublisher.publishEvent(ClientEvent.builder().action(EventAction.SAVE).client(savedClient).build());
    return savedClient;
  }

  public List<Client> findAllClients() {
    return store.findAll();
  }

  public Optional<Client> findClient(UUID id) {
    return store.findById(id);
  }

}
