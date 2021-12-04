package io.belldj.pg.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

public class EnumNames<E extends Enum<E>> {

  @SafeVarargs
  public static <E extends Enum<E>> EnumNames<E> from(E[] values, Function<String, E> valueOfSupplier, Function<E, String>... keySuppliers) {
    return new EnumNames<>(byName(values, keySuppliers), valueOfSupplier);
  }

  private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

  private final Map<String, E> names;
  private final Function<String, E> valueOfSupplier;

  private EnumNames(Map<String, E> names, Function<String, E> valueOfSupplier) {
    this.names = names;
    this.valueOfSupplier = valueOfSupplier;
  }

  public E from(String name) {
    E e = this.names.get(name.toLowerCase());
    return e != null ? e : this.valueOfSupplier.apply(name);
  }

  @SafeVarargs
  private static <E extends Enum<E>> Map<String, E> byName(E[] values, Function<E, String>... keySuppliers) {
    return Arrays
      .stream(values)
      .collect(HashMap::new, (map, value) -> {
        map.putIfAbsent(removeNonAlphanumeric(value.name()).toLowerCase(), value);
        if (keySuppliers != null) {
          Arrays.stream(keySuppliers)
            .map(func -> func.apply(value))
            .map(String::toLowerCase).forEach(key -> {
              map.putIfAbsent(key, value);
              map.putIfAbsent(removeNonAlphanumeric(key), value);
            });
        }
      }, Map::putAll);
  }

  private static String removeNonAlphanumeric(String from) {
    return PUNCTUATION_PATTERN.matcher(from).replaceAll("");
  }
}
