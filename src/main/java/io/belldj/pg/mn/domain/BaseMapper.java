package io.belldj.pg.mn.domain;

import static java.util.Optional.ofNullable;

import io.belldj.pg.mn.client.api.Employment;
import io.belldj.pg.mn.client.api.MaritalStatus;
import io.belldj.pg.mn.client.api.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface BaseMapper {

//    default UUID stringToUUID(String t) { return UUID.fromString(t); }
//    default String UUIDToString(UUID t) { return t.toString(); }

    default Optional<Sex>           wrapSex(Sex t)                     { return ofNullable(t); }
    default Optional<String>        wrapString(String t)               { return ofNullable(t); }
    default Optional<LocalDate>     wrapLocalDate(LocalDate t)         { return ofNullable(t); }
    default Optional<Employment>    wrapEmployment(Employment t)       { return ofNullable(t); }
    default Optional<LocalDateTime> wrapLocalDateTime(LocalDateTime t) { return ofNullable(t); }
    default Optional<MaritalStatus> wrapMaritalStatus(MaritalStatus t) { return ofNullable(t); }

    default Sex           unwrapSex(Optional<Sex> o)                     { return o.orElse(null); }
    default String        unwrapString(Optional<String> o)               { return o.orElse(null); }
    default LocalDate     unwrapLocalDate(Optional<LocalDate> o)         { return o.orElse(null); }
    default Employment    unwrapEmployment(Optional<Employment> o)       { return o.orElse(null); }
    default LocalDateTime unwrapLocalDateTime(Optional<LocalDateTime> o) { return o.orElse(null); }
    default MaritalStatus unwrapMaritalStatus(Optional<MaritalStatus> o) { return o.orElse(null); }

}
