-- Create table
create table SM_NODE_LIMIT
(
  DBID           NUMBER(19) not null,
  EVENT_TYPE     NUMBER(10),
  FLOW_DEP_ID    NUMBER(19),
  FLOW_PERSON_ID NUMBER(19),
  SYS_DEP_ID     NUMBER(19),
  SYS_PERSON_ID  NUMBER(19),
  NODE_ID        NUMBER(19) not null
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
comment on column SM_NODE_LIMIT.EVENT_TYPE
  is 'SEE ENUM EventTypeEnum';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SM_NODE_LIMIT
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
alter table SM_NODE_LIMIT
  add constraint FK_7CHD7LHKPIT2M5LHXGYJH923A foreign key (NODE_ID)
  references SM_NODE (DBID);
