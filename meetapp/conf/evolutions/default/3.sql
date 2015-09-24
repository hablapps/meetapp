# Members schema
 
# --- !Ups
 
CREATE TABLE Members (
    mid bigint(20) NOT NULL AUTO_INCREMENT,
    uid bigint(20) NOT NULL,
    gid bigint(20) NOT NULL,
    PRIMARY KEY (mid),
    FOREIGN KEY (uid) REFERENCES Users (uid),
    FOREIGN KEY (gid) REFERENCES `Groups` (gid),
    UNIQUE (uid, gid) 
);
 
CREATE TABLE Joins (
    jid bigint(20) NOT NULL AUTO_INCREMENT,
    uid bigint(20) NOT NULL,
    gid bigint(20) NOT NULL,
    PRIMARY KEY (jid),
    FOREIGN KEY (uid) REFERENCES Users (uid),
    FOREIGN KEY (gid) REFERENCES `Groups` (gid),
    UNIQUE (uid, gid) 
);
 
# --- !Downs
 
DROP TABLE Members;
DROP TABLE Joins;