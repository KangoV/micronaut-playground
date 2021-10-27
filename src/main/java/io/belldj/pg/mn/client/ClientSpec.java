package io.belldj.pg.mn.client;

import io.belldj.pg.mn.client.api.ClientStatus;
import io.belldj.pg.mn.client.api.Employment;
import io.belldj.pg.mn.client.api.MaritalStatus;
import io.belldj.pg.mn.client.api.Sex;
import io.belldj.pg.mn.client.web.PhoneT;
import io.belldj.pg.mn.domain.DomainEntity;
import io.belldj.pg.mn.util.ImmutableStyle;
import io.micronaut.core.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Value.Immutable
@ImmutableStyle
public
interface ClientSpec extends DomainEntity {
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
