package io.belldj.pg.mn;

import static io.belldj.pg.mn.client.api.ClientStatus.ACTIVE;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.belldj.pg.mn.client.api.PhoneType;
import io.belldj.pg.mn.client.web.AddClientT;
import io.belldj.pg.mn.client.web.ClientT;
import io.belldj.pg.mn.client.web.PhoneT;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientFT {

  @Client("/clients")
  interface Clients {
    @Get          List<ClientT> all();
    @Get("/{id}") ClientT       get(@PathVariable String id);
    @Post         ClientT       add(@Body AddClientT addClient);
    @Post         ClientT       save(@Body ClientT client);
  }

  private static final String CLIENT_ID_DARREN = "d967da01-9d66-4623-9762-68506151006c";
  private static final String CLIENT_ID_THERESA = "d967da01-9d66-4623-9762-68506151006d";

  @Inject Flyway flyway;
  @Inject Clients clients;

  @AfterEach
  public void afterEach() {
    flyway.clean();
  }

  @BeforeEach
  public void beforeEach() {
    flyway.migrate();
  }

  @Test
  @DisplayName("GET existing client")
  void testGet() {
    var client = clients.get(CLIENT_ID_DARREN);
    assertThat(client).isNotNull();
    assertSoftly(softly -> {
      softly.assertThat(client.getId()).isEqualTo(CLIENT_ID_DARREN);
      softly.assertThat(client.getForename()).isEqualTo("Darren");
      softly.assertThat(client.getCreated()).isCloseToUtcNow(within(2, HOURS));
      softly.assertThat(client.getPhones())
        .extracting("name", "number")
        .containsExactly(
          tuple(PhoneType.HOME.name(), "+1234123456"),
          tuple(PhoneType.WORK.name(), "+1234123456")
        );
    });
  }

  @Test
  @DisplayName("GET all clients")
  void testGetAll() {
    var allClients = this.clients.all();
    assertThat(allClients)
      .extracting("id", "forename")
      .containsExactly(
        tuple(CLIENT_ID_DARREN, "Darren"),
        tuple(CLIENT_ID_THERESA, "Theresa")
      );
  }

  @Test
  @DisplayName("PUT new client")
  void testAdd() { // POST
    var command = AddClientT.builder()
      .forename("darren")
      .surname("bell")
      .status(ACTIVE)
      .build();
    var clientT = this.clients.add(command);
    assertThat(clientT)
      .extracting("forename", "surname")
      .containsExactly("darren", "bell");
  }

  @Test
  @DisplayName("PUT existing client")
  void testSaveExists() { // PUT
    var client = clients.get(CLIENT_ID_DARREN);
    var command = AddClientT.builder()
      .forename("mia")
      .surname("bell")
      .status(ACTIVE)
      .build();
    var clientT = this.clients.add(command);
    assertThat(clientT).extracting("forename", "surname").containsExactly("mia", "bell");
  }

  @Test
  @DisplayName("POST new client")
  void testSaveNew() { // PUT
    var client = clients.get(CLIENT_ID_DARREN);
    var command = AddClientT.builder()
      .forename("darren")
      .surname("bell")
      .status(ACTIVE)
      .phones(List.of(PhoneT.builder().name("HOME").number("+1234123456").build()))
      .build();
    var clientT = this.clients.add(command);
    assertSoftly(softly -> {
      softly.assertThat(clientT)
        .extracting("forename", "surname")
        .containsExactly("darren", "bell");
      softly.assertThat(client.getPhones())
        .extracting("name", "number")
        .containsExactly(
          tuple(PhoneType.HOME.name(), "+1234123456"),
          tuple(PhoneType.WORK.name(), "+1234123456")
        );
    });
  }

}
