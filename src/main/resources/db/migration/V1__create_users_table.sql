drop table if exists users;
create table users (
id int unsigned primary key auto_increment,
first_name varchar(20) not null,
last_name varchar(20) not null,
birth_date date not null,
phone_no int(9) unique,
);




insert into users (first_name, last_name, birth_date) values ('Marian', 'Test1', '1991-01-01');
insert into users (first_name, last_name, birth_date, phone_no) values ('Aleksandra', 'Test2', '1993-05-06', '123456789');