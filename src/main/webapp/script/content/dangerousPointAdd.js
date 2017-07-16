/**
 * Created by lenovo on 2017/4/7.
 */
var DangerousPointAdd = function(){

    function showResponse(responseText, statusText, xhr, $form)  {
        if(responseText.success) {
            if(responseText.save=="add")addFeatureToLayer(responseText.model);
        }
        else{
            alert(responseText.errorMessage);
            $("#errorMessage").html(responseText.errorMessage);
        }

        $('#fm-point-add').form("clear");
        if(parent.document.getElementById('iframeDlg').contentWindow.refreshPointDg)
            parent.document.getElementById('iframeDlg').contentWindow.refreshPointDg();
        parent.Gis.clearCalculate();
        parent.layer.close(parent.Gis.getLayerDangerousPoint());
    }
    function addFeatureToLayer(model){
        var dangerSourceName = model.sourceName;
        var dangerSourceRank = model.sourceGrade;
        var longitude = model.longitude;
        var latitude = model.latitude;
        var dangerSourceDescription = model.description;
        var filePath = model.filePath;
        insertNewData(dangerSourceName,dangerSourceRank,longitude,latitude,dangerSourceDescription,filePath);
    }
    function insertNewData(dangerSourceName,dangerSourceRank,longitude,latitude,dangerSourceDesc,filePath) {
        var globalControl = parent.Gis.getGlobalControl();
        var modelPath = parent.Gis.getDangerPointModelPathByGrade(dangerSourceRank);
        var description = "aaa";
        var fields = ["name","rank","longitude","latitude","descirption","strategy"];
        var values = [dangerSourceName,dangerSourceRank,longitude,latitude,dangerSourceDesc,filePath];
        parent.Gis.addModelToLayer(modelPath,description,parent.Gis.getDangerPointLayerName(),parent.Gis.getDangerPoint3D(),dangerSourceName,fields,values);
        var layer = globalControl.Globe.Layers.GetLayerByCaption(parent.Gis.getDangerPointLayerName());
        var ds = parent.Gis.getDs();
        if (layer != null) globalControl.Globe.Layers.RemoveLayerByID(layer.ID);
        for (var i = 0; i < ds.Count; i++)
        {
            if (ds.Item(i).caption == parent.Gis.getDangerPointLayerName()) {
                var layer = globalControl.Globe.Layers.Add2(ds.Item(i));
                layer.ObjectMinVisiblePixelSize = -1;
                break;
            }
        }
        globalControl.Globe.ClearAnalysis();
        parent.Gis.setDangerSourceVisiable(true);
        globalControl.refresh();
    }
    return{
        init:function(){
            parent.Gis.setDangerSourceVisiable(true);

            $("#cancel").click(function(){
                var latitude = $("#latitude").val();
                var longitude = $("#longitude").val();
                $('#fm-point-add').form("clear");
                $("#latitude").textbox('setValue',latitude);
                $("#longitude").textbox('setValue',longitude);
                parent.Gis.clearCalculate();
                parent.layer.close(parent.Gis.getLayerDangerousPoint());
            });

            $("#ok").click(function(){
                $('#fm-point-add').ajaxSubmit({
                    type: 'post',
                    url: $('#context').val() + "/dangerSource/dangerSource-save.do?",
                    dataType:'json',
                    beforeSubmit:function(){
                        var areaName = $('#sourceName').filebox("getValue");
                        if(!areaName){
                            alert("请输入危险源名称");
                            return false;
                        }
                        return $('#fm-point-add').form('validate');
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