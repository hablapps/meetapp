# Groups schema
 
# --- !Ups
 
CREATE TABLE `Groups` (
    gid bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    city varchar(255) NOT NULL,
    must_approve boolean NOT NULL,
    PRIMARY KEY (gid)
);
 
# --- !Downs
 
DROP TABLE `Groups`;