package io.belldj.pg.mn;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.belldj.pg.mn.session.web.AddSessionT;
import io.belldj.pg.mn.session.web.SessionT;
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
class SessionFT {

  @Client("/sessions")
  interface Sessions {
    @Get          List<SessionT> getAll();
    @Get("/{id}") SessionT       get(@PathVariable String id);
    @Post         SessionT       post(@Body AddSessionT addSession);
    @Put("/{id}") SessionT       put(@Body SessionT client, String id);
  }

  private static final String DAVID = "David";
  private static final String SARAH = "Sarah";
  private static final String DAVE = "Dave";
  private static final String BALL = "Ball";

  private static final String CLIENT_ID_DAVID = "d967da01-9d66-4623-9762-68506151006c";
  private static final String CLIENT_ID_SARAH = "d967da01-9d66-4623-9762-68506151006d";

  @Inject Flyway flyway;
  @Inject
  Sessions sessions;

  @AfterEach
  public void afterEach() {
    flyway.clean();
  }

  @BeforeEach
  public void beforeEach() {
    flyway.migrate();
  }

  @Test
  @DisplayName("GET existing session")
  void testGet() {
    var session = sessions.get(CLIENT_ID_DAVID);
    assertThat(session).isNotNull();
    assertSoftly(softly -> {
      softly.assertThat(session.getId()).isEqualTo(CLIENT_ID_DAVID);
      softly.assertThat(session.getCreated()).isCloseToUtcNow(within(2, HOURS));
    });
  }

}
