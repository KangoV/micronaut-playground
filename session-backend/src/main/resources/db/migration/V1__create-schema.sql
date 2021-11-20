CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS session;
CREATE TABLE sess (
  id                 UUID          NOT NULL PRIMARY KEY,
  version            INTEGER       NOT NULL,
  created            TIMESTAMPTZ   NOT NULL,
  updated            TIMESTAMPTZ,
  client_id          UUID          NOT NULL,
  start              TIMESTAMPTZ   NOT NULL,
  finish             TIMESTAMPTZ   NOT NULL
);
INSERT INTO sess (id,version,created,client_id,start,finish)
    VALUES('d967da01-9d66-4623-9762-68506151007c', 0, CURRENT_TIMESTAMP, 'd967da01-9d66-4623-9762-68506151006c', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO sess (id,version,created,client_id,start,finish)
    VALUES('d967da01-9d66-4623-9762-68506151007d', 0, CURRENT_TIMESTAMP, 'd967da01-9d66-4623-9762-68506151006c', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

commit;