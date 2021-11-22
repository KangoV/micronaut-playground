package io.belldj.pg.sessions.domain;

import static java.util.Optional.ofNullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BaseMapper {

    default UUID stringToUUID(String t) { return UUID.fromString(t); }
    default String uUIDToString(UUID t) { return t.toString(); }

    default Optional<String>        wrapString(String t)               { return ofNullable(t); }
    default Optional<LocalDate>     wrapLocalDate(LocalDate t)         { return ofNullable(t); }
    default Optional<LocalDateTime> wrapLocalDateTime(LocalDateTime t) { return ofNullable(t); }

    default String        unwrapString(Optional<String> o)               { return o.orElse(null); }
    default LocalDate     unwrapLocalDate(Optional<LocalDate> o)         { return o.orElse(null); }
    default LocalDateTime unwrapLocalDateTime(Optional<LocalDateTime> o) { return o.orElse(null); }

}
