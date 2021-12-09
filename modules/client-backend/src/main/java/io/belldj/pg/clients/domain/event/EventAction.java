package io.belldj.pg.clients.domain.event;

import io.belldj.pg.common.EnumNames;

public enum EventAction {

  ADD("Add"),
  SAVE("Save");

  private final String name;

  private static final EnumNames<EventAction> NAMES = EnumNames.from(EventAction.values(), EventAction::valueOf, EventAction::getName);

  EventAction(String name) {
    this.name = name;
  }

  public static EventAction from(String name) {
    return NAMES.from(name);
  }

  public String getName() {
    return name;
  }

}
