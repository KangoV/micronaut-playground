CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS client;
CREATE TABLE client (
  id                 UUID          NOT NULL PRIMARY KEY,
  version            INTEGER       NOT NULL,
  created            TIMESTAMPTZ   NOT NULL,
  updated            TIMESTAMPTZ,
  forename           VARCHAR(64)   NOT NULL,
  middle_name        VARCHAR(64),
  surname            VARCHAR(64)   NOT NULL,
  preferred_name     VARCHAR(64),
  date_of_birth      DATE,
  line1              VARCHAR(128),
  line2              VARCHAR(128),
  line3              VARCHAR(128),
  city               VARCHAR(128),
  province           VARCHAR(128),
  post_code          VARCHAR(16),
  phones             VARCHAR(255),
  email              VARCHAR(255),
  telephone          VARCHAR(16),
  fax                VARCHAR(16),
  sex                VARCHAR(10),
  gender_identity    VARCHAR(255),
  sexual_orientation VARCHAR(255),
  race               VARCHAR(255),
  marital_status     VARCHAR(16),
  employment         VARCHAR(16),
  status             VARCHAR(32)   NOT NULL
);
INSERT INTO client (id,version,created,forename,surname,phones,email,telephone,sex,status)
    VALUES('d967da01-9d66-4623-9762-68506151006c', 0, CURRENT_TIMESTAMP, 'David', 'Ball', 'HOME:+1234123456,WORK:+1234123456', 'david.ball@nodomain', '+441234123456' , 'MALE', 'ACTIVE');
INSERT INTO client (id,version,created,forename,surname,email,telephone,sex,status)
    VALUES('d967da01-9d66-4623-9762-68506151006d', 0, CURRENT_TIMESTAMP, 'Sarah', 'Ball', 'sarah.ball@nodomain', '+441234123456' , 'FEMALE', 'SUSPENDED');

commit;