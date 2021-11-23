package io.belldj.pg.sessions.session;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public record SessionService(SessionStore store) {

  private static final Logger log = LoggerFactory.getLogger(SessionService.class);

  public Session saveClient(Session session) {
    return store.add(session);
  }

  public List<Session> findAllClients() {
    return store.findAll();
  }

  public Optional<Session> findClient(UUID id) {
    return store.findById(id);
  }

}
