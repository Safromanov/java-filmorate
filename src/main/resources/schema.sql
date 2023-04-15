CREATE TABLE  IF NOT EXISTS PUBLIC.GENRE
(
    GENRE_ID   INTEGER NOT NULL AUTO_INCREMENT primary key,
    GENRE_NAME CHARACTER VARYING(31)
);

MERGE INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');

CREATE TABLE IF NOT EXISTS PUBLIC.MPA
(
    MPA_ID   INTEGER NOT NULL AUTO_INCREMENT primary key,
    MPA_NAME CHARACTER VARYING(7)
);

MERGE  INTO MPA (MPA_id, MPA_NAME)
VALUES (1, 'G'), ( 2, 'PG'), ( 3, 'PG-13'), (4,  'R'), (5, 'NC-17');


CREATE TABLE IF NOT EXISTS PUBLIC.FILMS
(
    FILM_ID         INTEGER NOT NULL AUTO_INCREMENT primary key,
    FILM_NAME       CHARACTER VARYING(200),
    DESCRIPTION     CHARACTER VARYING(200),
    MPA_ID          INTEGER references MPA (MPA_ID),
    RELEASE_DATE    DATE,
    DURATION_MINUTE INTEGER
);

CREATE TABLE  IF NOT EXISTS PUBLIC.GENRE_FILMS
(
    FILM_ID INTEGER NOT NULL references FILMS(FILM_ID),
    GENRE_ID   INTEGER NOT NULL references GENRE(GENRE_ID),
    PRIMARY key (GENRE_ID, FILM_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS
(
    USER_ID  INTEGER NOT NULL AUTO_INCREMENT primary key,
    EMAIL    CHARACTER VARYING(63),
    LOGIN    CHARACTER VARYING(63),
    USER_NAME     CHARACTER VARYING(63),
    BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDSHIP
(
    USER_ID     INTEGER NOT NULL references USERS (USER_ID),
    FRIEND_ID   INTEGER NOT NULL references USERS (USER_ID),
    PRIMARY key (USER_ID, FRIEND_ID)
);


CREATE TABLE IF NOT EXISTS PUBLIC.LIKES_FILM
(
    FILM_ID INTEGER NOT NULL references FILMS(FILM_ID),
    USER_ID INTEGER NOT NULL references USERS(USER_ID),
    PRIMARY key (USER_ID, FILM_ID)
);
