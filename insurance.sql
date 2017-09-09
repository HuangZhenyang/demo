drop table if exists user;
drop table if exists news;
drop table if exists project;
drop table if exists organization;
drop table if exists user_project;
drop table if exists organization_project;

CREATE TABLE user (
  id           INT(20) AUTO_INCREMENT,
  name         VARCHAR (30),
  password       VARCHAR (10),
  email              VARCHAR (50),
  region         VARCHAR (30),
  gender         VARCHAR (10),
  balance        DOUBLE(10,2),
  PRIMARY KEY (id)
);

CREATE TABLE news (
  id           INT(20) AUTO_INCREMENT,
  title         VARCHAR (30),
  img_url       VARCHAR (50),
  news_detail     VARCHAR (5000),
  PRIMARY KEY (id)
);

CREATE TABLE project(
  id           INT(20) AUTO_INCREMENT,
  project_name         VARCHAR (30),
  initiator_name       VARCHAR (50),
  img_url              VARCHAR (50),
  description         VARCHAR (30),
  target_money         DOUBLE(10,2),
  current_money        DOUBLE(10,2),
  detail              VARCHAR (10000),
  PRIMARY KEY (id)
);

CREATE TABLE organization(
  id           INT(20) AUTO_INCREMENT,
  organization_name     VARCHAR (30),
  manager       VARCHAR (10),
  manager_img     VARCHAR (50),
  PRIMARY KEY (id)
);

CREATE TABLE user_project(
  user_id            INT(20),
  project_id         INT(20),
  timestamp         VARCHAR (50),
  donate_money       DOUBLE(10,2),
  PRIMARY KEY (user_id,project_id,timestamp)
);

CREATE TABLE organization_project(
  organization_id      INT(20),
  project_id           INT(20),
  timestamp         VARCHAR (50),
  PRIMARY KEY (organization_id,project_id)
);


INSERT INTO user (name, password, email, region, gender, balance)
  VALUES ("user1","123456","745125931@qq.com","厦门","man","100");

INSERT INTO user (name, password, email, region, gender, balance)
  VALUES ("user2","123456","6677888@qq.com","漳州","woman","50");


INSERT INTO project(project_name, initiator_name, img_url, description, target_money,current_money,detail)
  VALUES ("projectname1","initiatorName1","/img/project/001","项目测试描述1","10000","501","project1 detail test");

INSERT INTO project(project_name, initiator_name, img_url, description, target_money,current_money,detail)
  VALUES ("projectname2","initiatorName2","/img/project/002","项目测试描述2","3000","502","project2 detail test");


INSERT INTO news (title,img_url,news_detail)
  VALUES ("test1","/img/news/001","This is a test1 detail.");

INSERT INTO news (title, img_url, news_detail)
  VALUES ("test2","/img/news/002","This is a test2 detail.");

INSERT INTO news (title, img_url, news_detail)
  VALUES ("test3","/img/news/003","This is a test3 detail.");

INSERT INTO news (title, img_url, news_detail)
  VALUES ("test4","/img/news/004","This is a test4 detail.");


INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (1,1,"2017-09-08",1200);

INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (1,2,"2017-07-08",500);

INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (2,1,"2017-02-08",200);

INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (2,2,"2016-10-08",700);