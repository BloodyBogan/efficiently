USE efficiently;

DROP TABLE IF EXISTS efficiently.user_role;
CREATE TABLE user_role (role_id INT PRIMARY KEY, role VARCHAR(8) NOT NULL UNIQUE);

INSERT INTO user_role (role_id, role) VALUES (1, 'student'), (2, 'referent'), (3, 'admin');

DROP TABLE IF EXISTS efficiently.users;
CREATE TABLE users (user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, ais_id INT(6) NOT NULL UNIQUE, name VARCHAR(50) NOT NULL, password VARCHAR(60) NOT NULL, role INT(3) NOT NULL DEFAULT 1, CONSTRAINT fk_user_role FOREIGN KEY (role) REFERENCES user_role (role_id));

DROP TABLE IF EXISTS efficiently.appointments;
CREATE TABLE appointments (appointment_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, user INT NOT NULL, subject VARCHAR(255) NOT NULL, message TEXT(500) NOT NULL, response TEXT(500), date INT NOT NULL, isDone TINYINT(1) NOT NULL DEFAULT 0, CONSTRAINT fk_users_appointment FOREIGN KEY (user) REFERENCES users (user_id), CONSTRAINT fk_dates_appointment FOREIGN KEY (date) REFERENCES dates (date_id));

DROP TABLE IF EXISTS efficiently.dates;
CREATE TABLE dates (date_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, user INT NOT NULL, date DATETIME NOT NULL, isTaken TINYINT(1) NOT NULL DEFAULT 0, CONSTRAINT fk_users_date FOREIGN KEY (user) REFERENCES users (user_id));
