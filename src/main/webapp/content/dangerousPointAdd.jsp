<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="/taglibs.jsp" %>

<html>

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=10"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/script/easyui/themes/default/easyui.css"/>

    <style type="text/css">

        body {
            padding: 0;
            margin: 0;
        }
        table,td,tr {
            font-size: 12px;
        }
    </style>
</head>

<body style="text-align: left;background-color: #FFFFFF;height:250px;overflow-x:hidden;overflow-y:hidden;">
<input type="hidden" id="context" value="${ctx}"/>
<div>

    <form id="fm-point-add" enctype="multipart/form-data" method="post" class="fm">

        <table class="fitem" style="width: 100%;">
            <tr>
                <td><label>危险源名称</label></td>
                <td><input id="sourceName" name="sourceName" class="easyui-textbox easyui-validatebox" required="true"></td>
                <td><label>危险源等级</label></td>
                <td>
                    <select class="easyui-combobox" name="sourceGrade" panelHeight="auto" style="width:50%;" editable="false" >
                        <option value="一级" selected>一级</option>
                        <option value="二级">二级</option>
                        <option value="三级">三级</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>经度</label></td>
                <td><input id="longitude" name="longitude" class="easyui-textbox easyui-validatebox" readonly="readonly" required="true" value="<%=request.getParameter("longitude")%>"></td>
                <td><label>纬度</label></td>
                <td> <input id="latitude" name="latitude" class="easyui-textbox easyui-validatebox" readonly="readonly" required="true" value="<%=request.getParameter("latitude")%>"></td>
            </tr>
            <tr>
                <td><label>事故模型</label></td>
                <td>
                    <select class="easyui-combobox" name="errorMode" panelHeight="auto" style="width: 100%;" editable="false">
                        <option value="燃气泄露" selected>燃气泄露模型</option>
                        <option value="雨水溢流">雨水溢流模型</option>
                        <option value="给水泄露">给水泄露模型</option>
                    </select>
                </td>
                <td><label>危险源描述</label></td>
                <td><input  name="description"  class="easyui-textbox" editable="true"></td>
            </tr>

        </table>

        <div id="footer" style="text-align:center;padding:25px 0">
            <a class="easyui-linkbutton" id="cancel" style="width:80px">取消</a>
            <a class="easyui-linkbutton" id="ok" style="width:80px">提交</a>
        </div>
    </form>

</div>

</body>

</html>
<script type="text/javascript" src="${ctx}/script/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/script/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${ctx}/script/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/script/content/dangerousPointAdd.js"></script>
<script>
    DangerousPointAdd.init();
</script>


