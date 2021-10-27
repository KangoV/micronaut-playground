package io.belldj.pg.mn.client.api;

import io.belldj.pg.mn.api.EnumNames;

public enum Employment {

  EMPLOYED("Employed"),
  SELF_EMPLOYED("Self Employed");

  private final String name;

  private static final EnumNames<Employment> NAMES = EnumNames.from(Employment.values(), Employment::valueOf, Employment::getName);

  Employment(String name) {
    this.name = name;
  }

  public static Employment from(String name) {
    return NAMES.from(name);
  }

  public String getName() {
    return name;
  }

}
