-- Create table
create table SM_FLOW
(
  DBID        NUMBER(19) not null,
  ACTIVE      NUMBER(1) not null,
  DESCN       VARCHAR2(255 CHAR),
  FLOWCODE    VARCHAR2(255 CHAR),
  FLOWNAME    VARCHAR2(255 CHAR),
  FLOW_PIC    VARCHAR2(255 CHAR),
  UPDATE_TIME TIMESTAMP(6)
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
alter table SM_FLOW
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