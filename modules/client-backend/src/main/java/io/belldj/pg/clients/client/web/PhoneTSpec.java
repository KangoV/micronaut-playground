package io.belldj.pg.clients.client.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.belldj.pg.clients.util.ImmutableStyle;
import org.immutables.value.Value;

/*
 * The *TSpec interfaces should not have any extra fields as that would then
 * mean that the api exposed by this service would diverge from the interfaces
 * supplied in the api jar.
 */

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(builder = PhoneT.Builder.class)
public interface PhoneTSpec {
  String getName();
  String getNumber();
}
