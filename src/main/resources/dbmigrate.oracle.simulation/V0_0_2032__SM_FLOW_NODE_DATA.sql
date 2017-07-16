alter table SM_FLOW_NODE
  drop constraint FK_O8PVNYD3GKLON5NH4UOIR7ORL;
delete from sm_flow_node;
insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (1, 1, '废除', 'ABROGATE', '废除操作', 1, 2, 1);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (2, 0, '', '', '', 1, 2, 2);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (3, 1, '确认', 'CONFIRM', '确认', 1, 4, 1);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (4, 0, '派发', 'DISTRIBUTE', '派发', 1, 5, 3);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (5, 0, '签收', 'SIGN', '签收', 1, 6, 4);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (6, 0, '处理', 'HANDLE', '处理', 1, 7, 5);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (7, 0, '备案', 'RECORD', '备案', 1, 8, 6);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (8, 0, '修改备案', 'MODIFY', '修改备案', 1, 8, 7);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (9, 1, '派发', 'DISTRIBUTE', '派发', 2, 10, 3);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (10, 0, '签收', 'SIGN', '签收', 2, 11, 4);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (11, 0, '处理', 'HANDLE', '处理', 2, 12, 5);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (12, 0, '备案', 'RECORD', '备案', 2, 13, 6);

insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
values (13, 0, '修改备案', 'MODIFY', '修改备案', 2, 13, 7);

-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (14, 1, '废除', 'ABROGATE', '废除操作', 3, 15, 1);
--
-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (15, 0, '', '', '', 3, 15, 2);
--
-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (16, 1, '确认', 'CONFIRM', '确认', 3, 17, 1);
--
-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (17, 0, '派发', 'DISTRIBUTE', '派发', 3, 18, 3);
--
-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (18, 0, '签收', 'SIGN', '签收', 3, 19, 4);
--
-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (19, 0, '处理', 'HANDLE', '处理', 3, 20, 5);
--
-- insert into sm_flow_node (DBID, HEAD, OPERATION, OPERATION_CODE, OPERATION_DESC, FLOW_ID, NEXT_FLOW_NODE_ID, NODE_ID)
-- values (20, 0, '', '', '', 3, 20, 7);


alter table SM_FLOW_NODE
  add constraint FK_O8PVNYD3GKLON5NH4UOIR7ORL foreign key (NEXT_FLOW_NODE_ID)
  references SM_FLOW_NODE (DBID);