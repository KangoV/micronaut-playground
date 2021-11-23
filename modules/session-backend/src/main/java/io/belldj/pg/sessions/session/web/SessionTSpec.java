package io.belldj.pg.sessions.session.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.belldj.pg.sessions.session.api.SessionStatus;
import io.belldj.pg.sessions.util.ImmutableStyle;
import io.micronaut.core.annotation.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

/*
 * The *TSpec interfaces should not have any extra fields as that would then
 * mean that the api exposed by this service would diverge from the interfaces
 * supplied in the api jar.
 */

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(builder = SessionT.Builder.class)
@Schema(name="Session", description="Session description")
public interface SessionTSpec {
  default SessionT change(UnaryOperator<SessionT.Builder> builderFunc) {
    return builderFunc.apply(SessionT.builder().from(this)).build();
  }
            String        getId();
            Integer       getVersion();
            LocalDateTime getCreated();
  @Nullable LocalDateTime getUpdated();
            String        getClientId();
            LocalDateTime getStart();
            LocalDateTime getFinish();
            SessionStatus getStatus();
}
