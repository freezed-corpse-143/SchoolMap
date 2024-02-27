package com.example.project_map;

import com.supermap.data.DatasetVector;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.Workspace;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.navi.NaviInfo;
import com.supermap.navi.NaviListener;
import com.supermap.navi.Navigation2;

public class Navigation2Helper {
    //工作空间
    private Workspace workspace;
    //数据源序号
    private int dataSourceIndex;
    //数据集序号
    private int datasetIndex;
    //模型文件地址
    private String modelPath;
    //设置起始点
    private Point2D startPoint2D;
    //设置终点
    private Point2D destPoint2D;
    //设置地图控制
    private MapControl mapControl;
    //导航监听器
    private NaviListener naviListener;
    //模拟导航
    private int VR= 0;

    public NaviListener getNaviListener() {
        return naviListener;
    }
    public void setNaviListener(NaviListener naviListener) {
        this.naviListener = naviListener;
    }
    public MapControl getMapControl() {
        return mapControl;
    }
    public void setMapControl(MapControl mapControl) {
        this.mapControl = mapControl;
    }
    public Workspace getWorkspace() {
        return workspace;
    }
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }
    public int getDataSourceIndex() {
        return dataSourceIndex;
    }
    public void setDataSourceIndex(int dataSourceIndex) {
        this.dataSourceIndex = dataSourceIndex;
    }
    public int getDatasetIndex() {
        return datasetIndex;
    }
    public void setDatasetIndex(int datasetIndex) {
        this.datasetIndex = datasetIndex;
    }
    public String getModelPath() {
        return modelPath;
    }
    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }
    public Point2D getStartPoint2D() {
        return startPoint2D;
    }
    public void setStartPoint2D(Point2D startPoint2D) {
        this.startPoint2D = startPoint2D;
    }
    public Point2D getDestPoint2D() {
        return destPoint2D;
    }
    public void setDestPoint2D(Point2D destPoint2D) {
        this.destPoint2D = destPoint2D;
    }

    boolean doInitialize = false;

    public void navigate(){
        if(!doInitialize){
            init();
            doInitialize=true;
        }
        navigation2.setStartPoint(startPoint2D.getX(), startPoint2D.getY());
        navigation2.setDestinationPoint(destPoint2D.getX(),destPoint2D.getY());
        navigation2.setPathVisible(true);
        navigation2.setNodeInterval(0.000000001);
        boolean b = navigation2.routeAnalyst();
        navigation2.startGuide(VR);
    }


    Navigation2 navigation2;
    DatasetVector networkDataset;
    public void init(){
        navigation2 = mapControl.getNavigation2();
        networkDataset = (DatasetVector) workspace.getDatasources().get(dataSourceIndex).getDatasets().get(datasetIndex);
        navigation2 = mapControl.getNavigation2();      // 获取行业导航控件，只能通过此方法初始化m_Navigation2
        navigation2.setPathVisible(true);                 // 设置分析所得路径可见
        navigation2.setNetworkDataset(networkDataset);    // 设置网络数据集
        navigation2.loadModel( modelPath);  // 加载网络模型
        navigation2.addNaviInfoListener(naviListener);
        navigation2.cleanPath();
    }

    public void endNavigate(){
        if(navigation2!=null&&navigation2.isGuiding()){
            navigation2.stopGuide();
            navigation2.cleanPath();
        }
    }
    public void destroy(){
        networkDataset.close();
        networkDataset = null;
        navigation2 = null;
    }
}
