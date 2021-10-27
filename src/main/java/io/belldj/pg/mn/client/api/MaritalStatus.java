package io.belldj.pg.mn.client.api;

import io.belldj.pg.mn.api.EnumNames;

public enum MaritalStatus {

  MARRIED("Married"),
  SINGLE("Single");

  private final String name;

  private static final EnumNames<MaritalStatus> NAMES = EnumNames.from(MaritalStatus.values(), MaritalStatus::valueOf, MaritalStatus::getName);

  MaritalStatus(String name) {
    this.name = name;
  }

  public static MaritalStatus from(String name) {
    return NAMES.from(name);
  }

  public String getName() {
    return name;
  }

}
