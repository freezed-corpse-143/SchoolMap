package com.example.project_map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.example.project_map.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.supermap.analyst.BufferAnalystGeometry;
import com.supermap.analyst.BufferAnalystParameter;
import com.supermap.analyst.BufferEndType;
import com.supermap.data.Color;
import com.supermap.data.CursorType;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.DatasetVectorInfo;
import com.supermap.data.Datasets;
import com.supermap.data.Environment;
import com.supermap.data.GeoPoint;
import com.supermap.data.GeoRegion;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometrist;
import com.supermap.data.Geometry;
import com.supermap.data.GeometryType;
import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Action;
import com.supermap.mapping.CallOut;
import com.supermap.mapping.CalloutAlignment;
import com.supermap.mapping.GeometrySelectedEvent;
import com.supermap.mapping.GeometrySelectedListener;
import com.supermap.mapping.Layer;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.mapping.TrackingLayer;
import com.supermap.navi.NaviInfo;
import com.supermap.navi.NaviListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Mappage extends AppCompatActivity {

    private static final String TAG ="Mappage" ;
    BottomSheetBehavior behavior;

    List<String> data = new ArrayList<>();
    MapControl mapControl;
    Workspace workspace;
    MapView mapView;
    LinearLayout linearLayout;
    ListView listshow;
    boolean jud;
    int ceng = 0;
    Point2D mypoint;
    EditText editplace;
    Spinner spnSelect_1;
    int zoomLevel=2;
    SelfLocation selfLocation = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applyForPermissions();

        //初始化环境
//        Environment.setOpenGLMode(false);
//        Environment.setLicensePath("/data/app/SuperMap/License/");
//        Environment.setWebCacheDirectory("/data/app/SuperMap/WebCache/");
//        Environment.setTemporaryPath("/data/app/SuperMap/temp/");

        Environment.setLicensePath("/sdcard/SuperMap/license/");
        Environment.setWebCacheDirectory("/sdcard/SuperMap/WebCache/");
        Environment.setTemporaryPath("/sdcard/SuperMap/temp/");

        Environment.initialization(this);


        //设定软键盘弹出时页面不会被顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_mappage);

        //打开工作空间
        workspace = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();

//        info.setServer("/sdcard/Bookmap/Xicai.smwu");
        info.setServer("/sdcard/SuperMap/SampleData/Bookmap/Xicai.smwu");
        info.setType(WorkspaceType.SMWU);
        workspace.open(info);


        mapView = (MapView) findViewById(R.id.map);
        mapControl = mapView.getMapControl();
        mapControl.getMap().setWorkspace(workspace);

        //打开第一幅地图
        String mapName =workspace.getMaps().get(0);
        mapControl.getMap().open(mapName);
        mapControl.getMap().refresh();


        //运行GPS
        selfLocate();

        ZoomTo(4);
        //设置BottomSheet动作
        final View nestedScrollView = findViewById(R.id.bottom_sheet);
        ViewGroup.LayoutParams layoutParams = nestedScrollView.getLayoutParams();
        CoordinatorLayout.Behavior b = ((CoordinatorLayout.LayoutParams) layoutParams)
                .getBehavior();
        behavior = (BottomSheetBehavior) b;


        ImageButton Zoomout = (ImageButton) findViewById(R.id.Zoomout);
        Zoomout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     setZoom(0);
            }
        });

        ImageButton Zoomin = (ImageButton) findViewById(R.id.Zoomin);
        Zoomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setZoom(1);
            }
        });


        Button query_button = (Button) findViewById(R.id.query);
        editplace = (EditText) findViewById(R.id.place_edit);

        String[] item ={"条件查询","找我附近"};
        Spinner spnSelect = (Spinner)findViewById(R.id.spinner_map); //设置下拉菜单
        ArrayAdapter adtSelect = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,item);//设置下拉菜单内数据
        adtSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉款式
        spnSelect.setAdapter(adtSelect); //绑定adapter

        String[] item_1 ={"电子产品店","快递站","药店","电影院","饮品","小吃","超市","健身房","公交站","书店","住宿"};
        spnSelect_1 = (Spinner)findViewById(R.id.spinner2); //设置下拉菜单
        ArrayAdapter adtSelect_1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,item_1);//设置下拉菜单内数据
        adtSelect_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //设置下拉款式
        spnSelect_1.setAdapter(adtSelect_1);


        spnSelect.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if((String)spnSelect.getSelectedItem()=="条件查询"){
                      spnSelect_1.setVisibility(View.GONE);
                      editplace.setVisibility(View.VISIBLE);
                      query_button.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              Mappage.this.onClick();
                          }
                      });
                      editplace.setInputType(InputType.TYPE_CLASS_TEXT);
                }

                else if((String)spnSelect.getSelectedItem()=="找我附近"){
                    spnSelect_1.setVisibility(View.VISIBLE);
                    editplace.setVisibility(View.GONE);
                    query_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Query_around();
                        }
                    });

                    editplace.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //我的位置
        mypoint = new Point2D(103.823536,30.684456);

        //选中地图上的地点 得到相关信息
        mapControl.setAction(Action.SELECT);
        mapControl.addGeometrySelectedListener(new GeometrySelectedListener() {
            @Override
            public void geometrySelected(GeometrySelectedEvent geometrySelectedEvent) {
                mapView.removeAllCallOut();
                jud = true;
                int curID = -1;
                Log.i(TAG, "geometrySelected: "+ mapControl.getMap().getLayers().get(8).getName());
                //得到点击地址的id
                curID = geometrySelectedEvent.getGeometryID();
                //得到点击地址对应的图层
                Layer layer = geometrySelectedEvent.getLayer();
                //如果得到的发现是第三层数据集 说明是公交站 记录ceng 便于后续另外处理
                if(layer.getName().equals("公交站@地图信息"))
                    jud = false;

                //标记该点 得到该点的信息
                if(layer!=null && layer.getSelection().getCount() > 0){//判断图层是否为空，且选择了对象
                    Recordset recordset = layer.getSelection().toRecordset();
                    recordset.moveFirst();
                    Geometry geoMetry = recordset.getGeometry();
                    if(geoMetry.getType()== GeometryType.GEOPOINT){//判断是否选择了点
                        Detail detail = new Detail();
                        Point2D pointter = recordset.getGeometry().getInnerPoint();

                        detail.setDistance(measureDistance(mypoint,pointter));

                        if(recordset.getFieldInfos().get("Name")!=null) {//判断该选择集是否含有PointName的字段
//                            显示poi
                            LayoutInflater lfCallOut = getLayoutInflater();
                            View calloutLayout = lfCallOut.inflate(R.layout.layout_poi, null); //得到poi页面

                            TextView btnSelected = (TextView) calloutLayout.findViewById(R.id.tv_poiname); //得到poi页面中的textview
                            btnSelected.setText(recordset.getString("Name"));
                            //这里是让poi显示在正确的位置
                           CallOut callout = new CallOut(Mappage.this);
                            callout.setContentView(calloutLayout);                // 设置显示内容
                            callout.setCustomize(true);                            // 设置自定义背景图片
                            callout.setLocation(pointter.getX(), pointter.getY());
                            Log.i(TAG, "geometrySelected: "+pointter.getX()+" ; "+pointter.getY());// 设置显示位置
                            mapView.addCallout(callout);
                            ZoomTo(6);
                            mapControl.getMap().setCenter(pointter);

                            //如果不为公交站层 那么正常处理
                            if(jud){
                            detail.setPoint(pointter);
                            detail.setName(recordset.getString("Name"));
                            detail.setAddress(recordset.getString("Address"));
                            detail.setCategory(recordset.getString("Category"));
                            detail.setPhone(recordset.getString("Phone"));
                            detail.setScore(recordset.getString("Score"));
                            detail.setTime(recordset.getString("Time"));
                            detail.setJud(jud);}

                            //如果发现是公交站层 那么详情里面地址项就改成运营时间项 电话项改为停靠车辆项 方便显示
                            else {
                                detail.setName(recordset.getString("Name"));
                                detail.setAddress(recordset.getString("Time"));
                                detail.setPhone(recordset.getString("Cars"));
                                detail.setCategory("公交车站");
                                detail.setJud(jud);
                                detail.setPoint(pointter);
                            }

                            //放入显示方法
                            Show_detail(detail);

                            //自动打开BottomSheet
                            if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }

                        }
                    }
                }
            }

            @Override
            public void geometryMultiSelected(ArrayList<GeometrySelectedEvent> arrayList) {

            }

            @Override
            public void geometryMultiSelectedCount(int i) {

            }
        });




    }

  //查询按钮
    public void onClick() {
        goback();//显示详情变为显示列表
        Query(); // 查询
    }

    //查询方法
    private void Query() {
        EditText place = (EditText) findViewById(R.id.place_edit);
        String text = place.getText().toString();
        String strFilter = "Name Like '%" + text + "%'";  //写查询语句

        // 从第一个图层往下找
        ceng =0;
        ArrayList<Detail> m = new ArrayList<Detail>();

        //移除所有callout
        mapView.removeAllCallOut();
        while (ceng<mapControl.getMap().getLayers().getCount()) {
            jud = true;
            Layer layer = mapControl.getMap().getLayers().get(ceng);
            if(layer.getName().equals("公交站@地图信息"))
                jud=false;
            DatasetVector datasetvector = (DatasetVector) layer.getDataset();

            if(datasetvector.getDescription().equals("Point")) {

                // 设置查询参数
                QueryParameter parameter = new QueryParameter(); //初始化参数查找
                parameter.setAttributeFilter(strFilter); //设置参数查找
                parameter.setCursorType(CursorType.STATIC);

                // 查询，返回查询结果记录集
                Recordset recordset = datasetvector.query(parameter); //得到结果集

                if (recordset.getRecordCount() < 1) {//没找到对象咋办
                    ceng++;
                    continue;
                }


                Point2D ptInner; //2D点
                recordset.moveFirst();  //游标跑到第一行
                Geometry geometry = recordset.getGeometry();
                while (!recordset.isEOF()) {  //当record不为空
                    geometry = recordset.getGeometry(); //更新地点
                    ptInner = geometry.getInnerPoint(); //根据位置返回点

                    LayoutInflater lfCallOut = getLayoutInflater();
                    View calloutLayout = lfCallOut.inflate(R.layout.layout_poi, null);
                    TextView btnSelected = (TextView) calloutLayout.findViewById(R.id.tv_poiname); //得到poi页面中的textview
                    btnSelected.setText(recordset.getString("Name"));
                    //这里是让poi显示在正确的位置
                    CallOut callout = new CallOut(Mappage.this);
                    callout.setContentView(calloutLayout);                // 设置显示内容
                    callout.setCustomize(true);                            // 设置自定义背景图片
                    callout.setLocation(ptInner.getX(), ptInner.getY());// 设置显示位置
                    mapView.addCallout(callout);

                    Detail detail = new Detail();
                    Log.i(TAG, "Query: ceng"+layer.getName());
                    //当不为公交站层的时候 正常存放数据
                    if (jud) {
                        detail.setName(recordset.getString("Name"));
                        detail.setAddress(recordset.getString("Address"));
                        detail.setCategory(recordset.getString("Category"));
                        detail.setPhone(recordset.getString("Phone"));
                        detail.setScore(recordset.getString("Score"));
                        detail.setTime(recordset.getString("Time"));
                        detail.setDistance(measureDistance(mypoint, ptInner));
                        detail.setJud(jud);
                        detail.setPoint(ptInner);//存放这个是为了后续让这个点居中
                    }

                    //如果发现是公交站层 那么详情里面地址项就改成运营时间项 电话项改为停靠车辆项 方便显示
                    else {
                        detail.setName(recordset.getString("Name"));
                        detail.setAddress(recordset.getString("Time"));
                        detail.setPhone(recordset.getString("Cars"));
                        detail.setJud(jud);
                        detail.setPoint(ptInner);
                        detail.setDistance(measureDistance(mypoint, ptInner));
                    }

                    m.add(detail);//放入列表


                    recordset.moveNext();
                }

                mapView.showCallOut();            // 显示标注
                mapControl.getMap().refresh();   //更新地图

                // 释放资源
                recordset.dispose();
                geometry.dispose();
                ceng++;
            }
            else {
                ceng++;
            }
        }

        //设定listView
         listshow = (ListView) findViewById(R.id.list_place);

        //解决bottomSheet与listView的冲突
        listshow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!listshow.canScrollVertically(-1)) {      //canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                    listshow.requestDisallowInterceptTouchEvent(false);
                }else{
                    listshow.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });


      // 设定ListView
        ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        for(Detail s:m) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("Name",s.getName());
            map.put("Address",s.getAddress());
            map.put("Category",s.getCategory());
            map.put("Phone",s.getPhone());
            map.put("Score",s.getScore());
            map.put("Time",s.getTime());
            map.put("Ptinner",s.getPoint());
            map.put("Jud",s.isJud());
            map.put("Distance",s.getDistance());
            map.put("Distance_show",Reserve_Decimal(s.getDistance()));
            list.add(map);
        }

        //设定列表的Adapter
        SimpleAdapter listItemsAdapter = new SimpleAdapter(Mappage.this,(List)list,
                R.layout.list_mymap,new String[]{"Name","Address","Distance_show"},
                new int[]{R.id.name_detail,R.id.place_detail,R.id.distance});
        listshow.setAdapter(listItemsAdapter);

        //自动收起键盘
        InputMethodManager imm = (InputMethodManager) Mappage.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Mappage.this.getWindow().getDecorView().getWindowToken(), 0);

        //自动打开BottomSheet
        if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

       //给列表添加点击事件
        listshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: yes:");
                Object itemAtPosition=parent.getItemAtPosition(position);
                HashMap<String,Object> map=(HashMap<String,Object>)itemAtPosition;
                Detail detail = new Detail();
                jud = (boolean)map.get("Jud");
                //如果为公交站层 就正常取出数据放入Detail对象中
                if(jud){
                detail.setName((String)map.get("Name"));
                detail.setAddress((String)map.get("Address"));
                detail.setCategory((String)map.get("Category"));
                detail.setPhone((String)map.get("Phone"));
                detail.setTime((String)map.get("Time"));
                detail.setScore((String)map.get("Score"));
                detail.setJud((Boolean) map.get("Jud"));
                detail.setDistance((double)map.get("Distance"));
                detail.setPoint((Point2D)map.get("Ptinner"));
                }

                //如果不为公交站层 那么就放入特殊数据
                else {
                    detail.setName((String)map.get("Name"));
                    detail.setTime((String)map.get("Time"));
                    detail.setCategory("公交车站");
                    detail.setPhone((String)map.get("Phone"));
                    detail.setAddress((String)map.get("Address"));
                    detail.setJud((Boolean) map.get("Jud"));
                    detail.setDistance((double)map.get("Distance"));
                    detail.setPoint((Point2D)map.get("Ptinner"));
                }
                Show_detail(detail);

                //将该点居中
                Point2D ptinner =(Point2D) map.get("Ptinner");
                ZoomTo(6);
                mapControl.getMap().setCenter(ptinner);
                mapControl.getMap().refresh();

            }
        });


        //导航方法写在这里 point2D即为目标点
        listshow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Point2D point2D =(Point2D) ((HashMap<String,Object>)parent.getItemAtPosition(position)).get("Ptinner");
               init_road_analyse(mypoint,point2D);
                Point2D mid = new Point2D((mypoint.getX()+point2D.getX())/2,(mypoint.getY()+point2D.getY())/2);
               ZoomTo(4);
               mapControl.getMap().setCenter(mid);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });


    }

    //详情显示 到列表显示 的 方法
    private void goback(){
        listshow = (ListView) findViewById(R.id.list_place);
        linearLayout = (LinearLayout) findViewById(R.id.detail_layout);
        linearLayout.setVisibility(View.GONE);
        listshow.setVisibility(View.VISIBLE);
        ZoomTo(3);
    }

    //详情显示左上角的回退按钮
    public void Click_back(View v){
        goback();
    }

    //显示详情
    public void Show_detail(Detail detail){
        jud = detail.isJud();

        TextView name_detail = findViewById(R.id.Name);
        TextView address_detail = findViewById(R.id.Address_detail);
        TextView phone = findViewById(R.id.Phone_detail);
        ImageButton toThere = (ImageButton) findViewById(R.id.toThere);
        RatingBar score = findViewById(R.id.Score_detail);
        TextView time = findViewById(R.id.Time_detail);
        TextView  sco =findViewById(R.id.Score);
        TextView ti = findViewById(R.id.Time);


        //如果不为公交站层
        if(jud){
            //因为k=2时对pho,address内容做了更改 这里改回来
        TextView address = findViewById(R.id.Address);
        TextView pho = findViewById(R.id.Phone);
        pho.setText("电话:");
        address.setText("地址:");

        sco.setVisibility(View.VISIBLE);
        ti.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);

        name_detail.setText(detail.getName()+" ["+detail.getCategory()+"]");
        address_detail.setText(detail.getAddress());
        phone.setText(detail.getPhone());
        toThere.setTag(detail.getPoint());
        time.setText(detail.getTime());
        float star =Float.parseFloat(detail.getScore())/20;
        score.setRating(star);
        score.setEnabled(false);}

        //如果为公交站层 则特殊处理 并隐藏一些无用框
        else {
            TextView Time = findViewById(R.id.Address);
            TextView bus = findViewById(R.id.Phone);
            Time.setText("运营时间:");
            bus.setText("停靠车辆:");
            sco.setVisibility(View.GONE);
            ti.setVisibility(View.GONE);
            score.setVisibility(View.GONE);
            time.setVisibility(View.GONE);

            name_detail.setText(detail.getName()+" ["+detail.getCategory()+"]");
            address_detail.setText(detail.getAddress());
            phone.setText(detail.getPhone());


        }

        //显示详情 隐藏列表
        linearLayout = (LinearLayout) findViewById(R.id.detail_layout);
        listshow = (ListView) findViewById(R.id.list_place);
        linearLayout.setVisibility(View.VISIBLE);
        listshow.setVisibility(View.GONE);

    }

    public double measureDistance(Point2D p1,Point2D p2){
        Point2Ds two_point = new Point2Ds();
        two_point.clear();
        two_point.add(p1);
        two_point.add(p2);
        double x = Geometrist.computeGeodesicDistance(two_point,6378137, 0.00335281066474748);
        return  x;
    }

    public String Reserve_Decimal(Double d){
        DecimalFormat df = new DecimalFormat("#.00");
        return  String.valueOf(df.format(d))+"m";
    }

    public void Query_around(){
        goback();
        Layer layer_select = mapControl.getMap().getLayers().get((String)spnSelect_1.getSelectedItem()+"@地图信息");
        DatasetVector datasetvector = (DatasetVector) layer_select.getDataset();
     if(datasetvector.getDescription().equals("Point")) {
         Recordset recordset = datasetvector.getRecordset(false, CursorType.DYNAMIC);
         recordset.moveFirst();
         Geometry geoMetry = recordset.getGeometry();
         LayoutInflater lfCallOut = getLayoutInflater();


         ArrayList<Detail> detail_list = new ArrayList<Detail>();
         mapView.removeAllCallOut();
         jud = true;
         if (((String) spnSelect_1.getSelectedItem()).equals("公交站"))
             jud = false;

         while (!recordset.isEOF()) {
             Point2D here = recordset.getGeometry().getInnerPoint();
             double distance_here = measureDistance(mypoint, here);

             View calloutLayout = lfCallOut.inflate(R.layout.layout_poi, null);
             if (distance_here <= 500) {
                 TextView btnSelected = (TextView) calloutLayout.findViewById(R.id.tv_poiname); //得到poi页面中的textview
                 btnSelected.setText(recordset.getString("Name"));
                 CallOut callout = new CallOut(Mappage.this);
                 callout.setContentView(calloutLayout);                // 设置显示内容
                 callout.setCustomize(true);                            // 设置自定义背景图片
                 callout.setLocation(here.getX(), here.getY());// 设置显示位置
                 mapView.addCallout(callout);

                 Detail detail = new Detail();

                 //当不为公交站层的时候 正常存放数据
                 if (jud) {
                     detail.setName(recordset.getString("Name"));
                     detail.setAddress(recordset.getString("Address"));
                     detail.setCategory(recordset.getString("Category"));
                     detail.setPhone(recordset.getString("Phone"));
                     detail.setScore(recordset.getString("Score"));
                     detail.setTime(recordset.getString("Time"));
                     detail.setDistance(distance_here);
                     detail.setJud(jud);
                     detail.setPoint(here);//存放这个是为了后续让这个点居中
                 }

                 //如果发现是公交站层 那么详情里面地址项就改成运营时间项 电话项改为停靠车辆项 方便显示
                 else {
                     detail.setName(recordset.getString("Name"));
                     detail.setAddress(recordset.getString("Time"));
                     detail.setPhone(recordset.getString("Cars"));
                     detail.setJud(jud);
                     detail.setPoint(here);
                     detail.setDistance(measureDistance(mypoint, here));
                 }

                 detail_list.add(detail);//放入列表
             }
             recordset.moveNext();
         }
         mapView.showCallOut();            // 显示标注
         mapControl.getMap().refresh();   //更新地图

         // 释放资源
         recordset.dispose();
         geoMetry.dispose();

         //设定listView
         listshow = (ListView) findViewById(R.id.list_place);

         //解决bottomSheet与listView的冲突
         listshow.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 if (!listshow.canScrollVertically(-1)) {      //canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                     listshow.requestDisallowInterceptTouchEvent(false);
                 } else {
                     listshow.requestDisallowInterceptTouchEvent(true);
                 }
                 return false;
             }
         });


         // 设定ListView
         ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Detail s : detail_list) {
             Map<String, Object> map = new HashMap<String, Object>();
             map.put("Name", s.getName());
             map.put("Address", s.getAddress());
             map.put("Category", s.getCategory());
             map.put("Phone", s.getPhone());
             map.put("Score", s.getScore());
             map.put("Time", s.getTime());
             map.put("Ptinner", s.getPoint());
             map.put("Jud", s.isJud());
             map.put("Distance", s.getDistance());
             map.put("Distance_show", Reserve_Decimal(s.getDistance()));
             list.add(map);
         }

         //设定列表的Adapter
         SimpleAdapter listItemsAdapter = new SimpleAdapter(Mappage.this, (List) list,
                 R.layout.list_mymap, new String[]{"Name", "Address", "Distance_show"},
                 new int[]{R.id.name_detail, R.id.place_detail, R.id.distance});
         listshow.setAdapter(listItemsAdapter);

         //自动收起键盘
         InputMethodManager imm = (InputMethodManager) Mappage.this.getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(Mappage.this.getWindow().getDecorView().getWindowToken(), 0);

         //自动打开BottomSheet
         if (behavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
             behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
         }

         //给列表添加点击事件
         listshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Log.i(TAG, "onItemClick: yes:");
                 Object itemAtPosition = parent.getItemAtPosition(position);
                 HashMap<String, Object> map = (HashMap<String, Object>) itemAtPosition;
                 Detail detail = new Detail();
                 jud = (boolean) map.get("Jud");
                 //如果为公交站层 就正常取出数据放入Detail对象中
                 if (jud) {
                     detail.setName((String) map.get("Name"));
                     detail.setAddress((String) map.get("Address"));
                     detail.setCategory((String) map.get("Category"));
                     detail.setPhone((String) map.get("Phone"));
                     detail.setTime((String) map.get("Time"));
                     detail.setScore((String) map.get("Score"));
                     detail.setJud((Boolean) map.get("Jud"));
                     detail.setDistance((double) map.get("Distance"));
                     detail.setPoint((Point2D)map.get("Ptinner"));
                 }

                 //如果不为公交站层 那么就放入特殊数据
                 else {
                     detail.setName((String) map.get("Name"));
                     detail.setTime((String) map.get("Time"));
                     detail.setCategory("公交车站");
                     detail.setPhone((String) map.get("Phone"));
                     detail.setAddress((String) map.get("Address"));
                     detail.setJud((Boolean) map.get("Jud"));
                     detail.setDistance((double) map.get("Distance"));
                     detail.setPoint((Point2D)map.get("Ptinner"));
                 }
                 Show_detail(detail);

                 //将该点居中
                 ZoomTo(6);
                 Point2D ptinner = (Point2D) map.get("Ptinner");
                 mapControl.getMap().setCenter(ptinner);
                 mapControl.getMap().refresh();

             }
         });


         listshow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 Point2D point2D =(Point2D) ((HashMap<String,Object>)parent.getItemAtPosition(position)).get("Ptinner");
                 init_road_analyse(mypoint,point2D);
                 Point2D mid = new Point2D((mypoint.getX()+point2D.getX())/2,(mypoint.getY()+point2D.getY())/2);
                 ZoomTo(4);
                 mapControl.getMap().setCenter(mid);
                 behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                 return true;
             }
         });


     }
    }

    public void setZoom(int type){
        if(type==1){
            if(zoomLevel>0){
            mapControl.getMap().zoom(0.5);
            zoomLevel--;}

            else{
                Toast.makeText(Mappage.this,"已达到最小比例!",Toast.LENGTH_SHORT).show();
            }
        }

        else if(type==0){
            if(zoomLevel<7)
            {mapControl.getMap().zoom(2);
            zoomLevel++;}
            else {
                Toast.makeText(Mappage.this,"已达到最大比例!",Toast.LENGTH_SHORT).show();
            }
        }
        mapControl.getMap().refresh();
        Log.i(TAG, "setZoom: 倍数=" + zoomLevel);
    }


    public void ZoomTo(int level){
        if(zoomLevel>level){
            while(zoomLevel>level){
                zoomLevel--;
                mapControl.getMap().zoom(0.5);
            }
        }
        else if(zoomLevel<level){
            while (zoomLevel<level){
                zoomLevel++;
                mapControl.getMap().zoom(2);
            }
        }
        mapControl.getMap().refresh();
    }

    public void pilot (View v){
        Point2D d = (Point2D) v.getTag();
        Intent intent = new Intent(Mappage.this,MainActivity.class);
        intent.putExtra("start_x",mypoint.getX());
        intent.putExtra("start_y",mypoint.getY());
        intent.putExtra("des_x",d.getX());
        intent.putExtra("des_y",d.getY());
        startActivity(intent);


    }


    public void init_road_analyse(Point2D p1,Point2D p2){
        mapView.removeAllCallOut();
        showPointByCallout(p1,"startPoint", R.drawable.startpoint);
        showPointByCallout(p2,"despoint",R.drawable.despoint);
        Point2Ds pois = new Point2Ds();
        pois.add(p1);
        pois.add(p2);
        RoadAnalsys roadAnalsys = new RoadAnalsys();
        roadAnalsys.clean();
        roadAnalsys.setWorkspace(workspace);
        roadAnalsys.setDatasourceIndex(0);
        roadAnalsys.setMapControl(mapControl);
        roadAnalsys.setLayerIndex(7);
        roadAnalsys.setEdgeNameField("roadname");
        roadAnalsys.setTolerance(1);
        roadAnalsys.setPoint2Ds(pois);
        roadAnalsys.setWeightName("length");
        roadAnalsys.setDatasetIndex(21);
        roadAnalsys.init();
        roadAnalsys.analyst();

    }

    private void showPointByCallout(Point2D point, final String pointName,
                                    final int idDrawable) {
        CallOut callOut = new CallOut(this);
        callOut.setStyle(CalloutAlignment.BOTTOM);
        callOut.setCustomize(true);
        callOut.setLocation(point.getX(), point.getY());
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(idDrawable);
        callOut.setContentView(imageView);
        mapView.addCallout(callOut, pointName);
    }

    private void selfLocate(){
        if(selfLocation==null){
            selfLocation = new SelfLocation();
            selfLocation.setActivity(this);
            selfLocation.launch();
        }
        selfLocation.moniterLocation(this::test,1,"自动定位");
    }

    public void test(Object object){
        double[] location = (double[]) object;
        Log.i(TAG, "test: myplace"+location[0]);
        mypoint.setX(location[0]);
        mypoint.setY(location[1]);
        showPointByCallout(mypoint,"我的位置",R.drawable.red);
        mapControl.getMap().refresh();
    }

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


    public void self_tocenter(View v){
        selfLocate();
        mapControl.getMap().setCenter(mypoint);
        mapControl.getMap().refresh();
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        }
    }







}

