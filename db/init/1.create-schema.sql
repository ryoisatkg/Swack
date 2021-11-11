-- drop
DROP TABLE FAVORITE;
DROP TABLE CHATLOG;
DROP TABLE JOINROOM;
DROP TABLE ROOMS;
DROP TABLE USERS;
DROP SEQUENCE CHATLOGID_SEQ;

-- create
CREATE TABLE USERS(
  USERID CHAR(5) PRIMARY KEY,
  USERNAME VARCHAR(40) NOT NULL,
  MAILADDRESS VARCHAR(100) UNIQUE,
  PASSWORD VARCHAR(300) NOT NULL,
  LASTACCESSROOMID VARCHAR(5),
  IS_WITHDRAWAL BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE ROOMS(
  ROOMID VARCHAR(5) PRIMARY KEY,
  ROOMNAME VARCHAR(50) UNIQUE NOT NULL,
  CREATEDUSERID CHAR(5),
  DIRECTED BOOLEAN DEFAULT FALSE,
  PRIVATED BOOLEAN DEFAULT FALSE,
  FOREIGN KEY(CREATEDUSERID) REFERENCES USERS(USERID) ON DELETE SET NULL
);

CREATE TABLE JOINROOM(
  ROOMID VARCHAR(5),
  USERID CHAR(5),
  PRIMARY KEY(ROOMID, USERID),
  FOREIGN KEY (ROOMID) REFERENCES ROOMS(ROOMID) ON DELETE CASCADE,
  FOREIGN KEY (USERID) REFERENCES USERS(USERID) ON DELETE CASCADE
);

CREATE TABLE CHATLOG(
  CHATLOGID INTEGER PRIMARY KEY,
  ROOMID VARCHAR(5) NOT NULL,
  USERID CHAR(5),
  MESSAGE VARCHAR(500),
  CREATED_AT TIMESTAMP,
  FOREIGN KEY (ROOMID) REFERENCES ROOMS(ROOMID) ON DELETE CASCADE,
  FOREIGN KEY (USERID) REFERENCES USERS(USERID) ON DELETE SET NULL
);

create table favorite (
	chatlogid int,
	userid char(5),
	created_at timestamp,
	primary key(chatlogid, userid),
	foreign key (chatlogid) references chatlog(chatlogid) on delete cascade,
	foreign key (userid) references users(userid)
);

CREATE SEQUENCE CHATLOGID_SEQ
  START WITH 1
  INCREMENT BY 1;
