
-- conv tables
create table CONV_ROLE(
	ID bigint,
	DESCRIPTION varchar(50),
	primary key (ID)
);

create table CONV_ISSUE_TRACKER(
	ID bigint,
	DESCRIPTION varchar(50),
	primary key (ID)
);

create table CONV_PROJECT_STATUS(
	ID bigint,
	DESCRIPTION varchar(50),
	primary key (ID)
);

-- tables
create table TITA_USER(
	ID bigint,
	USERNAME varchar(20),
	PASSWORD varchar(20),
	FIRSTNAME varchar(20),
	LASTNAME varchar(20),
	EMAIL varchar(40),
	DELETED boolean,
	ROLE_ID bigint references CONV_ROLE(ID),
	MODIFICATION_VERSION bigint,
	primary key (ID)
);

create unique index AK_USERNAME on TITA_USER (USERNAME);

create table ISST_LOGIN( 
	ID bigint,
	USER_ID bigint references TITA_USER (ID),
	ISST_ID bigint references CONV_ISSUE_TRACKER(ID),
	NAME varchar(20),
	PASSWORD varchar(20),
	MODIFICATION_VERSION bigint,
	primary key (ID)
);

create unique index AK_USER_ISSUE_TRACKER on ISST_LOGIN (USER_ID, ISST_ID);

create table TITA_PROJECT( 
	ID bigint,
	DESCRIPTION varchar(50),
	NAME varchar(50),
	DELETED boolean,
	STATUS_ID bigint references CONV_PROJECT_STATUS(ID),
	MODIFICATION_VERSION bigint,
	primary key (ID)
);

create table ISSUE_TRACKER_PROJECT(
	ID bigint,
	TITA_PROJECT_ID bigint references TITA_PROJECT(ID),
	ISST_ID bigint references CONV_ISSUE_TRACKER (ID),
	ISST_PROJECT_ID bigint,
	MODIFICATION_VERSION bigint,
	primary key (ID)
);

create unique index AK_ISSUE_TRACKER_PROJECT on ISSUE_TRACKER_PROJECT (ISST_ID, ISST_PROJECT_ID);
--fail
create table USER_PROJECT(
	ID bigint,
	USER_ID bigint references TITA_USER (ID),
	TITA_PROJECT_ID bigint references TITA_PROJECT (ID),
    primary key (ID)
);

create unique index AK_USER_PROJECT on USER_PROJECT (USER_ID,TITA_PROJECT_ID);

create table TITA_TASK( 
	ID bigint,
	DESCRIPTION varchar(50),
	USER_ID bigint references TITA_USER (ID),
	TITA_PROJECT_ID bigint references TITA_PROJECT(ID),
	MODIFICATION_VERSION bigint,
	primary key (ID)
);

create table ISSUE_TRACKER_TASK(  
	ID bigint,
	ISSUE_TRACKER_PROJECT_ID bigint references ISSUE_TRACKER_PROJECT (ID),
	MODIFICATION_VERSION bigint,
	primary key (ID)
);

create table EFFORT(
	ID bigint,
	DESCRIPTION varchar(50),
	TITA_TASK_ID bigint references TITA_TASK (ID),
	ISSUET_TASK_ID bigint references ISSUE_TRACKER_TASK (ID),
	DATE date,
	DURATION bigint,
	DELETED boolean,
	USER_ID bigint references TITA_USER (ID),
	primary key (ID)
);
