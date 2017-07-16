-- Create table
create table SM_FLOW_DEPARTMENT
(
  DBID       NUMBER(19) not null,
  ACTIVE     NUMBER(1),
  DEP_NAME   VARCHAR2(255 CHAR),
  DESCN      VARCHAR2(255 CHAR),
  SYS_DEP_ID NUMBER(19),
  PARENT_ID  NUMBER(19)
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
comment on column SM_FLOW_DEPARTMENT.SYS_DEP_ID
  is '当该值为-1时表示无引用';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SM_FLOW_DEPARTMENT
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
alter table SM_FLOW_DEPARTMENT
  add constraint FK_N606B0UVEO0UUU3C1Y7HRWGJQ foreign key (PARENT_ID)
  references SM_FLOW_DEPARTMENT (DBID);
