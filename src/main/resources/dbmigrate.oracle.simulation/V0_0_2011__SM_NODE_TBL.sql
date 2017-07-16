-- Create table
create table SM_NODE
(
  DBID           NUMBER(19) not null,
  DESCN          VARCHAR2(255 CHAR),
  MESSAGE_STATUS NUMBER(10) not null,
  NODENAME       VARCHAR2(255 CHAR),
  SYS_CODE       VARCHAR2(255 CHAR),
  SYS_NAME       VARCHAR2(255 CHAR)
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
alter table SM_NODE
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
