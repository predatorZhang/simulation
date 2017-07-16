create table DMAINFO
(
  DBID            NUMBER(19) not null,
  BDATAPARENT_DMA VARCHAR2(255 CHAR),
  ACTIVE          NUMBER(1),
  ICF             FLOAT,
  LEAKCONTROLRATE FLOAT,
  NAME            VARCHAR2(255 CHAR),
  NO              VARCHAR2(255 CHAR),
  NORMALWATER     FLOAT,
  PIPELENG        FLOAT,
  PIPELINKS       FLOAT,
  SALEWATER       FLOAT,
  USERCOUNT       FLOAT,
  USERPIPELENG    FLOAT
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DMAINFO
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
create table POSITIONINFO
(
  DBID         NUMBER(19) not null,
  BDATAPOSTYPE VARCHAR2(255 CHAR),
  ACTIVE       NUMBER(1),
  POS_COMMENT  VARCHAR2(255 CHAR),
  ISUSE        NUMBER(1),
  LATITUDE     VARCHAR2(255 CHAR),
  LONGITUDE    VARCHAR2(255 CHAR),
  NAME         VARCHAR2(255 CHAR),
  OPERATETIME  TIMESTAMP(6),
  OPERATOR     VARCHAR2(255 CHAR),
  SORTCODE     VARCHAR2(255 CHAR)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table POSITIONINFO
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

create table POSDMA
(
  DBID              NUMBER(19) not null,
  DIRECTION         VARCHAR2(255 CHAR),
  DMAINFO_DBID      NUMBER(19),
  POSITIONINFO_DBID NUMBER(19),
  ACTIVE            NUMBER(1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table POSDMA
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
alter table POSDMA
  add constraint FK8D03B6243E94D979 foreign key (POSITIONINFO_DBID)
  references POSITIONINFO (DBID);
alter table POSDMA
  add constraint FK8D03B6249832FA37 foreign key (DMAINFO_DBID)
  references DMAINFO (DBID);
create table DEVPOS
(
  DBID              NUMBER(19) not null,
  HIGNINSTANTVALUE  FLOAT,
  LOWINSTANTVALUE   FLOAT,
  PIPEMATERIAL      VARCHAR2(255 CHAR),
  PIPESIZE          NUMBER(10),
  SENSORTYPE        VARCHAR2(255 CHAR),
  STARTTOTALVALUE   FLOAT,
  DEVICE_DBID       NUMBER(19),
  POSITIONINFO_DBID NUMBER(19),
  ACTIVE            NUMBER(1),
  APPEND_DAY        TIMESTAMP(6)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table DEVPOS
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
alter table DEVPOS
  add constraint FK77FE2E1F3E94D979 foreign key (POSITIONINFO_DBID)
  references POSITIONINFO (DBID);


create table ALARM_DMA_LOG
(
  DBID       NUMBER(19) not null,
  REGIONID   NUMBER(19),
  SUCCESS    NUMBER(1),
  MESSAGE    VARCHAR2(255 CHAR),
  CREATETIME DATE
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table ALARM_DMA_LOG
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

create table DMAMINIMUMFLOW
(
  DBID         NUMBER(19) not null,
  DMAINFO      NUMBER(19),
  MINFLOWTIME  DATE,
  MINFLOWVALUE VARCHAR2(255 CHAR)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );

create table ALARM_DMA_SALE_WATER
(
  DBID         NUMBER(19) not null,
  STARTDATE    DATE,
  ENDDATE      DATE,
  SALEWATER    NUMBER(10,3),
  NOVALUEWATER NUMBER(10,3),
  INSERTDATE   DATE,
  UPDATEDATE   DATE,
  FAMILYNUM    NUMBER(19),
  DMAINFO_DBID NUMBER(19),
  ACTIVE       NUMBER(1)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 8K
    minextents 1
    maxextents unlimited
  );
alter table ALARM_DMA_SALE_WATER
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
alter table ALARM_DMA_SALE_WATER
  add constraint FK8D03B6249832FA39 foreign key (DMAINFO_DBID)
  references DMAINFO (DBID);

