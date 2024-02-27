package com.example.project_map;

import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystParameter;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.analyst.networkanalyst.TransportationAnalystSetting;
import com.supermap.analyst.networkanalyst.WeightFieldInfo;
import com.supermap.analyst.networkanalyst.WeightFieldInfos;
import com.supermap.data.Color;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoLineM;
import com.supermap.data.GeoStyle;
import com.supermap.data.Point2Ds;
import com.supermap.data.Workspace;
import com.supermap.mapping.Layer;
import com.supermap.mapping.LayerSettingVector;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.TrackingLayer;

public class RoadAnalsys {
    private Workspace workspace;
    private int datasourceIndex=0;
    private int datasetIndex=0;
    private MapControl mapControl;
    private int layerIndex = 0;
    private String edgeNameField;
    private int tolerance;
    private Point2Ds point2Ds;
    private String weightName;

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public int getDatasourceIndex() {
        return datasourceIndex;
    }

    public void setDatasourceIndex(int datasourceIndex) {
        this.datasourceIndex = datasourceIndex;
    }

    public int getDatasetIndex() {
        return datasetIndex;
    }

    public void setDatasetIndex(int datasetIndex) {
        this.datasetIndex = datasetIndex;
    }

    public MapControl getMapControl() {
        return mapControl;
    }

    public void setMapControl(MapControl mapControl) {
        this.mapControl = mapControl;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    public String getEdgeNameField() {
        return edgeNameField;
    }

    public void setEdgeNameField(String edgeNameField) {
        this.edgeNameField = edgeNameField;
    }

    public int getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public Point2Ds getPoint2Ds() {
        return point2Ds;
    }

    public void setPoint2Ds(Point2Ds point2Ds) {
        this.point2Ds = point2Ds;
    }

    public String getWeightName() {
        return weightName;
    }

    public void setWeightName(String weightName) {
        weightName = weightName;
    }

    DatasetVector datasetVector;
    TrackingLayer trackingLayer;
    Layer layerLine;
    LayerSettingVector lineSetting;
    TransportationAnalyst analyst;
    public void init(){
        datasetVector=(DatasetVector)workspace.getDatasources().get(datasourceIndex).getDatasets().get(datasetIndex);
        trackingLayer=mapControl.getMap().getTrackingLayer();
        layerLine=mapControl.getMap().getLayers().get(layerIndex);
        layerLine.setSelectable(false);
        lineSetting=(LayerSettingVector)layerLine.getAdditionalSetting();
        GeoStyle lineStyle = new GeoStyle();
        lineStyle.setLineColor(new Color(0, 0, 255));
        lineStyle.setLineWidth(0.1);
        lineSetting.setStyle(lineStyle);

        TransportationAnalystSetting setting = new TransportationAnalystSetting();
        setting.setNetworkDataset(datasetVector);
        setting.setEdgeIDField("SmEdgeID");
        setting.setNodeIDField("SmNodeID");
        setting.setEdgeNameField(edgeNameField);

        setting.setTolerance(tolerance);

        WeightFieldInfos weightFieldInfos = new WeightFieldInfos();
        WeightFieldInfo weightFieldInfo = new WeightFieldInfo();
        weightFieldInfo.setFTWeightField("smLength");
        weightFieldInfo.setTFWeightField("smLength");
        weightFieldInfo.setName("length");
        weightFieldInfos.add(weightFieldInfo);
        setting.setWeightFieldInfos(weightFieldInfos);
        setting.setFNodeIDField("SmFNode");
        setting.setTNodeIDField("SmTNode");

        analyst = new TransportationAnalyst();
        analyst.setAnalystSetting(setting);
        analyst.load();
    }

    boolean doinit =false;
    TransportationAnalystResult result;
    public void analyst(){
        if(!doinit){
            init();
            doinit=true;
        }
        TransportationAnalystParameter parameter = new TransportationAnalystParameter();

        parameter.setWeightName(weightName);


        parameter.setPoints(point2Ds);
        parameter.setNodesReturn(true);
        parameter.setEdgesReturn(true);
        parameter.setPathGuidesReturn(true);
        parameter.setRoutesReturn(true);

        result=analyst.findPath(parameter,false);
        showResult();
    }

    public void showResult() {
        int count = trackingLayer.getCount();
        for (int i = 0; i < count; i++)
        {
            int index = trackingLayer.indexOf("result");
            if (index != -1)
                trackingLayer.remove(index);
        }

        GeoLineM[] routes = result.getRoutes();

        if (routes == null) {
            return;
        }

        for (int i = 0; i < routes.length; i++)
        {
            GeoLineM geoLineM = routes[i];
            GeoStyle style = new GeoStyle();
            style.setLineColor(new Color(255, 80, 0));
            style.setLineWidth(1);
            geoLineM.setStyle(style);
            trackingLayer.add(geoLineM, "result");
        }

        mapControl.getMap().refresh();
    }

    public void clean(){
        if(trackingLayer!=null&&trackingLayer.getCount()!=0)
        {trackingLayer.clear();
        mapControl.getMap().refresh();}
    }
}
