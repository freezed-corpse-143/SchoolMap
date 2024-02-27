package gettingstarted;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.supermap.data.Workspace;
import com.supermap.ui.Action;
import com.supermap.ui.MapControl;

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
 *      MapControl.MouseEvent event 
 *      TrackingLayer.Add method 
 *      TrackingLayer.remove method
 *      TrackingLayer.getTag method 
 *      TrackingLayer.indexOf method 
 *      LineSetting.setDataset method
 *      LineSetting.setLineIDField method 
 *      LineSetting.setNameField method
 *      LineSetting.setLengthField method 
 *      LineSetting.setFareFieldInfo method
 *      FareFieldInfo.setFareTypeField method 
 *      FareFieldInfo.setStartFareField method
 *      FareFieldInfo.setStartFareRangeField method 
 *      FareFieldInfo.setFareStepField method
 *      FareFieldInfo.setFareStepRangeField method 
 *      StopSetting.setDataset method 
 *      StopSetting.setStopIDField method
 *      StopSetting.setNameField method 
 *      RelationSetting.setDataset method
 *      RelationSetting.setLineIDField method 
 *      RelationSetting.setStopIDField method
 *      RelationSetting.setDatasetNetwork method
 *      RelationSetting.setEdgeIDField method
 *      RelationSetting.setNodeIDField method
 *      RelationSetting.setFNodeIDField method
 *      RelationSetting.setTNodeIDField method
 *      TransferAnalystSetting.setLineSetting method
 *      TransferAnalystSetting.setStopSetting method
 *      TransferAnalystSetting.setRelationSetting method
 *      TransferAnalystSetting.setSnapTolerance method
 *      TransferAnalystSetting.setMergeTolerance method
 *      TransferAnalystSetting.setWalkingTolerance method 
 *      TransferAnalystSetting.setUnit method 
 *      TransferAnalystParameter.setSearchMode method
 *      TransferAnalystParameter.setStartStopID method
 *      TransferAnalystParameter.setEndStopID method 
 *      TransferAnalystParameter.setTactic method 
 *      TransferAnalystParameter.setWalkingRatio method
 *      TransferAnalystParameter.setMaxTransferGuideCount method 
 *      TransferAnalyst.check method
 *      TransferAnalyst.load method 
 *      TransferAnalyst.findTransferLines method
 *      TransferAnalyst.findStopsByLine method 
 *      TransferAnalyst.findLinesByStop method
 *		TransferSolutions.getCount method 
 *		TransferSolutions.get method
 *		TransferSolution.getTransferTime method
 *		TransferSolution.get method
 *		TransferLines.getCount method
 *		TransferLines.get method
 *		TransferLine.getLineName method
  *      TransferGuide.get method
 *      TransferGuide.getCount method 
 *      TransferGuide.getTotalDistance method
 *      TransferGuide.getTotalFare method 
 *      TransferGuideItem.isWalking method
 *      TransferGuideItem.getStartStopInfo method 
 *      TransferGuideItem.getEndStopInfo method
 *      TransferGuideItem.getLineInfo method 
 *      TransferGuideItem.getDistance method
 *      TransferGuideItem.getFare method 
 *      TransferGuideItem.getPassStopCount method
 *      LineInfo.getLineID method 
 *      LineInfo.getName method 
 *      LineInfo.getTotalDistance method
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
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel m_jContentPane;
	private MapControl m_mapControl;
	private Workspace m_workspace;
	private SampleRun m_sampleRun;
	private JToolBar m_jToolBar;
	private JButton m_buttonLoad;
	private JRadioButton m_radioButtonStart;
	private JRadioButton m_radioButtonEnd;
	private JButton m_buttonAnalyst;
	private JPanel m_panelBottom;
	private JToolBar m_toolBarBottom;
	private JScrollPane m_ScrollPaneResult;
	private JTable m_tableResult;
	private JLabel m_label;
	private JComboBox m_comboBoxTactic;
	private JComboBox m_comboBoxGuide;
	private JButton m_buttonClear;
	private JTextField m_textFindLines;
	private JTextField m_textFindStops;
	private JButton m_buttonFindLines;
	private JButton m_buttonFindStops;
	private int m_tacticIndex;

	/**
	 * 程序入口点
	 * Entrance to the program
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame thisClass = new MainFrame();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * 构造函数
	 * Constructor
	 */
	public MainFrame() {
		super();
		initialize();
	}

	/**
	 * 初始化窗体
	 * Initialize the form.
	 */
	private void initialize() {
		// 最大化显示窗体
		// Maximize the form
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setSize(800, 500);
		this.setContentPane(getJContentPane());
		this.setTitle(getText("thisTitle"));
		m_buttonLoad.setEnabled(true);
		m_radioButtonStart.setEnabled(false);
		m_radioButtonEnd.setEnabled(false);
		m_comboBoxTactic.setEnabled(false);
		m_buttonAnalyst.setEnabled(false);
		m_buttonFindLines.setEnabled(false);
		m_buttonFindStops.setEnabled(false);
		m_buttonAnalyst.setEnabled(false);
		this.addWindowListener(new WindowAdapter() {
			// 在主窗体加载时，初始化SampleRun类型，来完成功能的展现
			// Initialize the SampleRun type when loading the main window
			public void windowOpened(java.awt.event.WindowEvent e) {
				m_workspace = new Workspace();
				m_sampleRun = new SampleRun(m_workspace, m_mapControl,
						m_tableResult, m_comboBoxGuide);
				ButtonGroup buttonGroup = new ButtonGroup();
				buttonGroup.add(getRadioButtonStart());
				buttonGroup.add(getRadioButtonEnd());
			}

			// 在窗体关闭时，需要释放相关的资源
			// Release resources when you close the window
			public void windowClosing(java.awt.event.WindowEvent e) {
				m_mapControl.dispose();
				m_workspace.dispose();
				m_sampleRun.dispose();
			}
		});
		
		// 为按钮添加图片
	    ImageIcon buttonLoad = new ImageIcon("Resources/H2.png");
	    buttonLoad.setImage(buttonLoad.getImage().getScaledInstance(20,20,0));
	    m_buttonLoad.setIcon(buttonLoad);
	    
	    ImageIcon buttonAnalyst = new ImageIcon("Resources/H1.png");
	    buttonAnalyst.setImage(buttonAnalyst.getImage().getScaledInstance(20,20,0));
	    m_buttonAnalyst.setIcon(buttonAnalyst);
	    
	    
	}

	/**
	 * 构建MapControl
	 * Build MapControl
	 */
	private MapControl getMapControl() {
		if (m_mapControl == null) {
			m_mapControl = new MapControl();
		}
		return m_mapControl;
	}

	/**
	 * 构建JToolBar工具条
	 * Build JToolBar
	 */
	private JToolBar getJToolBar() {
		if (m_jToolBar == null) {
			m_jToolBar = new JToolBar();
			m_jToolBar.add(getButtonLoad());
			m_jToolBar.add(getRadioButtonStart());
			m_jToolBar.add(getRadioButtonEnd());
			m_jToolBar.addSeparator();
			m_jToolBar.add(getLabel());
			m_jToolBar.add(getComboBoxTactic());
			m_jToolBar.addSeparator();
			m_jToolBar.add(getButtonAnalyst());
			m_jToolBar.addSeparator();
			m_jToolBar.add(getTextfindLines());
			m_jToolBar.add(getButtonFindLines());
			m_jToolBar.add(getTextFindStops());
			m_jToolBar.add(getButtonFindStops());
			m_jToolBar.addSeparator();
			m_jToolBar.add(getButtonClear());
		}
		return m_jToolBar;
	}

	
	/**
	 * 构建“加载公交数据”按钮
	 * Build the Load Transfer Data button
	 */
	private JButton getButtonLoad() {
		if (m_buttonLoad == null) {
			m_buttonLoad = new JButton();
			m_buttonLoad.setText(getText("m_buttonLoad"));
			m_buttonLoad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(m_sampleRun.load()){
						m_radioButtonStart.setEnabled(true);
						m_radioButtonStart.setSelected(true);
						m_radioButtonEnd.setEnabled(true);
						m_comboBoxTactic.setEnabled(true);
						m_buttonAnalyst.setEnabled(true);
						m_buttonFindLines.setEnabled(true);
						m_buttonFindStops.setEnabled(true);
						m_buttonAnalyst.setEnabled(true);
						
						m_mapControl.setAction(Action.SELECT);
					}
				}
			});
		}
		return m_buttonLoad;
	}

	/**
	 * 构建“起始点”单选按钮
	 * Build the Start Stop button
	 */
	private JRadioButton getRadioButtonStart() {
		if (m_radioButtonStart == null) {
			m_radioButtonStart = new JRadioButton();
			m_radioButtonStart.setEnabled(true);
			m_radioButtonStart.setText(getText("m_radioButtonStart"));
			m_radioButtonStart.setSelected(true);
			m_radioButtonStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_sampleRun.selectStartStop();
				}
			});
		}
		return m_radioButtonStart;
	}

	/**
	 * 构建“终止点”单选按钮
	 * Build the End Stop button
	 */
	private JRadioButton getRadioButtonEnd() {
		if (m_radioButtonEnd == null) {
			m_radioButtonEnd = new JRadioButton();
			m_radioButtonEnd.setText(getText("m_radioButtonEnd"));
			m_radioButtonEnd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_sampleRun.selectEndStop();
				}
			});
		}
		return m_radioButtonEnd;
	}

	
	private JLabel getLabel() {
		if(m_label==null){
			m_label=new JLabel();
			m_label.setText(getText("m_label"));
		}
		return m_label;
	}
	
	/**
	 * 构建换乘策略下拉列表
	 *  Build the transfer policy drop-down list
	 * @return
	 */
	private JComboBox getComboBoxTactic(){
		if(m_comboBoxTactic==null){
			m_comboBoxTactic=new JComboBox();
			m_comboBoxTactic.setPreferredSize(new java.awt.Dimension(100, 30));
			m_comboBoxTactic.setMaximumSize(new Dimension(100, 30));
			DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
			comboBoxModel.addElement(getText("Element0"));
			comboBoxModel.addElement(getText("Element1"));
			comboBoxModel.addElement(getText("Element2"));
			comboBoxModel.addElement(getText("Element3"));
			m_comboBoxTactic.setModel(comboBoxModel);
		}
		return m_comboBoxTactic;
	}

	/**
	 * 构建“换乘分析”按钮
	 * Build the Transfer Anlaysis button
	 */
	private JButton getButtonAnalyst() {
		if (m_buttonAnalyst == null) {
			m_buttonAnalyst = new JButton();
			m_buttonAnalyst.setText(getText("m_buttonAnalyst"));
			m_buttonAnalyst.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_tacticIndex=m_comboBoxTactic.getSelectedIndex();
					m_sampleRun.analyst(m_tacticIndex);
				}
			});
		}
		return m_buttonAnalyst;
	}

	/**
	 * 构建底部面板
	 * Build the bottom panel
	 */
	private JPanel getPanelBottom() {
		if (m_panelBottom == null) {
			m_panelBottom = new JPanel();
			m_panelBottom.setLayout(new BorderLayout());
			m_panelBottom.setPreferredSize(new java.awt.Dimension(0, 160));
			m_panelBottom.add(getToolBarBottom(), java.awt.BorderLayout.NORTH);
			m_panelBottom.add(getScrollPaneResult(),
					java.awt.BorderLayout.CENTER);
		}
		return m_panelBottom;
	}

	/**
	 * 构建底部面板上的工具条
	 * Build  the tool bar placed on the bottom panel
	 */
	private JToolBar getToolBarBottom() {
		if (m_toolBarBottom == null) {
			m_toolBarBottom = new JToolBar();
			m_toolBarBottom.add(getComboBoxGuide());
		}
		return m_toolBarBottom;
	}

	/**
	 * 构建JScrollPane，用于放置显示分析结果信息的JTable
	 * Build JScrollPane, used to store JTable
	 */
	private JScrollPane getScrollPaneResult() {
		if (m_ScrollPaneResult == null) {
			m_ScrollPaneResult = new JScrollPane();
			m_ScrollPaneResult.setViewportView(getTableResult());
		}
		return m_ScrollPaneResult;
	}

	/**
	 * 构建用于显示分析结果的JTable
	 * Build JTable for displaying analysis result
	 */
	private JTable getTableResult() {
		if (m_tableResult == null) {
			m_tableResult = new JTable();
			DefaultTableModel model = new DefaultTableModel();
			model.addColumn(getText("Column0"));
			model.addColumn(getText("Column1"));
			model.addColumn(getText("Column2"));
			model.addColumn(getText("Column3"));
			m_tableResult.setModel(model);
			TableColumnModel tcm = m_tableResult.getColumnModel();
			tcm.getColumn(0).setPreferredWidth(10);
			tcm.getColumn(1).setPreferredWidth(300);
			tcm.getColumn(2).setPreferredWidth(10);
			tcm.getColumn(3).setPreferredWidth(10);
		}
		return m_tableResult;
	}

	/**
	 * 构建JComboBox，用于显示换乘导引数目
	 * Build JComboBox to display the transfer guide items
	 */
	private JComboBox getComboBoxGuide() {
		if (m_comboBoxGuide == null) {
			m_comboBoxGuide = new JComboBox();
			m_comboBoxGuide.setPreferredSize(new java.awt.Dimension(600, 30));
			m_comboBoxGuide.setMaximumSize(new Dimension(600, 30));
			m_comboBoxGuide.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					m_sampleRun.showResult();
				}
			});
		}
		return m_comboBoxGuide;
	}

	/**
	 * 构建“清除”按钮
	 * Build the Clear button
	 */
	private JButton getButtonClear() {
		if (m_buttonClear == null) {
			m_buttonClear = new JButton();
			m_buttonClear.setText(getText("m_buttonClear"));
			m_buttonClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					m_sampleRun.clear();
					m_textFindStops.setText(null);
					m_textFindLines.setText(null);
				}
			});
		}
		return m_buttonClear;
	}

	/**
	 * 构建主面板
	 * Build the main panel
	 */
	private JPanel getJContentPane() {
		if (m_jContentPane == null) {
			m_jContentPane = new JPanel();
			m_jContentPane.setLayout(new BorderLayout());
			m_jContentPane.add(getMapControl(), java.awt.BorderLayout.CENTER);
			m_jContentPane.add(getJToolBar(), java.awt.BorderLayout.NORTH);
			m_jContentPane.add(getPanelBottom(), java.awt.BorderLayout.SOUTH);
		}
		return m_jContentPane;
	}

	/**
	 * 构建“站点查线路”按钮
	 * Build the Query Path By Stop button
	 */
	private JButton getButtonFindLines() {
		if (m_buttonFindLines == null) {
			m_buttonFindLines = new JButton();
			m_buttonFindLines.setText(getText("m_buttonFindLines"));
			m_buttonFindLines.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long stopID=-1L;
					if(m_textFindLines.getText().compareTo("")==0){
						stopID = m_sampleRun.getStopID();
						m_textFindLines.setText(stopID.toString());
					}
					else{
						stopID=Long.valueOf(m_textFindLines.getText());
					}
					m_sampleRun.findLinesByStop(stopID);
				}
			});
		}
		return m_buttonFindLines;
	}

	/**
	 * 构建文本框，用于输入站点ID
	 * Build a textbox to input Stop ID
	 */
	private JTextField getTextfindLines() {
		m_textFindLines = new JTextField(2);
		return m_textFindLines;
	}

	/**
	 * 构建“线路查站点”按钮
	 * Build the Query Stop By Path button
	 */
	private JButton getButtonFindStops() {
		if (m_buttonFindStops == null) {
			m_buttonFindStops = new JButton();
			m_buttonFindStops.setText(getText("m_buttonFindStops"));
			m_buttonFindStops.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					long lineID = Long.parseLong(m_textFindStops.getText());
					m_sampleRun.findStopsByLine(lineID);
				}
			});
		}
		return m_buttonFindStops;
	}

	/**
	 * 构建文本框，用于输入线路ID
	 * Build a textbox to input Path ID
	 */
	private JTextField getTextFindStops() {
		m_textFindStops = new JTextField(2);
		return m_textFindStops;
	}
	
	
	private String getText(String id) {	
		
		String text = "";
		
        	if(id == "thisTitle")
        	{       		
        		text = "公交地铁";
        	}
        	if(id == "m_buttonLoad")
        	{       		
        		text = "加载数据";
        	}
        	if(id == "m_radioButtonStart")
        	{       		
        		text = "起点";
        	}
        	if(id == "m_radioButtonEnd")
        	{       		
        		text = "终点";
        	}
        	if(id == "m_label")
        	{       		
        		text = "换乘方案：";
        	}
        	if(id == "m_buttonAnalyst")
        	{       		
        		text = "换乘分析";
        	}
        	if(id == "m_buttonClear")
        	{       		
        		text = "重置";
        	}
        	if(id == "m_buttonFindLines")
        	{       		
        		text = "站点查线路";
        	}
        	if(id == "m_buttonFindStops")
        	{       		
        		text = "线路查站点";
        	}
        	if(id == "Element0")
        	{       		
        		text = "少换乘";
        	}
        	if(id == "Element1")
        	{       		
        		text = "少步行";
        	}
        	if(id == "Element2")
        	{       		
        		text = "较快捷";
        	}
        	if(id == "Element3")
        	{       		
        		text = "距离最短";
        	}
        	if(id == "Column0")
        	{       		
        		text = "序号";
        	}
        	if(id == "Column1")
        	{       		
        		text = "导引";
        	}
        	if(id == "Column2")
        	{       		
        		text = "距离";
        	}
        	if(id == "Column3")
        	{       		
        		text = "费用";
        	}   
        return text;	
        
	}
}

