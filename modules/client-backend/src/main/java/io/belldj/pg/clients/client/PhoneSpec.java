package io.belldj.pg.clients.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.belldj.pg.common.ImmutableStyle;
import org.immutables.value.Value;

/*
 * The *TSpec interfaces should not have any extra fields as that would then
 * mean that the api exposed by this service would diverge from the interfaces
 * supplied in the api jar.
 */

@Value.Immutable
@ImmutableStyle
@JsonDeserialize(builder = Phone.Builder.class)
public interface PhoneSpec {
  String getName();
  String getNumber();
}
