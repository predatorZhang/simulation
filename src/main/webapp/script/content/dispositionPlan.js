/**
 * Created by lenovo on 2017/4/1.
 */
var fileInfo = function(){

    var cols = [ [ {
        field : 'dbId',
        title: '预案编号',
        hidden: true
    }, {
        field : 'fileName',
        title : '预案名称',
        width : "10%"
    }, {
        field : 'fileTypeName',
        title : '预案类型',
        width : "10%"
    }, {
        field : 'fileDisplayName',
        title : '预案文件',
        width : "25%"
    }, {
        field : 'upDateTimes',
        title : '上传日期',
        width : "12%"
    }, {
        field : 'upPerson',
        title : '上传人',
        width : "10%"
    }, {
        field : 'filePath',
        hidden: true
    }, {
        field : 'fileDown',
        title : '文件下载',
        formatter: function (value, row, index) {
            return '<a href="#" id="sheet' + index + '" class="link_button" onclick="fileInfo.fileDown('+row.id+')"></a>';
        },
        width : "10%"
    }, {
        field : 'fileScan',
        title : '文件预览',
        formatter: function (value, row, index) {
            return '<a href="#" id="file' + index + '" class="link_button" onclick="fileInfo.fileScan('+row.id+')"></a>';
        },
        width : "10%"
    }, {
        field : 'fileDelete',
        title : '文件删除',
        formatter: function (value, row, index) {
            return '<a href="#" id="fileDelete' + index + '" class="link_button" onclick="fileInfo.fileDelete('+row.id+')"></a>';
        },
        width : "10%"
    }] ];

    function initCSS(data) {
        for( var i=0; i < data.rows.length; i++ ) {
            $('#sheet' + i).linkbutton(
                {
                    text: '文件下载',
                    plain: false,
                    iconCls: 'icon-add'
                }
            );
            $('#file' + i).linkbutton(
                {
                    text: '文件预览',
                    plain: false,
                    iconCls: 'icon-search'
                }
            );
            $('#fileDelete' + i).linkbutton(
                {
                    text: '文件删除',
                    plain: false,
                    iconCls: 'icon-remove'
                }
            );
        }
    }

    /**
     * 查询按钮触发事件
     */
    function query(){
        var start = $("#date_start"), end = $("#date_end");
        if (parent.gisTools.checkStartEarlyerEnd(start, end)) {
            $('#plan-files').datagrid({
                queryParams : {
                    planName : $("#planName").textbox("getValue"),
                    planType : $("#planType").combobox("getValue"),
                    dateStart : start.datebox("getValue"),
                    dateEnd : end.datebox("getValue")
                }
            });
        } else {
            $.messager.alert('提示', "起始时间不能大于终止时间，请重新设置！");
        }
    }

    function clearFormAndReloadData() {
        $('#plan-info').form("clear");
        $("#plan-files").datagrid("reload");
        $('#plan-dialog').dialog('close');
    }

    return{
        init : function (){
            $("#plan-files").datagrid({
                columns : cols,
                pagePosition : "bottom",
                url : $('#context').val() + "/fileController/show-file.do",
                pageNumber : 1,
                queryParams : {
                    planName : $("#planName").textbox("getValue"),
                    planType : $("#planType").combobox("getValue"),
                    dateStart : $("#date_start").datebox("getValue"),
                    dateEnd : $("#date_end").datebox("getValue")
                },
                onLoadSuccess: function (data) {
                    initCSS(data);
                }
            });

            $("#queryBtn").click(query);
        },

        /**
         * 文件上传
         */
        submit : function() {
            $('#plan-info').ajaxSubmit({
                type: 'post',
                url: $('#context').val()+"/fileController/upload-file.do",
                dataType: 'json',
                beforeSubmit:function(){
                    var filename = $('#uploadFile').filebox("getValue");
                    if(!filename){
                        $.messager.alert('提示', "请选择上传文件");
                        return false;
                    }
                    return $('#plan-info').form('validate');
                },
                success: function (responseText, statusText, xhr) {
                    clearFormAndReloadData();
                    $.messager.alert('结果', responseText.msg);
                },
                error: function (errorThrown, textStatus, XmlHttpRequest) {
                    clearFormAndReloadData();
                    console.error("网络错误[{}]", errorThrown);
                }
            })
        },

        /**
         * 文件下载
         * @param id
         */
        fileDown:function(id) {
            $.ajax({
                url: $('#context').val() + "/fileController/file-check.do",
                type: "POST",
                dataType: 'json',
                data: {
                    'dbId': id
                },
                success: function (result) {
                    if (result.success) {
                        window.location.href=$('#context').val()+"/fileController/file-download.do?dbId="+id;
                    } else {
                        $.messager.alert('结果', result.msg);
                    }
                },
                error: function (XmlHttpRequest, textStatus, errorThrown) {
                    console.error("网络错误[{}]", errorThrown);
                }
            })
        },

        /**
         * 删除记录
         * @param id
         */
        fileDelete:function(id){
            $.messager.confirm("删除", "您确定删除吗?", function (r) {
                if (r) {
                    $.ajax({
                        url: $('#context').val() + "/fileController/file-delete.do",
                        type: "POST",
                        dataType: 'json',
                        data: {
                            'dbId': id
                        },
                        success: function (result) {
                            if (result.success) {
                                $.messager.alert('结果', "删除成功！");
                                $("#plan-files").datagrid("reload");
                            }
                        },
                        error: function (XmlHttpRequest, textStatus, errorThrown) {
                            console.error("网络错误[{}]", errorThrown);
                        }
                    })
                }
            });
        },

        /**
         * 文件预览
         * @param id
         */
        fileScan:function(id){
            $.ajax({
                url: $('#context').val() + "/fileController/file-scan.do",
                type: "POST",
                dataType:'json',
                data: {
                    'dbId': id
                },
                success: function (result) {
                    if (result.success) {
                        result.filePath =
                            $('#context').val() + "/" + result.filePath;
                        fileInfo.openWindow(result);
                    } else {
                        $.messager.alert('结果', result.msg);
                    }
                },
                error: function (XmlHttpRequest, textStatus, errorThrown) {
                    console.error("网络错误[{}]", errorThrown);
                }
            })
        },

        openWindow: function(result) {
            if (window.ActiveXObject || "ActiveXObject" in window) {
                var flag = false;
                for (var x = 2; x < 10; x++) {
                    try {
                        var oAcro = eval("new ActiveXObject('PDF.PdfCtrl." + x + "');");
                        if (oAcro) {
                            flag = true;
                        }
                    } catch (e) {
                        flag = false;
                    }
                }
                if (!flag) flag = fileInfo.newActiveXObject("PDF.PdfCtrl");
                if (!flag) flag = fileInfo.newActiveXObject("PDF.PdfCtrl.1");
                if (!flag) flag = fileInfo.newActiveXObject("AcroPDF.PDF");
                if (!flag) flag = fileInfo.newActiveXObject("AcroPDF.PDF.1");
                if (flag) {
                    window.open(result.filePath);
                } else {
                    alert("对不起,您还没有安装PDF阅读器软件,请选择安装!");
                    window.open("https://get2.adobe.com/cn/flashplayer/");
                }
            } else {
                window.showModalDialog($('#context').val() + "/content/file/file.jsp", result,"dialogWidth=1200px;dialogHeight=1100px;");
            }
        },

        newActiveXObject: function(name) {
            try {
                var o = new ActiveXObject(name);
                if (o) {
                    return true;
                }
            } catch (e) {}
            return false;
        }
    };
}();