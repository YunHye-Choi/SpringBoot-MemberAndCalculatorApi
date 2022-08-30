insert into user (username, password) values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi');
insert into user (username, password) values ('user1', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC');

insert into authority (authority_name) values ('ROLE_USER');
insert into authority (authority_name) values ('ROLE_ADMIN');

insert into user_authority (username, authority_name) values ('admin', 'ROLE_USER');
insert into user_authority (username, authority_name) values ('admin', 'ROLE_ADMIN');
insert into user_authority (username, authority_name) values ('user1', 'ROLE_USER');