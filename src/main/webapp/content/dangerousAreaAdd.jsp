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

    <form id="form-area-add" enctype="multipart/form-data" method="post"  class="fm">

        <table class="fitem" style="width: 100%;">
            <tr><td><input type="text" hidden="hidden" name="dbId" id="dangerArea-id" value="${model.dbId}"/></td></tr>
            <tr>
                <td><label>危险区域名称</label></td>
                <td><input id="areaName" name="areaName" class="easyui-textbox" required="true"></td>
                <td><label>危险区域等级</label></td>
                <td>
                    <select id="areaGrade" name="areaGrade" class="easyui-combobox" panelHeight="auto" style="width: 50%;" editable="false">
                        <option value="一级" selected>一级</option>
                        <option value="二级">二级</option>
                        <option value="三级">三级</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td><label>位置</label></td>
                <td>
                    <input id="location" name="location"  readonly="true" class="easyui-textbox" value="<%=request.getParameter("points")%>">
                </td>
                <td><label>危险区域描述</label></td>
                <td><input  name="description"  class="easyui-textbox"></td>
            </tr>
        </table>
        <div style="text-align:center;padding:5px 0">
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

<script type="text/javascript" src="${ctx}/script/content/dangerousAreaAdd.js"></script>
<script>
   DangerousAreaAdd.init();
</script>


