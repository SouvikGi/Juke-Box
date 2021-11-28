create database jukebox;

use jukebox;

create table song
(
songid char(7) primary key,
songname varchar(100) not null,
Artist varchar(30),
Genre varchar(30),
Album varchar(30),
Duration char(5) not null
);
select * from song;

create table songlist
(
id int unsigned primary key auto_increment,
songid char(7) not null,
playlistid char(7),
podcastid char(7)
);
select * from songlist;
alter table songlist auto_increment = 1;
delete from songlist ;

create table playlist
(
playlistid char(7) primary key,
playlistname varchar(30) not null,
userid varchar(20) not null
);
select * from playlist;
delete from playlist ;

create table user
(
userid varchar(20) primary key,
username varchar(30) not null,
password varchar(30) not null
);
select * from user;
delete from user ;

create table podcast
(
podcastid char(7) primary key,
podcastname varchar(35) not null,
celebrityname varchar(30) not null,
datepublished date not null
);
select * from podcast;
delete from podcast;

