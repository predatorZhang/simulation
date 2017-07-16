-- Create table
create table SM_PLAN_FILE
(
  DBID            NUMBER(19) not null,
  FILENAME        VARCHAR2(255 CHAR),
  FILEPATH        VARCHAR2(255 CHAR),
  PLANFILETYPE    VARCHAR2(255 CHAR),
  UPDATETIMES     TIMESTAMP(6),
  UPPERSON        VARCHAR2(255 CHAR),
  FILEDISPLAYNAME VARCHAR2(255 CHAR)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 8
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SM_PLAN_FILE
  add primary key (DBID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
