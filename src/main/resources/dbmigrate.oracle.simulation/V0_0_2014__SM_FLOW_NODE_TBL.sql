-- Create table
create table SM_FLOW_NODE
(
  DBID              NUMBER(19) not null,
  HEAD              NUMBER(1),
  OPERATION         VARCHAR2(255 CHAR),
  OPERATION_CODE    VARCHAR2(255 CHAR),
  OPERATION_DESC    VARCHAR2(255 CHAR),
  FLOW_ID           NUMBER(19) not null,
  NEXT_FLOW_NODE_ID NUMBER(19) not null,
  NODE_ID           NUMBER(19) not null
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
alter table SM_FLOW_NODE
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
alter table SM_FLOW_NODE
  add constraint FK_KVO9T5XOHQ7MGPU8OEU2CLOJL foreign key (FLOW_ID)
  references SM_FLOW (DBID);
alter table SM_FLOW_NODE
  add constraint FK_KW5LE4G4HQV7CLCEA7UO5RTIA foreign key (NODE_ID)
  references SM_NODE (DBID);
alter table SM_FLOW_NODE
  add constraint FK_O8PVNYD3GKLON5NH4UOIR7ORL foreign key (NEXT_FLOW_NODE_ID)
  references SM_FLOW_NODE (DBID);
