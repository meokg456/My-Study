create database quanlysinhvien;
use quanlysinhvien;

create table users(username varchar(30) primary key, password varchar(200) not null, permission int not null, name nvarchar(30) not null, gender int not null, CMND varchar(10) not null);

insert into users values ("giaovu", "giaovu", 0, "Giáo vụ", 0, "123456789");
insert into users values ("1712368", "1712368", 1, "Nguyễn Hữu Dũng", 0, "123406789");