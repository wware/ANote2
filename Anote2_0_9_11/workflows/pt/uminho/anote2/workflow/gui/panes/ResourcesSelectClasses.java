package pt.uminho.anote2.workflow.gui.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;


public class ResourcesSelectClasses extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<IResource<IResourceElement>> resources;
	private JTable jTableClasses;
	private JScrollPane jScrollPaneClasses;
	private static String[] columns = new String[] {"Class",""};

	public ResourcesSelectClasses(List<IResource<IResourceElement>> resources) throws DatabaseLoadDriverException, SQLException
	{
		this.resources = resources;
		initGUI();
	}

	private void initGUI() throws DatabaseLoadDriverException, SQLException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(640, 453));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createTitledBorder("Select Class(es)"));
			{
				jScrollPaneClasses = new JScrollPane();
				this.add(jScrollPaneClasses, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jScrollPaneClasses.setViewportView(getTableClasses());
				}
			}
		}

	}

	private JTable getTableClasses() throws DatabaseLoadDriverException, SQLException {
		if(jTableClasses == null)
		{
			jTableClasses = createTable();
			setTableModel(jTableClasses);
		}
		return jTableClasses;
	}
	
	

	private void setTableModel(JTable jtable) throws DatabaseLoadDriverException, SQLException {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);		
		Object[] data = new Object[2];
		Set<Integer> classesIDs = calculateClassID();
		for(int classID: classesIDs)
		{
			data[0] = ClassProperties.getClassIDClass().get(classID);
			data[1] = new Boolean(true);
			tableModel.addRow(data);
		}
		jtable.setModel(tableModel);
		constructTable(jtable);	
	}

	private void constructTable(JTable table) {
		table.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		table.getColumnModel().getColumn(1).setPreferredWidth(GlobalGUIOptions.tableSelectionSize);
	}

	private Set<Integer> calculateClassID() throws DatabaseLoadDriverException, SQLException {
		Set<Integer> classIDs = new HashSet<Integer>();
		for(IResource<IResourceElement> resource : this.resources)
		{
			classIDs.addAll(resource.getClassContent());
		}
		return classIDs;
	}

	private JTable createTable() {
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
	
	public List<Integer> getSelectedClasses()
	{
		List<Integer> list = new ArrayList<Integer>();
		int rows = jTableClasses.getModel().getRowCount();
		for(int i=0;i<rows;i++)
		{
			boolean isactive = (Boolean) jTableClasses.getModel().getValueAt(i, 1);
			if(isactive)
			{
				String classe = (String) jTableClasses.getModel().getValueAt(i, 0);
				list.add(ClassProperties.getClassClassID().get(classe));
			}
		}
		return list;
	}

}
