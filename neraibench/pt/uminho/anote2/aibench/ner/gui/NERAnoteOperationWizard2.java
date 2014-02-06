package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.utils.conf.Configuration;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.database.MySQLDatabase;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class NERAnoteOperationWizard2 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;

	private JPanel jPanelUpperPanel;
	private JButton jButtonAddRuleSet;
	private JButton jButtonAddLookup;
	private JPanel jPanelListResources;
	private JPanel jPanelButtonsMovePanel;
	private JButton jButtonAdd;
	private JButton jButtonRemove;
	private JButton jButtonUpAll;
	private JList jListSelectedResources;
	private JLabel jLabelSelectQueries;
	private JProgressBar jProgressBar;
	private JPanel jPanelBarPanel;
	private JPanel jPanelPresentationPAnel;
	
	private IDatabase database;
	private List<IResource<IResourceElement>> dics;
	private JScrollPane jScrollPaneLookupTables;
	private JList jListRuleSet;
	private JScrollPane jScrollPaneRuleSet;
	private JList jListLookupTable;
	private JLabel jLabelAddRulesSet;
	private JLabel jLabelLookups;
	private JList jListDics;
	private JScrollPane jScrollPaneDics;
	private JLabel jLabelDictionaries;
	private List<IResource<IResourceElement>> look;
	private List<IResource<IResourceElement>> rules;
	
	private IResource<IResourceElement> dic;
	private IResource<IResourceElement> loo;
	private IResource<IResourceElement> rule;


	public NERAnoteOperationWizard2(int sizeH, int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		getDatabase();
		try {
			fillModelQuerysTable("");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initGUI();
		setModelToResources();
		this.setTitle("NER @Note - Lexical Resources");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
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
				jPanelPresentationPAnel = new JPanel();
				GridBagLayout jPanelPresentationPAnelLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelPresentationPAnel, new GridBagConstraints(0, 1, 14, 13, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPresentationPAnelLayout.rowWeights = new double[] {0.0, 0.1, 0.0, 0.1};
				jPanelPresentationPAnelLayout.rowHeights = new int[] {7, 20, 7, 7};
				jPanelPresentationPAnelLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.0};
				jPanelPresentationPAnelLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelPresentationPAnel.setLayout(jPanelPresentationPAnelLayout);
				{
					jLabelSelectQueries = new JLabel();
					jPanelPresentationPAnel.add(jLabelSelectQueries, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelSelectQueries.setText("Select Resources");
				}
				{
					ListModel jListSelectedQueriesModel = 
						new DefaultComboBoxModel(
								new String[] {});
					jListSelectedResources = new JList();
					jPanelPresentationPAnel.add(jListSelectedResources, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jListSelectedResources.setModel(jListSelectedQueriesModel);
				}
				{
					jPanelButtonsMovePanel = new JPanel();
					GridBagLayout jPanelButtonsMovePanelLayout = new GridBagLayout();
					jPanelPresentationPAnel.add(jPanelButtonsMovePanel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPresentationPAnel.add(getJPanelListResources(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelButtonsMovePanelLayout.rowWeights = new double[] {0.1, 0.1};
					jPanelButtonsMovePanelLayout.rowHeights = new int[] {7, 20};
					jPanelButtonsMovePanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
					jPanelButtonsMovePanelLayout.columnWidths = new int[] {7, 7, 20, 7, 7};
					jPanelButtonsMovePanel.setLayout(jPanelButtonsMovePanelLayout);
					{
						jButtonUpAll = new JButton();
						jPanelButtonsMovePanel.add(jButtonUpAll, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonUpAll.setText("Remove All");
						jButtonUpAll.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/upall.png")));
						jButtonUpAll.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								removeAlll();
							}
						});
					}
					{
						jButtonRemove = new JButton();
						jPanelButtonsMovePanel.add(jButtonRemove, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jButtonRemove.setText("Remove");
						jButtonRemove.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/up.png")));
						jButtonRemove.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								remove();
							}
						});
					}
				}
			}
		}
		getJScrollPaneUpPanel().setViewportView(jPanelUpperPanel);
	}
	
	@SuppressWarnings("unchecked")
	protected void removeAlll() {
		int size = ((DefaultComboBoxModel) jListSelectedResources.getModel()).getSize();
		for(int i=0;i<size;i++)
		{
			Object obj = ((DefaultComboBoxModel) jListSelectedResources.getModel()).getElementAt(i);	
			IResource<IResourceElement> objResource = (IResource<IResourceElement>) obj;
			if(objResource.getType().equals("Dictionary"))
			{
				((DefaultComboBoxModel) jListDics.getModel()).addElement(objResource);
			}
			else if(objResource.getType().equals("LookUpTable"))
			{
				((DefaultComboBoxModel) jListLookupTable.getModel()).addElement(objResource);
			}
			else if(objResource.getType().equals("Rules"))
			{
				((DefaultComboBoxModel) jListRuleSet.getModel()).addElement(objResource);
			}
		}
		loo=null;
		rule=null;
		dic=null;
		((DefaultComboBoxModel) jListSelectedResources.getModel()).removeAllElements();
		updateGUI();
	}
	
	@SuppressWarnings("unchecked")
	protected void remove() {
		
		Object[] objects = jListSelectedResources.getSelectedValues();
		for(Object obj:objects)
		{
			IResource<IResourceElement> objResource = (IResource<IResourceElement>) obj;
			((DefaultComboBoxModel) jListSelectedResources.getModel()).removeElement(obj);
			if(objResource.getType().equals("Dictionary"))
			{
				((DefaultComboBoxModel) jListDics.getModel()).addElement(objResource);
				dic=null;
			}
			else if(objResource.getType().equals("LookUpTable"))
			{
				((DefaultComboBoxModel) jListLookupTable.getModel()).addElement(objResource);
				look=null;
			}
			else if(objResource.getType().equals("Rules"))
			{
				((DefaultComboBoxModel) jListRuleSet.getModel()).addElement(objResource);
				rule=null;
			}
		}
		updateGUI();
	}

	@SuppressWarnings("unchecked")
	protected void addDic() {
		Object objects = jListDics.getSelectedValues()[0];
		if(objects==null)
		{
			Workbench.getInstance().warn("Select Dictionary");
		}
		else if(dic!=null)
		{
			Workbench.getInstance().warn("You can´t select more that one Dictionary");
		}
		else
		{
		
			((DefaultComboBoxModel) jListSelectedResources.getModel()).addElement(objects);
			((DefaultComboBoxModel) jListDics.getModel()).removeElement(objects);
			dic = (IResource<IResourceElement>) objects;
		}
		updateGUI();
	}
	
	@SuppressWarnings("unchecked")
	protected void addLookupTAble() {
		Object objects = jListLookupTable.getSelectedValues()[0];
		if(objects==null)
		{
			Workbench.getInstance().warn("Select Lookup Table");
		}
		else if(loo!=null)
		{
			Workbench.getInstance().warn("You can´t select more that one Lookup TAble");
		}
		else
		{
			((DefaultComboBoxModel) jListSelectedResources.getModel()).addElement(objects);
			((DefaultComboBoxModel) jListLookupTable.getModel()).removeElement(objects);
			loo = (IResource<IResourceElement>) objects;
		}
		updateGUI();
	}

	@SuppressWarnings("unchecked")
	protected void addLookupRule() {
		Object objects = jListRuleSet.getSelectedValues()[0];
		if(objects==null)
		{
			Workbench.getInstance().warn("Select RuleSet");
		}
		else if(rule!=null)
		{
			Workbench.getInstance().warn("You can´t select more that one RuleSet");
		}
		else
		{	
			((DefaultComboBoxModel) jListSelectedResources.getModel()).addElement(objects);
			((DefaultComboBoxModel) jListRuleSet.getModel()).removeElement(objects);
			rule = (IResource<IResourceElement>) objects;
		}
		updateGUI();
	}
	
	private void updateGUI()
	{
		jListSelectedResources.updateUI();
		jListLookupTable.updateUI();
		jListRuleSet.updateUI();
		jListDics.updateUI();
	}
	
	private void fillModelQuerysTable(String order) throws SQLException{

		dics = new ArrayList<IResource<IResourceElement>>();
		look = new ArrayList<IResource<IResourceElement>>();
		rules = new ArrayList<IResource<IResourceElement>>();
		
		String getresources = "SELECT idresources,type,name,note "+
						 "FROM resources AS q JOIN resources_type ON (q.resources_type_idresources_type=resources_type.idresources_type); ";
		
		Statement statement = database.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(getresources);
		int id;
		String type,name,note;
		while(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			makeResource(id,type,name,note);
		}
		rs.close();

	}
	
	
	
	private IResource<IResourceElement> makeResource(int id, String type,String name, String note) {
		if(type.equals("dictionary"))
		{
			dics.add(new Dictionary(database, id, name, note));
		}
		else if(type.equals("lookuptable"))
		{
			look.add(new LookupTable(database, id, name, note));
		}
		else if(type.equals("ontology"))
		{
			/**
			 * For future
			 */
		}
		else if(type.equals("rules"))
		{
			rules.add(new RulesSet(database, id, name, note));
		}
		return null;
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

	
	public void done() {}

	public void goBack() {
		this.setVisible(false);
		new NERAnoteOperationWizard1();
	}

	@SuppressWarnings("unchecked")
	public void goNext() {
	
		int size = ((DefaultComboBoxModel) jListSelectedResources.getModel()).getSize();
		if(size==0)
		{
			Workbench.getInstance().warn("Please select Resources");
		}
		else
		{
			this.setVisible(false);
			List<IResource<IResourceElement>> resources = new ArrayList<IResource<IResourceElement>>();
			for(int i=0;i<size;i++)
			{
				Object obj = ((DefaultComboBoxModel) jListSelectedResources.getModel()).getElementAt(i);
				resources.add((IResource<IResourceElement>)obj);
			}
			List<Object> objs = getParam();
			objs.add(resources);
			new NERAnoteOperationWizard3(800,600,objs);
		}
	}
	
	public void finish() {
		this.setVisible(false);
		this.dispose();
		Workbench.getInstance().warn("Operation Ner Anote Cancel");
		return;
	}

	public static void main(String[] args) throws IOException{
		//new NERAnoteOperationWizard2(600,400,null);
	}
	
	private JPanel getJPanelListResources() {
		if(jPanelListResources == null) {
			jPanelListResources = new JPanel();
			GridBagLayout jPanelListResourcesLayout = new GridBagLayout();
			jPanelListResourcesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelListResourcesLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelListResourcesLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			jPanelListResourcesLayout.columnWidths = new int[] {7, 7, 7};
			jPanelListResources.setLayout(jPanelListResourcesLayout);
			{
				jButtonAdd = new JButton();
				jPanelListResources.add(jButtonAdd, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJLabelDictionaries(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJScrollPaneDics(), new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJLabelLookups(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJButtonAddLookup(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJButtonAddRuleSet(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJLabelAddRulesSet(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJScrollPaneLookupTables(), new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelListResources.add(getJScrollPaneRuleSet(), new GridBagConstraints(2, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jButtonAdd.setText("Add Dictionary");
				jButtonAdd.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/down.png")));
				jButtonAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						addDic();
					}
				});
			}
		}
		return jPanelListResources;
	}

	private JButton getJButtonAddLookup() {
		if(jButtonAddLookup == null) {
			jButtonAddLookup = new JButton();
			jButtonAddLookup.setText("Add LookupTable");
			jButtonAddLookup.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/down.png")));
			jButtonAddLookup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					addLookupTAble();
				}
			});
		}
		return jButtonAddLookup;
	}
	
	private JButton getJButtonAddRuleSet() {
		if(jButtonAddRuleSet == null) {
			jButtonAddRuleSet = new JButton();
			jButtonAddRuleSet.setText("Add RuleSet");
			jButtonAddRuleSet.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/down.png")));
			jButtonAddRuleSet.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					addLookupRule();
				}
			});
			
		}
		return jButtonAddRuleSet;
	}
	
	private void setModelToResources()
	{
		DefaultComboBoxModel listDics = new DefaultComboBoxModel();
		for(IResource<IResourceElement> resource:dics)
		{
			listDics.addElement(resource);
		}
		jListDics.setModel(listDics);
		
		DefaultComboBoxModel listLooks = new DefaultComboBoxModel();
		for(IResource<IResourceElement> resource:look)
		{
			listLooks.addElement(resource);
		}
		jListLookupTable.setModel(listLooks);
		
		DefaultComboBoxModel listRules = new DefaultComboBoxModel();
		for(IResource<IResourceElement> resource:rules)
		{
			listRules.addElement(resource);
		}
		jListRuleSet.setModel(listRules);	
	}
	
	private JLabel getJLabelDictionaries() {
		if(jLabelDictionaries == null) {
			jLabelDictionaries = new JLabel();
			jLabelDictionaries.setText("Dictionaries");
		}
		return jLabelDictionaries;
	}
	
	private JScrollPane getJScrollPaneDics() {
		if(jScrollPaneDics == null) {
			jScrollPaneDics = new JScrollPane();
			jScrollPaneDics.setViewportView(getJListDics());
		}
		return jScrollPaneDics;
	}
	
	private JList getJListDics() {
		if(jListDics == null) {
			ListModel jListDicsModel = 
				new DefaultComboBoxModel(
						new String[] {});
			jListDics = new JList();
			jListDics.setModel(jListDicsModel);
		}
		return jListDics;
	}
	
	private JLabel getJLabelLookups() {
		if(jLabelLookups == null) {
			jLabelLookups = new JLabel();
			jLabelLookups.setText("Lookup Tables");
		}
		return jLabelLookups;
	}
	
	private JLabel getJLabelAddRulesSet() {
		if(jLabelAddRulesSet == null) {
			jLabelAddRulesSet = new JLabel();
			jLabelAddRulesSet.setText("RuleSet");
		}
		return jLabelAddRulesSet;
	}
	
	private JScrollPane getJScrollPaneLookupTables() {
		if(jScrollPaneLookupTables == null) {
			jScrollPaneLookupTables = new JScrollPane();
			jScrollPaneLookupTables.setViewportView(getJListLookupTable());
		}
		return jScrollPaneLookupTables;
	}
	
	private JList getJListLookupTable() {
		if(jListLookupTable == null) {
			ListModel jListLookupTableModel = 
				new DefaultComboBoxModel(
						new String[] {});
			jListLookupTable = new JList();
			jListLookupTable.setModel(jListLookupTableModel);
		}
		return jListLookupTable;
	}
	
	private JScrollPane getJScrollPaneRuleSet() {
		if(jScrollPaneRuleSet == null) {
			jScrollPaneRuleSet = new JScrollPane();
			jScrollPaneRuleSet.setViewportView(getJListRuleSet());
		}
		return jScrollPaneRuleSet;
	}
	
	private JList getJListRuleSet() {
		if(jListRuleSet == null) {
			ListModel jListRuleSetModel = 
				new DefaultComboBoxModel(
						new String[] {});
			jListRuleSet = new JList();
			jListRuleSet.setModel(jListRuleSetModel);
		}
		return jListRuleSet;
	}

}
