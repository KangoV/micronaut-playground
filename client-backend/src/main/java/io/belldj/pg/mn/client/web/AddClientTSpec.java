package io.belldj.pg.mn.client.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.belldj.pg.mn.client.api.ClientStatus;
import io.belldj.pg.mn.client.api.Employment;
import io.belldj.pg.mn.client.api.MaritalStatus;
import io.belldj.pg.mn.client.api.Sex;
import io.belldj.pg.mn.util.ImmutableStyle;
import io.micronaut.core.annotation.Nullable;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.function.UnaryOperator;

/*
 * The *TSpec interfaces should not have any extra fields as that would then
 * mean that the api exposed by this service would diverge from the interfaces
 * supplied in the api jar.
 */

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(builder = AddClientT.Builder.class)
public interface AddClientTSpec {
            String        getForename();
  @Nullable String        getMiddleName();
            String        getSurname();
  @Nullable String        getPreferredName();
  @Nullable LocalDate     getDateOfBirth();
  @Nullable String        getLine1();
  @Nullable String        getLine2();
  @Nullable String        getLine3();
  @Nullable String        getCity();
  @Nullable String        getProvince();
  @Nullable String        getPostCode();
            List<PhoneT>  getPhones();
  @Nullable String        getEmail();
  @Nullable String        getTelephone();
  @Nullable String        getFax();
  @Nullable Sex           getSex();
  @Nullable String        getGenderIdentity();
  @Nullable String        getSexualOrientation();
  @Nullable String        getRace();
  @Nullable MaritalStatus getMaritalStatus();
  @Nullable Employment    getEmployment();
            ClientStatus  getStatus();
}
