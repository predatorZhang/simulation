create table SM_EVENT_PROCESS_DETAIL
(
  DBID                  NUMBER(19) not null,
  EVENT_ID              NUMBER(19),
  EVENT_SRC             NUMBER(1),
  PROCESS_ID            NUMBER(19),
  CONFIRM_TIME          DATE,
  CONFIRM_UID           NUMBER(19),
  PAD_PIC_PATH          VARCHAR2(255),
  PAD_EVENT_DESCRIPTION VARCHAR2(300),
  PAD_SIGN_TIME         DATE,
  PAD_SIGN_UID          NUMBER(19),
  PAD_PROCESS_TIME      DATE,
  BACKUP_TIME           DATE,
  BACKUP_UPDATE_TIME     DATE,
  BACKUP_UID            NUMBER(19),
  BACKUP_FILE           VARCHAR2(255),
  BACKUP_ORIGINAL_PATH           VARCHAR2(255),
  BACKUP_REASON         VARCHAR2(255),
  BACKUP_INFO           VARCHAR2(100),
  BACKUP_MEASURE        VARCHAR2(100),
  MESSAGE_STATUS        NUMBER(1),
  MESSAGE               NUMBER(1),
  DISTRIBUTE_UID          NUMBER(19),
  DISTRIBUTE_RES           VARCHAR(255)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 16
    next 8
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column SM_EVENT_PROCESS_DETAIL.EVENT_SRC
  is '1:设备事件  2:人员上报事件  3:pad端上报事件';
comment on column SM_EVENT_PROCESS_DETAIL.BACKUP_MEASURE
  is '备案填写的采取措施';
comment on column SM_EVENT_PROCESS_DETAIL.MESSAGE_STATUS
  is '事件当前状态';
comment on column SM_EVENT_PROCESS_DETAIL.MESSAGE
  is '事件种类（设备超限等）';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SM_EVENT_PROCESS_DETAIL
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