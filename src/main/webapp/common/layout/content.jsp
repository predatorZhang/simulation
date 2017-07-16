<%@ page language="java" pageEncoding="UTF-8" %>
<div title="GIS数据浏览">
    <div id="global_center_layout" class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'" id="centerArea">
            <div id="earthDiv" style="height:100%; width:100%"></div>
        </div>
        <div data-options="region:'south',title:'结果分析',split:true,collapsed:true,noheader:true" id="resultRegion" style="height:200px;overflow-x:hidden;overflow-y:hidden;">
            <iframe id="iframeDlg" src="" frameborder="0" scrolling="no" width="100%" height="100%"></iframe>
        </div>
    </div>
</div>
