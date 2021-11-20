package io.belldj.pg.mn;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
  info = @Info(
    title = "Micronaut Playground",
    version = "${api.version}",
    description = "${openapi.description}",
    license = @License(name = "Apache 2.0", url = "https://foo.bar"),
    contact = @Contact(url = "https://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
  )
)
public class Application {
  public static void main(String[] args) {
    Micronaut.run(Application.class, args);
  }
}
