<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<style type="text/css">
    #menu-accordion li:HOVER {
        background-color: #F2F2F2;
    }
</style>

<div id="menu-accordion" class="easyui-accordion" data-options="iconCls:'icon-help', fit:true">

    <div title="事件管理">
        <div style="height:100%;">
            <table id="event_management" class="easyui-treegrid" data-options="idField:'id',
                treeField: 'name',
                animate: true,
                fit: true,
                fitColumns: true,
                collapsible: true,
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                data: [{
                            'iconCls': 'icon-menm-item',
                            'id': 1,
                            'name': '事件管理'
                        }]">
                <thead>
                <tr>
                    <th data-options="field:'name',width:180">功能列表</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div title="事件仿真分析">
        <div style="height:100%; ">
            <table id="rainPipeLineFunTbl" class="easyui-treegrid" data-options="animate:true,
                fit: true,
                fitColumns: true,
                collapsible: true,
                idField: 'id',
                treeField: 'name',
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                data: [{
                            'iconCls': 'icon-menm-item',
                            'id': 1,
                            'name': '危险源管理',
                        },{
                            'iconCls': 'icon-menm-item',
                            'id': 2,
                            'name': '危险区域管理',
                        },{
                            'iconCls': 'icon-menm-item',
                            'id': 3,
                            'name': '雨水溢流分析'
                        },{
                            'iconCls': 'icon-menm-item',
                            'id': 5,
                            'name': '健康度分析',
                        },{
                            'iconCls': 'icon-menm-item',
                            'id': 6,
                            'name': '漏损分析'
                        }]">
                <thead>
                <tr>
                    <th data-options="field:'name', width:180">功能列表</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div title="处置预案管理">
        <div style="height:100%; ">
            <table id="disposition_plan_manage" class="easyui-treegrid" data-options="animate:true,
                fit: true,
                fitColumns: true,
                collapsible: true,
                idField: 'id',
                treeField: 'name',
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                data: [{
                            'iconCls': 'icon-menm-item',
                            'id': 1,
                            'name': '处置预案管理'
                        }]">
                <thead>
                <tr>
                    <th data-options="field:'name', width:180">功能列表</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>

    <div title="统计分析">
        <div style="height:100%;">
            <table id="statistic_analysis_table" class="easyui-treegrid" data-options="idField:'id',
                treeField: 'name',
                animate: true,
                fit: true,
                fitColumns: true,
                collapsible: true,
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                data: [{
                        'iconCls': 'icon-menm-item',
                        'id': 1,
                        'name': '统计分析'
                    }]">
                <thead>
                <tr>
                    <th data-options="field:'name',width:180">功能列表</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <div title="基础信息管理">
        <div style="height:100%;">
            <table id="process_config_manage_table" class="easyui-treegrid" data-options="idField:'id',
                treeField: 'name',
                animate: true,
                fit: true,
                fitColumns: true,
                collapsible: true,
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                data: [{
                        'iconCls': 'icon-menm-item',
                        'id': 1,
                        'name': '处置流程管理'
                    },{
                        'iconCls': 'icon-menm-item',
                        'id': 2,
                        'name': '事件派发管理'
                    },{
                        'iconCls': 'icon-menm-item',
                        'id': 3,
                        'name': '事件处置节点设置',
                    }]">
                <thead>
                <tr>
                    <th data-options="field:'name',width:180">功能列表</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
    <div title="系统管理">
        <div style="height:100%;">
            <table id="system_manage_table" class="easyui-treegrid" data-options="idField:'id',
                treeField: 'name',
                animate: true,
                fit: true,
                fitColumns: true,
                collapsible: true,
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                data: [{
                        'iconCls' : 'icon-menm-item',
                        'id' : 1,
                        'name' : '日志管理'

                    }, {
                        'iconCls' : 'icon-menm-item',
                        'id' : 2,
                        'name' : '模型集成管理',
                        'state': 'closed',
                        'children':
                        [{
                            'id' : 21,
                            'iconCls' : 'icon-menm-item',
                            'name' : 'DMA模型管理'
                        }]
                    }]">
                <thead>
                <tr>
                    <th data-options="field:'name',width:180">功能列表</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>





