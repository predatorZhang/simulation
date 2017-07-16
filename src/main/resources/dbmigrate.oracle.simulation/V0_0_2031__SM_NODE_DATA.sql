delete from sm_node;
insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (1, '待确认', 0, '待确认', 'ztcg', '物联网实时数据分析及可视化系统');

insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (2, '废除', 1, '废除', '', '');

insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (3, '待派发', 2, '待派发', 'simulation', '事件仿真及协同处置系统');

insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (4, '待签收', 3, '待签收', 'patrol', '移动物联网平台');

insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (5, '待处理', 4, '待处理', 'patrol', '移动物联网平台');

insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (6, '待备案', 5, '待备案', 'simulation', '事件仿真及协同处置系统');

insert into sm_node (DBID, DESCN, MESSAGE_STATUS, NODENAME, SYS_CODE, SYS_NAME)
values (7, '完成', 6, '完成', 'simulation', '事件仿真及协同处置系统');