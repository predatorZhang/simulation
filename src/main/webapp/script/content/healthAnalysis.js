/**
 * Created by lenovo on 2017/3/28.
 */
var HealthAnalysis = function(){

    var  colorGreen  =  parent.Gis.getGlobalControl().CreateColorRGBA();
    var  colorRed    =  parent.Gis.getGlobalControl().CreateColorRGBA();
    var  colorYellow =  parent.Gis.getGlobalControl().CreateColorRGBA();
    var  colorBlue   =  parent.Gis.getGlobalControl().CreateColorRGBA();
    var  colorPink   =  parent.Gis.getGlobalControl().CreateColorRGBA();
    var  colorBrown  =  parent.Gis.getGlobalControl().CreateColorRGBA();
    colorRed.SetValue(255, 0, 0, 255);
    colorGreen.SetValue(0, 255, 0, 255);
    colorYellow.SetValue(255, 255, 0, 255);
    colorBlue.SetValue(0, 0, 255, 255);
    colorPink.SetValue(255, 128, 128, 255);
    colorBrown.SetValue(128, 64, 64, 255);

    function view(row){
        var pipeId = row.pipeId;
        var healthRank = row.healthRank;
        var pipeType = row.pipeType;
        var pipeFeature = getFeatureByName(pipeId);
        if(!pipeFeature){
            alert("图层中找不到编号为："+pipeId+"的管线");
            return;
        }
        var message= "管线编号：" + pipeId + "\n"
            + "管线健康度：" + row.healthRank ;
        var textColor;
        if(healthRank=="健康")
        {
            textColor = colorGreen;
        }
        else if(healthRank=="较健康")
        {
            textColor = colorBlue;
        }
        else if (healthRank=="亚健康")
        {
            textColor = colorBrown;
        }
        else if(healthRank=="疾病")
        {
            textColor = colorPink;
        }
        else if(healthRank=="严重疾病")
        {
            textColor = colorRed;
        }

        if(pipeFeature!=null)
        {
            createBuffer(pipeFeature,message,textColor,pipeType);
            globalControl.Globe.JumpToFeature(pipeFeature,20);
        }
    }
    function getFeatureByName(featureName)
    {
        var globalControl = parent.Gis.getGlobalControl();
        for(var i = 1;i < globalControl.Globe.Layers.Count+1;i++)
        {
            var layer = globalControl.Globe.Layers.GetLayerByID(i);
            var layerName=layer.Caption();//获取图层名称，不包括路径

            if (layer.Caption.indexOf("管线") != -1)
            {
                var features =layer.GetFeatureByName(featureName,true);
                for(var j = 0;j<features.count();j++)
                {
                    var feature = features.item(j);
                    if(feature.Geometry.Type == 302)
                    {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    function createBuffer(lineFeature,labelMsg,textColor,name)
    {
        var globalControl = parent.Gis.getGlobalControl();
        var line = lineFeature.Geometry;
        if(line.Type == 302)
        {
            var polygon = line.CreateBuffer(1, false, 14, false, false);
            var ff = globalControl.CreateFeature();// new ActiveXObject("LOCASPACEPLUGIN.GSAFeature");

            var style = globalControl.CreateSimplePolygonStyle3D();
            style.FillColor = textColor;
            polygon.Style = style;

            polygon.AltitudeMode = 2;
            polygon.moveZ(3.7);
            ff.Geometry = polygon;

            var label = globalControl.CreateLabel();
            label.Text = labelMsg;
            label.Style = globalControl.CreateLabelStyle();
            label.Style.TextStyle = globalControl.CreateTextStyle();
            label.Style.TextStyle.FontSize = 12;

            label.Style.TextStyle.ForeColor = textColor;
            ff.Label = label;
            ff.name = name;
            globalControl.Globe.MemoryLayer.AddFeature(ff);
            globalControl.refresh();
        }
    }
    return {
        init:function(){
            $(function(){
                $('#health-dg').datagrid({
                    url: $("#context").val() + '/health/health-info-list.do'
                });
            });

            $("#health-dg").datagrid({
                onClickCell:function(rowIndex, field, value) {

                    $('#health-dg').datagrid("selectRow",rowIndex);
                    var row = $('#health-dg').datagrid('getSelected');
//                    if (field == "positionBtn") {//编辑
////                        view(row);
//                        return;
//                    }
                    if (row) {
//                        alert("地图展示的操作");
                        view(row);
                    }
                }
            });
        },//init

        query:function(){
                $('#health-dg').datagrid({
                    url: $("#context").val() + '/health/health-info-list.do',
                    queryParams: {
                        no: $("#query-pipeId").textbox("getValue"),
                        level: $("#query-health-status").combobox("getValue"),
                        desc: $("#query-health-desc").textbox("getValue")
                    }
                });
        }
    }
}();