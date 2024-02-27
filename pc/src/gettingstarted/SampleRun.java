package gettingstarted;

import java.awt.*;

import java.awt.event.*;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.supermap.analyst.trafficanalyst.*;
import com.supermap.data.*;
import com.supermap.mapping.*;
import com.supermap.ui.*;

/**
 * <p>
 * Title:公交分析
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------版权声明----------------------------
 * 此文件为SuperMap Objects Java 的示范代码 
 * 版权所有：北京超图软件股份有限公司
 * ----------------------------------------------------------------
 * ---------------------SuperMap iObjects Java 示范程序说明------------------------
 *	1、范例简介：示范如何进行公交换乘分析、根据站点查找线路以及根据线路查找站点
 *	2、示例数据：安装目录\SampleData\City\Changchun.smwu 
 *	3、关键类型/成员: 
 *      Workspace.open 方法
 *		MapControl.MouseEvent 事件 
 *		TrackingLayer.add 方法 
 *		TrackingLayer.remove 方法
 *		TrackingLayer.getTag 方法 
 *		TrackingLayer.indexOf 方法 
 *		LineSetting.setDataset 方法
 *		LineSetting.setLineIDField 方法 
 *		LineSetting.setNameField 方法
 *		LineSetting.setLengthField 方法 
 *		LineSetting.setFareFieldInfo 方法
 *		FareFieldInfo.setFareTypeField 方法 
 *		FareFieldInfo.setStartFareField 方法
 *		FareFieldInfo.setStartFareRangeField 方法 
 *		FareFieldInfo.setFareStepField 方法
 *		FareFieldInfo.setFareStepRangeField 方法 
 *		StopSetting.setDataset 方法 
 *		StopSetting.setStopIDField 方法
 *		StopSetting.setNameField 方法 
 *		RelationSetting.setDataset 方法
 *		RelationSetting.setLineIDField 方法 
 *		RelationSetting.setStopIDField 方法
 *		RelationSetting.setDatasetNetwork 方法
 *		RelationSetting.setEdgeIDField 方法
 *		RelationSetting.setNodeIDField 方法
 *		RelationSetting.setFNodeIDField 方法
 *		RelationSetting.setTNodeIDField 方法
 *		TransferAnalystSetting.setLineSetting 方法
 *		TransferAnalystSetting.setStopSetting 方法
 *		TransferAnalystSetting.setRelationSetting 方法
 *		TransferAnalystSetting.setSnapTolerance 方法
 *		TransferAnalystSetting.setMergeTolerance 方法
 *		TransferAnalystSetting.setWalkingTolerance 方法 
 *		TransferAnalystSetting.setUnit 方法 
 *		TransferAnalystParameter.setSearchMode 方法
 *		TransferAnalystParameter.setStartStopID 方法
 *		TransferAnalystParameter.setEndStopID 方法 
 *		TransferAnalystParameter.setTactic 方法 
 *		TransferAnalystParameter.setWalkingRatio 方法
 *		TransferAnalystParameter.setSolutionCount 方法 
 *		TransferAnalyst.check 方法
 *		TransferAnalyst.load 方法 
 *		TransferAnalyst.findTransferSolutions 方法
 *		TransferAnalyst.findStopsByLine 方法 
 *		TransferAnalyst.findLinesByStop 方法
 *		TransferSolutions.getCount 方法 
 *		TransferSolutions.get 方法
 *		TransferSolution.getTransferTime 方法
 *		TransferSolution.get 方法
 *		TransferLines.getCount 方法
 *		TransferLines.get 方法
 *		TransferLine.getLineName 方法
 *		TransferGuide.get 方法
 *		TransferGuide.getCount 方法 
 *		TransferGuide.getTotalDistance 方法
 *		TransferGuide.getTotalFare 方法 
 *		TransferGuideItem.isWalking 方法
 *		TransferGuideItem.getStartName 方法 
 *		TransferGuideItem.getEndName 方法
 *		TransferGuideItem.getLineName 方法 
 *		TransferGuideItem.getDistance 方法
 *		TransferGuideItem.getFare 方法 
 *		TransferGuideItem.getPassStopCount 方法
 *		LineInfo.getLineID 方法 
 *		LineInfo.getName 方法 
 *		LineInfo.getTotalDistance 方法
 *		LineInfo.getTotalLine 方法
 *		StopInfo.getStopID 方法 
 *		StopInfo.getName 方法 
 *	4、使用步骤： 
 *   (1)点击“加载公交数据”按钮，使公交分析环境设置生效
 *   (2)点击“起始点”单选按钮，在地图上选择一个点作为起始点；点击“终止点”单选按钮，在地图上选择一个点作为终止点；在换乘策略下拉框的列表可选择一项换乘策略
 *   (3)点击“换乘分析”按钮进行公交换乘分析。在地图下方的换乘方案下拉框中选择项，可以查看各个换乘方案的详细信息
 *   (4)在地图上选择一个起始站点，或在文本框内输入站点ID，然后点击“站点查线路”按钮，根据指定的站点ID查询经过该站点的公交线路
 *   (5)在文本框内输入线路ID，点击“线路查站点”按钮，根据指定的线路ID查询该线路上的公交站点 (6)点击“清除”按钮，将清空分析结果和跟踪图层中的对象
 * -- --------------------------------------------------------------------------
 * ============================================================================>
 * </p>
 * 
 * <p>
 * Company: 北京超图软件股份有限公司
 * </p>
 * 
 */
/**
 * <p>
 *Title: Bus Transfer Analysis
 * </p>
 * 
 * <p>
 * Description:
 * ============================================================================>
 * ------------------------------Copyright Statement----------------------------
 * SuperMap Objects Java Sample Code 
 * Copyright: SuperMap Software Co., Ltd. All Rights Reserved.
 * ------------------------------------------------------------------------------
 *-----------------------Description--------------------------
 * 1. How to make bus transfer analysis, query path by stop, and query stop by path.
 * 2. Sample Data: Installation directory\SampleData\City\Changchun.smwu 
 * 3. Key classes and members 

 *      Workspace.open method
 *		MapControl.MouseEvent event 
 *		TrackingLayer.add method 
 *		TrackingLayer.remove method
 *		TrackingLayer.getTag method 
 *		TrackingLayer.indexOf method 
 *		LineSetting.setDataset method
 *		LineSetting.setLineIDField method 
 *		LineSetting.setNameField method
 *		LineSetting.setLengthField method 
 *		LineSetting.setFareFieldInfo method
 *		FareFieldInfo.setFareTypeField method 
 *		FareFieldInfo.setStartFareField method
 *		FareFieldInfo.setStartFareRangeField method 
 *		FareFieldInfo.setFareStepField method
 *		FareFieldInfo.setFareStepRangeField method 
 *		StopSetting.setDataset method 
 *		StopSetting.setStopIDField method
 *		StopSetting.setNameField method 
 *		RelationSetting.setDataset method
 *		RelationSetting.setLineIDField method 
 *		RelationSetting.setStopIDField method
 *		RelationSetting.setDatasetNetwork method
 *		RelationSetting.setEdgeIDField method
 *		RelationSetting.setNodeIDField method
 *		RelationSetting.setFNodeIDField method
 *		RelationSetting.setTNodeIDField method
 *		TransferAnalystSetting.setLineSetting method
 *		TransferAnalystSetting.setStopSetting method
 *		TransferAnalystSetting.setRelationSetting method
 *		TransferAnalystSetting.setSnapTolerance method
 *		TransferAnalystSetting.setMergeTolerance method
 *		TransferAnalystSetting.setWalkingTolerance method 
 *		TransferAnalystSetting.setUnit method 
 *		TransferAnalystParameter.setSearchMode method
 *		TransferAnalystParameter.setStartStopID method
 *		TransferAnalystParameter.setEndStopID method 
 *		TransferAnalystParameter.setTactic method 
 *		TransferAnalystParameter.setWalkingRatio method
 *		TransferAnalystParameter.setSolutionCount method 
 *		TransferAnalyst.check method
 *		TransferAnalyst.load method 
 *		TransferAnalyst.findTransferSolutions method
 *		TransferAnalyst.findStopsByLine method 
 *		TransferAnalyst.findLinesByStop method
 *		TransferSolutions.getCount method 
 *		TransferSolutions.get method
 *		TransferSolution.getTransferTime method
 *		TransferSolution.get method
 *		TransferLines.getCount method
 *		TransferLines.get method
 *		TransferLine.getLineName method
 *		TransferGuide.get method
 *		TransferGuide.getCount method 
 *		TransferGuide.getTotalDistance method
 *		TransferGuide.getTotalFare method 
 *		TransferGuideItem.isWalking method
 *		TransferGuideItem.getStartName method 
 *		TransferGuideItem.getEndName method
 *		TransferGuideItem.getLineName method 
 *		TransferGuideItem.getDistance method
 *		TransferGuideItem.getFare method 
 *		TransferGuideItem.getPassStopCount method
 *		LineInfo.getLineID method 
 *		LineInfo.getName method 
 *		LineInfo.getTotalDistance method
 *		LineInfo.getTotalLine method
 *      StopInfo.getStopID method 
 *      StopInfo.getName method 
 * 4. Steps: 
 * 1. Click Load Transfer Data. In this case, the environment settings are valid.
 * 2. Check Start Stop, and select a start stop on the map. Check End Stop, and select an end stop on the map. Select a transfer way under the ComboBox.
 * 3. Click Analyze to make a bus transfer analysis. Select different item in the drop-down list to view different transfer solution.
 * 4. Select a new stop on the map, or type a stop ID to the textbox. Click Query Path by Stop to view all the bus paths that goes through this stop.
 * 5. Type a path ID and click Query Stop by Path. All the stops along this path can be displayed. Click Clear to clear the result
 * -- --------------------------------------------------------------------------
 * ============================================================================>
 * </p>
 * 
 * <p>
 * Company: SuperMap Software Co., Ltd.
 * </p>
 * 
 */
public class SampleRun extends MouseAdapter implements MouseMotionListener,
		MouseWheelListener {
	private Workspace m_workspace;
	private MapControl m_mapControl;
	private Datasource m_datasource;
	private JTable m_tableResult;
	private JComboBox m_comboBoxGuide;
	// 公交站点数据集
	// Bus stop dataset
	private DatasetVector m_datasetStop;
	// 公交线路数据集
	// Path dataset
	private DatasetVector m_datasetLine;
	// 网络数据集
	// Network dataset
	private DatasetVector m_datasetNetwork;
	// 公交站点图层
	// Stop layer
	private Layer m_layerStop;
	// 公交数据是否加载成功
	//Whether the data is loaded successfully or not
	private boolean m_isLoad = false;
	// 选择起始点
	// Select a start stop
	private boolean m_isStartPoint = true;
	// 起始站点ID
	// Start stop ID
	private long m_startStopID;
	// 终止站点ID
	// End stop ID
	private long m_endStopID;
	// 跟踪图层
	// Tracking layer
	private TrackingLayer m_trackingLayer;
	// 公交站点选择集对象
	// Stop selection object
	private Selection m_selection;
	// 公交分析对象
	// Transfer analysis object
	private TransferAnalyst m_transferAnalyst;
	// 公交换乘分析方案集合对象
	// TrafficTransferAnalystResult
	private TransferSolutions m_solutions;

	/**
	 * 根据Workspace和MapControl等构造SampleRun对象
	 * Initialize the SampleRun object with the specified workspace and mapControl
	 */
	public SampleRun(Workspace workspace, MapControl mapControl,
			JTable tableResult, JComboBox comboBoxGuide) {
		m_workspace = workspace;
		m_mapControl = mapControl;
		m_tableResult = tableResult;
		m_comboBoxGuide = comboBoxGuide;
		m_mapControl.getMap().setWorkspace(m_workspace);

		initialize();
	}

	
	/**
	 * 打开需要的工作空间，加载数据到地图上
	 * Open the workspace and add data to the map
	 */
	private void initialize() {
		try {
			// 打开工作空间
        	// Open the workspace
			String filePath = "C:\\Users\\49373\\Desktop\\Campus\\hhh.smwu";
			WorkspaceConnectionInfo info = new WorkspaceConnectionInfo(filePath);
			info.setType(WorkspaceType.SMWU);
			m_workspace.open(info);
			
			// 加载底图
            // Load the base map
			//if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        //{  
			//	m_mapControl.getMap().open("长春市区图");
	        //}
			//else
			//{
			//	m_mapControl.getMap().open("ChangChunCityMap");
			//}
			m_mapControl.getMap().open("西财周边地图");

			
			// 获取公交线路（BusLine）、站点（BusStop）和网络数据集
            // Get BusLine, BusStop and network dataset
			m_datasource = m_workspace.getDatasources().get("地图信息");
			m_datasetLine = (DatasetVector) m_datasource.getDatasets().get(
					"公交路线");
			m_datasetStop = (DatasetVector) m_datasource.getDatasets().get(
					"公交站");
			m_datasetNetwork=(DatasetVector)m_datasource.getDatasets().get("地图信息_Network");
			
			// 底图中有线路数据集的图层，因此不再添加
			// The base map has a path dataset
			// 为突出显示站点，将站点添加到地图上并设置风格
			// Add stops the map and set their style
			m_layerStop = m_mapControl.getMap().getLayers().add(m_datasetStop,
					true);
			m_layerStop.setSelectable(true);

			// 设置站点数据集的图层风格
			// Set the layer style for the stops
			LayerSettingVector stopSetting = new LayerSettingVector();
			GeoStyle stopStyle = new GeoStyle();
			stopStyle.setLineColor(new Color(170, 0, 192));
			stopStyle.setMarkerSize(new Size2D(4, 4));
			stopSetting.setStyle(stopStyle);
			m_layerStop.setAdditionalSetting(stopSetting);

			m_mapControl.getMap().setAntialias(true);
			m_mapControl.getMap().refresh();

			// 设置跟踪图层
			// Set the tracking layer
			m_trackingLayer = m_mapControl.getMap().getTrackingLayer();

			mapControlAddListener();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 加载公交分析环境
	 * Load the bus transfer analysis environment
	 */
	public boolean load() {
		try {
			if (!m_isLoad) {

				// 公交线路环境设置
            	// Set the bus line environment
				LineSetting lineSetting = new LineSetting();
				lineSetting.setDataset(m_datasetLine);
				lineSetting.setLineIDField("LINEID");			
				lineSetting.setNameField("NAME");
				lineSetting.setLengthField("SMLENGTH");
				lineSetting.setLineTypeField("LINETYPE");
				
				// 设置票价信息
                // Set the price information
				FareFieldInfo fareFieldInfo = new FareFieldInfo();
				fareFieldInfo.setFareTypeField("FARETYPE");
				fareFieldInfo.setStartFareField("STARTFARE");
				fareFieldInfo.setStartFareRangeField("STARTFARERANGE");
				fareFieldInfo.setFareStepField("FARESTEP");
				fareFieldInfo.setFareStepRangeField("FARESTARTRANGE");
				lineSetting.setFareFieldInfo(fareFieldInfo);

				// 公交站点环境设置
                // Set the stop environment
				StopSetting stopSetting = new StopSetting();
				stopSetting.setDataset(m_datasetStop);
				stopSetting.setStopIDField("STOPID"); 
				stopSetting.setNameField("NAME");

				// 公交关系设置
                // Set the bus line relations
				RelationSetting relationSetting = new RelationSetting();
				relationSetting.setDataset((DatasetVector) m_datasource
						.getDatasets().get("LineStopRelation")); //问题
				//System.out.println(m_datasource2.getDatasets().get("LineStopRelation"));
				
				relationSetting.setLineIDField("LINEID");
				
				relationSetting.setStopIDField("STOPID");
				//if(relationSetting.getLineIDField() != null) System.out.println(relationSetting.getStopIDField());
				
				// 设置网络数据集的信息
                // Set the network dataset information
				relationSetting.setDatasetNetwork(m_datasetNetwork);
				relationSetting.setEdgeIDField("SMEDGEID");
				relationSetting.setNodeIDField("SMNODEID");
				relationSetting.setFNodeIDField("SMFNODE");
				relationSetting.setTNodeIDField("SMTNODE");

				TransferAnalystSetting transferAnalystSetting = new TransferAnalystSetting();
				transferAnalystSetting.setLineSetting(lineSetting);
				transferAnalystSetting.setStopSetting(stopSetting);
				transferAnalystSetting.setRelationSetting(relationSetting);
				// 设置站点捕捉容限
                // Set the stop snapping tolerance
				transferAnalystSetting.setSnapTolerance(50.0);
				// 设置站点归并容限
                // Set the stop merge tolerance
				transferAnalystSetting.setMergeTolerance(100.0);
				// 设置步行阈值
                // Set the walking tolerance
				transferAnalystSetting.setWalkingTolerance(500.0);
				// 设置站点捕捉容限、归并容限及步行阈值的单位
                // Set the units
				transferAnalystSetting.setUnit(Unit.METER);

				// 实例化一个TransferAnalyst对象
                // Initialize a TransferAnalyst object
				m_transferAnalyst = new TransferAnalyst();
				
				// 加载公交数据
				// Load the bus data
				m_isLoad = m_transferAnalyst.load(transferAnalystSetting); //m_isLoad不为true

				if (m_isLoad) {
					m_mapControl.setAction(Action.SELECT);
					if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
			        {  
						JOptionPane.showMessageDialog(null, "成功加载公交数据！");
			        }
					else
					{
						JOptionPane.showMessageDialog(null, "Load the bus data successfully!");
					}
				} else {
					if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
			        {  
						JOptionPane.showMessageDialog(null,
						"公交数据加载失败！请检查公交分析环境设置或公交数据。");
			        }
					else
					{
						JOptionPane.showMessageDialog(null,
                    	"Failed to load the bus data! Please check the environment settings or data.");
					}
				}
			}
			m_mapControl.getMap().refresh();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			//System.out.println("ASDDASDASDA");
		}
		return m_isLoad;
	}

	/**
	 * 公交换乘分析
	 * Bus Transfer Analysis
	 */
	public void analyst(int tacticIndex) {
		try {
			if (m_isLoad) {
				// 实例化一个公交分析参数设置对象，并设置相关参数
				// Initialize a TransferAnalystParameter, and set it
				TransferAnalystParameter parameter = new TransferAnalystParameter();
				// 设置分析模式为使用站点ID分析
				// Set the search mode to Stop ID
				parameter.setSearchMode(TransferSearchMode.ID);
				parameter.setStartStopID(m_startStopID);
				parameter.setEndStopID(m_endStopID);
				parameter.setTactic(getTactic(tacticIndex));
				parameter.setWalkingRatio(10.0);
				parameter.setSolutionCount(5);

				// 进行公交换乘分析
				// Traffic transfer analysis
				m_solutions = m_transferAnalyst.findTransferSolutions(parameter);

				// 解析换乘分析结果
				// Result
				if (m_solutions != null) {
					// 将换乘方案的概要信息添加到m_comboGuide中
					// Store the result to m_comboGuide
					fillComboBox();
					// 在地图和JTable中显示换乘导引
					// Display the transfer guide on the map and the JTable
					showResult();
				} else {
					if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
			        {  
						JOptionPane.showMessageDialog(null, "抱歉！没有寻找到合适的换乘方案！");
			        }
					else
					{
						JOptionPane.showMessageDialog(null, "Sorry! There is no proper transfer line!");
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 根据站点查询线路
	 * Query the path by stops
	 */
	public void findLinesByStop(long stopID) {
		try {
			if (m_isLoad) {
				clearComboBox();

				// 清除跟踪层上已有的线路
				// Clear all lines on the tracking layer
				for (int i = m_trackingLayer.getCount() - 1; i >= 0; i--) {
					if (m_trackingLayer.getTag(i).compareTo("StartStop") != 0
							&& m_trackingLayer.getTag(i).compareTo(
									"StartStopName") != 0) {
						m_trackingLayer.remove(i);
					}
				}
				m_mapControl.getMap().refreshTrackingLayer();

				TableColumnModel columnModel = (TableColumnModel) m_tableResult
						.getColumnModel();
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {  
					columnModel.getColumn(1).setHeaderValue("线路ID");
					columnModel.getColumn(2).setHeaderValue("名称");
					columnModel.getColumn(3).setHeaderValue("总长度");
		        }
				else
				{
					columnModel.getColumn(1).setHeaderValue("Path ID");
                    columnModel.getColumn(2).setHeaderValue("Name");
                    columnModel.getColumn(3).setHeaderValue("Length");
				}
				m_tableResult.updateUI();
				
				DefaultTableModel model = (DefaultTableModel) m_tableResult
						.getModel();
				// 清除JTable中的条目
				// Clear items of JTable
				clearTableRows(model);

				// 查询经过指定站点的线路
				// Query the path by stops
				LineInfo[] lineInfos = m_transferAnalyst
						.findLinesByStop(stopID);

				Object[] objs = new Object[4];
				if (lineInfos.length > 0) {
					for (int i = 0; i < lineInfos.length; i++) {
						objs[0] = i + 1;
						objs[1] = lineInfos[i].getLineID();
						objs[2] = lineInfos[i].getName();
						objs[3] = lineInfos[i].getTotalDistance();
						model.addRow(objs);

						// 在跟踪图层中绘制查询出的线路
						// Draw the path on the tracking layer
						GeoLine geoLine = lineInfos[i].getTotalLine();
						GeoStyle style = new GeoStyle();
						style.setLineColor(Colors.makeRandom(
								lineInfos[i].getID()).get(i));
						style.setLineWidth(1);
						geoLine.setStyle(style);
						m_trackingLayer.add(geoLine, "lines");
						m_mapControl.getMap().isCustomBoundsEnabled();
						m_mapControl.getMap().setCustomBounds(m_datasetLine.getBounds());
						m_mapControl.getMap().viewEntire();
					}
					m_mapControl.getMap().refreshTrackingLayer();
				}
			} else {
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {  
					JOptionPane.showMessageDialog(null, "抱歉！没有查找到经过该站点的线路！");
		        }
				else
				{
					JOptionPane.showMessageDialog(null, "Sorry! There is no path that goes through this stop!");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 根据线路查询站点
	 * Query stop by the path
	 */
	public void findStopsByLine(long lineID) {
		try {
			if (m_isLoad) {
				clearComboBox();

				// 清除跟踪层上的对象
				// Clear object on the tracking layer
				m_trackingLayer.clear();
				m_mapControl.getMap().refreshTrackingLayer();

				TableColumnModel columnModel = (TableColumnModel) m_tableResult
						.getColumnModel();
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {  
					columnModel.getColumn(1).setHeaderValue("站点ID");
					columnModel.getColumn(2).setHeaderValue("名称");
		        }
				else
				{
					columnModel.getColumn(1).setHeaderValue("Sotp ID");
                    columnModel.getColumn(2).setHeaderValue("Name");
				}
				columnModel.getColumn(3).setHeaderValue("----");
				m_tableResult.updateUI();

				// 查询经过指定站点的线路
				// Query the path by stops
				StopInfo[] stopInfos = m_transferAnalyst
						.findStopsByLine(lineID);
				if (stopInfos.length > 0) {
					// 将该线路添加到跟踪层上显示
					// Add the path to the tracking layer
					LineInfo[] lineInfos = m_transferAnalyst
							.findLinesByStop(stopInfos[0].getStopID());
					for (int i = 0; i < lineInfos.length; i++) {
						if (lineInfos[i].getLineID() == lineID) {
							GeoLine line = lineInfos[i].getTotalLine();
							m_mapControl.getMap().setCustomBounds(
									line.getBounds());
							m_mapControl.getMap().isCustomBoundsEnabled();
							m_mapControl.getMap().viewEntire();

							GeoStyle style = new GeoStyle();
							style.setLineColor(Color.BLUE);
							style.setLineWidth(1.0);
							line.setStyle(style);
							m_trackingLayer.add(line, "Line");
							break;
						}
					}

					DefaultTableModel model = (DefaultTableModel) m_tableResult
							.getModel();
					// 清除JTable中的条目
					// Clear items of JTable
					clearTableRows(model);
					Object[] objs = new Object[4];
					// 添加站点信息
					// Add stop information
					for (int j = 0; j < stopInfos.length; j++) {

						// 将线路信息添加到JTable中
						// Add the path information to JTable
						objs[0] = j + 1;
						objs[1] = stopInfos[j].getStopID();
						objs[2] = stopInfos[j].getName();
						objs[3] = "----";
						model.addRow(objs);

						// 在跟踪图层中绘制查询出的站点及其名称
						// Draw stops and names on the tracking layer
						Point2D stopPoint = stopInfos[j].getPosition();
						GeoPoint point = new GeoPoint(stopPoint);
						point.setStyle(getStopStyle(new Size2D(5, 5),
								Color.orange));

						GeoText geoText = new GeoText();
						TextPart part = new TextPart(stopInfos[j].getName(),
								stopPoint);
						geoText.addPart(part);
						geoText.setTextStyle(getStopTextStyle(5.0, new Color(
								153, 0, 255)));

						m_trackingLayer.add(point, "Stops");
						m_trackingLayer.add(geoText, "StopNames");
					}
					m_mapControl.getMap().refreshTrackingLayer();
				}
			} else {
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        {  
					JOptionPane.showMessageDialog(null, "抱歉！没有查找到该线路上的站点！");
		        }
				else
				{
					JOptionPane.showMessageDialog(null, "Sorry! There is no stop on this path!");
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 在地图上显示换乘的图形导引，在DataGridView中显示详细信息
	 * Show the transfer guide on the map, and detailed information on DataGriveView
	 */
	public void showResult() {
		TableColumnModel columnModel = (TableColumnModel) m_tableResult
				.getColumnModel();
		if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
        {  
			columnModel.getColumn(1).setHeaderValue("导引");
			columnModel.getColumn(2).setHeaderValue("距离");
			columnModel.getColumn(3).setHeaderValue("费用");
        }
		else
		{
			columnModel.getColumn(1).setHeaderValue("Guilde");
            columnModel.getColumn(2).setHeaderValue("Distance");
            columnModel.getColumn(3).setHeaderValue("Cost");
		}
		m_tableResult.updateUI();

		// 删除跟踪图层上除起始、终止站点及其名称外的几何对象
		// Delete objects on the tracking layer except the start and end stop
		for (int i = m_trackingLayer.getCount() - 1; i >= 0; i--) {
			String tag = m_trackingLayer.getTag(i);
			if (tag.compareTo("StartStop") != 0
					&& tag.compareTo("EndStop") != 0
					//&& tag.compareTo("StartStopName") != 0
					//&& tag.compareTo("EndStopName") != 0
					) {
				m_trackingLayer.remove(i);
			}
		}
		m_mapControl.getMap().refreshTrackingLayer();

		TransferSolution solution=null;				
		if(m_comboBoxGuide.getSelectedIndex()==-1){
			solution=m_solutions.get(0);
		}
		else{
			solution=m_solutions.get(m_comboBoxGuide.getSelectedIndex());
		}
		
		// 提取换乘方案中的第一条换乘路线，即每段乘车段集合中的第一段乘车路线的组合对应的完整路线
		// Extraction Transfer scheme first transfer routes, namely a complete line corresponding to the combination of each vehicle segment in the collection of the first paragraph Bus
		TransferLine[] linesOnOne= new TransferLine[solution.getTransferTime()+1];
		for(int j=0;j<solution.getTransferTime()+1;j++){
			linesOnOne[j]=solution.get(j).get(0);
		}
		// 获取换乘导引
		// Get transfer guidance
		TransferGuide transferGuide=m_transferAnalyst.getDetailInfo(m_startStopID, m_endStopID, linesOnOne);
		
		// 从换乘导引中提取详细的导引信息
		// Extracting detailed information from the transfer guidance in the guide
		if (transferGuide!=null) {
			for (int i = 0; i < transferGuide.getCount(); i++) {
				TransferGuideItem item = transferGuide.get(i);
				// 获取换乘导引子项的路径对象
				// Get the path objects of the guide items
				GeoLine geoLine = item.getRoute();
				GeoStyle style = new GeoStyle();
				if (transferGuide.get(i).isWalking()) {
					style.setLineColor(new Color(255,87,87));
					style.setLineWidth(0.6);
					style.setLineSymbolID(12);
				} else {
					style.setLineColor(Color.blue);
					style.setLineWidth(1.0);
				}
				geoLine.setStyle(style);
				m_trackingLayer.add(geoLine, "Path");

				// 绘制中间站点
				// Draw mid-stops
				GeoPoint transferStop = new GeoPoint(item.getStartPosition()
						.getX(), item.getStartPosition().getY());
				transferStop.setStyle(getStopStyle(new Size2D(5, 5), new Color(
						87, 255, 255)));
				GeoText transferStopName = new GeoText();
				TextPart part = new TextPart(item.getStartName(), new Point2D(
						transferStop.getX(), transferStop.getY()));
				transferStopName.addPart(part);
				transferStopName.setTextStyle(getStopTextStyle(5.0, new Color(
						89, 89, 89)));
				if (i != 0) {
					m_trackingLayer.add(transferStop, "transferStop");
				}
				m_trackingLayer.add(transferStopName, "transferStopName");

				transferStop = new GeoPoint(item.getEndPosition().getX(), item
						.getEndPosition().getY());
				transferStop.setStyle(getStopStyle(new Size2D(5, 5), new Color(
						87, 255, 255)));
				transferStopName = new GeoText();
				part = new TextPart(item.getEndName(), new Point2D(transferStop
						.getX(), transferStop.getY()));
				transferStopName.addPart(part);
				transferStopName.setTextStyle(getStopTextStyle(5.0, new Color(
						89, 89, 89)));
				if (i != transferGuide.getCount() - 1) {
					m_trackingLayer.add(transferStop, "transferStop");
				}
				m_trackingLayer.add(transferStopName, "transferStopName");
				m_mapControl.getMap().refreshTrackingLayer();
			}
			m_mapControl.getMap().refreshTrackingLayer();

			// 添加信息到JTable
			// Add information to JTable
			fillGuidesInfo(transferGuide);
		} else {
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {  
				JOptionPane.showMessageDialog(null, "抱歉！换乘方案详细信息提取出错！");
	        }
			else
			{
				JOptionPane.showMessageDialog(null, "Sorry! There is no proper transfer line!");
			}
		}
	}

	/**
	 * 将换乘方案信息添加到JTable上
	 * Add transfer information to JTable
	 * @param transferGuide
	 */
	private void fillGuidesInfo(TransferGuide transferGuide) {
		DefaultTableModel model = (DefaultTableModel) m_tableResult.getModel();
		// 清除JTable中的条目
		// Clear items of JTable
		clearTableRows(model);
		// 添加换乘方案详细信息到JTable
		// Add detailed transfer information to JTable
		Object[] values = new Object[4];
		// 步行的距离
		// Walking distance
		int disWalk = 0;
		// 乘车经过的距离
		// Bus path distance
		int disRide = 0;
		for (int i = 0; i < transferGuide.getCount(); i++) {
			TransferGuideItem item = transferGuide.get(i);

			values[0] = i + 1;
			if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
	        {  
				if (item.isWalking()) {
					if (i == transferGuide.getCount() - 1) {
						values[1] = "步行至终点";
					} else {
						values[1] = "步行至"
								+ transferGuide.get(i + 1).getStartName();
					}
					disWalk += item.getDistance();
				} else {
					values[1] = "从" + item.getStartName() + "乘坐"
							+ item.getLineName() + "经过"
							+ (item.getPassStopCount() - 1) + "站，在"
							+ item.getEndName() + "下车";
					disRide += item.getDistance();
				}
	        }
			else
			{
				if (item.isWalking()) {
					if (i == transferGuide.getCount() - 1) {
	                              values[1] = "Walk to the destination";
					} else {
	                              values[1] = "Walk to"
								+ transferGuide.get(i + 1).getStartName();
					}
					disWalk += item.getDistance();
				} else {
                    values[1] = "From " + item.getStartName() + ", take "
                                + item.getLineName() + " with "
                                + (item.getPassStopCount()-1) + " stops, and get off at "
                                + item.getEndName();
                    disRide += item.getDistance();
	            }
			}
			values[2] = item.getDistance();
			values[3] = item.getFare();
			model.addRow(values);
		}

		values[0] = transferGuide.getCount() + 1;
		if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
        { 
			values[1] = "乘车总距离：" + disRide + "，步行总距离：" + disWalk;
			values[2] = "总距离：" + transferGuide.getTotalDistance();
			values[3] = "总费用：" + transferGuide.getTotalFare();
        }
		else
		{
			values[1] = "Bus distance: " + disRide + ", walking distance: " + disWalk;
            values[2] = "Total distance: " + transferGuide.getTotalDistance();
            values[3] = "Total cost: " + transferGuide.getTotalFare();
		}
		model.addRow(values);
	}

	/**
	 * 标记当前为在地图上选择起始站点
	 * Mark as the start stop
	 */
	public void selectStartStop() {
		m_isStartPoint = true;
	}

	/**
	 * 标记当前为在地图上选择终止站点
	 * Mark as the end stop
	 */
	public void selectEndStop() {
		m_isStartPoint = false;
	}

	/**
	 * 获得公交换乘策略
	 * Get the bus transfer policy
	 */
	public TransferTactic getTactic(int value) {
		TransferTactic tactic = null;
		switch (value) {
		case 0:
			tactic = TransferTactic.LESS_TRANSFER;
			break;
		case 1:
			tactic = TransferTactic.LESS_WALK;
			break;
		case 2:
			tactic = TransferTactic.LESS_TIME;
			break;
		case 3:
			tactic = TransferTactic.MIN_DISTANCE;
			break;
		default:
			break;
		}
		return tactic;
	}

	/**
	 * 获取捕捉点
	 * Get the snap points
	 */
	private void snapPoint() {
		try {
			if (m_selection != null) {
				// 获取被选中的站点对象
				// Get the selected stop
				Recordset recordset = m_selection.toRecordset();
		        GeoPoint snapPoint = (GeoPoint) recordset.getGeometry();
			  //Point2D snapPoint = recordset.getGeometry().getInnerPoint();

				// 构造文本对象，用于在跟踪层上显示站点的名称
				// Build a text object to display stop names on the tracking layer
				GeoText stopText = new GeoText();
				// 站点名称
				// Stop name
				String stopName = "";
				stopName = recordset.getFieldValue("NAME").toString();
				TextPart textPart = new TextPart(stopName, new Point2D(
				snapPoint.getX(), snapPoint.getY()));
				stopText.addPart(textPart);

				if (m_isStartPoint) {
					// 绘制之前先清空跟踪层上的点和文字
					// Clear all points and words on the tracking layer before drawing
					int indexStartStop = m_trackingLayer.indexOf("StartStop");
					if (indexStartStop != -1) {
						m_trackingLayer.remove(indexStartStop);
					}
					int indexStartText = m_trackingLayer
							.indexOf("StartStopName");
					if (indexStartText != -1) {
						m_trackingLayer.remove(indexStartText);
					}
					// 分别设置站点及其名称文本的风格，并添加到跟踪层上
					// Set the stop and name style and add them to the tracking layer
					snapPoint.setStyle(getStopStyle(new Size2D(8, 8),
							new Color(236, 118, 0)));
					m_trackingLayer.add(snapPoint, "StartStop");
					stopText.setTextStyle(getStopTextStyle(6.0, new Color(0,
							0, 0)));
					m_trackingLayer.add(stopText, "StartStopName");

					// 设置为起始站点ID
					// Set the strat stop ID
					m_startStopID = recordset.getInt64("STOPID");
				} else {
					// 绘制之前先清空跟踪层上的点和文字
					// Clear all points and words on the tracking layer before drawing
					int indexEndPoint = m_trackingLayer.indexOf("EndStop");
					if (indexEndPoint != -1) {
						m_trackingLayer.remove(indexEndPoint);
					}
					int indexEndText = m_trackingLayer.indexOf("EndStopName");
					if (indexEndText != -1) {
						m_trackingLayer.remove(indexEndText);
					}
					// 分别设置站点及其名称文本的风格，并添加到跟踪层上
					// Set the stop and name style and add them to the tracking layer
					snapPoint.setStyle(getStopStyle(new Size2D(8, 8),
							new Color(22, 255, 0)));
					m_trackingLayer.add(snapPoint, "EndStop");
					stopText.setTextStyle(getStopTextStyle(6.0, new Color(0,
							0, 0)));
					m_trackingLayer.add(stopText, "EndStopName");

					// 设置为起始站点ID
					// Set the strat stop ID
					m_endStopID = recordset.getInt64("STOPID");
				}
				recordset.dispose();
			}
		} catch (RuntimeException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * 设置捕捉框
	 * Set snap box
	 */
	private void setBound(Point point) {
		try {
			// 清除跟踪图层上的捕捉框
			// Clear snap box on the tracking layer
			int indexSnapPane = m_trackingLayer.indexOf("snapPane");
			if (indexSnapPane != -1) {
				m_trackingLayer.remove(indexSnapPane);
				m_mapControl.getMap().refreshTrackingLayer();
			}

			// 将地图中指定点的像素坐标转换为地图坐标
			// Transform the pixel coordinates to the map coordinates
			Point2D point2D = m_mapControl.getMap().pixelToMap(point);

			double scale = (3 * 10E-4) / m_mapControl.getMap().getScale();
			 m_selection = m_layerStop.hitTest(point2D, 4 / 3 * scale);
			if (m_selection != null && m_selection.getCount() > 0) {
				Recordset recordset = m_selection.toRecordset();
				GeoPoint geoPoint = (GeoPoint) recordset.getGeometry();

				// 构造捕捉框
				// Build the snap box
				double pointX = geoPoint.getX();
				double pointY = geoPoint.getY();
				Point2Ds point2Ds = new Point2Ds();
				point2Ds.add(new Point2D(pointX - scale, pointY - scale));
				point2Ds.add(new Point2D(pointX + scale, pointY - scale));
				point2Ds.add(new Point2D(pointX + scale, pointY + scale));
				point2Ds.add(new Point2D(pointX - scale, pointY + scale));
				point2Ds.add(new Point2D(pointX - scale, pointY - scale));
				GeoLine snapPane = new GeoLine(point2Ds);

				m_mapControl.setSelectionTolerance(2);
				m_trackingLayer.add(snapPane, "snapPane");
				m_mapControl.getMap().refreshTrackingLayer();

				recordset.dispose();
			}
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 将换乘方案的概述添加到m_comboBoxGuide中
	 * Add the transfer guide to m_comboBoxGuide
	 */
	private void fillComboBox() {
		try {
			TransferSolution solution = null;
			String summary = "";
			DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
			for (int i = 0; i < m_solutions.getCount(); i++) {
				solution = m_solutions.get(i);
				if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
		        { 
					summary = "方案 " + (i + 1) + "：";
		        }
				else
				{
					summary = "Policy " + (i + 1) + "：";
				}
				TransferLines lines = null;
				for (int j = 0; j < solution.getTransferTime() + 1; j++) {
					lines = solution.get(j);
					for (int k = 0; k < lines.getCount(); k++) {
						if (k == 0) {
							summary += lines.get(0).getLineName();
						} else {
							summary += "/" + lines.get(k).getLineName();
						}
					}
					if (solution.getTransferTime() > 0) {
						if (j != solution.getTransferTime()) {
							if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
					        { 
								summary += "换乘";
					        }
							else
							{
								summary += "Transfer";
							}
						}
					}
				}
				comboBoxModel.addElement(summary);
			}
			m_comboBoxGuide.setModel(comboBoxModel);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 站点名称文本的风格，用于在跟踪层上绘制
	 * The style of the stop text
	 */
	private TextStyle getStopTextStyle(Double fontHeight, Color color) {
		TextStyle style = new TextStyle();
		style.setBackOpaque(true);
		style.setShadow(true);
		style.setBackColor(new Color(255,255,0));
		style.setFontHeight(fontHeight);
		if (com.supermap.data.Environment.getCurrentCulture().contentEquals("zh-CN"))
        { 
			style.setFontName("微软雅黑");
        }
		else
		{
			style.setFontName("Times New Roman");
		}
		style.setBold(true);
		style.setForeColor(color);
		return style;
	}

	/**
	 * 设置站点几何风格，用于在跟踪层上绘制
	 * Set the stop geometry style and draw it on the tracking layer
	 */
	private GeoStyle getStopStyle(Size2D size2D, Color color) {
		GeoStyle style = new GeoStyle();
		style.setMarkerSize(size2D);
		style.setLineColor(color);
		return style;
	}

	/**
	 * 清空跟踪图层及分析结果
	 * Clear the tracking layer and the analysis result
	 */
	public void clear() {
		m_solutions = null;
		m_startStopID = -1;
		m_endStopID = -1;
		m_layerStop.getSelection().clear();
		// 清除跟踪图层
		// Clear the tracking layer
		m_trackingLayer.clear();

		// 清除m_comboBoxCount列表
		// Clear m_comboBoxCount list
		clearComboBox();

		// 清除m_tableResult中的信息
		// Clear the m_tableResult information
		DefaultTableModel model = (DefaultTableModel) m_tableResult.getModel();
		clearTableRows(model);

		m_mapControl.getMap().refresh();

	}

	/**
	 * 清除m_comboBoxGuide中的条目
	 * Clear items of m_comboBoxGuide
	 */
	private void clearComboBox() {
		DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
		comboBoxModel.removeAllElements();
		m_comboBoxGuide.setModel(comboBoxModel);
	}

	/**
	 * 清除m_tableResult列表
	 * Clear m_tableResult list
	 */
	private void clearTableRows(DefaultTableModel model) {
		// 删除JTable中的所有行
		// Delete all records in JTable
		if (model.getRowCount() > 0) {
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
		}
	}

	public void dispose() {
		if (m_transferAnalyst != null) {
			m_transferAnalyst.dispose();
		}
	}

	public long getStopID() {
		return m_startStopID;
	}

	private void mapControlAddListener() {
		m_mapControl.addMouseListener(new MouseAdapter() {
			/**
			 * 监听鼠标单击事件
			 * The MouseClicked event listener
			 */
			public void mouseClicked(MouseEvent e) {
				snapPoint();
			}
		});
		m_mapControl.addMouseMotionListener(new MouseMotionAdapter() {
			/**
			 * 监听鼠标移动事件
			 * The MouseMoved event listener
			 */
			public void mouseMoved(MouseEvent e) {
				try {
					if (m_mapControl.getAction() == Action.SELECT) {
						// 获取鼠标点对应的地图点
						// Get the map point that corresponds to the mouse point
						Point point = new Point(e.getX(), e.getY());
						setBound(point);
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
	}

}

