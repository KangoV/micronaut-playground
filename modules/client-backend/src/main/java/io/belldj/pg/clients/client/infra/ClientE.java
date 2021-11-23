package io.belldj.pg.clients.client.infra;

import io.belldj.pg.clients.client.api.ClientStatus;
import io.belldj.pg.clients.client.api.Employment;
import io.belldj.pg.clients.client.api.MaritalStatus;
import io.belldj.pg.clients.client.api.Sex;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Introspected
@MappedEntity("client")
public record ClientE(

  // standard entity fields
  @Id                    UUID          id,
  @Version               Integer       version,
//  @Nullable              Integer       version,
  @DateCreated           LocalDateTime created,
  @Nullable @DateUpdated LocalDateTime updated,

  // instance fields
            String        forename,
  @Nullable String        middleName,
            String        surname,
  @Nullable String        preferredName,
  @Nullable LocalDate     dateOfBirth,
  @Nullable String        line1,
  @Nullable String        line2,
  @Nullable String        line3,
  @Nullable String        city,
  @Nullable String        province,
  @Nullable String        postCode,
  @Nullable String        phones,
  @Nullable String        email,
  @Nullable String        telephone,
  @Nullable String        fax,
  @Nullable Sex           sex,
  @Nullable String        genderIdentity,
  @Nullable String        sexualOrientation,
  @Nullable String        race,
  @Nullable MaritalStatus maritalStatus,
  @Nullable Employment    employment,
            ClientStatus  status
) {}
