USE efficiently;

DROP TABLE users IF EXISTS;
CREATE TABLE users(ais_id int(5) NOT NULL UNIQUE, name VARCHAR(50) NOT NULL, password VARCHAR(60) NOT NULL, PRIMARY KEY (ais_id));
