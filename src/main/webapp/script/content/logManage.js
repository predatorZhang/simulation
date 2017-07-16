/**
 * Created by lenovo on 2017/5/9.
 */
var LogManage = function(){
    return {
        init:function(){
            $("#log_list").datagrid({
                url: $('#context').val() + '/SysLog/SysLogList.do',
                queryParams: {
                    beginDate: $("#date_start").datebox("getText"),
                    endDate: $("#date_end").datebox("getText")
                }
            });
            $("#btn_search_log").click(function(){
                $("#log_list").datagrid("reload", {
                    createUser: $("#operator_name").textbox("getValue"),
                    beginDate: $("#date_start").datebox("getText"),
                    endDate: $("#date_end").datebox("getText")
                })
            });
        }
    }
}();