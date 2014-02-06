package pt.uminho.anote2.workflow.gui.panes;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import pt.uminho.anote2.aibench.ner.gui.help.ResourcesFinderGUIHelp;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDictionary;

public class ResourcesSelectDictionaries extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ResourcesFinderGUIHelp resources;
	private JTable jTableDictionaries;
	private JScrollPane jScrollPaneDictionaries;

	public ResourcesSelectDictionaries() throws SQLException, DatabaseLoadDriverException
	{
		resources = new ResourcesFinderGUIHelp();
		initGUI();
	}

	private void initGUI() throws DatabaseLoadDriverException, SQLException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createTitledBorder("Select Dictionaries"));
			{
				jScrollPaneDictionaries = new JScrollPane();
				this.add(jScrollPaneDictionaries, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jTableDictionaries = resources.getDictionariesjTableDetails();
					jScrollPaneDictionaries.setViewportView(jTableDictionaries);
				}
			}
		}		
	}

	public List<IResource<IResourceElement>> getSelectResources() {
		List<IResource<IResourceElement>> list = new ArrayList<IResource<IResourceElement>>();
		int rows = jTableDictionaries.getModel().getRowCount();
		for(int i=0;i<rows;i++)
		{
			boolean isactive = (Boolean) jTableDictionaries.getModel().getValueAt(i, 3);
			if(isactive)
			{
				IResource<IResourceElement> resource = (IResource<IResourceElement>) jTableDictionaries.getModel().getValueAt(i, 0);
				list.add(resource);
			}
		}
		return list;
	}

	public void setResourcesSelection(List<IResource<IResourceElement>> resources) {
		List<Integer> ids = new ArrayList<Integer>();
		for(IResource<IResourceElement> resource : resources)
		{
			ids.add(resource.getID());
		}
		for(int i=0;i<jTableDictionaries.getModel().getRowCount();i++)
		{
			IDictionary dic = (IDictionary) jTableDictionaries.getValueAt(i, 0);
			if(ids.contains(dic.getID()))
			{
				jTableDictionaries.setValueAt(true, i, 3);
			}
			else
			{
				jTableDictionaries.setValueAt(false, i, 3);
			}
		}
		
	}
	
}
