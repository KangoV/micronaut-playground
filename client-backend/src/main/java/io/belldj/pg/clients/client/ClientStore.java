package io.belldj.pg.clients.client;

import io.belldj.pg.clients.domain.BaseMapper;
import io.belldj.pg.clients.client.infra.ClientDao;
import io.belldj.pg.clients.client.infra.ClientE;
import jakarta.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.StreamSupport;

@Singleton
public record ClientStore(ClientDao clientDao, InfraClientMapper clientMapper) {

  private static final Logger log = LoggerFactory.getLogger(ClientStore.class);

  @Mapper
  public interface InfraClientMapper extends BaseMapper {

    @Mapping(target="phones", qualifiedByName = { "PhonesStringToList" })
    Client map(ClientE e);

    @Mapping(target="phones", qualifiedByName = { "PhonesListToString" })
    ClientE map(Client e);

    @Named("PhonesStringToList")
    default List<Phone> phonesStringToList(String phones) {
      return phones == null ? List.of() : toMap(phones);
    }

    @Named("PhonesListToString")
    default String phonesListToString(List<Phone> phones) {
      return (phones == null || phones.isEmpty()) ? null : fromMap(phones);
    }

    private static String fromMap(List<Phone> phones) {
      var result = new StringBuilder();
      for (Phone phone : phones) {
        if (!result.isEmpty()) result.append(",");
        result.append(String.format("%s:%s", phone.getName(), phone.getNumber()));
      }
      return result.toString();
    }

    private static List<Phone> toMap(String string) {
      var result = new ArrayList<Phone>();
      var pairs = string.split(",");
      for (String pair : pairs) {
        var keyValue = pair.split(":");
        result.add(Phone.builder().name(keyValue[0]).number(keyValue[1]).build());
      }
      return result;
    }

  }

  List<Client> findAll() {
    log.debug("Finding all clients");
    return StreamSupport.stream(clientDao.findAll().spliterator(), false).map(clientMapper::map).toList();
  }

  Optional<Client> findById(UUID id) {
    log.debug("Finding client with id: {}", id);
    return clientDao.findById(id)
      .map(c -> {
        log.debug("Found client: {}", c);
        return c;
      })
      .map(clientMapper::map);
  }

  Client add(Client client) {
    ClientE e = clientMapper.map(client);
    ClientE f;
    if (clientDao.existsById(client.getId())) { // update
      log.debug("Saving {}", client);
      f = clientDao.update(e);
    } else { // insert
      log.debug("Adding {}", client);
      f = clientDao.save(e);
    }
    return clientMapper.map(f);
  }

}
