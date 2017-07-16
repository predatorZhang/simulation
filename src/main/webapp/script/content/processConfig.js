var PCManager = function () {

    /**
     * 异步删除树节点公共方法
     * @param id
     * @param url
     * @param msg
     */
    function delTreeNode(id, url, msg) {
        $.ajax({
            type: "POST",
            url: $("#ctx").val() + url,
            data: {id:id},
            dataType: "json",
            success: function(result) {
                if (result.success) {
                    flashRuleTable();
                    $('#department-person-tree').tree("reload");
                } else {
                    $.messager.alert('结果', result.msg);
                }
                parent.gisTools.addSysLog(
                    "处置部门/人员管理", "del",
                        msg + ",结果:" + result.msg);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }

    /**
     * 刷新表格
     */
    function flashRuleTable() {
        $('#rule-table').datagrid("reload", {
            queryNode:$("#rule-query-task").combobox("getValue"),
            eventType:$("#rule-query-eventtype").combobox("getValue"),
            depID:$("#rule-query-dep").combobox("getValue")
        });
    }

    /**
     * 初始化按钮点击事件
     */
    function initButtons() {
        $("#node-limit-query").click(function() {
            flashRuleTable();
        });
        $("#node-limit-modify").click(function() {
            $('#infowindow').window('setTitle', "规则修改")
                .window('open');
        });
        $("#modify-rule-button").click(ajaxUpdateRules);
        $("#query-rule-button").click(flashModifyTree);
    }

    /**
     * 初始化combobox
     */
    function initComboboxOnselect() {
        $('#related-task-modify').combobox({
            valueField:'id',
            textField:'nodeName',
            editable: false,
            required:true,
            labelPosition:'top',
            url:$("#ctx").val() + '/node-limit/queryNodeExceptAll.do',
            onLoadSuccess: function(){
                var value = $(this).combobox('getValue');
                if (!value) {
                    var data = $(this).combobox('getData');
                    if (data.length > 0) {
                        $(this).combobox('select', data[0].id);
                    }
                }
            }
        });
        $('#event-type-left-modify').combobox({
            valueField:'index',
            textField:'typeName',
            editable: false,
            required:true,
            labelPosition:'top',
            url:$("#ctx").val() + '/event/queryEventType.do',
            onLoadSuccess: function(){
                var value = $(this).combobox('getValue');
                if (!value) {
                    var data = $(this).combobox('getData');
                    if (data.length > 0) {
                        $(this).combobox('select', data[0].index);
                    }
                }
            }
        });
    }

    /**
     * 规则管理树
     */
    function flashModifyTree() {
        var task = $("#related-task-modify").combobox("getValue");
        var eventType = $("#event-type-left-modify").combobox("getValue");
        if (task && eventType) {
            $("#related-task-choosen").val(task);
            $("#event-type-left-choosen").val(eventType);
            $("#department-person-modify-tree").tree({
                url: $("#ctx").val() + '/node-limit/get-tree-with-info.do',
                queryParams: {
                    queryNode: task,
                    eventType: eventType
                },
                method: 'post',
                animate: true,
                checkbox: true
            });
        }
    }

    /**
     * 异步修改规则
     */
    function ajaxUpdateRules() {
        var nodes = $('#department-person-modify-tree').tree('getChecked');
        var data = new Array();
        for(var i = 0; i < nodes.length; i++){
            if (nodes[i].type == "person")
                data.push(nodes[i].id);
        }
        if (data.length == 0) {
            $.messager.alert('结果', '处置人员不能为空');
            return;
        }
        $.ajax({
            type: "POST",
            url: $("#ctx").val() + "/node-limit/modify-node-limit.do",
            data: {
                persons: data,
                queryNode: $("#related-task-choosen").val(),
                eventType: $("#event-type-left-choosen").val()
            },
            dataType: "json",
            success: function(result) {
                flashRuleTable();
                $.messager.alert('结果', result.msg);
                parent.gisTools.addSysLog(
                    "流程规则管理", "modify",
                        "任务[" +
                        $("#related-task-modify").combobox("getText")
                        + "]在事件类型[" +
                        $("#event-type-left-modify").combobox("getText")
                        + "]下负责处理的人员id有[" + data.toString()
                        + "],结果:" + result.msg);
            },
            error:function(XMLHttpRequest, textStatus, errorThrown){}
        });
    }

    return {

        init: function () {
            initButtons();
            initComboboxOnselect();
        },

        /**
         * 打开dialog
         * @param dlgId
         */
        openDialog: function (dlgId) {
            $(".validatebox-tip").remove();
            $(".validatebox-invalid").removeClass("validatebox-invalid");
            $(dlgId).dialog('open').dialog('center');
        },

        /**
         * 清空form表单
         * @param formId
         */
        clearForm: function (formId) {
            $(formId).form('clear');
        },

        /**
         * 删除部门或人员
         */
        removeit: function(){
            var node = $('#department-person-tree').tree('getSelected');
            if (node.type == "root") {
                $.messager.alert('提示', '无法删除根节点');
            } else {
                $.messager.confirm("删除", "您确定删除吗?", function (r) {
                    if (r) {
                        if (node.type == "department") {
                            delTreeNode(node.id,
                                "/flow-dep-person/del-dep.do",
                                "删除部门[" + node.text +
                                    "]节点[" + node.id + "]"
                            );
                        } else if (node.type == "person") {
                            delTreeNode(node.id,
                                "/flow-dep-person/del-person.do",
                                "删除人员[" + node.text +
                                    "]节点[" + node.id + "]"
                            );
                        }
                    }
                })
            }
        },

        /**
         * 打开增加部门弹窗
         */
        appendDepartment: function (){
            $("#add-dep-deplist-combox").combobox("reload");
            var t = $('#department-person-tree');
            var node = t.tree('getSelected');
            if (node.type == "department" || node.type == "root") {
                $("#add-dep-depID").val(node.id);
                PCManager.openDialog("#dialog-add-department");
            } else {
                $.messager.alert('提示', '人员节点下无法添加部门');
            }
        },

        /**
         * 增加新的部门 ajax处理
         */
        newDepartment: function() {
            if ($('#form-add-department').form('validate')) {
                $.ajax({
                    type: "post",
                    url: $('#ctx').val() + "/flow-dep-person/add-dep.do",
                    data: $('#form-add-department').serialize(),
                    dataType: "json",
                    success: function(result) {
                        var depName = $("#depName").textbox("getText");
                        if (result.success) {
                            $('#dialog-add-department').dialog('close');
                            PCManager.clearForm('#form-add-department');
                        } else {
                            $.messager.alert('结果', result.msg);
                        }
                        parent.gisTools.addSysLog(
                            "处置部门/人员管理", "add",
                                "增加新的处置部门[" + depName
                                 + "],结果:" + result.msg);
                        $('#department-person-tree').tree("reload");
                        $('#add-dep-deplist-combox').combobox('reload');
                    },
                    error:function(XMLHttpRequest,
                                   textStatus, errorThrown){}
                });
            }
        },

        /**
         * 打开增加人员弹窗
         */
        appendPerson: function (){
            var t = $('#department-person-tree');
            var node = t.tree('getSelected');
            if (node.type == "department" || node.type == "root") {
                $("#belongToDepName").textbox('setValue', node.text);
                $("#add-person-depID").val(node.id);
                PCManager.openDialog("#dialog-add-person");
            } else {
                $.messager.alert('提示', '人员节点下无法添加人员');
            }
        },

        /**
         * 增加新的人员 ajax处理
         */
        newPerson: function() {
            if ($('#form-add-person').form('validate')) {
                $.ajax({
                    type: "post",
                    url: $('#ctx').val() + "/flow-dep-person/add-person.do",
                    data: $('#form-add-person').serialize(),
                    dataType: "json",
                    success: function(result) {
                        var personName = $("#add-person-personlist-combox")
                            .combobox("getText");
                        var depName = $("#belongToDepName")
                            .textbox("getText");
                        if (result.success) {
                            $('#dialog-add-person').dialog('close');
                            PCManager.clearForm('#form-add-person');
                        } else {
                            $.messager.alert('结果', result.msg);
                        }
                        parent.gisTools.addSysLog(
                            "处置部门/人员管理", "add",
                                "增加新的处置人员[" + personName
                                    + "]，所属部门[" + depName
                                    + "],结果:" + result.msg);
                        $('#department-person-tree').tree("reload");
                        $('#add-person-personlist-combox')
                            .combobox('reload');
                    },
                    error:function(XMLHttpRequest,
                                   textStatus, errorThrown){}
                });
            }
        },

        flashPage: function(node) {
        }
    }
}();