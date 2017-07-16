-- Create table
create table SM_FLOW_PERSON
(
  DBID               NUMBER(19) not null,
  ACTIVE             NUMBER(1),
  SYS_PERSON_ID      NUMBER(19) not null,
  USERNAME           VARCHAR2(255 CHAR) not null,
  FLOW_DEPARTMENT_ID NUMBER(19)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64
    next 1
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table SM_FLOW_PERSON
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
alter table SM_FLOW_PERSON
  add constraint FK_QXOO7EUV0JDBJRMON0SES57D6 foreign key (FLOW_DEPARTMENT_ID)
  references SM_FLOW_DEPARTMENT (DBID);
