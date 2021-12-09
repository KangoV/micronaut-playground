package io.belldj.pg.clients.client;

import io.belldj.pg.clients.domain.DomainEntity;
import io.belldj.pg.common.ImmutableStyle;
import io.belldj.pg.clients.client.api.ClientStatus;
import io.belldj.pg.clients.client.api.Employment;
import io.belldj.pg.clients.client.api.MaritalStatus;
import io.belldj.pg.clients.client.api.Sex;
import io.micronaut.core.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
@ImmutableStyle
public
interface ClientSpec extends DomainEntity {
  @Value.Default default UUID getId() { return UUID.randomUUID(); }
  @Value.Default default Integer getVersion() { return Integer.valueOf(0); }
  @Value.Default default LocalDateTime getCreated() { return LocalDateTime.now(); }
  @Value.Default default Optional<LocalDateTime> getUpdated() { return Optional.empty(); }
  String                  getForename();
  Optional<String>        getMiddleName();
  String                  getSurname();
  @Nullable String        getPreferredName();
  Optional<LocalDate>     getDateOfBirth();
  Optional<String>        getLine1();
  Optional<String>        getLine2();
  Optional<String>        getLine3();
  Optional<String>        getCity();
  Optional<String>        getProvince();
  Optional<String>        getPostCode();
  List<Phone>             getPhones();
  Optional<String>        getEmail();
  Optional<String>        getTelephone();
  Optional<String>        getFax();
  Optional<Sex>           getSex();
  Optional<String>        getGenderIdentity();
  Optional<String>        getSexualOrientation();
  Optional<String>        getRace();
  Optional<MaritalStatus> getMaritalStatus();
  Optional<Employment>    getEmployment();
  ClientStatus            getStatus();
}
