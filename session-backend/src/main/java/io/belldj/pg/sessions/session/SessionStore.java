package io.belldj.pg.sessions.session;

import io.belldj.pg.sessions.domain.BaseMapper;
import io.belldj.pg.sessions.session.infra.SessionDao;
import io.belldj.pg.sessions.session.infra.SessionE;
import jakarta.inject.Singleton;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Singleton
public record SessionStore(SessionDao sessionDao, InfraClientMapper sessionMapper) {

  private static final Logger log = LoggerFactory.getLogger(SessionStore.class);

  @Mapper
  public interface InfraClientMapper extends BaseMapper {
    Session map(SessionE e);
    SessionE map(Session e);
  }

  List<Session> findAll() {
    log.debug("Finding all clients");
    return StreamSupport.stream(sessionDao.findAll().spliterator(), false).map(sessionMapper::map).toList();
  }

  Optional<Session> findById(UUID id) {
    log.debug("Finding session with id: {}", id);
    return sessionDao.findById(id)
      .map(c -> {
        log.debug("Found session: {}", c);
        return c;
      })
      .map(sessionMapper::map);
  }

  Session add(Session session) {
    SessionE e = sessionMapper.map(session);
    SessionE f;
    if (sessionDao.existsById(session.getId())) { // update
      log.debug("Saving {}", session);
      f = sessionDao.update(e);
    } else { // insert
      log.debug("Adding {}", session);
      f = sessionDao.save(e);
    }
    return sessionMapper.map(f);
  }

}
