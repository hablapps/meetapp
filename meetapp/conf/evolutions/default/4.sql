# Populate DB

# --- !Ups

INSERT INTO `Groups` (`gid`, `name`, `city`, `must_approve`)
VALUES
  (1,'ScalaMAD', 'Madrid', false),
  (2,'BitCoin', 'Barna', false),
  (3,'PapersWeLove', 'Ciudad Real', true);
 
INSERT INTO `Users` (`uid`, `name`)
VALUES
  (1,'Juanma'),
  (2,'Juan Carlos'),
  (3,'Joaquín'),
  (4,'Augusto');
 
# --- !Downs

DELETE FROM `Groups`;

DELETE FROM `Users`;