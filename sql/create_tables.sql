CREATE TABLE Challenge(
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) UNIQUE NOT NULL,  
  description TEXT NOT NULL,
  level INT NOT NULL,
  rating FLOAT,
  PRIMARY KEY (id)
);

CREATE TABLE Task(
  id INT(11) NOT NULL AUTO_INCREMENT,
  challenge_id INT(11) NOT NULL,
  description TEXT NOT NULL,
  level INT NOT NULL,
  rating FLOAT,
  PRIMARY KEY (id),
  FOREIGN KEY(challenge_id) REFERENCES Challenge(id)
);

CREATE TABLE Implementation(
  id INT(11) NOT NULL AUTO_INCREMENT,
  task_id INT(11),
  state_id INT(11),
  code TEXT NOT NULL,
  rating FLOAT,
  PRIMARY KEY (id),
  FOREIGN KEY(task_id) REFERENCES Task(id),
  FOREIGN KEY(state_id) REFERENCES State(id)
);

CREATE TABLE Test(
  id INT(11) NOT NULL AUTO_INCREMENT,
  task_id INT(11),
  state_id INT(11),
  code TEXT NOT NULL,
  rating FLOAT,
  PRIMARY KEY (id),
  FOREIGN KEY(task_id) REFERENCES Task(id),
  FOREIGN KEY(state_id) REFERENCES State(id)
);

CREATE TABLE State(
  id INT(11) NOT NULL AUTO_INCREMENT,
  user_id INT(11),
  status VARCHAR(10),
  PRIMARY KEY (id),
  FOREIGN KEY(user_id) REFERENCES User(id)
);

CREATE TABLE User(
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  password_hash VARCHAR(256),
  level INT NOT NULL,
  PRIMARY KEY (id)
);


