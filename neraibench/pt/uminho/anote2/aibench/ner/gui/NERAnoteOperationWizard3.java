package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationWizard3 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JList jListResources;
	private JTable jTablePubs;
	private JScrollPane jScrollPanePubs;
	private JScrollPane jScrollPaneQueries;
	private JProgressBar jProgressBar;
	private JPanel jPanelBarPanel;
	private JPanel jPanelPresentationPAnel;
	private JPanel jPanelOptionsPanel;
	
	private JCheckBox box;
	
	private IDatabase database;
	
	private Map<Integer,String> classIDclass;
	private Map<String,Integer> classClassID;
	private List<Set<Integer>>  resourceSelectedClassContent;
	private List<Set<Integer>>  resourceallClassContent;
	private List<IResource<IResourceElement>> resources;

	public NERAnoteOperationWizard3(int sizeH, int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		getDatabase();
		initClasses();
		initResourcesContents();
		initGUI();
		this.setTitle("NER @Note - Lexical Resources");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	@SuppressWarnings("unchecked")
	private void initResourcesContents() {
		resources = (List<IResource<IResourceElement>>) getParam().get(0);
		resourceSelectedClassContent = new ArrayList<Set<Integer>>();
		resourceallClassContent = new ArrayList<Set<Integer>>();
		for(IResource<IResourceElement> resource:resources)
		{	
			resourceSelectedClassContent.add(resource.getClassContent());
			resourceallClassContent.add(resource.getClassContent());
		}
	}


	private void initClasses() {
		classClassID = new HashMap<String, Integer>();
		try {
			classIDclass = ResourcesHelp.getClassIDClassOnDatabase(database);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Integer classID:classIDclass.keySet())
		{
			classClassID.put(classIDclass.get(classID), classID);
		}
	}

	private void initGUI() {
		setEnableDoneButton(false);
		{
			jPanelUpperPanel = new JPanel();
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				{
					jPanelPresentationPAnel = new JPanel();
					GridBagLayout jPanelPresentationPAnelLayout = new GridBagLayout();
					jPanelUpperPanel.add(jPanelPresentationPAnel, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPresentationPAnelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelPresentationPAnelLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelPresentationPAnelLayout.columnWeights = new double[] {0.1, 0.025, 0.1};
					jPanelPresentationPAnelLayout.columnWidths = new int[] {7, 7, 7};
					jPanelPresentationPAnel.setLayout(jPanelPresentationPAnelLayout);
					{
						jScrollPaneQueries = new JScrollPane();
						jPanelPresentationPAnel.add(jScrollPaneQueries, new GridBagConstraints(0, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							ListModel jListQueriesModel = fillJlistResources();
							jListResources = new JList();
							jScrollPaneQueries.setViewportView(jListResources);
							jListResources.setModel(jListQueriesModel);
							jListResources.addMouseListener(new MouseListener() {

								public void mouseClicked(MouseEvent arg0) {
									changeResource();
								}

								public void mouseEntered(MouseEvent arg0) {
								}

								public void mouseExited(MouseEvent arg0) {
								}

								public void mousePressed(MouseEvent arg0) {
								}

								public void mouseReleased(MouseEvent arg0) {
								}
							});
						}
					}
					{
						jScrollPanePubs = new JScrollPane();
						jPanelPresentationPAnel.add(jScrollPanePubs, new GridBagConstraints(2, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPresentationPAnel.add(getJPanelOptionsPanel(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							TableModel jTablePubsModel = 
								new DefaultTableModel();
							jTablePubs = new JTable(){

								private static final long serialVersionUID = -4090662450740771673L;

								@Override
								public boolean isCellEditable(int x,int y){
									if(y==1)
										return true;
									return false;
								}
								
								public Class<?> getColumnClass(int column){
									if(column == 1)
										return Boolean.class;
									return String.class;
								}
							};
							jScrollPanePubs.setViewportView(jTablePubs);
							jTablePubs.setModel(jTablePubsModel);
						}
					}
				}
			}
			getJScrollPaneUpPanel().setViewportView(jPanelUpperPanel);
		}
	}
	
	private void getDatabase()
	{
		ArrayList<String> elements = new ArrayList<String>();
		elements.add("DB-Host");
		elements.add("DB-Port");
		elements.add("DB-Schema");
		elements.add("DB-User");
		elements.add("DB-Pwd");
		ArrayList<String> data = Configuration.getElementByXMLFile("conf/settings.conf",elements);
		database = new MySQLDatabase(data.get(0),data.get(1),data.get(2),data.get(3),data.get(4));
	}
	
	protected void changeResource() {
		
		jTablePubs.setModel(getTableModel());
		constructTable();
		jTablePubs.updateUI();
	}

	private void constructTable() {
		box = new JCheckBox();
		box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(box.isSelected())
					add();
				else
					remve();
			}
		});
		
		jTablePubs.getColumnModel().getColumn(1).setMaxWidth(25);
		jTablePubs.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(box));	
		jTablePubs.getColumnModel().getColumn(1).setMinWidth(15);
		jTablePubs.getColumnModel().getColumn(1).setPreferredWidth(15);
	}

	protected void add() {
		int selectedResource = jListResources.getSelectedIndex();
		String classname = (String) jTablePubs.getValueAt(jTablePubs.getSelectedRow(),0);
		Integer classID = classClassID.get(classname);
		resourceSelectedClassContent.get(selectedResource).add(classID);
	}

	protected void remve() {
		int selectedResource = jListResources.getSelectedIndex();
		String classname = (String) jTablePubs.getValueAt(jTablePubs.getSelectedRow(),0);
		Integer classID = classClassID.get(classname);
		resourceSelectedClassContent.get(selectedResource).remove(classID);
	}

	private TableModel getTableModel(){
		
		String[] columns = new String[] {"Class",""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);
		
		Object[][] data;
		int i = 0;
		int selectedResource = jListResources.getSelectedIndex();
		Set<Integer> all = resourceallClassContent.get(selectedResource);
		Set<Integer> selected = resourceSelectedClassContent.get(selectedResource);
		data = new Object[all.size()][2];
			
		for(Integer classContentID: all)
		{
			data[i][0] = classIDclass.get(classContentID);
			if(selected.contains(classContentID))
			{
				data[i][1] = new Boolean(true);
			}
			else
			{
				data[i][1] = new Boolean(false);
			}
			tableModel.addRow(data[i]);
			i++;
		}
		return tableModel;		
	}
	
	private  DefaultComboBoxModel fillJlistResources(){

		DefaultComboBoxModel list = new DefaultComboBoxModel();	
		for(IResource<IResourceElement> resource:resources)
		{
			list.addElement(resource);
		}
		return list;
	}	
	
	public void done() {}

	public void goBack() {
		this.setVisible(false);
		new NERAnoteOperationWizard2(600,400,new ArrayList<Object>());
	}

	public void goNext() {
		
		ResourcesToNerAnote resouNerAnote = new ResourcesToNerAnote();
		for(int i=0;i<resources.size();i++)
		{
			Set<Integer> selected = resourceSelectedClassContent.get(i);
			Set<Integer> all = resourceallClassContent.get(i);
			IResource<IResourceElement> res = resources.get(i);
			resouNerAnote.add(res, all, selected);
		}
		List<Object> param = getParam();
		param.add(resouNerAnote);
		this.setVisible(false);
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		Corpus corpus = (Corpus) obj;
		
		ResourcesToNerAnote resources = (ResourcesToNerAnote) getParam().get(1);
		
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("Corpus", Corpus.class,corpus, null),
				new ParamSpec("resources", ResourcesToNerAnote.class,resources, null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.neranote")){			
				Workbench.getInstance().executeOperation(def, paramsSpec);
				this.setVisible(false);
				this.dispose();
				this.setModal(false);
				return;
			}
		}
	}
	
	private JPanel getJPanelOptionsPanel() {
		if(jPanelOptionsPanel == null) {
			jPanelOptionsPanel = new JPanel();
			GridBagLayout jPanelOptionsPanelLayout = new GridBagLayout();
			jPanelOptionsPanelLayout.rowHeights = new int[] {7};
			jPanelOptionsPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOptionsPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		}
		return jPanelOptionsPanel;
	}
	
	
	public void finish() {
		this.setVisible(false);
		this.dispose();
		Workbench.getInstance().warn("Operation Ner Anote Cancel");
		return;
	}

}
