package io.belldj.pg.events;

import io.micronaut.configuration.kafka.annotation.*;
import org.slf4j.*;

@KafkaListener
public class EventsListener {

    private static final Logger log = LoggerFactory.getLogger(EventsListener.class);

    @Topic("events")
    void receive(@KafkaKey String key, String event) {
      System.out.println("### Received from kafka: "+event);
    }

}