package io.belldj.pg.mn;

import static io.belldj.pg.mn.client.api.ClientStatus.ACTIVE;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.belldj.pg.mn.client.api.PhoneType;
import io.belldj.pg.mn.client.web.AddClientT;
import io.belldj.pg.mn.client.web.ClientT;
import io.belldj.pg.mn.client.web.PhoneT;
import io.micronaut.http.annotation.*;
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
    @Get          List<ClientT> getAll();
    @Get("/{id}") ClientT       get(@PathVariable String id);
    @Post         ClientT       post(@Body AddClientT addClient);
    @Put("/{id}") ClientT       put(@Body ClientT client, String id);
  }

  private static final String DAVID = "David";
  private static final String SARAH = "Sarah";
  private static final String DAVE = "Dave";
  private static final String BALL = "Ball";

  private static final String CLIENT_ID_DAVID = "d967da01-9d66-4623-9762-68506151006c";
  private static final String CLIENT_ID_SARAH = "d967da01-9d66-4623-9762-68506151006d";

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
    var client = clients.get(CLIENT_ID_DAVID);
    assertThat(client).isNotNull();
    assertSoftly(softly -> {
      softly.assertThat(client.getId()).isEqualTo(CLIENT_ID_DAVID);
      softly.assertThat(client.getForename()).isEqualTo(DAVID);
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
    var allClients = this.clients.getAll();
    assertThat(allClients)
      .extracting("id", "forename")
      .containsExactly(
        tuple(CLIENT_ID_DAVID, DAVID),
        tuple(CLIENT_ID_SARAH, SARAH)
      );
  }

  @Test
  @DisplayName("POST new client")
  void testAdd() { // POST
    var command = AddClientT.builder()
      .forename(DAVID)
      .surname(BALL)
      .status(ACTIVE)
      .build();
    var clientT = this.clients.post(command);
    assertThat(clientT)
      .extracting("forename", "surname")
      .containsExactly(DAVID, BALL);
  }

  @Test
  @DisplayName("PUT existing client")
  void testSaveExists() { // PUT
    var oldClient = clients.get(CLIENT_ID_DAVID);
    var changedClient = oldClient.change(c -> c.preferredName(DAVE));
    var newClient = this.clients.put(changedClient, oldClient.getId());
    assertSoftly(softly -> {
      softly.assertThat(changedClient).extracting("forename", "preferredName").containsExactly(DAVID, DAVE);
      softly.assertThat(newClient.getVersion()).isGreaterThan(oldClient.getVersion());
    });
  }

  @Test
  @DisplayName("POST new client")
  void testSaveNew() { // PUT
    var client = clients.get(CLIENT_ID_DAVID);
    var command = AddClientT.builder()
      .forename("bill")
      .surname("gates")
      .status(ACTIVE)
      .phones(List.of(PhoneT.builder().name("HOME").number("+1234123456").build()))
      .build();
    var clientT = this.clients.post(command);
    assertSoftly(softly -> {
      softly.assertThat(clientT)
        .extracting("forename", "surname")
        .containsExactly("bill", "gates");
      softly.assertThat(client.getPhones())
        .extracting("name", "number")
        .containsExactly(
          tuple(PhoneType.HOME.name(), "+1234123456"),
          tuple(PhoneType.WORK.name(), "+1234123456")
        );
    });
  }

}
