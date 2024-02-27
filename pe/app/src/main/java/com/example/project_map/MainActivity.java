package com.example.project_map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTransParameter;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.DatasetVector;
import com.supermap.data.Environment;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.PrjCoordSysType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.CallOut;
import com.supermap.mapping.CalloutAlignment;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.navi.NaviInfo;
import com.supermap.navi.NaviListener;
import com.supermap.navi.Navigation2;

import java.io.File;
import java.util.Arrays;

public class MainActivity extends Activity {
    private static final String TAG ="daohang" ;
    private Workspace m_Workspace       = null;
    private MapControl m_MapControl      = null;
    private static Map m_Map             = null;
    private MapView m_MapView         = null;
    private Navigation2 m_Navigation2     = null;
    private Point2D startPoint        = null;
    private Point2D            destPoint         = null;
    private boolean longPressEnable = false;
    private boolean setStartPoint   = false;
    private boolean setDestPoint    = false;
    private boolean isFindPath      = false;
    private boolean m_ExitEnable    = false;
    private int  zoomLevel = 5;




    private final String dataPath =
//            "/sdcard/SuperMap/SampleData/Navigation2Data/navi_beijing.smwu"
            "/sdcard/SuperMap/SampleData/Bookmap/Xicai.smwu"
            ;
    private int mapIndex =
            0
            ;
    private int dataSourceIndex =
            0
            ; //数据源的序号
    private int datasetIndex =
//            1
            21
            ; //数据集的序号
    private String modelPath =
//            "/sdcard/SuperMap/SampleData/Navigation2Data/netModel.snm"
            "/sdcard/Bookmap/NetworkModel.snm"
            ;//模型地址
    private double[] firstcenter = new double[]{
//            12953693.6950684, 4858067.04711915
            103.80904540315,30.685177029782
    };
    private double scaleRate =
//            1/229492.1875
            1.1
            ;
    private int[] firstStart = new int[]{25,25};
    private int[] firstEnd = new int[]{200,200};

    private double[] fs = new double[]{103.80904540315,30.685177029782};
    private double[] fd = new double[]{103.809376546469,30.6878192811684};


    private View layout;
    /**
     * 需要申请的权限数组
     */

    public boolean exist(String path){
        File file = new File(path);
        return file.exists();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyForPermissions();
        //初始化环境
        Environment.setOpenGLMode(false);
//        Environment.setLicensePath("/data/app/SuperMap/License/");
//        Environment.setWebCacheDirectory("/data/app/SuperMap/WebCache/");
//        Environment.setTemporaryPath("/data/app/SuperMap/temp/");

        Environment.setLicensePath("/sdcard/SuperMap/license/");
        Environment.setWebCacheDirectory("/sdcard/SuperMap/WebCache/");
        Environment.setTemporaryPath("/sdcard/SuperMap/temp/");
        Environment.initialization(this);


        setContentView(R.layout.activity_main2);
        boolean isOpen = openWorkspace();

        if(isOpen){
            initNavigation2();
            startDefaultNavi();
            m_Navigation2.setSimulationSpeed(3.6);
            m_Navigation2.startGuide(0);
        }
    }
    /**
     * 检测权限
     * return true:已经获取权限
     * return false: 未获取权限，主动请求权限
     */

    /**
     * 申请动态权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void applyForPermissions(){
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        requestPermissions(permissions,1);
    }
    /**
     * 打开工作空间，显示地图
     * @return
     */
    private boolean openWorkspace() {

        m_Workspace = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
        info.setServer(dataPath);
        info.setType(WorkspaceType.SMWU);
        boolean isOpen = m_Workspace.open(info);
        if(!isOpen){
            showInfo("Workspace open failed!");
            return false;
        }
        m_MapView = findViewById(R.id.mapView);
        m_MapControl = m_MapView.getMapControl();
        m_Map = m_MapControl.getMap();
        m_Map.setWorkspace(m_Workspace);
        m_Map.open(m_Workspace.getMaps().get(mapIndex));    // open map
//        m_Map.setScale(scaleRate);
        m_Map.setCenter(new Point2D(firstcenter[0], firstcenter[1]));
        m_Map.refresh();
        m_MapControl.setGestureDetector(new GestureDetector(longTouchListener));
        return true;
    }

    /**
     * 初始化行业导航控件
     */
    DatasetVector networkDataset;
    private void initNavigation2() {
        // 初始化行业导航对象

        networkDataset = (DatasetVector) m_Workspace.getDatasources().get(dataSourceIndex).getDatasets().get(datasetIndex);
        m_Navigation2 = m_MapControl.getNavigation2();      // 获取行业导航控件，只能通过此方法初始化m_Navigation2
        m_Navigation2.setPathVisible(true);                 // 设置分析所得路径可见
        m_Navigation2.setNetworkDataset(networkDataset);    // 设置网络数据集
        m_Navigation2.loadModel( modelPath);  // 加载网络模型
        m_Navigation2.load();
        m_Navigation2.addNaviInfoListener(new NaviListener() {

            @Override
            public void onStopNavi() {
                // TODO Auto-generated method stub
                layout.setVisibility(View.VISIBLE);     // 导航停止后，显示按钮界面
                clean();
            }

            @Override
            public void onStartNavi() {
            }

            @Override
            public void onNaviInfoUpdate(NaviInfo arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAarrivedDestination() {
                clean();
            }

            @Override
            public void onAdjustFailure() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPlayNaviMessage(String arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 启动默认设置的模拟导航
     */
    private void startDefaultNavi() {
        Intent intent = getIntent();
        startPoint = new Point2D(intent.getDoubleExtra("start_x",0),intent.getDoubleExtra("start_y",0));
        destPoint = new Point2D(intent.getDoubleExtra("des_x",0),intent.getDoubleExtra("des_y",0));

        routeAnalyze();
        //m_Navigation2.enablePanOnGuide(true);
        //m_Navigation2.startGuide(1);
    }

    /**
     * 初始化主界面控件
     */

    // 按钮单击监听事件


    /**
     * 路径分析
     */
    private void routeAnalyze() {
        Point2D point2D = new Point2D((startPoint.getX()+destPoint.getX())/2,(startPoint.getY()+destPoint.getY())/2);
        m_MapControl.getMap().setCenter(point2D);
        ZoomTo(3);
        Log.i(TAG, "routeAnalyze: start"+startPoint.getX()+";"+startPoint.getY());
        Log.i(TAG, "routeAnalyze: des"+destPoint.getX()+";"+destPoint.getY());
        if (startPoint == null || destPoint == null) {

            showInfo("请先设置起点、终点");
        } else {
            showPointByCallout(startPoint,"起点",R.drawable.startpoint);
            showPointByCallout(destPoint,"终点",R.drawable.despoint);


            m_Navigation2.setStartPoint(startPoint.getX(), startPoint.getY());        // 设置起点
            m_Navigation2.setDestinationPoint(destPoint.getX(),destPoint.getY());     // 设置终点
            m_Navigation2.setPathVisible(true);// 设置路径可见
            m_Navigation2.setIsAutoNavi(true);
            m_Navigation2.setNodeInterval(0.00001);
            isFindPath = m_Navigation2.routeAnalyst();// 路径分析
            if(isFindPath){
                m_Map.refresh();                                                      // 刷新后可显示所得路径
            }else{
                showInfo("路径分析失败！");
            }
        }
    }

    /**
     * 显示或隐藏路径
     */

    /**
     * 停止导航、清除起点、终点、路径
     */
    private void clean() {

        if(m_Navigation2.isGuiding())
            m_Navigation2.stopGuide();           // 停止正在进行的导航
        m_Navigation2.cleanPath();               // 清除路径，需在停止导航后进行，否则无效
        m_Navigation2.enablePanOnGuide(false);
        m_MapView.removeAllCallOut();
        m_Map.refresh();

        startPoint = null;
        destPoint  = null;
        isFindPath = false;
//        btn_showPath.setText("显示路径");
    }

    /**
     * 修改道路信息栏和转向信息栏
     */

    // 长按监听事件
    private GestureDetector.SimpleOnGestureListener longTouchListener = new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent event) {
            if (!longPressEnable)
                return;
            if (setStartPoint)
                startPoint = getStartPoint(event);
            if (setDestPoint)
                destPoint = getDestPoint(event);

            longPressEnable = false;
            setStartPoint = false;
            setDestPoint = false;
        }
        // 地图漫游
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (m_Navigation2!=null&&m_Navigation2.isGuiding())
                m_Navigation2.enablePanOnGuide(true);
            return false;
        }

    };

    /**
     * 从屏幕 获取起点
     * @param event
     * @return
     */
    private Point2D getStartPoint(MotionEvent event) {
        int x = 0;
        int y = 0;
        if (event == null) {
            x = firstStart[0];
            y = firstStart[1];
        } else {
            x = (int) event.getX();
            y = (int) event.getY();
        }
        Point point = new Point(x, y);
        return getPoint(point, "startPoint", R.drawable.startpoint);
    }

    /**
     * 从屏幕 获取终点
     * @param event
     * @return
     */
    private Point2D getDestPoint(MotionEvent event) {
        int x = 0;
        int y = 0;
        if (event == null) {
            x = firstEnd[0];
            y = firstEnd[1];
        } else {
            x = (int) event.getX();
            y = (int) event.getY();
        }
        Point point = new Point(x, y);
        return getPoint(point, "destPoint", R.drawable.despoint);
    }

    /**
     * 将屏幕上的点转换为地图上的点和经纬坐标点
     *
     * @param point
     * @param pointName
     * @param idDrawable
     * @return
     */
    private Point2D getPoint(Point point, final String pointName,
                             final int idDrawable) {
        Point2D point2D = null;

        // 转换为地图上的二维点
        point2D = m_MapControl.getMap().pixelToMap(point);
        showPointByCallout(point2D, pointName, idDrawable);

        if (m_Map.getPrjCoordSys().getType() != PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE) {
            PrjCoordSys srcPrjCoordSys = m_Map.getPrjCoordSys();
            Point2Ds point2Ds = new Point2Ds();
            point2Ds.add(point2D);
            PrjCoordSys desPrjCoordSys = new PrjCoordSys();
            desPrjCoordSys.setType(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
            // 转换投影坐标
            CoordSysTranslator.convert(point2Ds, srcPrjCoordSys,
                    desPrjCoordSys, new CoordSysTransParameter(),
                    CoordSysTransMethod.MTH_GEOCENTRIC_TRANSLATION);

            point2D = point2Ds.getItem(0);
        }

        return point2D;
    }

    /**
     * 显示Callout
     *
     * @param point
     * @param pointName
     * @param idDrawable
     */
    private void showPointByCallout(Point2D point, final String pointName,
                                    final int idDrawable) {
        CallOut callOut = new CallOut(this);
        callOut.setStyle(CalloutAlignment.BOTTOM);
        callOut.setCustomize(true);
        callOut.setLocation(point.getX(), point.getY());
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(idDrawable);
        callOut.setContentView(imageView);
        m_MapView.addCallout(callOut, pointName);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(!m_ExitEnable){
                showInfo("再按一次退出程序！");
                m_ExitEnable = true;
            }else{
                exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showInfo(String err){
        Toast toast = Toast.makeText(this, "Error: " + err, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void exit(){
        this.finish();
        Process.killProcess(Process.myPid());
    }

    @Override
    protected void onStop() {
        m_Workspace.dispose();
        m_Workspace = null;
        m_Map.dispose();
        m_Map = null;
        m_MapControl.dispose();
        m_MapControl = null;
        networkDataset.close();
        networkDataset = null;
        super.onStop();
    }

    public void ZoomTo(int level){
        if(zoomLevel>level){
            while(zoomLevel>level){
                zoomLevel--;
                m_MapControl.getMap().zoom(0.5);
            }
        }
        else if(zoomLevel<level){
            while (zoomLevel<level){
                zoomLevel++;
                m_MapControl.getMap().zoom(2);
            }
        }
        m_MapControl.getMap().refresh();
    }

}