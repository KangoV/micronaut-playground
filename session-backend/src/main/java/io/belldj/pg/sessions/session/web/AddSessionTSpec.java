package io.belldj.pg.sessions.session.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.belldj.pg.sessions.session.api.SessionStatus;
import io.belldj.pg.sessions.util.ImmutableStyle;
import org.immutables.value.Value;

import java.time.LocalDateTime;

/*
 * The *TSpec interfaces should not have any extra fields as that would then
 * mean that the api exposed by this service would diverge from the interfaces
 * supplied in the api jar.
 */

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(builder = AddSessionT.Builder.class)
public interface AddSessionTSpec {
  String        getClientId();
  LocalDateTime getStart();
  LocalDateTime getEnd();
  SessionStatus getStatus();
}
