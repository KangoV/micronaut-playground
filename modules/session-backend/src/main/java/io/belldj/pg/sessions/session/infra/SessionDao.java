package io.belldj.pg.sessions.session.infra;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect= Dialect.POSTGRES)
public interface SessionDao extends CrudRepository<SessionE, UUID> {

  @NonNull
  Optional<SessionE> findById(@NotNull @NonNull UUID id);

}
