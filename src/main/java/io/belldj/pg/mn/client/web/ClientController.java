package io.belldj.pg.mn.client.web;

import io.belldj.pg.mn.client.Client;
import io.belldj.pg.mn.client.ClientService;
import io.belldj.pg.mn.domain.BaseMapper;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.problem.HttpStatusType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Mapping(target = "id",      ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    Client map(AddClientT e);
    Client map(ClientT e);
    ClientT map(Client e);
  }

  /**
   * Returns a list of all clients
   * @return the list of clients
   */
  @Get(produces = MediaType.APPLICATION_JSON)
  List<ClientT> all() {
    return service.findAllClients().stream().map(clientMapper::map).toList();
  }

  /**
   * Returns the client for the provided id
   * @return the matching client
   */
  @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
  @ApiResponse(responseCode = "404", description = "Client not found")
  ClientT find(@PathVariable String id) {
    return service
      .findClient(UUID.fromString(id))
      .map(clientMapper::map)
      .orElseThrow(() -> Problem.builder()
        .withType(URI.create("https://db.belldj.io/client/not-found"))
        .withTitle("Client not found")
        .withStatus(new HttpStatusType(HttpStatus.NOT_FOUND))
        .withDetail("Client with ID " + id + " does not exist")
        .withInstance(URI.create("/clients/"+id))
        .with("id", id)
        .build());
  }

  /**
   * Registers a new client
   * @param newClient new client
   * @return the newly registered client
   */
  @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  ClientT add(@Body AddClientT newClient) {
    return clientMapper.map(service.saveClient(clientMapper.map(newClient)));
  }

  /**
   * Modifies the client pointed to by the provided id. Note that the id in the url must
   * match that provided in the body
   * @param client to update
   * @param id of the client to update
   * @return the updated client
   */
  @Put(uri = "/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  ClientT save(@Body ClientT client, @PathVariable String id) {
    if (!id.equals(client.getId())) {
      throw Problem.builder()
        .withType(URI.create("https://db.belldj.io/client/id-immutable"))
        .withTitle("Cannot change the ID of a client")
        .withStatus(new HttpStatusType(HttpStatus.BAD_REQUEST))
        .withDetail("Once a client id create, its assigned unique ID cannot be changed.")
        .withInstance(URI.create("/clients/"+id))
        .with("id", id)
        .build();
    }
    return clientMapper.map(service.saveClient(clientMapper.map(client)));
  }

}
