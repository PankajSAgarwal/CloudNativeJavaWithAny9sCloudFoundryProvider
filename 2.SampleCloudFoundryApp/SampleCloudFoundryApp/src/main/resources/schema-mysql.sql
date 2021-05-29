-- Rename this file 'schema-mysql.sql' while running it on cloudfoundry
DROP TABLE IF EXISTS CUSTOMERS;

CREATE TABLE CUSTOMERS (
  id    BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  email VARCHAR(255)                      NOT NULL
);