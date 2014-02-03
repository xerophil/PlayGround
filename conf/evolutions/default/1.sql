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

create table UserGroup (
  id                        varchar(40) not null,
  name                      varchar(255),
  builtin                   boolean,
  constraint uq_UserGroup_name unique (name),
  constraint pk_UserGroup primary key (id))
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

create table user (
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
  constraint pk_user primary key (id))
;


create table UserGroup_user (
  UserGroup_id                   varchar(40) not null,
  user_id                        varchar(40) not null,
  constraint pk_UserGroup_user primary key (UserGroup_id, user_id))
;
alter table comment add constraint fk_comment_parent_1 foreign key (parent_id) references comment (id) on delete restrict on update restrict;
create index ix_comment_parent_1 on comment (parent_id);
alter table comment add constraint fk_comment_comment_2 foreign key (comment_id) references note (id) on delete restrict on update restrict;
create index ix_comment_comment_2 on comment (comment_id);
alter table user add constraint fk_user_session_3 foreign key (session_id) references session (id) on delete restrict on update restrict;
create index ix_user_session_3 on user (session_id);
alter table user add constraint fk_user_token_4 foreign key (token_id) references token (id) on delete restrict on update restrict;
create index ix_user_token_4 on user (token_id);



alter table UserGroup_user add constraint fk_UserGroup_user_UserGroup_01 foreign key (UserGroup_id) references UserGroup (id) on delete restrict on update restrict;

alter table UserGroup_user add constraint fk_UserGroup_user_user_02 foreign key (user_id) references user (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists UserGroup;

drop table if exists UserGroup_user;

drop table if exists note;

drop table if exists session;

drop table if exists token;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

