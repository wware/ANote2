package pt.uminho.anote2.aibench.corpus.view;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.gui.RemoveProcessGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.manualcuration.ManualCurationEnum;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;



public class CorpusProcessesView extends JPanel implements Observer{

	private static final long serialVersionUID = 1L;
	private JPanel jPanelCorpusProcessesContent;
	private Corpus corpus;
	private List<IProcess> processes;
	private JButton jButtonHelp;
	private TableSearchPanel jTableSearch;

	public CorpusProcessesView(Corpus corpus)
	{
		super();
		this.corpus=corpus;
		this.corpus.addObserver(this);
		try {
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelCorpusProcessesContent = new JPanel();
				jPanelCorpusProcessesContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Corpus Processes", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelCorpusProcessesContentLayout = new GridBagLayout();
				this.add(jPanelCorpusProcessesContent, new GridBagConstraints(0, 0, 5, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelCorpusProcessesContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
				jPanelCorpusProcessesContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelCorpusProcessesContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelCorpusProcessesContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelCorpusProcessesContent.setLayout(jPanelCorpusProcessesContentLayout);
				{
					jPanelCorpusProcessesContent.add(getSearchPanel(), new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelCorpusProcessesContent.add(getJButton1(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
	}
	
	private JPanel getSearchPanel() throws SQLException, DatabaseLoadDriverException {
		if(jTableSearch == null) {
			jTableSearch = new TableSearchPanel();
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableSearch.setModel(getTableModelProcesses());
			completeTable(jTableSearch.getMainTable());
		}
		return jTableSearch;
	}

	/** create a schema from processes aplied to Corpus 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException */
	private TableModel getTableModelProcesses() throws SQLException, DatabaseLoadDriverException
	{
		List<IIEProcess> proce = this.corpus.getIProcesses();
		Set<String> specialcases = getSpecialCases();
		processes = new ArrayList<IProcess>(proce);
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 0 )?(Integer.class):String.class;
			}
			
		};
		String[] columns = new String[] {"ID", "Type", "Name", "Properties","DB","Load"};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;
		data = new Object[this.processes.size()][6];
		Properties prop;
		String properties;
		String value;
		for(IProcess process: processes)
		{
			IIEProcess processIE = (IIEProcess) process;
			data[i][0] = processIE.getID()+"\n";
			data[i][1] = processIE.getType()+"\n";
			data[i][2] = processIE.getName()+"\n";
			prop = processIE.getProperties();
			properties = new String();
			for(String key:prop.stringPropertyNames())
			{
				if(Utils.isIntNumber(key))
				{
					try {
						IResource<IResourceElement> elem = getResourceInfo(Integer.parseInt(key));
						properties = properties+elem.toString()+"\n";
						String classesTring = prop.getProperty(key);
						Set<Integer> classes;
						if(classesTring.equals(GlobalNames.allclasses))
						{
							classes = elem.getClassContent();
						}
						else
						{
							classes = getClassContent(classesTring);
						}
						properties = properties + "Class(es) :"+printClasses(classes) + "\n";
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				else
				{
					value = prop.getProperty(key);
					if(specialcases.contains(key))
					{
						try {
							IResource<IResourceElement> elem = getResourceInfo(Integer.parseInt(value));
							properties = properties+key+": "+elem.toString()+"\n";
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
					else
					{
						properties = properties+key+": "+value+"\n";
					}
				}
			}
			data[i][3] = properties;
			data[i][4] = "";
			data[i][5] = "";
			tableModel.addRow(data[i]);
			i++;
		}	
		return tableModel;
	}

	
	private String printClasses(Set<Integer> classes) {
		String result = new String();
		for(int classID : classes)
		{
			result = result +ClassProperties.getClassIDClass().get(classID) + ", ";
		}
		if(result.length()-2>0)
			return result.substring(0,result.length()-2);
		return result;
	}

	private Set<Integer> getClassContent(String value) {
		String[] split = value.split("\\,");
		Set<Integer> classContentID = new HashSet<Integer>();
		for(String contentID:split)
		{
			if(contentID.length()>0)
			{
				classContentID.add(Integer.valueOf(contentID));
			}
		}
		return classContentID;
	}
	private Set<String> getSpecialCases() {
		Set<String> cases = new HashSet<String>();
		cases.add(GlobalNames.verbFilter);
		cases.add(GlobalNames.verbAddition);
		cases.add(GlobalNames.stopWordsResourceID);
		return cases;
	}

	private IResource<IResourceElement> getResourceInfo(int resourceID) throws SQLException, DatabaseLoadDriverException{						 
		PreparedStatement statement = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.getResourcesInfo);
		IResource<IResourceElement> resource = null;
		statement.setInt(1, resourceID);
		ResultSet rs = statement.executeQuery();
		int id;
		String type,name,note;
		boolean active;
		if(rs.next())
		{
			id = rs.getInt(1);
			type = rs.getString(2);
			name = rs.getString(3);
			note = rs.getString(4);
			active = rs.getBoolean(5);
			resource = makeResource(id,type,name,note,active);
		}
		rs.close();
		statement.close();
		return resource;
	}
	
	private IResource<IResourceElement> makeResource(int id, String type,String name, String note, boolean active) {
		if(type.equals(GlobalOptions.resourcesDictionaryName))
		{
			return new Dictionary( id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesLookupTableName))
		{
			return new LookupTable(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesOntologyName))
		{
			return new Ontology(id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesRuleSetName))
		{
			return new RulesSet( id, name, note,active);
		}
		else if(type.equals(GlobalOptions.resourcesLexicalWords))
		{
			return new LexicalWords(id, name, note,active);
		}
		return null;
	}
	

	private void completeTable(JTable jtable) {
				
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(1).setMaxWidth(100);
		jtable.getColumnModel().getColumn(1).setMinWidth(70);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(70);
		jtable.getColumnModel().getColumn(2).setMaxWidth(750);
		jtable.getColumnModel().getColumn(2).setMinWidth(150);
		jtable.getColumnModel().getColumn(3).setMaxWidth(750);
		jtable.getColumnModel().getColumn(3).setMinWidth(150);
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);
		jtable.getColumnModel().getColumn(5).setMaxWidth(PreferencesSizeComponents.loadfieldmaxWith);
		jtable.getColumnModel().getColumn(5).setMinWidth(PreferencesSizeComponents.loadfieldminWith);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(PreferencesSizeComponents.loadfieldpreferredWith);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png"));
		jtable.getColumn("Load").setCellRenderer(new ButtonLoadResourceRenderer(icon2));
		jtable.getColumn("Load").setCellEditor(new ButtonLoadResourceCellEditor());
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for (int j = 0; j < 4; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}
	
	
	private void removeFromDB() throws SQLException, DatabaseLoadDriverException {
		if(jTableSearch.getSelectedRowsInOriginalModel()[0]==-1)
		{
			
		}
		else
		{
			IProcess process = processes.get(jTableSearch.getSelectedRowsInOriginalModel()[0]);
			new RemoveProcessGUI(process);
		}
	}
	
	
	private void loadProcess() {
		IProcess process = processes.get(jTableSearch.getSelectedRowsInOriginalModel()[0]);
		if(process.getType().equals(GlobalNames.ner)||process.getType().equals(ManualCurationEnum.NER.getProcessName()))
		{
			IIEProcess processIE = (IIEProcess) process;
			NERSchema nerProcess = new NERSchema(process.getID(),corpus, processIE.getName(),GlobalNames.ner , processIE.getProperties());
			corpus.addProcess(nerProcess);
		}
		else if(process.getType().equals(GlobalNames.re)||process.getType().equals(ManualCurationEnum.RE.getProcessName()))
		{
			IIEProcess processIE = (IIEProcess) process;
			RESchema reProcess = new RESchema(process.getID(),corpus, processIE.getName(),GlobalNames.re , processIE.getProperties());
			corpus.addProcess(reProcess);
		}
	}

	public void update(Observable arg0, Object arg1) {
		try {
			jTableSearch.setModel(getTableModelProcesses());
			completeTable(jTableSearch.getMainTable());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private JButton getJButton1() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Corpus_Load_Process");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			removeFromDB();
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			removeFromDB();	
		}
		
	}
	
	class ButtonLoadResourceRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadResourceRenderer(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() {
			loadProcess();
		}

	}

	class ButtonLoadResourceCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			loadProcess();
		}
	}

}
