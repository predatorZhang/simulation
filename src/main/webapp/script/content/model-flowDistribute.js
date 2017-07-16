var FlowDistribute = function() {

    /**
     * 初始化页面表格
     */
    function initPagination() {
        var pager = $('#distribute-table').datagrid({
            url: $("#ctx").val() + '/allocation/flow-allocation-show.do'
        }).datagrid('getPager');
        pager.pagination({
            buttons:[{
                iconCls:'icon-add',
                handler:function(){
                    $("#add-distribution-button").linkbutton({text:'增加'});
                    $('#infowindow').window('setTitle', "增加事件派发")
                        .window('open');
                    $('#distributeID').val('');
                    $('#event-type-left-combox').combobox('reload')
                        .combobox({readonly: false});
                    $('#flow-combox').combobox('reload');
                }
            },{
                iconCls:'icon-remove',
                handler:function(){
                    var row = $('#distribute-table').datagrid('getSelected');
                    if (row){
                        $.messager.confirm("删除", "您确定删除吗?",
                            function (r) {if (r) {deleteAllocation(row);}}
                        );
                    } else {
                        $.messager.alert('结果', "请选择要删除的行");
                    }
                }
            },{
                iconCls:'icon-edit',
                handler:function(){
                    var row = $('#distribute-table').datagrid('getSelected');
                    if (row){
                        $("#add-distribution-button").linkbutton({text:'修改'});
                        $('#infowindow').window('setTitle', "修改事件派发")
                            .window('open');
                        $('#distributeID').val(row.id);
                        $('#event-type-left-combox').combobox('reload')
                            .combobox({readonly: true})
                            .combobox('setValue', row.eventType);
                        $('#flow-combox').combobox('reload')
                            .combobox('setValue', row.flowID);
                    } else {
                        $.messager.alert('结果', "请选择要编辑的行");
                    }
                }
            }]
        });
    }

    /**
     * 刷新表格内容
     */
    function flashTable() {
        $('#distribute-table').datagrid("reload", {
            eventType:$("#event-type-combox").combobox("getValue")
        });
    }

    /**
     * 删除选中的allocation
     * @param row
     */
    function deleteAllocation(row) {
        $.ajax({
            type: "POST",
            url: $("#ctx").val() + "/allocation/del-allocation.do",
            data: {id: row.id},
            dataType: "json",
            success: function(result) {
                if (result.success) {
                    $("#distribute-query").click();
                }
                parent.gisTools.addSysLog(
                    "事件派发管理", "del",
                        "删除为事件类型[" + row.eventTypeDesc +
                        "]设置的派发规则" + ",结果:" + result.msg);
                flashTable();
                $.messager.alert('结果', result.msg);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }

    /**
     * 初始化按钮事件绑定
     */
    function initButtons() {
        $("#distribute-query").click(function() {
            flashTable();
        });
        $("#add-distribution-button").click(function() {
            if ($('#addFlowDistribute').form('validate')) {
                $.ajax( {
                    type: "POST",
                    url: $('#ctx').val() + "/allocation/save-allocation.do",
                    data: $('#addFlowDistribute').serialize(),
                    dataType: "json",
                    success: function(result) {
                        if (result.success) {
                            $("#distribute-query").click();
                        }
                        var combox = $('#event-type-left-combox');
                        if ($("#distributeID").val()) {
                            parent.gisTools.addSysLog(
                                "事件派发管理", "edit",
                                    "修改为事件类型[" +
                                    combox.combobox("getText") +
                                    "]设置的派发规则,结果:" + result.msg);
                        } else {
                            parent.gisTools.addSysLog(
                                "事件派发管理", "add",
                                    "为事件类型[" +
                                    combox.combobox("getText") +
                                    "]增加派发规则,结果:" + result.msg);
                        }

                        flashTable();
                        $.messager.alert('结果', result.msg);
                        $('#infowindow').window('close');
                    },
                    error:function(XMLHttpRequest, textStatus, errorThrown){}
                });
            }
        });
    }

    return {
        init: function () {
            initPagination();
            initButtons();
        }
    }
}();