package gettingstarted;                                    //点击之后   像公交那样标黄 显示名字     

                                                                            //匹配ComboBox中的最近餐厅
                                                                           //TO DO 类别type的匹配 图查属性改成自动 属性查图为什么要两步  字段Table显示为什么能多类统一
import com.supermap.data.*;



import com.supermap.mapping.*;
import com.supermap.ui.*;
import com.supermap.ui.Action;


import com.supermap.analyst.networkanalyst.DirectionType;
import com.supermap.analyst.networkanalyst.PathGuide;
import com.supermap.analyst.networkanalyst.PathGuideItem;
import com.supermap.analyst.networkanalyst.SideType;
import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystParameter;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.analyst.networkanalyst.TransportationAnalystSetting;
import com.supermap.analyst.networkanalyst.WeightFieldInfo;
import com.supermap.analyst.networkanalyst.WeightFieldInfos;
import com.supermap.analyst.spatialanalyst.*;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

public class Frame1 extends JFrame {
	/**
	 * Application entry point.
	 *
	 * @param args String[]
	 */
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                UIManager.setLookAndFeel(
	                		UIManager.getSystemLookAndFeelClassName());
	            } catch (Exception exception) {
	                exception.printStackTrace();
	            }

	            Frame1 frame = new Frame1();
	            frame.setVisible(true);
	        }
	    });
	}
	
	Point2Ds m_point2Ds = new Point2Ds();
    Workspace workspace = new Workspace();
    MapControl mapControl = new MapControl();
    TrackingLayer m_trackingLayer;
	TransportationAnalystResult m_result;
    PointMs m_pointMs;
    TransportationAnalyst m_analyst ;
    DatasetVector m_datasetLine;
	String m_nodeID = "SmNodeID";
	String m_edgeID = "SmEdgeID";
    Layer m_layerPoint;
    Layer m_layerLine;
    String m_datasetName = "路径规划_Network";
    int m_count;
	SelectMode m_selectMode;
	 DatasetVector m_datasetPoint;
	 
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    DefaultComboBoxModel model1 = new DefaultComboBoxModel();
    DefaultComboBoxModel model2 = new DefaultComboBoxModel();
    DefaultComboBoxModel model3 = new DefaultComboBoxModel();

    DatasourceConnectionInfo datasourceConnectionInfo = new 
    		DatasourceConnectionInfo(
    				"C:\\Program Files (x86)\\supermap-iobjectsjava-10.1.2-19530-86195-win-all\\SampleData\\World\\World.udb", "hhh","");


    JPanel panelMain;
    JButton jButtonOpen = new JButton();//打开工作空间
    JButton jButtonPan = new JButton();//漫游地图
    JButton jButtonZoomIn = new JButton();//放大
    JButton jButtonZoomOut = new JButton();//缩小
    JButton jButtonZoomFree = new JButton();//自由缩放
    JButton jButtonViewEntire = new JButton();//全屏显示
    JButton jButtonSelect = new JButton();//选择
    JButton jButtonSelectQuery = new JButton();//图查属性
    JScrollPane jScrollPane1 = new JScrollPane();//滚动面板
    JTable table = new JTable();
    JLabel lblTag = new JLabel();
    JLabel lblTag1 = new JLabel();
    JLabel lblTag2 = new JLabel();
    JLabel lblTag3 = new JLabel();
    JLabel lblTag4 = new JLabel();
    JLabel lblTag0 = new JLabel();
    JLabel lblTag01 = new JLabel();
    JLabel lblTag02 = new JLabel();
    JLabel lblTag03 = new JLabel();
    JLabel lblTag04 = new JLabel();
    JLabel lblTagg = new JLabel();
	
    JButton jButtonSQLQuery = new JButton();//属性查图
    JButton jButtonSQLQuery2 = new JButton();//属性查图
    JTextField txtFilter1 = new JTextField();
    JTextField txtFilter2 = new JTextField();
    JTextField txtFilter3 = new JTextField();
    JTextField txtFilter4 = new JTextField();
    JTextField txtFilter0 = new JTextField();
	JComboBox combo1 = new JComboBox(model); //结果下拉框
    JButton jButtonSQLQuery01 = new JButton();//属性查图1
    JToolBar jToolBar1 = new JToolBar();//工具栏
    JPanel panelSide;    //左侧的面板
    JPanel panelBelow; //下方的面板
    JPanel panelDetail;//下拉框的面板
    JButton m_buttonAnalyst=new JButton();//分析按钮
    JButton m_buttonDelete=new JButton();//删除按钮
    JButton m_buttonAnalyst1=new JButton();//分析按钮
    JButton m_buttonDelete1=new JButton();//删除按钮
    
    JComboBox combo2 = new JComboBox(model1); 
    JComboBox combo3 = new JComboBox(model2); 
    JComboBox combo4 = new JComboBox(model3); 

    
    // 为按钮添加图片
    ImageIcon selectionIcon = new ImageIcon("Resources/action_select.png");
    ImageIcon openIcon = new ImageIcon("Resources/workspace_open.png");
    ImageIcon panIcon = new ImageIcon("Resources/action_pan.png");
    ImageIcon zoominIcon = new ImageIcon("Resources/action_zoomin.png");
    ImageIcon zoomoutIcon = new ImageIcon("Resources/action_zoomout.png");
    ImageIcon zoomfreeIcon = new ImageIcon("Resources/action_zoomfree.png");
    ImageIcon viewentireIcon = new ImageIcon("Resources/action_viewentire.png");
    ImageIcon selectqueryIcon = new ImageIcon("Resources/action_selectquery.png");
    ImageIcon sqlqueryIcon = new ImageIcon("Resources/action_sqlquery.png");

    
    public Frame1() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            initialize();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

   private void initialize() throws Exception {          //初始化
	   
        panelMain = (JPanel) getContentPane();
        panelSide= (JPanel) getContentPane();
        panelDetail=(JPanel) getContentPane();
        this.setSize(new Dimension(710, 350));
        this.setTitle("Our Map");
        this.addWindowListener(new Frame1_this_windowAdapter(this));
        this.addComponentListener(new Frame1_this_componentAdapter(this));
        
        model1.addElement("电子产品店"); 
        model1.addElement("快递站"); 
        model1.addElement("药店"); 
        model1.addElement("电影院"); 
        model1.addElement("饮品"); 
        model1.addElement("小吃"); 
        model1.addElement("超市"); 
        model1.addElement("健身房"); 
        model1.addElement("公交站"); 
        model1.addElement("餐馆"); 
        model1.addElement("住宿"); 
        
        model2.addElement("电子产品店"); 
        model2.addElement("快递站"); 
        model2.addElement("药店"); 
        model2.addElement("电影院"); 
        model2.addElement("饮品"); 
        model2.addElement("小吃"); 
        model2.addElement("超市"); 
        model2.addElement("健身房"); 
        model2.addElement("公交站"); 
        model2.addElement("餐馆"); 
        model2.addElement("住宿"); 
        
        model3.addElement("电子产品店");
        model3.addElement("快递站"); 
        model3.addElement("药店"); 
        model3.addElement("电影院"); 
        model3.addElement("饮品"); 
        model3.addElement("小吃"); 
        model3.addElement("超市"); 
        model3.addElement("健身房"); 
        model3.addElement("公交站"); 
        model3.addElement("餐馆"); 
        model3.addElement("住宿"); 
        
        mapControl.setLayout(null);
        panelMain.setLayout(null);
        panelSide.setBounds(new Rectangle(9, 44, 400, 181));

        mapControl.setBounds(new Rectangle(409, 44, 650, 181));
        
        jButtonOpen.setToolTipText("打开地图");
        jButtonOpen.setIcon(openIcon);
        jButtonOpen.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 打开工作空间及地图
		        openMap();
			}
        });
        
        
        jButtonPan.setToolTipText("漫游");
        jButtonPan.setIcon(panIcon);
        jButtonPan.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				 // 地图漫游
		        mapControl.setAction(Action.PAN);	
			}
        });
        
        jButtonZoomIn.setToolTipText("放大");
        jButtonZoomIn.setIcon(zoominIcon);
        jButtonZoomIn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 地图放大
		        mapControl.setAction(Action.ZOOMIN);
			}
        });
        
        jButtonZoomOut.setToolTipText("缩小");
        jButtonZoomOut.setIcon(zoomoutIcon);
        jButtonZoomOut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 地图缩小
		        mapControl.setAction(Action.ZOOMOUT);
			}
        });
        
        jButtonZoomFree.setToolTipText("自由缩放");
        jButtonZoomFree.setIcon(zoomfreeIcon);
        jButtonZoomFree.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 地图自由缩放
		        mapControl.setAction(Action.ZOOMFREE);
			}
        });
        
        jButtonViewEntire.setToolTipText("全幅显示");
        jButtonViewEntire.setIcon(viewentireIcon);
        jButtonViewEntire.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 地图全幅显示
		        mapControl.getMap().viewEntire();
		        mapControl.getMap().refresh();
			}
        });
        
        jButtonSelect.setToolTipText("选择");
        jButtonSelect.setIcon(selectionIcon);
        jButtonSelect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 选择对象（此处支持选择多个对象）
		        mapControl.setAction(Action.SELECT2);
			}
        });
        
        jButtonSelectQuery.setToolTipText("图查属性");
        jButtonSelectQuery.setIcon(selectqueryIcon);
        jButtonSelectQuery.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行图查属性
				doSelectQuery();
			}
        });
        
        jScrollPane1.setBounds(new Rectangle(8, 226, 650, 82));

        lblTag.setToolTipText("");
        lblTag.setText("路径规划（请至少输入一个目的地）：");
        lblTag1.setToolTipText("");
        lblTag1.setText(" 搜索");
        lblTag2.setToolTipText("");
        lblTag2.setText(" 起点*");
        lblTag3.setToolTipText("");
        lblTag3.setText("目的地1");
        lblTag4.setToolTipText("");
        lblTag4.setText("目的地2");
        lblTag0.setToolTipText("");
        lblTag0.setText("多目标规划：");
        lblTag01.setToolTipText("");
        lblTag01.setText("所在地");
        lblTag02.setToolTipText("");
        lblTag02.setText("目标1");
        lblTag03.setToolTipText("");
        lblTag03.setText("目标2");
        lblTag04.setToolTipText("");
        lblTag04.setText("目标3");
        lblTagg.setToolTipText("");
        lblTagg.setText("具体细节：");
        
        jButtonSQLQuery.setToolTipText("搜索");
        jButtonSQLQuery.setIcon(sqlqueryIcon);
        jButtonSQLQuery2.setToolTipText("搜索");
        jButtonSQLQuery2.setIcon(sqlqueryIcon);
        jButtonSQLQuery01.setToolTipText("确认");
        jButtonSQLQuery01.setIcon(sqlqueryIcon);
        jButtonSQLQuery.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行属性查图
		        
				doSQLQuery();
				doSelectQuery();
				//doSQLQuery1();
			}
        });
        jButtonSQLQuery01.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行属性查图1
		        
				doSQLQuery1();
			}
        });
        jButtonSQLQuery2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行属性查图1
		        
				doSQLQuery2();
				doSelectQuery();
			}
        });

        
        
        m_buttonAnalyst.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行属性查图1
		        
				analyst();
			}
        });
        m_buttonDelete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearResult();
			}
        });
        m_buttonAnalyst1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// 执行属性查图1
		        
				morePointTSP();
			}
        });
        m_buttonDelete1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				clearResult();
			}
        });
//        m_buttonAnalyst1.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// 执行属性查图1
//		        
//				analyst();
//			}
//        });
//        m_buttonDelete1.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//		        
//				analyst();
//			}
//        });
        
        jToolBar1.setBounds(new Rectangle(3, 10, 1000, 27));
        panelDetail.setBounds(new Rectangle(1200, 10, 1000, 30));
        combo1.setBounds(1470, 10, 400, 30);

        jButtonSQLQuery01.setBounds(1880, 10, 30, 30);

        
        txtFilter1.setToolTipText("");                                                                                                   //txtFilter  定位和初始化      
        txtFilter1.setColumns(10);
        txtFilter2.setToolTipText("");                                                                                                   //txtFilter  定位和初始化      
        txtFilter2.setColumns(10);
        txtFilter3.setToolTipText("");                                                                                                   //txtFilter  定位和初始化      
        txtFilter3.setColumns(10);
        txtFilter4.setToolTipText("");                                                                                                   //txtFilter  定位和初始化      
        txtFilter4.setColumns(10);
        txtFilter0.setToolTipText("");                                                                                                   //txtFilter  定位和初始化      
        txtFilter0.setColumns(10);
        m_buttonAnalyst.setText(" 开始规划！");
        m_buttonDelete.setText("删除线路！");
        m_buttonAnalyst1.setText(" 开始规划！");
        m_buttonDelete1.setText("删除线路！");
        
        //PanelSide布局
        lblTag.setBounds(20, 60, 400, 30);
        lblTag1.setBounds(20, 100, 80, 30);
        lblTag2.setBounds(20, 140, 80, 30);
        lblTag3.setBounds(20, 180, 80, 30);
        lblTag4.setBounds(20, 220, 80, 30);
        txtFilter1.setBounds(100, 100, 250, 30); 
        txtFilter2.setBounds(100, 140, 250, 30); 
        txtFilter3.setBounds(100, 180, 250, 30);
        txtFilter4.setBounds(100, 220, 250, 30);
        jButtonSQLQuery.setBounds(360,100,30,30);
        jButtonSQLQuery2.setBounds(360, 420, 30, 30);
        m_buttonAnalyst.setBounds(150, 260, 150, 40);
        m_buttonDelete.setBounds(150, 310, 150, 40);
        
        lblTag0.setBounds(20,380,300,30);
        lblTag01.setBounds(30,420,60,30);
        lblTag02.setBounds(30,460,60,30);
        lblTag03.setBounds(30,500,60,30);
        lblTag04.setBounds(30,540,60,30);
        txtFilter0.setBounds(100, 420, 250, 30);
        combo2.setBounds(100, 460, 250, 30);
        combo3.setBounds(100, 500, 250, 30);
        combo4.setBounds(100, 540, 250, 30);
        m_buttonAnalyst1.setBounds(150, 580, 150, 40);
        m_buttonDelete1.setBounds(150, 630, 150, 40);
        
        panelMain.add(jToolBar1);
        jToolBar1.add(jButtonOpen);
        jToolBar1.add(jButtonPan);
        jToolBar1.add(jButtonZoomIn);
        jToolBar1.add(jButtonZoomOut);
        jToolBar1.add(jButtonZoomFree);
        jToolBar1.add(jButtonViewEntire);
        jToolBar1.add(jButtonSelect);
        jToolBar1.add(lblTagg);
        jToolBar1.add(jButtonSelectQuery);
        panelSide.add(txtFilter1);      
        panelSide.add(txtFilter2); 
        panelSide.add(txtFilter3); 
        panelSide.add(txtFilter4); 
        panelSide.add(txtFilter0); 
        panelSide.add(lblTag);
        panelSide.add(lblTag1);
        panelSide.add(lblTag2);
        panelSide.add(lblTag3);
        panelSide.add(lblTag4);
        panelSide.add(lblTag0);
        panelSide.add(lblTag01);
        panelSide.add(lblTag02);
        panelSide.add(lblTag03);
        panelSide.add(lblTag04);
        panelSide.add(jButtonSQLQuery);
        panelSide.add(jButtonSQLQuery2);
        panelSide.add(m_buttonAnalyst);
        panelSide.add(combo2);
        panelSide.add(combo3);
        panelSide.add(combo4);
        panelSide.add(m_buttonDelete);
        panelDetail.add(combo1);
        panelDetail.add(jButtonSQLQuery01);
        panelMain.add(mapControl, null);
        panelMain.add(jScrollPane1);
        jScrollPane1.getViewport().add(table);
        panelSide.add(m_buttonAnalyst1);
        panelSide.add(m_buttonDelete1);
        
       
    }

    public void openMap() {                                         //打开地图 205-250
        // 设置公用打开对话框
    	//FileDialog 类显示出一个对话框窗口,用户可以从中选择文件
        //因为它是一个模式对话框，当应用调用它的 show 方法来显示对话框时
        //它会阻塞应用的其余部分直到用户选择了一个文件
        FileDialog openFileDialog = new FileDialog(
        				this, "打开工作空间", FileDialog.LOAD);
        
        openFileDialog.setVisible(true);

        // 定义并获得一个工作空间连接信息的实例
        WorkspaceConnectionInfo workspaceConnectionInfo 
        						= new WorkspaceConnectionInfo();

        // 定义连接工作空间的类型为SMWU
        workspaceConnectionInfo.setType(WorkspaceType.SMWU);

        // 设置连接工作空间文件的路径名
        String filename = openFileDialog.getFile();
        String filedir = openFileDialog.getDirectory();
        String fileworkspace = filedir + filename;
        workspaceConnectionInfo.setServer(fileworkspace);

        // 判断是否成功打开工作空间文件
        if ( !workspace.open(workspaceConnectionInfo) ) {
        	JOptionPane.showMessageDialog(this, "打开工作空间失败！");
            return;
        }
        workspace.open(workspaceConnectionInfo);
        // 建立MapControl与Workspace的连接
        mapControl.getMap().setWorkspace(workspace);

        // 判断工作空间中是否有地图，如果有的话获取工作空间中的地图集
        if (workspace.getMaps().getCount() <= 0) {
            JOptionPane.showMessageDialog(this, "工作空间中没有地图");
            return;
        } 
        
        // 通过名称打开工作空间中的地图
        mapControl.getMap().open("西财周边地图");

        //mapControl.getMap().refresh();
        
		m_trackingLayer = mapControl.getMap().getTrackingLayer();                    //获得路网图层 m_trackingLayer
		m_trackingLayer.setAntialias(true);

        //打开地图后，将打开地图按钮设为不可用
        jButtonOpen.setEnabled(false);
        m_datasetLine = (DatasetVector)workspace.getDatasources().get(0)         //得到矢量数据集 m_datasetLine
				.getDatasets().get(m_datasetName);
		

		m_datasetPoint = m_datasetLine.getChildDataset();                                      //子数据集  m_datasetPoint

//初始化各变量
        // Initialzie variables
		m_point2Ds = new Point2Ds();
		m_trackingLayer = mapControl.getMap().getTrackingLayer();                    //获得路网图层 m_trackingLayer
		m_trackingLayer.setAntialias(true);
		m_selectMode = SelectMode.SELECTPOINT;                                                    //选择的模式
		m_count = 0;
		//m_timer = new Timer(200, this);

//加载点数据集及线数据集并设置各自风格                                    // -------------------------------数据可视化          
//把数据集中的点和线画出来
        // Add point, line datasets and set their styles

		m_layerLine = mapControl.getMap().getLayers().add(m_datasetLine,true);  //矢量数据集加到图层里面
		LayerSettingVector lineSetting = (LayerSettingVector)m_layerLine
				.getAdditionalSetting();
		GeoStyle lineStyle = new GeoStyle();
		lineStyle.setLineColor(Color.LIGHT_GRAY);
		lineStyle.setLineWidth(0.1);
		lineSetting.setStyle(lineStyle);

		m_layerPoint = mapControl.getMap().getLayers().add(m_datasetPoint, true);
		LayerSettingVector pointSetting = (LayerSettingVector)m_layerPoint
				.getAdditionalSetting();
		GeoStyle pointStyle = new GeoStyle();
		pointStyle.setLineColor(new Color(180,180,180));
		pointStyle.setMarkerSize(new Size2D(2, 2));
		pointSetting.setStyle(pointStyle);

//调整mapControl的状态
        // Adjust the status of mapControl
		mapControl.setWaitCursorEnabled(false);
		mapControl.getMap().setAntialias(true);
		mapControl.getMap().refresh();
		
		load();
    }

    
    public void doSelectQuery() {                                                   //图查属性
         model.removeAllElements();
        // 定义记录集
        Recordset recordset = null;

        // 获取图层集合
        Layers layers = mapControl.getMap().getLayers();

        // 获取选择集
        Selection selection = null;
        
        for (int i = 0; i < layers.getCount(); i++) {
            if (layers.get(i).getSelection() != null 
            		&& layers.get(i).getSelection().getCount() != 0) {
                selection = layers.get(i).getSelection();
                recordset = selection.toRecordset();
//                while(!recordset.isEOF()) {
//                Object obj= recordset.getFieldValue("name");
//                Point2D mapPoint = recordset.getGeometry().getInnerPoint();
//        		GeoPoint geoPoint = new GeoPoint(mapPoint);
//        		TextPart part = new TextPart();
//        		part.setX(geoPoint.getX());
//        		part.setY(geoPoint.getY());
//        		part.setText("\n"+(String)obj);
//        		
//        		GeoText text = new GeoText(part);
//        		TextStyle textstyle = new TextStyle();
//        		textstyle.setFontHeight(5);
//        		text.setTextStyle(textstyle);
//        		m_trackingLayer.add(text, "text");
//        		recordset.moveNext();
//                }
                
            }
        }

        // 判断选择集是否为空
        if ((selection == null) || (selection.getCount() == 0)) {
            JOptionPane.showMessageDialog(this, "请选择要查询属性的空间对象");
            return;
        }

        // 将选择集转换为记录集
        recordset = selection.toRecordset();
        
        mapControl.getMap().ensureVisible(recordset,1);
        mapControl.getMap().refresh();
        
        // 获取记录集中记录的字段数
        int fieldCount = recordset.getFieldCount();

        // 获取记录集中的记录数
        int recordCount = recordset.getRecordCount();

        // 定义Table
        Object[][] tableValues = new Object[recordCount][fieldCount-6];
        Object[] tableColumns = new Object[fieldCount-6];
        tableColumns[0]="搜索结果匹配";
        // 将字段值添加到jTable控件相应的位置
        for (int i = 0; i < fieldCount-7; i++) {
            tableColumns[i+1] = recordset.getFieldInfos().get(i+7).getName();
        }

        for (int i = 0; i < recordCount; i++) {
            for (int j = 0; j < fieldCount-7; j++) {
                tableValues[i][j+1] = recordset.getFieldValue(j+7);
            }
          	 Object obj= recordset.getFieldValue("name");
           	 model.addElement(obj);                                   //加入comboBox

            recordset.moveNext();
        }
        
        recordset.dispose();
        recordset = null;

        // 将查询到信息加到 Table 中显示
        DefaultTableModel tableModel = new DefaultTableModel(tableValues,tableColumns);
        table.removeAll();
        this.table.setModel(tableModel);
    }
 
    public void doSQLQuery() {                                                //属性查图//关于结果集的输出的问题
    	//mapControl.getMap().refresh();
        // 判断JTextField 的输入内容是否为空
    	//m_trackingLayer.clear();
    	String filter =txtFilter1.getText().trim();                                                                      //txtFilter判断
    	System.out.print(filter);
        if (filter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "查询信息不能为空");
            return;
        }

        // 定义查询条件信息
        QueryParameter queryParameter1 = new QueryParameter();
        queryParameter1.setAttributeFilter("name like "+"'%"+filter+"%'");
        queryParameter1.setCursorType(CursorType.STATIC);                                  //queryParameter
        
        // 定义图层个数，判断当前地图窗口中是否有打开的图层
        int queryCount = mapControl.getMap().getLayers().getCount();
        if (queryCount == 0) {
            JOptionPane.showMessageDialog(this, "请先打开一个矢量数据集!");
            return;
        }

        // 遍历每一个图层，实现多图层查询
        int flag = 0;
        for (int i = 0; i < queryCount; i++) {
            // 得到矢量和栅格数据集并强制转换为矢量数据集类型
			Dataset dataset = mapControl.getMap().getLayers().get(i).getDataset();
			if (dataset.getType() == DatasetType.IMAGE) {continue;}
            DatasetVector datasetvector=(DatasetVector)dataset;
            if (datasetvector == null) {
                continue;
            }

            // 打开矢量数据集，才可进行查询
            datasetvector.open();

            // 通过查询条件对矢量数据集进行查询,从数据集中查询出属性数据
            Recordset recordset1 = datasetvector.query(queryParameter1);       //queryParameter
           
            // 查询结果不为空则给 flag 赋值为1
            if (recordset1.getRecordCount() > 0) {
            	 flag = 1;
	         // 把查询得到的数据加入到选择集中(使其高亮显示)
            	 Selection selection = mapControl.getMap()
    					.getLayers().get(i).getSelection();
                selection.fromRecordset(recordset1);
                
                
                
                //放大和定位结果集
            	Layer m_layer = mapControl.getMap().getLayers().add(datasetvector,true);
    			m_layer.setSelectable(true);	
                mapControl.getMap().ensureVisible(recordset1,1);
                mapControl.getMap().refresh();

                recordset1.dispose();
                recordset1 = null;
                //dataset.refresh();
                
            }
        }
        // 判断记录集是否为空
        if (flag == 0) {
            JOptionPane.showMessageDialog(
            		this, "查询得到的记录集为空或者没有满足查询条件的记录!");
        }
        
        // 刷新地图窗口显示
        //mapControl.getMap().refresh();

        // 当可创建对象使用完毕后，使用 dispose 方法来释放所占用的内部资源。
        queryParameter1.dispose();
    }
    public void doSQLQuery2() {                                                //属性查图//关于结果集的输出的问题
    	//mapControl.getMap().refresh();
        // 判断JTextField 的输入内容是否为空
    	String filter =txtFilter0.getText().trim();                                                                      //txtFilter判断
    	System.out.print(filter);
        if (filter.isEmpty()) {
            JOptionPane.showMessageDialog(this, "查询信息不能为空");
            return;
        }

        // 定义查询条件信息
        QueryParameter queryParameter1 = new QueryParameter();
        queryParameter1.setAttributeFilter("name like "+"'%"+filter+"%'");
        queryParameter1.setCursorType(CursorType.STATIC);                                  //queryParameter
        
        // 定义图层个数，判断当前地图窗口中是否有打开的图层
        int queryCount = mapControl.getMap().getLayers().getCount();
        if (queryCount == 0) {
            JOptionPane.showMessageDialog(this, "请先打开一个矢量数据集!");
            return;
        }

        // 遍历每一个图层，实现多图层查询
        int flag = 0;
        for (int i = 0; i < queryCount; i++) {
            // 得到矢量和栅格数据集并强制转换为矢量数据集类型
			Dataset dataset = mapControl.getMap().getLayers().get(i).getDataset();
			if (dataset.getType() == DatasetType.IMAGE) {continue;}
            DatasetVector datasetvector=(DatasetVector)dataset;
            if (datasetvector == null) {
                continue;
            }

            // 打开矢量数据集，才可进行查询
            datasetvector.open();

            // 通过查询条件对矢量数据集进行查询,从数据集中查询出属性数据
            Recordset recordset1 = datasetvector.query(queryParameter1);       //queryParameter
           
            // 查询结果不为空则给 flag 赋值为1
            if (recordset1.getRecordCount() > 0) {
            	 flag = 1;
	         // 把查询得到的数据加入到选择集中(使其高亮显示)
            	 Selection selection = mapControl.getMap()
    					.getLayers().get(i).getSelection();
                selection.fromRecordset(recordset1);
                
                
                
                //放大和定位结果集
            	Layer m_layer = mapControl.getMap().getLayers().add(datasetvector,true);
    			m_layer.setSelectable(true);	
                mapControl.getMap().ensureVisible(recordset1,1);
                mapControl.getMap().refresh();

                recordset1.dispose();
                recordset1 = null;
                //dataset.refresh();
                
            }
        }
        // 判断记录集是否为空
        if (flag == 0) {
            JOptionPane.showMessageDialog(
            		this, "查询得到的记录集为空或者没有满足查询条件的记录!");
        }
        
        // 刷新地图窗口显示
        //mapControl.getMap().refresh();

        // 当可创建对象使用完毕后，使用 dispose 方法来释放所占用的内部资源。
        queryParameter1.dispose();
    }
    public void doSQLQuery1() { 
    	       //selection.clear();
    	        String filter =combo1.getSelectedItem().toString();                                                                      //txtFilter判断
    	        if (filter.isEmpty()) {
    	            JOptionPane.showMessageDialog(this, "查询信息不能为空");
    	            return;
    	        }

    	        // 定义查询条件信息
    	        QueryParameter queryParameter1 = new QueryParameter();
    	        queryParameter1.setAttributeFilter("name like "+"'%"+filter+"%'");
    	        queryParameter1.setCursorType(CursorType.STATIC);                                  //queryParameter
    	        
    	        // 定义图层个数，判断当前地图窗口中是否有打开的图层
    	        int queryCount = mapControl.getMap().getLayers().getCount();
    	        if (queryCount == 0) {
    	            JOptionPane.showMessageDialog(this, "请先打开一个矢量数据集!");
    	            return;
    	        }
                //建立面矢量数据集，用于存储缓冲区分析的结果
    	        Datasource targetDatasource = workspace.getDatasources().open(datasourceConnectionInfo);
                String resultDatasetName = targetDatasource.getDatasets().getAvailableDatasetName( "resultDatasetBuffer");
               DatasetVectorInfo datasetvectorInfo = new DatasetVectorInfo();
               datasetvectorInfo.setType(DatasetType.REGION);
               datasetvectorInfo.setName(resultDatasetName);
               datasetvectorInfo.setEncodeType(EncodeType.NONE);
               DatasetVector resultDatasetBuffer = targetDatasource.getDatasets().create(datasetvectorInfo);
               
    	        // 遍历每一个图层，实现多图层查询
    	        int flag = 0;
    	        int flagg=0;
    	        for (int i = 0; i < queryCount; i++) {
    	            // 得到矢量和栅格数据集并强制转换为矢量数据集类型
    				Dataset dataset = mapControl.getMap().getLayers().get(i).getDataset();
    				if (dataset.getType() == DatasetType.IMAGE) {continue;}
    	            DatasetVector datasetvector=(DatasetVector)dataset;
    	            if (datasetvector == null) {
    	                continue;
    	            }

    	            // 打开矢量数据集，才可进行查询
    	            datasetvector.open();

    	            // 通过查询条件对矢量数据集进行查询,从数据集中查询出属性数据
    	            Recordset recordset1 = datasetvector.query(queryParameter1);       //queryParameter

    	            // 查询结果不为空则给 flag 赋值为1
    	            if (recordset1.getRecordCount() > 0&&flagg==0) {
    	            	flag = 1;

    	                //获得他的x和y 在路上标出来
    	            	 /*double x= (double)recordset1.getObject("X");
    	            	 double y= (double)recordset1.getObject("Y");
    	            	 Point2D mapPoint=new Point2D(x,y);*/
    	            	 Point2D mapPoint = recordset1.getGeometry().getInnerPoint();
    	            	
    	            	 //addPoint(mapPoint);
    	            	 addP(mapPoint);

    	            	 
    	    			Object obj= recordset1.getFieldValue("name");
    	    			
    	    			GeoPoint geoPoint = new GeoPoint(mapPoint);
    	    			TextPart part = new TextPart();
    	    			part.setX(geoPoint.getX());
    	    			part.setY(geoPoint.getY());
    	    			part.setText("\n"+(String)obj);
    	    			
    	    			GeoText text = new GeoText(part);
    	    			TextStyle textstyle = new TextStyle();
    	    			textstyle.setFontHeight(5);
    	    			text.setTextStyle(textstyle);
    	    			m_trackingLayer.add(text, "text");
    	    			
    	    			System.out.print(m_point2Ds.getCount());
    	    			
   	            	     if(m_point2Ds.getCount()==1) {
   	            	    	txtFilter2.setText((String)obj);                       //返回让选中的地点填充回去
   	            	        addPoint0(mapPoint);
   	            	     }
   	            	     if(m_point2Ds.getCount()==2) {
   	            	    	txtFilter3.setText((String)obj);       
   	            	         addPoint1(mapPoint);
   	            	     }
   	            	     if(m_point2Ds.getCount()==3) {
   	            	    	txtFilter4.setText((String)obj);     
   	            	         addPoint2(mapPoint);
   	            	     }
   	            	     if(m_point2Ds.getCount()==4) {
   	            	    	txtFilter0.setText((String)obj);     
   	            	        m_point2Ds = new Point2Ds();
   	            	        addPoint(mapPoint);
   	            	     }
    	                //放大和定位结果集
    	            	Layer m_layer = mapControl.getMap().getLayers().add(datasetvector,true);
    	    			m_layer.setSelectable(true);	
    	                mapControl.getMap().ensureVisible(recordset1,1);
    	                mapControl.getMap().refresh();

    	                bufferAnalystForRecordset(recordset1,resultDatasetBuffer);
    	                Recordset recordset = resultDatasetBuffer.getRecordset(false,CursorType.DYNAMIC);
    	                flagg=1;
    	                
    	    			//设置查询参数
    	    			//Set the query parameter.
    	    			QueryParameter parameter = new QueryParameter();
    	    			parameter.setSpatialQueryObject(recordset);
    	    			parameter.setSpatialQueryMode(SpatialQueryMode.CONTAIN);

    	    			//对指定查询的图层进行查询
    	    			//Query the specified layer.
    	    		    Selection selection = null;
    	    			Layer layer = mapControl.getMap().getLayers().get(i);
    	    			DatasetVector dataset1 = (DatasetVector) layer.getDataset();
    	    			Recordset recordsett = dataset1.query(parameter);
    	                 selection= mapControl.getMap()
    	     					.getLayers().get(i).getSelection();
    	                selection.fromRecordset(recordsett);
    	   // 获取记录集中记录的字段数
    	    	        int fieldCount = recordsett.getFieldCount();

    	    	        // 获取记录集中的记录数
    	    	        int recordCount = recordsett.getRecordCount();

    	    	        // 定义Table
    	    	        Object[][] tableValues = new Object[recordCount][fieldCount-6];
    	    	        Object[] tableColumns = new Object[fieldCount-6];
    	    	        tableColumns[0]="周边500m同类店铺";
    	    	        // 将字段值添加到jTable控件相应的位置
    	    	        for (int m = 0; m < fieldCount-7; m++) {
    	    	            tableColumns[m+1] = recordsett.getFieldInfos().get(m+7).getName();
    	    	        }

    	    	        for (int m = 0; m < recordCount; m++) {
    	    	            for (int j = 0; j < fieldCount-7; j++) {
    	    	                tableValues[m][j+1] = recordsett.getFieldValue(j+7);
    	    	            }
    	    	            recordsett.moveNext();
    	    	        }
    	    	        
    	    	        recordsett.dispose();
    	    	        recordsett = null;

    	    	        // 将查询到信息加到 Table 中显示
    	    	        DefaultTableModel tableModel = new DefaultTableModel(tableValues,
    	    	                tableColumns);
    	    	        
    	    	        this.table.setModel(tableModel);
    	    //结束
    	    	        
    	                recordset1.dispose();
    	                recordset1 = null;
    	            }
    	        }
    	        // 判断记录集是否为空
    	        if (flag == 0) {
    	            JOptionPane.showMessageDialog(
    	            		this, "查询得到的记录集为空或者没有满足查询条件的记录!");
    	        }
    	        
    	        // 刷新地图窗口显示
    	        //mapControl.getMap().refresh();

    	        // 当可创建对象使用完毕后，使用 dispose 方法来释放所占用的内部资源。
    	        queryParameter1.dispose();
    	        
    	        targetDatasource.close();
    	    }

    
    public void bufferAnalystForRecordset(Recordset recordsetLine, DatasetVector resultDatasetBuffer){
                // 设置缓冲区分析参数
                BufferAnalystParameter bufferAnalystParam = new BufferAnalystParameter();
                bufferAnalystParam.setEndType(BufferEndType.ROUND);
                bufferAnalystParam.setLeftDistance(500);
                bufferAnalystParam.setRightDistance(500);

    // 调用为记录集建立缓冲区的方法
    BufferAnalyst.createBuffer(recordsetLine, resultDatasetBuffer, bufferAnalystParam, false,true);
   }

    public void addPoint(Point2D mapPoint)                               //----------------------------------------------------------添加站点
	{
		try {
			m_point2Ds.add(mapPoint);

			// 添加点
            // Add points
			GeoPoint geoPoint = new GeoPoint(mapPoint);
			GeoStyle style = new GeoStyle();
			style.setMarkerSize(new Size2D(8, 8));
			style.setLineColor(Color.GREEN);
			geoPoint.setStyle(style);
			m_trackingLayer.add(geoPoint, "point2D");

			// 添加文本
            // Add text
			TextPart part = new TextPart();
			part.setX(geoPoint.getX());
			part.setY(geoPoint.getY());
			part.setText(String.valueOf(m_point2Ds.getCount()));
			
			GeoText text = new GeoText(part);
			m_trackingLayer.add(text, "text");
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
    
    public boolean analyst()           //----------------------------分析参数的设置
	{
		try
		{
			TransportationAnalystParameter parameter = new TransportationAnalystParameter();

			parameter.setWeightName("smlength");

			// 设置旅行商分析的返回对象  即分析结果
            // Set return objects of TSP
			parameter.setPoints(m_point2Ds);
			parameter.setNodesReturn(true);
			parameter.setEdgesReturn(true);
			parameter.setPathGuidesReturn(true);
			parameter.setStopIndexesReturn(true);
			parameter.setRoutesReturn(true);

			// 进行分析并显示结果
            // Analyze
			m_result = m_analyst.findTSPPath(parameter, true);
			if (m_result == null)
			{
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {
					JOptionPane.showMessageDialog(null, "分析失败");
		        }
				else
				{
					JOptionPane.showMessageDialog(null, "Failed to analyze");
				}	
				return false;
			}
			showResult();
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//System.out.println(e.getMessage());
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {
				JOptionPane.showMessageDialog(null, "分析失败");
	        }
			else
			{
				JOptionPane.showMessageDialog(null, "Failed to analyze");
			}	
			return false;
		}
	}

	/**
	 * 显示结果
     * Show result
	 */
	public void showResult()                                                          //--------------------------------TSP分析结果的可视化
	{

		for (int i = 0; i < m_result.getRoutes().length; i++)
		{
			GeoLineM geoLineM = m_result.getRoutes()[i];
			GeoStyle style = new GeoStyle();
			style.setLineColor(Color.BLUE);
			style.setLineWidth(1);
			geoLineM.setStyle(style);
			m_trackingLayer.add(geoLineM, "result");
			m_pointMs = geoLineM.getPart(0);
		}
		mapControl.getMap().refresh();
		fillResultTable();
	}
	public void load()     //-----------------------配置网络分析的环境
	{
		try
		{
			// 设置网络分析基本环境，这一步骤需要设置　分析权重、节点、弧段标识字段、容限
            // Set the basic environment of network analysis, including weight, node, edge, tolerance.
			TransportationAnalystSetting setting = new TransportationAnalystSetting();
			setting.setNetworkDataset(m_datasetLine);
			setting.setEdgeIDField(m_edgeID);
			setting.setNodeIDField(m_nodeID);
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {
				setting.setEdgeNameField("roadName");
	        }
			else
			{
				setting.setEdgeNameField("roadName_en");
			}	
			setting.setTolerance(89);

			WeightFieldInfos weightFieldInfos = new WeightFieldInfos();
			WeightFieldInfo weightFieldInfo = new WeightFieldInfo();
			weightFieldInfo.setFTWeightField("smLength");
			weightFieldInfo.setTFWeightField("smLength");
			weightFieldInfo.setName("smlength");
			weightFieldInfos.add(weightFieldInfo);
			setting.setWeightFieldInfos(weightFieldInfos);
			setting.setFNodeIDField("SmFNode");
			setting.setTNodeIDField("SmTNode");

			// 构造交通网络分析对象，加载环境设置对象
            // Build the TransportationAnalyst object , and add environment setting object
			m_analyst = new TransportationAnalyst();
			m_analyst.setAnalystSetting(setting);
			m_analyst.load();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	public enum SelectMode
	{
		SELECTPOINT, SELECTBARRIER, NONE
	}
	/**
	 * 清除结果
     * Clear results
	 */
	public void clearResult()
	{
		// 清除跟踪图层上的对象及结果表
        // Clear objects on the tracking layer and result table
		m_trackingLayer.clear();
		m_count = 0;
		mapControl.getMap().refresh();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		while (model.getRowCount() != 0)
		{
			model.removeRow(0);
		}
		//m_point2Ds = new Point2Ds();
	}

	public void fillResultTable()
	{
		try
		{
			// 清除原数据，添加初始点信息
            // Clear original data and add start point information
		    String[] col= {"","导引路线","花费","距离"};
		    DefaultTableModel model0 =new DefaultTableModel(col,10000);
		    table.setModel(model0);
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			while (model.getRowCount() != 0)
			{
				model.removeRow(0);
			}
			Object[] objs = new Object[4];
			objs[0] = model.getRowCount() + 1;
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {
				objs[1] = "从起始点出发";
	        }
			else
			{
				objs[1] = "Start";
			}	
			objs[2] = "--";
			objs[3] = "--";
			model.addRow(objs);

			// 得到行驶导引对象，根据导引子项类型的不同进行不同的填充
            // Get the PathGuide object, and make different fill according to different items
			PathGuide[] pathGuides = m_result.getPathGuides();
			for (int i = 0; i < pathGuides.length; i++)
			{
				PathGuide pathGuide = pathGuides[i];

				for (int j = 1; j < pathGuide.getCount(); j++)
				{
					PathGuideItem item = pathGuide.get(j);
					objs[0] = model.getRowCount() + 1;
					if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
			        {
						
	   // If the item is a stop
						if (item.isStop())
						{
							String side = "无";
							if (item.getSideType() == SideType.LEFT)
								side = "左侧";
							if (item.getSideType() == SideType.RIGHT)
								side = "右侧";
							if (item.getSideType() == SideType.MIDDLE)
								side = "上";
							String dis = NumberFormat.getInstance().format(
									item.getDistance());
							if (item.getIndex() == -1 && item.getID() == -1)
							{
								continue;
							}
							if (j != pathGuide.getCount() - 1)
							{
								objs[1] = "到达[" + item.getIndex() + "号路由点],在道路" + side+" 约"
										+ (int)Double.parseDouble(dis)+"米处";
							}
							else
							{
								objs[1] = "到达终点,在道路" + side +" 约"
										+ (int)Double.parseDouble(dis)+"米处";
								//System.out.print(objs[1]);
							}
							objs[2] = "";
							objs[3] = "";
							model.addRow(objs);
						}

// If the item is a edge
						if (item.isEdge())
						{
							String direct = "无方向";
							if (item.getDirectionType() == DirectionType.EAST)
								direct = "东";
							if (item.getDirectionType() == DirectionType.WEST)
								direct = "西";
							if (item.getDirectionType() == DirectionType.SOUTH)
								direct = "南";
							if (item.getDirectionType() == DirectionType.NORTH)
								direct = "北";
							String weight = NumberFormat.getInstance().format(
									item.getWeight());
							String roadName = item.getName();
							if (weight.equals("0") && roadName.equals(""))
							{
								objs[1] = "朝" + direct + "行走 约"+ (int)item.getLength()+"米";
								objs[2] = (int)Double.parseDouble(weight);
								objs[3] = (int) item.getLength()+"米";
								model.addRow(objs);
							}
							else
							{
								String roadString = roadName.equals("") ? "匿名路段" : roadName;
								objs[1] = "沿着[" + roadString + "],朝" + direct + "行走 约"+ (int)item.getLength()+"米";
								objs[2] = (int)Double.parseDouble(weight);
								objs[3] =(int) item.getLength()+"米";
								model.addRow(objs);
							}
						}
}
					
					
					else
					{
						// If the item is a stop
                        if (item.isStop())
                        {
                              String side = "None";
                              if (item.getSideType() == SideType.LEFT)
                                    side = "Left";
                              if (item.getSideType() == SideType.RIGHT)
                                    side = "Right";
                              if (item.getSideType() == SideType.MIDDLE)
                                    side = "On the road";
                              String dis = NumberFormat.getInstance().format(
                                          item.getDistance());
                              if (item.getIndex() == -1 && item.getID() == -1)
                              {
                                    continue;
                              }
                              if (j != pathGuide.getCount() - 1)
                              {
                                    objs[1] = "Arrive at [" + item.getIndex() + " route], on the " + side
                                                + dis;
                              }
                              else
                              {
                                    objs[1] = "Arrive at destination " + side + dis;
                              }
                              objs[2] = "";
                              objs[3] = "";
                              model.addRow(objs);
                        }
                        // If the item is a edge
                        if (item.isEdge())
                        {
                              String direct = "No direction";
                              if (item.getDirectionType() == DirectionType.EAST)
                                    direct = "East";
                              if (item.getDirectionType() == DirectionType.WEST)
                                    direct = "West";
                              if (item.getDirectionType() == DirectionType.SOUTH)
                                    direct = "South";
                              if (item.getDirectionType() == DirectionType.NORTH)
                                    direct = "North";
                              String weight = NumberFormat.getInstance().format(
                                          item.getWeight());
                              String roadName = item.getName();
                              if (weight.equals("0") && roadName.equals(""))
                              {
                                    objs[1] = "Go " + direct + " " + item.getLength();
                                    objs[2] = weight;
                                    objs[3] = item.getLength();
                                    model.addRow(objs);
                              }
                              else
                              {
                                    String roadString = roadName.equals("") ? "Anonymous road" : roadName;
                                    objs[1] = "Go along with [" + roadString + "], " + direct + " direction"
                                                + item.getLength();
                                    objs[2] = weight;
                                    objs[3] = item.getLength();
                                    model.addRow(objs);
                              }
                        }
					}					
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean morePointTSP() {
		TransportationAnalystParameter parameterTSP = new TransportationAnalystParameter();
		
        double min = 100000000000000000.0;
        
        int sw = 0;
        
		Point2D mapPoint11 = new Point2D();
		Point2D mapPoint22 = new Point2D();
		Point2D mapPoint33 = new Point2D();
		Recordset recordset11 = null ;
		Recordset recordset22 = null ;
		Recordset recordset33 = null ;
		
		//String m = "书店", n = "药店", h = "电影院";
		String m =combo2.getSelectedItem().toString();
		String n =combo3.getSelectedItem().toString();
		String h =combo4.getSelectedItem().toString();
		String str;
		DatasetVector Stop1, Stop2, Stop3; 
		Datasource m_datasource = workspace.getDatasources().get("地图信息");
		Stop1 = (DatasetVector) m_datasource.getDatasets().get(m);
		Stop2 = (DatasetVector) m_datasource.getDatasets().get(n);
		Stop3 = (DatasetVector) m_datasource.getDatasets().get(h);
		QueryParameter queryParameter = new QueryParameter();
        queryParameter.setAttributeFilter("Name like "+"'%'");
        queryParameter.setCursorType(CursorType.STATIC);     
        Recordset recordset1 = Stop1.query(queryParameter); 
        Recordset recordset2 = Stop2.query(queryParameter); 
        Recordset recordset3 = Stop3.query(queryParameter); 

        int recordCount1 = recordset1.getRecordCount();
        int recordCount2 = recordset2.getRecordCount();
        int recordCount3 = recordset3.getRecordCount();

        
      for (int i = 0; i < recordCount1-1; i++) {
    	//Point2D mapPoint1 = new Point2D(a[i],b[i]);
    	  Point2D mapPoint1 = recordset1.getGeometry().getInnerPoint();
    	  
//			Object obj1= recordset1.getFieldValue("name");
//			GeoPoint geoPoint1 = new GeoPoint(mapPoint1);
//			TextPart part1 = new TextPart();
//			part1.setX(geoPoint1.getX());
//			part1.setY(geoPoint1.getY());
//			part1.setText((String)obj1);
//			GeoText text1 = new GeoText(part1);
//			m_trackingLayer.add(text1, "text");
//			
    	  recordset1.moveNext();
		
		if (mapControl.getMap().getBounds().contains(mapPoint1)) {
		    addP(mapPoint1);
		
		  for (int r = 0; r < recordCount2; r++) {
		    //Point2D mapPoint2 = new Point2D(c[r],d[r]);
			  Point2D mapPoint2 = recordset2.getGeometry().getInnerPoint();
//			  
//				Object obj2= recordset2.getFieldValue("name");
//    			GeoPoint geoPoint2 = new GeoPoint(mapPoint2);
//    			TextPart part2 = new TextPart();
//    			part2.setX(geoPoint2.getX());
//    			part2.setY(geoPoint2.getY());
//    			part2.setText((String)obj2);
//    			GeoText text2 = new GeoText(part2);
//    			m_trackingLayer.add(text2, "text");
    			
	    	  recordset2.moveNext();
			  
			if (mapControl.getMap().getBounds().contains(mapPoint2)) {
				addP(mapPoint2);
			
			for (int q = 0; q < recordCount3; q++) {
				//Point2D mapPoint3 = new Point2D(e[q],f[q]);
				Point2D mapPoint3 = recordset3.getGeometry().getInnerPoint();
				
//				Object obj3= recordset3.getFieldValue("name");
//    			GeoPoint geoPoint3 = new GeoPoint(mapPoint3);
//    			TextPart part3 = new TextPart();
//    			part3.setX(geoPoint3.getX());
//    			part3.setY(geoPoint3.getY());
//    			part3.setText((String)obj3);
//    			GeoText text3 = new GeoText(part3);
//    			m_trackingLayer.add(text3, "text");
    			
		    	recordset3.moveNext();
				
				if (mapControl.getMap().getBounds().contains(mapPoint3)) {
					addP(mapPoint3);	
				
				try
				{
					
					TransportationAnalystParameter parameter = new TransportationAnalystParameter();
					//  构造一个新的 TransportationAnalystParameter 对象。
					
					parameter.setWeightName("smlength");  //  设置权值字段信息的名称

					// 设置旅行商分析的返回对象  即分析结果
		            // Set return objects of TSP
					
					parameter.setPoints(m_point2Ds);  //  设置分析时途经点的集合
					parameter.setNodesReturn(true);  //  设置分析结果中是否包含结点的集合
					parameter.setEdgesReturn(true);  //  设置分析结果中是否包含途经弧段的集合
					parameter.setPathGuidesReturn(true);  //  设置分析结果中是否包含行驶导引集合
					parameter.setStopIndexesReturn(true);  //  设置分析结果中是否要包含站点索引的集合
					parameter.setRoutesReturn(true);  //  设置分析结果中是否包含路由（GeoLineM）对象的集合
					
					// 进行分析并显示结果
		            // Analyze
					
					m_result = m_analyst.findTSPPath(parameter, true); //  是否指定终点	
					
					if (m_result == null) {	
						m_point2Ds.remove(3);
						sw ++;
						System.out.println(sw);
						continue;
						}
					sw ++;
					System.out.println(sw);
					
					double sum = 0;
					
					for (int s = 0; s < m_result.getWeights().length; s++)
						sum = sum + m_result.getWeights()[s];
					
						if(min >= sum) {
							   min = sum;
							   parameterTSP = parameter;
							   mapPoint11 = mapPoint1;
							   mapPoint22 = mapPoint2;
							   mapPoint33 = mapPoint3;
							   recordset11 = recordset1;
							   recordset22 = recordset2;
							   recordset33 = recordset3;
						} 
					m_point2Ds.remove(3);
				}
				
				
				catch (Exception u)
				{    
				    u.printStackTrace();
					JOptionPane.showMessageDialog(null, "分析失败");
					return false;
				}		
			  }
			}
			recordset3.moveFirst();
			m_point2Ds.remove(2);
           }
		 }
		  recordset2.moveFirst();
		  m_point2Ds.remove(1);
        }
      }
	  	
		addPoint1(mapPoint11);
		addPoint2(mapPoint22);
		addPoint3(mapPoint33);
		
		TextStyle textstyle = new TextStyle();
		textstyle.setFontHeight(5);
		
		Object obj1= recordset11.getFieldValue("name");
		GeoPoint geoPoint1 = new GeoPoint(mapPoint11);
		TextPart part1 = new TextPart();
		part1.setX(geoPoint1.getX());
		part1.setY(geoPoint1.getY());
		part1.setText("\n"+(String)obj1);
		GeoText text1 = new GeoText(part1);
		text1.setTextStyle(textstyle);
		m_trackingLayer.add(text1, "text");
		
		Object obj2= recordset22.getFieldValue("name");
		GeoPoint geoPoint2 = new GeoPoint(mapPoint22);
		TextPart part2 = new TextPart();
		part2.setX(geoPoint2.getX());
		part2.setY(geoPoint2.getY());
		part2.setText("\n"+(String)obj2);
		GeoText text2 = new GeoText(part2);
		text2.setTextStyle(textstyle);
		m_trackingLayer.add(text2, "text");
		
		Object obj3= recordset33.getFieldValue("name");
		GeoPoint geoPoint3 = new GeoPoint(mapPoint33);
		TextPart part3 = new TextPart();
		part3.setX(geoPoint3.getX());
		part3.setY(geoPoint3.getY());
		part3.setText("\n"+(String)obj3);
		GeoText text3 = new GeoText(part3);
		text3.setTextStyle(textstyle);
		m_trackingLayer.add(text3, "text");
		
        m_result = m_analyst.findTSPPath(parameterTSP, true);
        
        if (m_result == null)
			{   
				JOptionPane.showMessageDialog(null, "分析失败");
				return false;
			}
		showResult();
		return true;
	}
	public void addPoint0(Point2D mapPoint)       //加点到地图上
	{
		try {
			// 添加点
            // Add points
			GeoPoint geoPoint = new GeoPoint(mapPoint);
			GeoStyle style = new GeoStyle();
			style.setMarkerSize(new Size2D(10, 10));
			style.setLineColor(Color.GREEN);
			geoPoint.setStyle(style);
			m_trackingLayer.add(geoPoint, "point2D");
			
			// 添加文本
            // Add text
			TextPart part = new TextPart();
			part.setX(geoPoint.getX());
			part.setY(geoPoint.getY());
			part.setText("1");
			GeoText text = new GeoText(part);
			m_trackingLayer.add(text, "text");
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void addPoint1(Point2D mapPoint)       //加点到地图上
	{
		try {
			// 添加点
            // Add points
			GeoPoint geoPoint = new GeoPoint(mapPoint);
			GeoStyle style = new GeoStyle();
			style.setMarkerSize(new Size2D(8, 8));
			style.setLineColor(Color.GREEN);
			geoPoint.setStyle(style);
			m_trackingLayer.add(geoPoint, "point2D");
			
			// 添加文本
            // Add text
			TextPart part = new TextPart();
			part.setX(geoPoint.getX());
			part.setY(geoPoint.getY());
			part.setText("2");
			GeoText text = new GeoText(part);
			m_trackingLayer.add(text, "text");
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void addPoint2(Point2D mapPoint)       //加点到地图上
	{
		try {
			// 添加点
            // Add points
			GeoPoint geoPoint = new GeoPoint(mapPoint);
			GeoStyle style = new GeoStyle();
			style.setMarkerSize(new Size2D(8, 8));
			style.setLineColor(Color.ORANGE);
			geoPoint.setStyle(style);
			m_trackingLayer.add(geoPoint, "point2D");
			
			// 添加文本
            // Add text
			TextPart part = new TextPart();
			part.setX(geoPoint.getX());
			part.setY(geoPoint.getY());
			part.setText("3");
			GeoText text = new GeoText(part);
			m_trackingLayer.add(text, "text");
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void addPoint3(Point2D mapPoint)       //加点到地图上
	{
		try {
			// 添加点
            // Add points
			GeoPoint geoPoint = new GeoPoint(mapPoint);
			GeoStyle style = new GeoStyle();
			style.setMarkerSize(new Size2D(8, 8));
			style.setLineColor(Color.RED);
			geoPoint.setStyle(style);
			m_trackingLayer.add(geoPoint, "point2D");
			
			// 添加文本
            // Add text
			TextPart part = new TextPart();
			part.setX(geoPoint.getX());
			part.setY(geoPoint.getY());
			part.setText("4");
			GeoText text = new GeoText(part);
			m_trackingLayer.add(text, "text");
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/**
	 * 添加站点
     * Add stops
	 */
	public void addP(Point2D mapPoint)                               //----------------------------------------------------------添加站点
	{
		try {
		    m_point2Ds.add(mapPoint);
		    
		} catch(Exception ex) {
			ex.printStackTrace();
			//System.out.println("HIOGOPH");
		}
	}


	// 设置地图及属性表的大小随窗体变化而改变
    public void this_componentResized(ComponentEvent e) {
        mapControl.setSize(this.getWidth() - 25,
                            7 * (int)this.getHeight() / 12);

        jScrollPane1.setLocation(10, 7 * (int)this.getHeight() / 12 + 46);
        jScrollPane1.setSize(this.getWidth() - 25,
                             (int) 5 * (int)this.getHeight() / 12 - 120);
        table.setLocation(10,
                          7 * (int)this.getHeight() / 12 + 46);
        table.setSize(this.getWidth() - 25,
                      (int)this.getHeight() / 2 - 150);
    }

    public void this_windowClosing(WindowEvent e) {
        if (mapControl != null) {
            mapControl.dispose();
            mapControl = null;
        }
        if (workspace != null) {
            workspace.dispose();
            workspace = null;
        }
    }
}


class Frame1_this_componentAdapter extends ComponentAdapter {
    private Frame1 adaptee;
    Frame1_this_componentAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void componentResized(ComponentEvent e) {
        adaptee.this_componentResized(e);
    }
}

class Frame1_this_windowAdapter extends WindowAdapter {
    private Frame1 adaptee;
    Frame1_this_windowAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosing(WindowEvent e) {
        adaptee.this_windowClosing(e);
    }

}




