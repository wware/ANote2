package pt.uminho.anote2.aibench.ner.gui.help;

import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.resources.QueriesResources;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lexiacalwords.LexicalWords;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.resources.ontology.Ontology;
import pt.uminho.anote2.datastructures.resources.rule.RulesSet;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;

public class ResourcesFinderGUIHelp {
	


	private List<IResource<IResourceElement>> dictionaries;
	private List<IResource<IResourceElement>> lookupTables;
	private List<IResource<IResourceElement>> rules;
	private List<IResource<IResourceElement>> ontologies;
	private List<IResource<IResourceElement>> lexicalWords;

	private static String[] columns = new String[] {"Resource",""};
	private static String[] columnsExtended = new String[] {"Resource","Classes","Notes",""};

	
	public ResourcesFinderGUIHelp() throws SQLException, DatabaseLoadDriverException
	{
		fillModelQuerysTable();
	}


	private void fillModelQuerysTable() throws SQLException, DatabaseLoadDriverException{

		dictionaries = new ArrayList<IResource<IResourceElement>>();
		lookupTables = new ArrayList<IResource<IResourceElement>>();
		rules = new ArrayList<IResource<IResourceElement>>();
		ontologies = new ArrayList<IResource<IResourceElement>>();
		lexicalWords = new ArrayList<IResource<IResourceElement>>();
		PreparedStatement resourceType = Configuration.getDatabase().getConnection().prepareStatement(QueriesResources.selectAllresources);
		ResultSet rs = resourceType.executeQuery();
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
		resourceType.close();
	}

	private void makeResource(int id, String type,String name, String note) {
		if(type.equals(GlobalOptions.resourcesDictionaryName))
		{
			dictionaries.add(new Dictionary( id, name, note));
		}
		else if(type.equals(GlobalOptions.resourcesLookupTableName))
		{
			lookupTables.add(new LookupTable(id, name, note));
		}
		else if(type.equals(GlobalOptions.resourcesOntologyName))
		{
			ontologies.add(new Ontology(id, name, note));
		}
		else if(type.equals(GlobalOptions.resourcesRuleSetName))
		{
			rules.add(new RulesSet(id, name, note));
		}
		else if(type.equals(GlobalOptions.resourcesLexicalWords))
		{
			lexicalWords.add(new LexicalWords(id, name, note));
		}
	}
	
	
	public List<IResource<IResourceElement>> getDictionaries() {
		return dictionaries;
	}


	public List<IResource<IResourceElement>> getLookupTables() {
		return lookupTables;
	}


	public List<IResource<IResourceElement>> getRules() {
		return rules;
	}


	public List<IResource<IResourceElement>> getOntologies() {
		return ontologies;
	}
	
	public synchronized List<IResource<IResourceElement>> getLexicalWords() {
		return lexicalWords;
	}
	
	private JTable createJtableSelection()
	{
		JTable jTable = new JTable()
		{

			private static final long serialVersionUID = 1L;

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
		return jTable;
	}
	
	private JTable createJtableExtendedSelection() {
		JTable jTable = new JTable()
		{

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int x,int y){
				if(y==3)
					return true;
				return false;
			}
			
			public Class<?> getColumnClass(int column){
				if(column == 3)
					return Boolean.class;
				return String.class;
			}
		};
		return jTable;
	}
	
	public JTable getDictionariesjTable()
	{
		JTable dics = createJtableSelection();
		setModalJTableResources(dics,this.dictionaries);
		constructTable(dics);
		return dics;
	}
	
	public JTable getDictionariesjTableDetails() throws DatabaseLoadDriverException, SQLException
	{
		JTable dics = createJtableExtendedSelection();
		setModalJTableResourcesDetails(dics,this.dictionaries);
		constructTableDetails(dics);
		return dics;
	}


	public JTable getLookupTablejTable()
	{
		JTable dics = createJtableSelection();
		setModalJTableResources(dics,this.lookupTables);
		constructTable(dics);
		return dics;
	}
	
	public JTable getOntologiesjTable()
	{
		JTable dics = createJtableSelection();
		setModalJTableResources(dics,this.ontologies);
		constructTable(dics);
		return dics;
	}
	
	public JTable getRulesSetjTable()
	{
		JTable dics = createJtableSelection();
		setModalJTableResources(dics,this.rules);
		constructTable(dics);
		return dics;
	}
	
	public JTable getLexicalWordsjTable()
	{
		JTable dics = createJtableSelection();
		setModalJTableResources(dics,this.lexicalWords);
		constructTable(dics);
		return dics;
	}

	private void setModalJTableResources(JTable jtable, List<IResource<IResourceElement>> resources) {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);		
		Object[] data = new Object[2];
		for(IResource<IResourceElement> resource: resources)
		{
			data[0] = resource;
			data[1] = new Boolean(false);
			tableModel.addRow(data);
		}
		jtable.setModel(tableModel);
		constructTable(jtable);
	}
	
	private void setModalJTableResourcesDetails(JTable jtable, List<IResource<IResourceElement>> resources) throws DatabaseLoadDriverException, SQLException {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columnsExtended);		
		Object[] data = new Object[4];
		for(IResource<IResourceElement> resource: resources)
		{
			data[0] = resource;
			data[1] = getResoucesClasses(resource);
			data[2] = resource.getInfo();
			data[3] = new Boolean(false);
			tableModel.addRow(data);
		}
		jtable.setModel(tableModel);
		constructTableDetails(jtable);
	}
	
	private String getResoucesClasses(IResource<IResourceElement> resource) throws DatabaseLoadDriverException, SQLException {
		Set<Integer> classes = resource.getClassContent();
		String result = new String();
		if(classes.size()==0)
		{
			return result;
		}
		for(int classID : classes)
		{
			result = result + ClassProperties.getClassIDClass().get(classID) + ",";
		}
		return result.substring(0, result.length()-1);
	}


	private void constructTable(JTable table) {
		table.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for (int j = 0; j < 1; j++) {
			table.getColumnModel().getColumn(j).setCellRenderer(renderer);
			table.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}
	
	private void constructTableDetails(JTable table)
	{
		table.getColumnModel().getColumn(3).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(3).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(3).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(0).setCellRenderer(new ButtonTableRender());
		table.setRowHeight(20);
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();		
		// mudar numeros de colunas
		for (int j = 0; j < 2; j++) {
			table.getColumnModel().getColumn(j).setCellRenderer(renderer);
			table.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}
	
	class ButtonTableRender extends ButtonTableRenderer
	{

		private static final long serialVersionUID = 1L;
		private JTextPane viewButton;
	
		public ButtonTableRender()
		{
			viewButton = new JTextPane();
		}
		
		public void whenClick() {
			
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			viewButton.setText(((IResource<IResourceElement>)value).getName());
			return viewButton;
		}
		
	}
	
	
}
