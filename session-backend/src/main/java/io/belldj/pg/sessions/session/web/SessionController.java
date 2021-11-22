package io.belldj.pg.sessions.session.web;

import io.belldj.pg.sessions.domain.BaseMapper;
import io.belldj.pg.sessions.session.Session;
import io.belldj.pg.sessions.session.SessionService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.problem.HttpStatusType;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.zalando.problem.Problem;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Controller("/sessions")
public record SessionController(SessionService service, WebClientMapper clientMapper) {

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
    Session map(AddSessionT e);
    Session map(SessionT e);
    SessionT map(Session e);
  }

  /**
   * Returns a list of all clients
   * @return the list of clients
   */
  @ExecuteOn(TaskExecutors.IO)
  @Get(produces = MediaType.APPLICATION_JSON)
  List<SessionT> all() {
    return service.findAllClients().stream().map(clientMapper::map).toList();
  }

  /**
   * Returns the session for the provided id
   * @return the matching session
   */
  @ExecuteOn(TaskExecutors.IO)
  @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
  @ApiResponse(responseCode = "404", description = "Client not found")
  SessionT find(@PathVariable String id) {
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
   * Registers a new session
   * @param newClient new session
   * @return the newly registered session
   */
  @ExecuteOn(TaskExecutors.IO)
  @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  SessionT add(@Body AddSessionT newClient) {
    return clientMapper.map(service.saveClient(clientMapper.map(newClient)));
  }

  /**
   * Modifies the session pointed to by the provided id. Note that the id in the url must
   * match that provided in the body
   * @param client to update
   * @param id of the session to update
   * @return the updated session
   */
  @ExecuteOn(TaskExecutors.IO)
  @Put(uri = "/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  SessionT save(@Body SessionT client, @PathVariable String id) {
    if (!id.equals(client.getId())) {
      throw Problem.builder()
        .withType(URI.create("https://db.belldj.io/client/id-immutable"))
        .withTitle("Cannot change the ID of a session")
        .withStatus(new HttpStatusType(HttpStatus.BAD_REQUEST))
        .withDetail("Once a session id create, its assigned unique ID cannot be changed.")
        .withInstance(URI.create("/clients/"+id))
        .with("id", id)
        .build();
    }
    return clientMapper.map(service.saveClient(clientMapper.map(client)));
  }

}
