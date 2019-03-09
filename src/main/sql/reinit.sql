drop table book_ratings;

drop table book_genres;

drop table fav_genres;

drop table genres;

drop table custom_list_element;

drop table books;

drop table authors;

drop table publishers;

drop table custom_list_types;

drop table users;


drop sequence author_id_seq;

drop sequence publisher_id_seq;

drop sequence book_id_seq;

drop sequence custom_list_type_id_seq;

drop sequence user_id_seq;

drop sequence genre_id_seq;

drop sequence rating_id_seq;

--sequences
CREATE SEQUENCE IF NOT EXISTS author_id_seq
  START 1
  INCREMENT 1;

CREATE SEQUENCE IF NOT EXISTS publisher_id_seq
  START 1
  INCREMENT 1;

CREATE SEQUENCE IF NOT EXISTS book_id_seq
  START 1
  INCREMENT 1;

CREATE SEQUENCE IF NOT EXISTS custom_list_type_id_seq
  START 1
  INCREMENT 1;

CREATE SEQUENCE IF NOT EXISTS user_id_seq
  START 1
  INCREMENT 1;

CREATE SEQUENCE IF NOT EXISTS genre_id_seq
  START 1
  INCREMENT 1;

CREATE SEQUENCE IF NOT EXISTS rating_id_seq
  START 1
  INCREMENT 1;


--tables
create table if not exists authors
(
  author_id   int default nextval('author_id_seq')
    constraint authors_pk
      primary key,
  name        varchar(65) not null,
  birth_date  date,
  death_date  date,
  image_hash  varchar(100000),
  description varchar(5000)
);

create table if not exists publishers
(
  publisher_id int default nextval('publisher_id_seq')
    constraint publishers_pk
      primary key,
  title        varchar(150) not null
);

create table if not exists books
(
  book_id        int default nextval('book_id_seq')
    constraint books_pk
      primary key,
  title          varchar(100) not null,
  created_year   int,
  published_year int,
  description    varchar(5000),
  image_hash     varchar(100000),
  publisher_id   int
    constraint publisher_id_fk
      references publishers (publisher_id)
      on update restrict on delete restrict,
  author_id      int
    constraint author_id_fk
      references authors (author_id)
      on update restrict on delete restrict
);

create table if not exists custom_list_types
(
  custom_list_type_id int default nextval('custom_list_type_id_seq')
    constraint custom_list_types_pk
      primary key,
  type_name           varchar(60) not null
);

create table if not exists "users"
(
  user_id  int default nextval('user_id_seq')
    constraint users_pk
      primary key,
  login    varchar(20) not null,
  password varchar(32) not null
);
create unique index if not exists users_login_uindex
  on "users" (login);

create table if not exists genres
(
  genre_id   int default nextval('genre_id_seq')
    constraint genres_pk
      primary key,
  genre_name varchar(80) not null
);
create unique index if not exists genre_genre_name_uindex
  on genres (genre_name);

create table if not exists book_ratings
(
  rating_id int default nextval('rating_id_seq')
    constraint book_ratings_pk
      primary key,
  value     smallint,
  book_id   int
    constraint book_id_fk
      references books (book_id)
      on update restrict on delete restrict,
  user_id   int
    constraint user_id_fk
      references "users" (user_id)
      on update restrict on delete restrict
);

create table if not exists book_genres
(
  book_id  int
    constraint book_id_fk
      references books (book_id)
      on update restrict on delete restrict,
  genre_id int
    constraint genre_id_fk
      references genres (genre_id)
      on update restrict on delete restrict
);

create table if not exists fav_genres
(
  user_id  int
    constraint user_id_fk
      references users (user_id)
      on update restrict on delete restrict,
  genre_id int
    constraint genre_id_fk
      references genres (genre_id)
      on update restrict on delete restrict
);

create table if not exists custom_list_element
(
  type_id int
    constraint type_id_fk
      references custom_list_types (custom_list_type_id)
      on update restrict on delete restrict,
  user_id int
    constraint user_id_fk
      references users (user_id)
      on update restrict on delete restrict,
  book_id int
    constraint book_id_fk
      references books (book_id)
      on update restrict on delete restrict
);