# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table permissions (
  id                            bigserial not null,
  name                          varchar(255),
  status                        varchar(255),
  constraint pk_permissions primary key (id)
);

create table pictures (
  id                            bigserial not null,
  name                          varchar(255),
  type                          varchar(255),
  status                        varchar(255),
  url                           varchar(255),
  published                     timestamptz,
  user_id                       bigint,
  constraint uq_pictures_user_id unique (user_id),
  constraint pk_pictures primary key (id)
);

create table roles (
  id                            bigserial not null,
  status                        varchar(255),
  name                          varchar(255),
  constraint pk_roles primary key (id)
);

create table roles_permissions (
  roles_id                      bigint not null,
  permissions_id                bigint not null,
  constraint pk_roles_permissions primary key (roles_id,permissions_id)
);

create table transactions (
  id                            bigserial not null,
  amount                        float,
  status                        varchar(255),
  date                          timestamptz,
  type_id                       bigint,
  constraint pk_transactions primary key (id)
);

create table types (
  id                            bigserial not null,
  name                          varchar(255),
  status                        varchar(255),
  constraint pk_types primary key (id)
);

create table users (
  id                            bigserial not null,
  name                          varchar(255) not null,
  surname                       varchar(255) not null,
  email                         varchar(255) not null,
  password                      varchar(255),
  auth_token                    varchar(255),
  status                        varchar(255),
  verified                      boolean,
  bank_account                  varchar(255) not null,
  balance                       float,
  date                          timestamptz,
  avatar_id                     bigint,
  constraint uq_users_email unique (email),
  constraint uq_users_bank_account unique (bank_account),
  constraint uq_users_avatar_id unique (avatar_id),
  constraint pk_users primary key (id)
);

create table users_roles (
  users_id                      bigint not null,
  roles_id                      bigint not null,
  constraint pk_users_roles primary key (users_id,roles_id)
);

create table users_transactions (
  users_id                      bigint not null,
  transactions_id               bigint not null,
  constraint pk_users_transactions primary key (users_id,transactions_id)
);

alter table pictures add constraint fk_pictures_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;

create index ix_roles_permissions_roles on roles_permissions (roles_id);
alter table roles_permissions add constraint fk_roles_permissions_roles foreign key (roles_id) references roles (id) on delete restrict on update restrict;

create index ix_roles_permissions_permissions on roles_permissions (permissions_id);
alter table roles_permissions add constraint fk_roles_permissions_permissions foreign key (permissions_id) references permissions (id) on delete restrict on update restrict;

create index ix_transactions_type_id on transactions (type_id);
alter table transactions add constraint fk_transactions_type_id foreign key (type_id) references types (id) on delete restrict on update restrict;

alter table users add constraint fk_users_avatar_id foreign key (avatar_id) references pictures (id) on delete restrict on update restrict;

create index ix_users_roles_users on users_roles (users_id);
alter table users_roles add constraint fk_users_roles_users foreign key (users_id) references users (id) on delete restrict on update restrict;

create index ix_users_roles_roles on users_roles (roles_id);
alter table users_roles add constraint fk_users_roles_roles foreign key (roles_id) references roles (id) on delete restrict on update restrict;

create index ix_users_transactions_users on users_transactions (users_id);
alter table users_transactions add constraint fk_users_transactions_users foreign key (users_id) references users (id) on delete restrict on update restrict;

create index ix_users_transactions_transactions on users_transactions (transactions_id);
alter table users_transactions add constraint fk_users_transactions_transactions foreign key (transactions_id) references transactions (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists pictures drop constraint if exists fk_pictures_user_id;

alter table if exists roles_permissions drop constraint if exists fk_roles_permissions_roles;
drop index if exists ix_roles_permissions_roles;

alter table if exists roles_permissions drop constraint if exists fk_roles_permissions_permissions;
drop index if exists ix_roles_permissions_permissions;

alter table if exists transactions drop constraint if exists fk_transactions_type_id;
drop index if exists ix_transactions_type_id;

alter table if exists users drop constraint if exists fk_users_avatar_id;

alter table if exists users_roles drop constraint if exists fk_users_roles_users;
drop index if exists ix_users_roles_users;

alter table if exists users_roles drop constraint if exists fk_users_roles_roles;
drop index if exists ix_users_roles_roles;

alter table if exists users_transactions drop constraint if exists fk_users_transactions_users;
drop index if exists ix_users_transactions_users;

alter table if exists users_transactions drop constraint if exists fk_users_transactions_transactions;
drop index if exists ix_users_transactions_transactions;

drop table if exists permissions cascade;

drop table if exists pictures cascade;

drop table if exists roles cascade;

drop table if exists roles_permissions cascade;

drop table if exists transactions cascade;

drop table if exists types cascade;

drop table if exists users cascade;

drop table if exists users_roles cascade;

drop table if exists users_transactions cascade;

