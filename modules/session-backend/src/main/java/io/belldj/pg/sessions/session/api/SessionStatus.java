package io.belldj.pg.sessions.session.api;

import io.belldj.pg.sessions.api.EnumNames;

public enum SessionStatus {

  WAITING("Waiting"),
  ACCEPTED("Accepted"),
  TENTATIVE("tentative"),
  DECLINED("Declined");

  private static final EnumNames<SessionStatus> NAMES = EnumNames.from(SessionStatus.values(), SessionStatus::valueOf, SessionStatus::getName);

  private final String name ;

  SessionStatus(String name) {
    this.name = name;
  }

  public static SessionStatus from(String name) {
    return NAMES.from(name);
  }

  public String getName() {
    return name;
  }

}
