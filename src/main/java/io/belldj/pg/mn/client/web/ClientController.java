package io.belldj.pg.mn.client.web;

import io.belldj.pg.mn.client.Client;
import io.belldj.pg.mn.client.ClientService;
import io.belldj.pg.mn.domain.BaseMapper;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.problem.HttpStatusType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.zalando.problem.Problem;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller("/clients")
public record ClientController(ClientService service, WebClientMapper clientMapper) {

  /**
   * This is the Mapstruct mappers. As the field names are the same, no extra mappings
   * are needed.
   */

  @Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
  public interface WebClientMapper extends BaseMapper {

    Client map(ClientT e);

    ClientT map(Client e);

    @Mapping(target = "id",      ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Client map(AddClientT e);

  }

  @Get
  List<ClientT> all() {
    return service.findAllClients().stream().map(clientMapper::map).toList();
  }

  @Get("/{id}")
  ClientT find(@PathVariable String id) {
    return service
      .findClient(UUID.fromString(id))
      .map(clientMapper::map)
      .orElseThrow(() -> Problem.builder()
        .withType(URI.create("https://db.belldj.io/customer/not-found"))
        .withTitle("Client not found")
        .withStatus(new HttpStatusType(HttpStatus.NOT_FOUND))
        .withDetail("Client with ID " + id + " does not exist")
        .withInstance(URI.create("/clients/"+id))
        .with("id", id)
        .build());
  }

  @Post
  ClientT add(@Body AddClientT command) {
    return clientMapper.map(service.addClient(clientMapper.map(command)));
  }

  @Put
  ClientT save(@Body ClientT client) {
    return clientMapper.map(service.saveClient(clientMapper.map(client)));
  }

}
