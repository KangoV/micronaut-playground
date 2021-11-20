package io.belldj.pg.mn.client.api;

import io.belldj.pg.mn.api.EnumNames;

public enum PhoneType {

  HOME("Home"),
  WORK("Work"),
  MOBILE("Mobile"),
  OTHER("Other");

  private final String name;

  private static final EnumNames<PhoneType> NAMES = EnumNames.from(PhoneType.values(), PhoneType::valueOf, PhoneType::getName);

  PhoneType(String name) {
    this.name = name;
  }

  public static PhoneType from(String name) {
    return NAMES.from(name);
  }

  public String getName() {
    return name;
  }

}
