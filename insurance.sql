# drop table if exists user;
# drop table if exists news;
# drop table if exists project;
# drop table if exists organization;
# drop table if exists user_project;
# drop table if exists organization_project;
#
# CREATE TABLE user (
#   id           INT(20) AUTO_INCREMENT,
#   name         VARCHAR (30),
#   password       VARCHAR (10),
#   email              VARCHAR (50),
#   region         VARCHAR (30),
#   gender         VARCHAR (10),
#   balance        DOUBLE(10,2),
#   head          INT(20),
#   PRIMARY KEY (id)
# );
#
# CREATE TABLE news (
#   id           INT(20) AUTO_INCREMENT,
#   title         VARCHAR (30),
#   img       INT (20),
#   news_detail     VARCHAR (5000),
#   PRIMARY KEY (id)
# );
#
# CREATE TABLE project(
#   id           INT(20) AUTO_INCREMENT,
#   project_name         VARCHAR (30),
#   initiator_name       VARCHAR (50),
#   img              INT (20),
#   description         VARCHAR (30),
#   target_money         DOUBLE(10,2),
#   current_money        DOUBLE(10,2),
#   detail              VARCHAR (10000),
#   PRIMARY KEY (id)
# );
#
# CREATE TABLE organization(
#   id           INT(20) AUTO_INCREMENT,
#   organization_name     VARCHAR (30),
#   manager       VARCHAR (10),
#   manager_img     VARCHAR (50),
#   PRIMARY KEY (id)
# );
#
# CREATE TABLE user_project(
#   user_id            INT(20),
#   project_id         INT(20),
#   timestamp         VARCHAR (50),
#   donate_money       DOUBLE(10,2),
#   PRIMARY KEY (user_id,project_id,timestamp)
# );
#
# CREATE TABLE organization_project(
#   organization_id      INT(20),
#   project_id           INT(20),
#   timestamp         VARCHAR (50),
#   PRIMARY KEY (organization_id,project_id)
# );
#
# CREATE TABLE plan(
#   id                    INT(20) AUTO_INCREMENT,
#   userId                INT(20),
#   planName              VARCHAR (50),
#   finishedTimes         INT (10),
#   value                 INT(20),
#   startDate             VARCHAR (50),
#   deadline              VARCHAR (50),
#   lastClock             VARCHAR (50),
#   stillKeeping          VARCHAR(10),
#   PRIMARY KEY (id)
# );
#
# CREATE TABLE token (
#   id                    INT(20) AUTO_INCREMENT,
#   userId                INT(20),
#   tokenStr              VARCHAR (50),
#   status                VARCHAR (20),
#   PRIMARY KEY (id)
# );
#
# CREATE TABLE user_plan (
#   id                    INT(20) AUTO_INCREMENT,
#   userId                INT(20),
#   planNumber            INT(20),
#   PRIMARY KEY (id)
# );
#


# user
INSERT INTO user (name, password, email, region, gender, balance, head, value)
  VALUES ("user1","123456","745125931@qq.com","厦门","man","100",1,10.00);

INSERT INTO user (name, password, email, region, gender, balance, head, value)
  VALUES ("user2","123456","6677888@qq.com","漳州","woman","50", 2,20.00);


# project
INSERT INTO project(project_name, initiator_name, img, description, target_money,current_money,detail,img_list_str,user_id,over,start_date)
  VALUES ("projectname1","initiatorName1",1,"项目测试描述1","10000","501","project1 detail test","1_2_",1,"false", "2017-5-6");

INSERT INTO project(project_name, initiator_name, img, description, target_money,current_money,detail,img_list_str,user_id,over,start_date)
  VALUES ("projectname2","initiatorName2",2,"项目测试描述2","3000","502","project2 detail test","3_4_",1,"false", "2017-11-6");

#  news
INSERT INTO news (title,img,news_detail)
  VALUES ("test1",1,"This is a test1 detail.");

INSERT INTO news (title, img, news_detail)
  VALUES ("test2",2,"This is a test2 detail.");

INSERT INTO news (title, img, news_detail)
  VALUES ("test3",3,"This is a test3 detail.");

INSERT INTO news (title, img, news_detail)
  VALUES ("test4",4,"This is a test4 detail.");

# user_project
INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (1,1,"2017-09-08 06:12:12",100);

INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (1,2,"2017-07-08  06:13:12",50);

INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (2,1,"2017-02-08  06:15:12",20);

INSERT INTO user_project (user_id, project_id, timestamp, donate_money)
  VALUES (2,2,"2016-10-08  06:16:12",300);

# plan
INSERT INTO plan (id, user_id, plan_name, finished_times, value, start_date, deadline, last_clock, still_keeping)
    VALUES (1,1,"学英语",5,6,"2017-11-1","2017-11-23","2017-11-1","true");

INSERT INTO plan (id, user_id, plan_name, finished_times, value, start_date, deadline, last_clock, still_keeping)
    VALUES (2,2,"学英语化学物理",3,6,"2017-11-1","2017-11-23","2017-11-1","true");

# user_plan
INSERT INTO user_plan(id, user_id, plan_number)
    VALUES (1, 1, 1);

INSERT INTO user_plan(id, user_id, plan_number)
VALUES (2, 2, 1);
