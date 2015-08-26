CREATE SCHEMA salestax;

CREATE TABLE salestax.account(
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
  email VARCHAR(500) NOT NULL
);

CREATE TABLE salestax.jurisdiction(
  fips VARCHAR(15) PRIMARY KEY,
  state CHAR(2) NOT NULL,
  county CHAR(3),
  city CHAR(5),
  name VARCHAR(500) NOT NULL,
  abbreviation VARCHAR(500)
);

CREATE TABLE salestax.invoice(
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1) PRIMARY KEY,
  "date" TIMESTAMP NOT NULL
);

CREATE TABLE salestax.shipping (
  invoice_id BIGINT NOT NULL REFERENCES salestax.invoice(id),
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1) PRIMARY KEY,
  street_line1 VARCHAR(500),
  street_line2 VARCHAR(500),
  city VARCHAR(500),
  state CHAR(2),
  postal_code VARCHAR(16)
);

CREATE TABLE salestax.line_item (
  shipping_id BIGINT NOT NULL REFERENCES salestax.shipping(id),
  id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1) PRIMARY KEY,
  sku BIGINT NOT NULL,
  cost BIGINT NOT NULL
);


