CREATE SEQUENCE users_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE profil_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE sector_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE accompaniement_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE appointment_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE post_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       email VARCHAR(255),
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE profil (
                        id BIGSERIAL PRIMARY KEY,
                        availability VARCHAR(255),
                        content TEXT,
                        city VARCHAR(255),
                        department VARCHAR(255),
                        region VARCHAR(255),
                        image VARCHAR(255),
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        user_id BIGINT UNIQUE NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE sector (
                        id BIGSERIAL PRIMARY KEY,
                        content TEXT NOT NULL
);

CREATE TABLE accompaniement (
                                id BIGSERIAL PRIMARY KEY,
                                content TEXT NOT NULL
);

CREATE TABLE user_match (
                            godparent_id BIGINT NOT NULL,
                            leaderproject_id BIGINT NOT NULL,
                            PRIMARY KEY (godparent_id, leaderproject_id),
                            FOREIGN KEY (godparent_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (leaderproject_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE appointment (
                             id BIGSERIAL PRIMARY KEY,
                             hour TIME NOT NULL,
                             date DATE NOT NULL,
                             location VARCHAR(255) NOT NULL,
                             creation_date DATE NOT NULL
);

CREATE TABLE appointment_user (
                                  appointment_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  PRIMARY KEY (appointment_id, user_id),
                                  FOREIGN KEY (appointment_id) REFERENCES appointment(id) ON DELETE CASCADE,
                                  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE profil_sectors (
                                profil_id BIGINT NOT NULL,
                                sector_id BIGINT NOT NULL,
                                PRIMARY KEY (profil_id, sector_id),
                                FOREIGN KEY (profil_id) REFERENCES profil(id) ON DELETE CASCADE,
                                FOREIGN KEY (sector_id) REFERENCES sector(id) ON DELETE CASCADE
);

CREATE TABLE profil_accompaniements (
                                        profil_id BIGINT NOT NULL,
                                        accompaniement_id BIGINT NOT NULL,
                                        PRIMARY KEY (profil_id, accompaniement_id),
                                        FOREIGN KEY (profil_id) REFERENCES profil(id) ON DELETE CASCADE,
                                        FOREIGN KEY (accompaniement_id) REFERENCES accompaniement(id) ON DELETE CASCADE
);

