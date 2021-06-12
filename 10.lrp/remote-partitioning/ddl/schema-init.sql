-- BATCH METADATA TABLES
-- SET foreign_key_checks = 0;
DROP TABLE IF EXISTS "BATCH_JOB_EXECUTION"  ;
DROP TABLE IF EXISTS "BATCH_JOB_EXECUTION_CONTEXT" ;
DROP TABLE IF EXISTS "BATCH_JOB_EXECUTION_PARAMS" ;
DROP TABLE IF EXISTS "BATCH_JOB_EXECUTION_SEQ" ;
DROP TABLE IF EXISTS "BATCH_JOB_INSTANCE" ;
DROP TABLE IF EXISTS "BATCH_JOB_SEQ" ;
DROP TABLE IF EXISTS "BATCH_STEP_EXECUTION" ;
DROP TABLE IF EXISTS "BATCH_STEP_EXECUTION_CONTEXT" ;
DROP TABLE IF EXISTS "BATCH_STEP_EXECUTION_SEQ" ;
-- SET foreign_key_checks = 1;

-- CUSTOMERS

DROP TABLE IF EXISTS "customer";
DROP TABLE IF EXISTS "new_customer";

CREATE TABLE "customer" (
                            id SERIAL PRIMARY KEY,
                            firstName varchar(255) default NULL,
                            lastName varchar(255) default NULL,
                            birthdate varchar(255)
);

CREATE TABLE "new_customer" (
                                id SERIAL PRIMARY KEY,
                                firstName varchar(255) default NULL,
                                lastName varchar(255) default NULL,
                                birthdate varchar(255)
);