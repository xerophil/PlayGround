# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        varchar(40) not null,
  parent_id                 varchar(40),
  comment_id                varchar(40),
  text                      varchar(255),
  constraint pk_comment primary key (id))
;

create table groups (
  id                        varchar(40) not null,
  name                      varchar(255),
  builtin                   boolean,
  constraint uq_groups_name unique (name),
  constraint pk_groups primary key (id))
;

create table note (
  id                        varchar(40) not null,
  title                     varchar(255),
  text                      varchar(255),
  constraint pk_note primary key (id))
;

create table session (
  id                        varchar(40) not null,
  creation_time             timestamp,
  last_access               timestamp,
  address                   varchar(255),
  constraint pk_session primary key (id))
;

create table token (
  id                        varchar(40) not null,
  timestamp                 timestamp,
  constraint pk_token primary key (id))
;

create table users (
  id                        varchar(40) not null,
  name                      varchar(255),
  mail                      varchar(255),
  password                  varchar(255),
  builtin                   boolean,
  locked                    boolean,
  lock_time                 timestamp,
  last_login                timestamp,
  creation_time             timestamp,
  login_attempts            integer,
  session_id                varchar(40),
  token_id                  varchar(40),
  constraint pk_users primary key (id))
;


create table groups_users (
  groups_id                      varchar(40) not null,
  users_id                       varchar(40) not null,
  constraint pk_groups_users primary key (groups_id, users_id))
;
alter table comment add constraint fk_comment_parent_1 foreign key (parent_id) references comment (id) on delete restrict on update restrict;
create index ix_comment_parent_1 on comment (parent_id);
alter table comment add constraint fk_comment_comment_2 foreign key (comment_id) references note (id) on delete restrict on update restrict;
create index ix_comment_comment_2 on comment (comment_id);
alter table users add constraint fk_users_session_3 foreign key (session_id) references session (id) on delete restrict on update restrict;
create index ix_users_session_3 on users (session_id);
alter table users add constraint fk_users_token_4 foreign key (token_id) references token (id) on delete restrict on update restrict;
create index ix_users_token_4 on users (token_id);



alter table groups_users add constraint fk_groups_users_groups_01 foreign key (groups_id) references groups (id) on delete restrict on update restrict;

alter table groups_users add constraint fk_groups_users_users_02 foreign key (users_id) references users (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists groups;

drop table if exists groups_users;

drop table if exists note;

drop table if exists session;

drop table if exists token;

drop table if exists users;

SET REFERENTIAL_INTEGRITY TRUE;

