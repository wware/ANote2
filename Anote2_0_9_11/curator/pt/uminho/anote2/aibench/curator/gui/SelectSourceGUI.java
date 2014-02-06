package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.utils.MenuItem;
import pt.uminho.anote2.datastructures.utils.Source;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class SelectSourceGUI extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<Integer, Source> sourceIDSource;
	private MenuItem item;
	private JPanel jPanelUpperPanel;
	private JList jListSources;
	private JList jListSourceLingage;
	private JScrollPane jScrollPaneSources;
	
	
	public SelectSourceGUI(Map<Integer, Source> sourceIDSource, MenuItem item,JList jListSourceLingage) {
		super("Select Link-out Sources");
		initParameters(sourceIDSource, item);
		this.jListSourceLingage = jListSourceLingage;
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}


	private void initParameters(Map<Integer, Source> sourceIDSource,MenuItem item) {
		this.sourceIDSource = sourceIDSource;
		this.item = item;
		initGUI();
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		super.changeOkButtonName("Add");
	}


	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelUpperPanel = new JPanel();
				GridLayout jPanelUpperPanelLayout = new GridLayout(1, 1);
				jPanelUpperPanelLayout.setColumns(1);
				jPanelUpperPanelLayout.setHgap(5);
				jPanelUpperPanelLayout.setVgap(5);
				jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
				jScrollPaneSources = new JScrollPane();
				getContentPane().add(getJPanelUpperPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelUpperPanel.add(jScrollPaneSources);
				}
				jScrollPaneSources.setViewportView(getSourcesList());
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			} 
		}
		
	}
	
	public JList getSourcesList()
	{
		if(jListSources == null)
		{
			jListSources = new JList();
			jListSources.setModel(getSourcesModel());
		}
		return jListSources;
	}
	
	private ListModel getSourcesModel() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(Source source :sourceIDSource.values())
		{
			model.addElement(source);
		}
		for(int sourceID:item.getSourcesLinkage())
		{
				Source source = sourceIDSource.get(sourceID);
				model.removeElement(source);
		}
		return model;
	}


	public JPanel getJPanelUpperPanel() {
		return jPanelUpperPanel;
	}


	@Override
	protected void okButtonAction() {
		Object[] objects = jListSources.getSelectedValues();
		List<Source> list = new ArrayList<Source>();
		for(Object source:objects)
		{
			list.add((Source) source);
		}
		if(list == null ||  list.size() == 0)
		{
			Workbench.getInstance().warn("Please Select one or more Sources");
		}
		else
		{
			for(Source source:list)
			{
				item.getSourcesLinkage().add(source.getSourceID());
				((DefaultComboBoxModel) jListSourceLingage.getModel()).addElement(source);
			}
			try {
				HelpDatabase.addMenuItemSourceLinkage(item, list);
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
			finish();
		}
	}


	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Linkout_Management#Add_Sources_to_Link-out";
	}

}
