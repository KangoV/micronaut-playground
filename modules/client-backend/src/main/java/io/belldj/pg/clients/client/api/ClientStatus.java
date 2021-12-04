package io.belldj.pg.clients.client.api;

import io.belldj.pg.common.EnumNames;

public enum ClientStatus {

  PROBATION("Probation"),
  ACTIVE("Active"),
  SUSPENDED("Suspended"),
  BANNED("Banned");

  private static final EnumNames<ClientStatus> NAMES = EnumNames.from(ClientStatus.values(), ClientStatus::valueOf, ClientStatus::getName);

  private final String name ;

  ClientStatus(String name) {
    this.name = name;
  }

  public static ClientStatus from(String name) {
    return NAMES.from(name);
  }

  public String getName() {
    return name;
  }

}
