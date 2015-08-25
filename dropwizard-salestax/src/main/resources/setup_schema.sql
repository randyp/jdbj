CREATE SCHEMA salestax;

CREATE TABLE salestax.account(
  id NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1),
  email VARCHAR(500) NOT NULL,
  CONSTRAINT primary_key PRIMARY KEY (id)
);

CREATE TABLE salestax.jurisdiction(
  fips VARCHAR(15) PRIMARY KEY,
  state CHAR(2) NOT NULL,
  county CHAR(3),
  city CHAR(4),
  name VARCHAR(500) NOT NULL,
  abbreviation VARCHAR(500)
);