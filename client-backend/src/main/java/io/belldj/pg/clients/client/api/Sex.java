package io.belldj.pg.clients.client.api;

import io.belldj.pg.clients.api.EnumNames;

public enum Sex {

  MALE("Male"),
  FEMALE("Female");

  private final String firstChar;
  private final String name;

  private static final EnumNames<Sex> NAMES = EnumNames.from(Sex.values(), Sex::valueOf, Sex::getName, Sex::firstChar);

  Sex(String name) {
    this.firstChar = name.substring(0,1).toUpperCase();
    this.name = name;
  }

  public static Sex from(String name) {
    return NAMES.from(name);
  }

  public String firstChar() {
    return firstChar;
  }

  public String getName() {
    return name;
  }

}
