# User schema
 
# --- !Ups
 
CREATE TABLE Users (
    uid bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    PRIMARY KEY (uid)
);
 
# --- !Downs
 
DROP TABLE Users;