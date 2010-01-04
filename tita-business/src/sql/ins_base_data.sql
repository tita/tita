insert into CONV_ISSUE_TRACKER (ID, DESCRIPTION) values (1, 'it 1');
insert into CONV_ROLE (ID, DESCRIPTION) values (1, 'Administrator');
insert into CONV_ROLE (ID, DESCRIPTION) values (2, 'Time consumer');
insert into CONV_ROLE (ID, DESCRIPTION) values (3, 'Time controller');

insert into CONV_PROJECT_STATUS (ID, DESCRIPTION) values (1, 'stat 1');

INSERT INTO tita_user (id, deleted, email, firstname, lastname, modification_version, password, username, role_id)
 VALUES (5, false, NULL, NULL, NULL, 0, 'Ã�3Ã¢*Ã£HÂ®ÂµfÃ‚
Ã¬5â€¦MÂ©â€”', 'admin', 1);
INSERT INTO tita_user (id, deleted, email, firstname, lastname, modification_version, password, username, role_id)
 VALUES (6, false, NULL, NULL, NULL, 0, 'EPÂ¬Ãµ{gmÃ¨!nC(OynÂ¤â„¢Ãº', 'timeconsumer', 2);
INSERT INTO tita_user (id, deleted, email, firstname, lastname, modification_version, password, username, role_id)
 VALUES (7, false, NULL, NULL, NULL, 0, 'zÂ¡Ã¼â‚¬Â»GÃ¯Ã‘Ã›
Ã¦Ã™â€ºrÂ¿ÃŽ&', 'timecontroller', 3);


insert into TITA_PROJECT (ID, DESCRIPTION, NAME, DELETED, STATUS_ID, MODIFICATION_VERSION)
values (1, 'nix', 'name', false, 1, 0);

insert into ISSUE_TRACKER_PROJECT (ID, TITA_PROJECT_ID, ISST_ID, ISST_PROJECT_ID, MODIFICATION_VERSION)
values (1,1,1,97,0);
insert into ISSUE_TRACKER_PROJECT (ID, TITA_PROJECT_ID, ISST_ID, ISST_PROJECT_ID, MODIFICATION_VERSION)
values (2,1,1,39,0);

insert into TITA_TASK (ID, DESCRIPTION, USER_ID, TITA_PROJECT_ID, MODIFICATION_VERSION) values
(1, 'bla 1', 5, 1, 0);
insert into TITA_TASK (ID, DESCRIPTION, USER_ID, TITA_PROJECT_ID, MODIFICATION_VERSION) values
(2, 'bla 1', 6, 1, 0);

insert into ISSUE_TRACKER_TASK (ID, ISSUE_TRACKER_PROJECT_ID, MODIFICATION_VERSION) values
(1,1,0);
insert into ISSUE_TRACKER_TASK (ID, ISSUE_TRACKER_PROJECT_ID, MODIFICATION_VERSION) values
(2,2,0);

insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (1, 'e1', 1, null, '2009-10-12', 2000, false, 5);
insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (2, 'e2', 1, null, '2009-03-24', 2000, false, 6);

insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (3, 'e3', 2, null, '2009-07-09', 2000, false, 7);
insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (4, 'e4', 2, null, '2009-05-17', 2000, false, 6);

insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (5, 'e5', null, 1, '2009-01-012', 2000, false, 6);
insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (6, 'e6', null, 1, '2009-11-18', 2000, false, 7);

insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (7, 'e7', null, 2, '2009-06-29', 2000, false, 5);
insert into EFFORT (ID, DESCRIPTION, TITA_TASK_ID, ISSUET_TASK_ID, DATE, DURATION, DELETED, USER_ID)
values (8, 'e8', null, 2, '2009-10-24', 2000, false, 5);