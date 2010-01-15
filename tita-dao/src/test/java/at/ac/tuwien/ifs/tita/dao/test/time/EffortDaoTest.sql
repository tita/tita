--   Copyright 2009 TiTA Project, Vienna University of Technology
--   
--   Licensed under the Apache License, Version 2.0 (the "License");
--   you may not use this file except in compliance with the License.
--   You may obtain a copy of the License at
--   
--       http://www.apache.org/licenses/LICENSE\-2.0
--       
--   Unless required by applicable law or agreed to in writing, software
--   distributed under the License is distributed on an "AS IS" BASIS,
--   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--   See the License for the specific language governing permissions and
--   limitations under the License.

delete from EFFORT;
delete from TITA_TASK;
delete from ISSUE_TRACKER_TASK;
delete from ISSUE_TRACKER_PROJECT;
delete from USER_PROJECT;
delete from TITA_PROJECT;
delete from CONV_PROJECT_STATUS;
delete from CONV_ISSUE_TRACKER;
delete from ISST_LOGIN;
delete from TITA_USER;
delete from CONV_ROLE;

insert into conv_issue_tracker (id, description, url) values (1, 'issue tracker 1', null);
insert into conv_role (id, description) values (1, 'role 1');
insert into conv_project_status (id, description) values (1, 'bla');

insert into tita_user (id,username) values (1, 'hans');
insert into tita_user (id,username) values (2, 'max');

insert into tita_project (id, description, name, deleted, status_id, modification_version) values (1, 'tita_test', 'tita_test', false, 1, 0);
insert into tita_project (id, description, name, deleted, status_id, modification_version) values (2, 'tita_test_2', 'tita_test_2', false, 1, 0);

insert into issue_tracker_project (id, project_name, tita_project_id, isst_id, isst_project_id, modification_version) values (1, 'itt 1', 1, 1, 96, 0);
insert into issue_tracker_project (id, project_name, tita_project_id, isst_id, isst_project_id, modification_version) values (2, 'itt 2', 1, 1, 97, 0);

insert into issue_tracker_task (id, isst_task_id, issue_tracker_project_id, modification_version) values (1, 23, 1, 0);
insert into issue_tracker_task (id, isst_task_id, issue_tracker_project_id, modification_version) values (2, 24, 1, 0);
insert into issue_tracker_task (id, isst_task_id, issue_tracker_project_id, modification_version) values (3, 25, 2, 0);
insert into issue_tracker_task (id, isst_task_id, issue_tracker_project_id, modification_version) values (4, 26, 1, 0);
insert into issue_tracker_task (id, isst_task_id, issue_tracker_project_id, modification_version) values (5, 27, 2, 0);
insert into issue_tracker_task (id, isst_task_id, issue_tracker_project_id, modification_version) values (6, 28, 1, 0);

insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (1, 'task 1', 1, 1, 0);
insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (2, 'task 2', 1, 1, 0);
insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (3, 'task 3', 1, 1, 0);
insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (4, 'task 4', 2, 1, 0);
insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (5, 'task 5', 2, 1, 0);
insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (6, 'task 6', 2, 1, 0);
insert into tita_task (id, description, user_id, tita_project_id, modification_version) values (7, 'task 7', 2, 1, 0);

insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (1, null, null, 1, '2010-01-01', null, null, 7000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (2, null, null, 1, '2010-01-01', null, null, 1000, false, 2);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (3, null, null, 2, '2010-01-01', null, null, 8000, false, 2);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (4, null, null, 2, '2010-01-01', null, null, 2000, false, 1);

insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (5, null, 1, null, '2010-01-01', null, null, 1000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (6, null, 1, null, '2010-01-01', null, null, 2000, false, 2);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (7, null, 2, null, '2010-01-01', null, null, 5000, false, 2);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (8, null, 2, null, '2010-01-01', null, null, 3000, false, 1);

insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (9, null, null, 3, '2010-02-02', null, null, 2000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (10, null, null, 3, '2010-01-06', null, null, 3000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (11, null, null, 4, '2010-06-02', null, null, 5000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (12, null, null, 4, '2010-10-05', null, null, 4000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (13, null, null, 5, '2010-08-08', null, null, 1000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (14, null, null, 5, '2010-09-05', null, null, 1000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (15, null, null, 6, '2010-03-02', null, null, 4000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (16, null, null, 6, '2010-04-06', null, null, 5500, false, 1);

insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (17, null, 3, null, '2010-02-11', null, null, 22000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (18, null, 3, null, '2010-02-10', null, null, 2000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (19, null, 4, null, '2010-03-05', null, null, 44000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (20, null, 4, null, '2010-07-07', null, null, 5000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (21, null, 5, null, '2010-05-07', null, null, 66000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (22, null, 5, null, '2010-07-02', null, null, 7000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (23, null, 6, null, '2010-04-03', null, null, 66000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (24, null, 6, null, '2010-06-04', null, null, 55000, false, 1);
insert into effort (id, description, tita_task_id, issuet_task_id, date, start_time, end_time, duration, deleted, user_id) values (25, null, 7, null, '2010-09-06', null, null, 6000, false, 1);

insert into user_project (id, user_id, tita_project_id, target_hours) values (1, 1, 1, 200);
insert into user_project (id, user_id, tita_project_id, target_hours) values (2, 2, 1, 100);