-- Create table
create table SM_SYS_LOG
(
  DBID          NUMBER(19) not null,
  BUSSNESSNAME  VARCHAR2(255 CHAR),
  CONTENT       VARCHAR2(255 CHAR),
  CREATETIME    TIMESTAMP(6),
  CREATEUSER    VARCHAR2(255 CHAR),
  OPERATIONTYPE VARCHAR2(255 CHAR)
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
alter table SM_SYS_LOG
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
