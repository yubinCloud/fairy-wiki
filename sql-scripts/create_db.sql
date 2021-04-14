CREATE DATABASE wiki;

create table test
(
    id int,
    name varchar(255) not null,
    password varchar(255) not null
);

create unique index test_id_uindex
    on test (id);

alter table test
    add constraint test_pk
        primary key (id);

alter table test modify id int auto_increment;