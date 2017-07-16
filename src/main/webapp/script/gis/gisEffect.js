/**
 * Created by liyulong on 2016/3/7.
 */
var gisEffect = function() {
    return {
        //画溢水的圆，页面是蓝色的
        drawBlueCircle : function(feature, radius) {
            var point = feature.Geometry.Position;
            if (point == null) {
                return null;
            }

            var line = Gis.getGlobalControl().CreateGeoPolyline3D();
            var part = Gis.getGlobalControl().CreatePoint3ds();
            part.Add2(point);
            point.X += 0.000000001;
            part.Add2(point);
            line.AddPart(part);
            var polygon = line.CreateBuffer(radius * 2, true, 5, true, false);
            polygon.AltitudeMode = 1;
            polygon.MoveZ(2);

            var circleFeature = Gis.getGlobalControl().CreateFeature();
            circleFeature.Name = "Circle," + feature.Name.substring(DeviceService.getMarkerPrefix().length);
            var geoWater = polygon.ConvertToGeoWater();
            geoWater.ReflectSky = false;
            geoWater.Play();
            geoWater.WaveSpeedX = 20;
            geoWater.WaveSpeedY = 20;
            circleFeature.Geometry = geoWater;
            Gis.getGlobalControl().Globe.MemoryLayer.AddFeature(circleFeature);
            return circleFeature;
        },

        //画圆
        drawCircle : function(feature, radius, color, distance) {
            var point;
            var code = "";
            if(feature.Geometry.Type == 301) { //点
                point = feature.Geometry;
                if (point == null) {
                    return null;
                }
                code = feature.Name.substring(DeviceService.getMarkerPrefix().length);
            } else if(feature.Geometry.Type == 302) { //线
                var line = feature.Geometry;
                if (line == null) {
                    return;
                }
                var segment = line.GetSegment(0, distance);
                if (segment == null) {
                    return;
                }
                point = segment.Item(segment.PartCount - 1).Item(segment.Item(segment.PartCount - 1).Count - 1);
                if (point == null) {
                    return null;
                }
                code = feature.getFieldValue("编号");
            } else {
                return;
            }
            return gisEffect._drawCircle(point, radius, color, code);
        },

        //画圆
        _drawCircle : function(point, radius, color, code) {
            var pntPosition = new ActiveXObject("LOCASPACEPLUGIN.GSAPoint3d");
            pntPosition.X = point.X.toFixed(5);
            pntPosition.Y = point.Y.toFixed(5);
            pntPosition.Z = point.Z.toFixed(5);

            var line = Gis.getGlobalControl().CreateGeoPolyline3D();
            var part = Gis.getGlobalControl().CreatePoint3ds();
            part.Add2(pntPosition);
            pntPosition.X += 0.000000001;
            part.Add2(pntPosition);
            line.AddPart(part);
            var polygon = line.CreateBuffer(radius * 2, true, 5, true, false);
            polygon.AltitudeMode = 1;
            polygon.MoveZ(2);
            var circleFeature = Gis.getGlobalControl().CreateFeature();
            circleFeature.Name = "Circle," + code;
            circleFeature.Geometry = polygon;
            circleFeature.Geometry.Style = Gis.getGlobalControl().CreateSimplePolygonStyle3D();
            circleFeature.Geometry.Style.FillColor = color;
            Gis.getGlobalControl().Globe.MemoryLayer.AddFeature(circleFeature);
            return circleFeature;
        },

        /***
         * 燃气烟雾效果显示
         */
        AddWaterLine:function (feature, distance) {
            var point;
            if(feature.Geometry.Type == 301) { //点
                point = feature.Geometry;
                if (point == null) {
                    return null;
                }
            } else if(feature.Geometry.Type == 302) { //线
                var line = feature.Geometry;
                if (line == null) {
                    return;
                }
                var segment = line.GetSegment(0, distance);
                if (segment == null) {
                    return;
                }
                point = segment.Item(segment.PartCount - 1).Item(segment.Item(segment.PartCount - 1).Count - 1);
                if (point == null) {
                    return null;
                }
            } else {
                return;
            }
            gisEffect._AddWaterLine(point);
        },

        /***
         * 燃气烟雾效果显示
         */
        _AddWaterLine:function (point) {
            var geoParticle = Gis.getGlobalControl().CreateGeoParticle();
            geoParticle.PositonX = point.X; //117.370542817108; //117.370477513519;
            geoParticle.PositonY = point.Y; //39.171062773766; //39.1710458878595;
            geoParticle.PositonZ = point.Z;

            var emitter = Gis.getGlobalControl().CreatePointParticleEmitter();
            emitter.TexturePath = "Resource/ParticleImage/drop3.png";
            geoParticle.TimerInterval = 1;
            emitter.SetSizeFix1(2.0, 2.0);
            emitter.VelFix = 30;
            emitter.VelRnd = 2;
            emitter.GravityAcc = 9.8;
            //水柱发射方向
            emitter.AngleXYFix = 180;
            emitter.AngleXYRnd = 90;

            //水柱分散左右角度
            //emitter.AngleXYRnd = 10;
            //水柱和地面的角度
            emitter.AngleXZFix = 80;
            //水柱上下分散的角度
            emitter.AngleXZRnd = 2;
            emitter.LifeFix = 1.3;
            emitter.LifeRnd = 1.0;
            emitter.RotFix = 0;
            emitter.RotRnd = 0;
            emitter.RotVelFix = 0;
            emitter.RotVelRnd = 0;
            emitter.EmitPerSec = 1500;
            var colorRndStart = Gis.getGlobalControl().CreateColorRGBA();
            colorRndStart.SetValue(222, 222, 222, 100);
            var colorRndEnd = Gis.getGlobalControl().CreateColorRGBA();
            colorRndEnd.SetValue(222, 222, 222, 50);
            emitter.ColorRndStart = colorRndStart; //Color.FromArgb(100, 222, 222, 222);
            emitter.ColorRndEnd = colorRndEnd; //Color.FromArgb(50, 222, 222, 222);
            emitter.Name = "点发射器1";
            // 将发射器添加到粒子对象中
            geoParticle.AddEmitter(emitter);
            geoParticle.Play();
            /* var feature = myGlobeCtrl.CreateFeature();
             geoParticle.AltitudeMode = 2;
             feature.Geometry = geoParticle;
             //myGlobeCtrl.Globe.FlyToFeature(feature);
             return layerAdd.AddFeature(feature);*/

            var feature = Gis.getGlobalControl().CreateFeature();
            feature.Geometry = geoParticle;
            Gis.getGlobalControl().Globe.MemoryLayer.AddFeature(feature);
        }
    }
} ();