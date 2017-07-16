delete from sm_flow;
insert into sm_flow (DBID, ACTIVE, DESCN, FLOWCODE, FLOWNAME, FLOW_PIC, UPDATE_TIME)
values (1, 1, '按照规定节点完全执行', 'DEFAULT_FLOW', '默认流程', '', '28-4月 -17 03.46.37.000000 下午');

insert into sm_flow (DBID, ACTIVE, DESCN, FLOWCODE, FLOWNAME, FLOW_PIC, UPDATE_TIME)
values (2, 1, '巡检上报流程，pad端上传后直接指派即可', 'PATROL_FLOW', '巡检流程', '', '28-4月 -17 03.46.37.000000 下午');

-- insert into sm_flow (DBID, ACTIVE, DESCN, FLOWCODE, FLOWNAME, FLOW_PIC, UPDATE_TIME)
-- values (3, 1, '公众上报事件多样，无需备案', 'PUBLIC_FLOW', '公众上报流程', '', '28-4月 -17 03.46.37.000000 下午');