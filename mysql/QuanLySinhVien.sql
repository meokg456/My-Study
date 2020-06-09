create database quanlysinhvien;
use quanlysinhvien;

drop table classes;
create table classes(
	className varchar(10) primary key not null
);

drop table students;
create table students(
	studentId varchar(10) primary key,
	name varchar(30) not null,
	gender nvarchar(5) not null,
	personalId varchar(10) not null,
	className varchar(10) not null,
	constraint foreign key(className) references classes(className)
);

drop table users;
create table users(
	username varchar(30) primary key,
	password varchar(200) not null,
	permission int not null,
	studentId varchar(10),
	constraint foreign key(studentId) references students(studentId)
);

drop table courses;
create table courses(
	courseId varchar(10) primary key not null,
    courseName nvarchar(30) not null,
    RoomId varchar(10) not null
);

drop table registrations;
create table registrations(
	studentId varchar(10) not null,
    courseId varchar(10) not null,
    constraint primary key(studentId, courseId),
    constraint foreign key(studentId) references students(studentId),
    constraint foreign key(courseId) references courses(courseId)
    
);
drop table timetable;
create table timetable(
	className varchar(10) not null,
    courseId varchar(10) not null,
    constraint primary key(className, courseId),
	constraint foreign key(className) references classes(className),
	constraint foreign key(courseId) references courses(courseId)
);

drop table results;
create table results(
	studentId varchar(10) not null,
    courseId varchar(10) not null,
    midTermGrade float not null,
    finalExamGrade float not null,
    otherGrade float not null,
    totalGrade float not null,
    constraint primary key(studentId, courseId),
    constraint foreign key(studentId, courseId) references registrations(studentId, courseId)
);

insert into users values ("giaovu", "giaovu", 0, null);

