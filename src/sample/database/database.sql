CREATE DATABASE admin; 

CREATE TABLE userAccount(
idUserAccount int unsigned not null auto_increment,
firstName varchar(200) not null, 
lastName varchar(200) not null, 
userName varchar(200) not null, 
password varchar(200) not null, 
primary key(idUserAccount) 
); 

INSERT INTO userAccount(firstName,lastName,username,password)
VALUES ('Blerta','Mecini','admin','fiekadmin2021');
 

CREATE TABLE  addBook (
id varchar(200),
title varchar(200),
author varchar(200),
publisher varchar(200),
isAvail boolean default true,
primary key(id)
);

CREATE TABLE  addMember (
memberID varchar(200) not null,
name varchar(200) not null,
email varchar(200) not null ,
phone varchar(200)not null ,
gender enum('female','male') not null,
primary key(memberID)
);


CREATE TABLE  issuedBooks (
bookID varchar(200) not null,
memberID varchar(200) not null,
issueTime timestamp default CURRENT_TIMESTAMP,
renew_count integer default 0,
primary key(bookID),
foreign key(bookID) references addBook(id),
foreign key(memberID) references addMember(id)
);




