-- Create table
create table SM_FLOW_ALLOCATION
(
  DBID        NUMBER(19) not null,
  CREATE_TIME TIMESTAMP(6),
  DESCN       VARCHAR2(255 CHAR),
  EVENT_TYPE  NUMBER(10),
  RULECODE    VARCHAR2(255 CHAR),
  RULENAME    VARCHAR2(255 CHAR),
  UPDATE_TIME TIMESTAMP(6),
  FLOW_ID     NUMBER(19) not null
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
-- Add comments to the columns
comment on column SM_FLOW_ALLOCATION.EVENT_TYPE
  is 'SEE ENUM EventTypeEnum';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SM_FLOW_ALLOCATION
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
alter table SM_FLOW_ALLOCATION
  add constraint FK_O168UAKBNO8FIIHJSO452XVUT foreign key (FLOW_ID)
  references SM_FLOW (DBID);
