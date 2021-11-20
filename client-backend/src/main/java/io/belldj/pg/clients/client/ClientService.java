package io.belldj.pg.clients.client;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public record ClientService(ClientStore store) {

  private static final Logger log = LoggerFactory.getLogger(ClientService.class);

  public Client saveClient(Client client) {
    return store.add(client);
  }

  public List<Client> findAllClients() {
    return store.findAll();
  }

  public Optional<Client> findClient(UUID id) {
    return store.findById(id);
  }

}
