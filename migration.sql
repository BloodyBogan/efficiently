USE efficiently;

DROP TABLE IF EXISTS efficiently.user_role;
CREATE TABLE user_role (role_id INT PRIMARY KEY, role VARCHAR(8) NOT NULL UNIQUE);

INSERT INTO user_role (role_id, role) VALUES (1, 'student'), (2, 'referent'), (3, 'admin');

DROP TABLE IF EXISTS efficiently.users;
CREATE TABLE users (user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, ais_id INT(6) NOT NULL UNIQUE, name VARCHAR(50) NOT NULL, password VARCHAR(60) NOT NULL, role INT(3) NOT NULL DEFAULT 1, CONSTRAINT fk_user_role FOREIGN KEY (role) REFERENCES user_role (role_id));