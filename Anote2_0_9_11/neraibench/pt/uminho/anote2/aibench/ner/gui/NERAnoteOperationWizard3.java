package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.ner.datastructures.ResourcesToNerAnote;
import pt.uminho.anote2.aibench.ner.settings.NERLexicalResourcesDefaultSettings;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.GenericTriple;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationWizard3 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JList jListResources;
	private JPanel jPanel1;
	private JCheckBox jCheckBoxCaseSensative;
	private JTable jTableLexicalResources;
	private JScrollPane jScrollPanePubs;
	private JScrollPane jScrollPaneQueries;
	private JPanel jPanelPresentationPAnel;
	private JPanel jPanelOptionsPanel;
	private JCheckBox box;
	private List<Set<Integer>>  resourceSelectedClassContent;
	private List<Set<Integer>>  resourceallClassContent;
	private List<IResource<IResourceElement>> resources;
	private boolean usingRulePartialMatichingWithDictionaries;

	public NERAnoteOperationWizard3(List<Object> param) throws DatabaseLoadDriverException, SQLException {
		super(param);
		initResourcesContents();
		updateJlist();
		initGUI();
		if(param.get(1) instanceof ResourcesToNerAnote)
		{
			fillWithPreviousSettings(param.get(1));
			param.remove(1);
		}
		else
		{
			fiilDefautSettings();
		}
		this.setTitle("NER @Note - Select Resources Class(es)");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void fillWithPreviousSettings(Object object) {
		ResourcesToNerAnote resouNerAnote = (ResourcesToNerAnote) object;
		usingRulePartialMatichingWithDictionaries = resouNerAnote.isUseOtherResourceInformationInRules();
		jCheckBoxCaseSensative.setSelected(resouNerAnote.isCaseSensitive());
		List<GenericTriple<IResource<IResourceElement>, Set<Integer>, Set<Integer>>> data = resouNerAnote.getList();
		for(int i=0;i<data.size();i++)
		{
			resourceSelectedClassContent.add(i, data.get(i).getZ());
		}
	}

	private void fiilDefautSettings() {
		usingRulePartialMatichingWithDictionaries = (Boolean) getParam().get(1);
		boolean caseSensitive = (Boolean) PropertiesManager.getPManager().getProperty(NERLexicalResourcesDefaultSettings.CASE_SENSITIVE);
		jCheckBoxCaseSensative.setSelected(caseSensitive);
	}

	public void updateJlist()
	{
		ListModel jListQueriesModel = fillJlistResources();
		jListResources.setModel(jListQueriesModel);
		jListResources.updateUI();
	}


	@SuppressWarnings("unchecked")
	private void initResourcesContents() throws DatabaseLoadDriverException, SQLException {
		resources = (List<IResource<IResourceElement>>) getParam().get(0);
		resourceSelectedClassContent = new ArrayList<Set<Integer>>();
		resourceallClassContent = new ArrayList<Set<Integer>>();
		for(IResource<IResourceElement> resource:resources)
		{
			Set<Integer> set = resource.getClassContent();
			Set<Integer> set2 = resource.getClassContent();

			resourceSelectedClassContent.add(set);
			resourceallClassContent.add(set2);
		}
	}


	private void initGUI() {
		setEnableDoneButton(false);	
	}
	
	protected void changeResource() {
		
		jTableLexicalResources.setModel(getTableModel());
		constructTable();
		jTableLexicalResources.updateUI();
	}

	private void constructTable() {
		box = new JCheckBox();
		box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				if(box.isSelected())
					add();
				else
					remove();
			}
		});
		
		jTableLexicalResources.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		jTableLexicalResources.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(box));	
		jTableLexicalResources.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jTableLexicalResources.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
	}

	protected void add() {
		int selectedResource = jListResources.getSelectedIndex();
		String classname = (String) jTableLexicalResources.getValueAt(jTableLexicalResources.getSelectedRow(),0);
		Integer classID = ClassProperties.getClassClassID().get(classname);
		resourceSelectedClassContent.get(selectedResource).add(classID);
	}

	protected void remove() {
		int selectedResource = jListResources.getSelectedIndex();
		String classname = (String) jTableLexicalResources.getValueAt(jTableLexicalResources.getSelectedRow(),0);
		Integer classID = ClassProperties.getClassClassID().get(classname);
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
			data[i][0] = ClassProperties.getClassIDClass().get(classContentID);
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
		closeView();
		List<Object> param = getParam();
		param.add(usingRulePartialMatichingWithDictionaries);
		new NERAnoteOperationWizard2(param);
	}

	public void goNext() {		
		ResourcesToNerAnote resouNerAnote = new ResourcesToNerAnote(jCheckBoxCaseSensative.isSelected(),usingRulePartialMatichingWithDictionaries);
		for(int i=0;i<resources.size();i++)
		{
			Set<Integer> selected = resourceSelectedClassContent.get(i);
			Set<Integer> all = resourceallClassContent.get(i);
			IResource<IResourceElement> res = resources.get(i);
			resouNerAnote.add(res, all, selected);
		}
		List<Object> param = getParam();
		param.remove(1);
		param.add(resouNerAnote);
		closeView();
		try {
			new NERAnoteOperationWizard4(param);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();		}
	}
	
	private JPanel getJPanelOptionsPanel() {
		if(jPanelOptionsPanel == null) {
			jPanelOptionsPanel = new JPanel();
			GridBagLayout jPanelOptionsPanelLayout1 = new GridBagLayout();
			jPanelOptionsPanelLayout1.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelOptionsPanelLayout1.rowHeights = new int[] {7, 7, 7};
			jPanelOptionsPanelLayout1.columnWeights = new double[] {0.1};
			jPanelOptionsPanelLayout1.columnWidths = new int[] {7};
			jPanelOptionsPanel.setLayout(jPanelOptionsPanelLayout1);
			jPanelOptionsPanel.setBorder(BorderFactory.createTitledBorder("Case Sensitive"));
			jPanelOptionsPanel.add(getJCheckBoxCaseSensative(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			GridBagLayout jPanelOptionsPanelLayout = new GridBagLayout();
			jPanelOptionsPanelLayout.rowHeights = new int[] {7};
			jPanelOptionsPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOptionsPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		}
		return jPanelOptionsPanel;
	}

	public JComponent getMainComponent() {
		if(jPanelUpperPanel==null)
		{
			jPanelUpperPanel = new JPanel();
			jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder(null, "Select Resource Class(es)", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			jPanelUpperPanelLayout.rowWeights = new double[] {0.0, 0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				{
					jPanelPresentationPAnel = new JPanel();
					GridBagLayout jPanelPresentationPAnelLayout = new GridBagLayout();
					jPanelUpperPanel.add(jPanelPresentationPAnel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPresentationPAnelLayout.rowWeights = new double[] {0.1, 0.0};
					jPanelPresentationPAnelLayout.rowHeights = new int[] {7, 7};
					jPanelPresentationPAnelLayout.columnWeights = new double[] {0.1, 0.0, 0.1};
					jPanelPresentationPAnelLayout.columnWidths = new int[] {7, 7, 7};
					jPanelPresentationPAnel.setLayout(jPanelPresentationPAnelLayout);
					{
						jScrollPaneQueries = new JScrollPane();
						jPanelPresentationPAnel.add(jScrollPaneQueries, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							jListResources = new JList();
							jScrollPaneQueries.setViewportView(jListResources);
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
							jListResources.addKeyListener(new KeyListener() {
								
								@Override
								public void keyTyped(KeyEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void keyReleased(KeyEvent arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void keyPressed(KeyEvent arg0) {
									changeResource();									
								}
							});
						}
					}
					{
						jScrollPanePubs = new JScrollPane();
						jPanelPresentationPAnel.add(jScrollPanePubs, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPresentationPAnel.add(getJPanelOptionsPanel(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						{
							TableModel jTablePubsModel = 
								new DefaultTableModel();
							jTableLexicalResources = new JTable(){

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
							jScrollPanePubs.setViewportView(jTableLexicalResources);
							jTableLexicalResources.setModel(jTablePubsModel);
						}
					}
				}
			}
		}
		return jPanelUpperPanel;
	}
	
	private JCheckBox getJCheckBoxCaseSensative() {
		if(jCheckBoxCaseSensative == null) {
			jCheckBoxCaseSensative = new JCheckBox();
			jCheckBoxCaseSensative.setText("Case Sensitive");
		}
		return jCheckBoxCaseSensative;
	}
	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Create_Annotation_Schema_By_NER_Lexical_Resources#Select_Class_and_Case_Sensitivity";
	}

}
