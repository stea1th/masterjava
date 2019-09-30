DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS city;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;


CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE TYPE group_type AS ENUM ('FINISHED', 'CURRENT', 'REGISTERING');

CREATE SEQUENCE user_seq START 100000;

CREATE TABLE city
(
    id   CHAR(4) NOT NULL PRIMARY KEY,
    name TEXT    NOT NULL
);

CREATE TABLE groups
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    name TEXT       NOT NULL,
    type group_type NOT NULL
);

CREATE TABLE projects
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    name        TEXT    NOT NULL,
    description TEXT    NOT NULL,
    group_id    INTEGER NOT NULL,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);

CREATE TABLE users
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
    full_name TEXT      NOT NULL,
    email     TEXT      NOT NULL,
    flag      user_flag NOT NULL,
    city_id   CHAR(4)   NOT NULL,
    group_id  INTEGER,
    FOREIGN KEY (city_id) REFERENCES city (id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX email_idx ON users (email);