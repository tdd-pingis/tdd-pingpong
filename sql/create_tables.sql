CREATE TABLE Challenge(
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL,  
  description TEXT NOT NULL,
  level INT NOT NULL,
  rating FLOAT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Task(
  id INT(11) NOT NULL AUTO_INCREMENT,
  challenge_id INT(11) REFERENCES Challenge(id),
  description TEXT NOT NULL,
  level INT NOT NULL,
  rating FLOAT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Implementation(
  id INT(11) NOT NULL AUTO_INCREMENT,
  task_id INT(11) REFERENCES Task(id),
  state_id INT(11) REFERENCES State(id),
  code TEXT NOT NULL,
  rating FLOAT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Test(
  id INT(11) NOT NULL AUTO_INCREMENT,
  task_id INT(11) REFERENCES Task(id),
  state_id INT(11) REFERENCES State(id),
  code TEXT NOT NULL,
  rating FLOAT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE State(
  id INT(11) NOT NULL AUTO_INCREMENT,
  user_id INT(11) REFERENCES User(id),
  status VARCHAR(10),
  PRIMARY KEY (id)
);

CREATE TABLE User(
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  password_hash VARCHAR(256),
  level INT NOT NULL,
  PRIMARY KEY (id)
);


