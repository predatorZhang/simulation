/**
 * Created by lenovo on 2017/4/7.
 */
var DangerousAreaAdd = function(){

    function showResponse(responseText, statusText, xhr, $form)  {
        if(responseText.success==true) {
            if(responseText.save=="add")
            {
                addFeatureToLayer(responseText.model);
                parent.gisTools.addSysLog(
                    "增加危险区域", "add",
                        "危险区域:"+responseText.model.areaName + ",新增成功!"
                );
            }
        }
        else{
            alert(responseText.errorMessage);
            $("#errorMessage").html(responseText.errorMessage);
        }
        $('#form-area-add').form("clear");
        if(parent.document.getElementById('iframeDlg').contentWindow.refreshDg)
            parent.document.getElementById('iframeDlg').contentWindow.refreshDg();
        parent.Gis.clearCalculate();
        parent.Gis.setDangerAreaVisiable(true);
        parent.layer.close(parent.Gis.getLayerDangerousArea());
    }
    function addFeatureToLayer(model){
        var dangerAreaName = model.areaName;
        var dangerAreaRank = model.areaGrade;
        var dangerAreaPosition = model.location;
        var dangerAreaDescription = model.description;
        var filePath = model.filePath;

        insertNewData(dangerAreaName,dangerAreaRank,dangerAreaPosition,dangerAreaDescription,filePath);
    }
    function insertNewData(dangerAreaName,dangerAreaRank,dangerAreaPosition,dangerAreaDesc,filePath) {
        var globalControl = parent.Gis.getGlobalControl();
        var color = parent.Gis.getDangerAreaColor(dangerAreaRank);
        var fields = ["name","rank","position","description","strategy"];
        var values = [dangerAreaName,dangerAreaRank,dangerAreaPosition,dangerAreaDesc,filePath];
        parent.Gis.addPolygonToLayer(color,parent.Gis.getDangerArea_polygon(),parent.Gis.getDangerAreaLayerName(),dangerAreaName,fields,values);
        globalControl.Globe.ClearAnalysis();
        globalControl.refresh();
    }

    return{
        init:function(){
            $("#cancel").click(function(){
                var location = $("#location").val();
                $('#form-area-add').form("clear");
                $("#location").textbox('setValue',location);
                parent.Gis.clearCalculate();
                parent.layer.close(parent.Gis.getLayerDangerousArea());
            });

            $("#ok").click(function(){
                $('#form-area-add').ajaxSubmit({
                    type: 'post',
                    url: $('#context').val() + "/dangerArea/dangerArea-save.do?",
                    dataType:'json',
                    beforeSubmit:function(){
                        var areaName = $('#areaName').filebox("getValue");
                        if(!areaName){
                            alert("请输入区域名称");
                            return false;
                        }
                        return $('#form-area-add').form('validate');
                    },
                    success:showResponse,
                    error: function (XmlHttpRequest, textStatus, errorThrown) {
                        alert("error");
                        console.log(XmlHttpRequest);
                        console.log(textStatus);
                        console.log(errorThrown);
                    }
                });
            });
        }
    }
}();