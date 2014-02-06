package pt.uminho.anote2.aibench.corpus.gui.wizard.reexport;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.process.re.export.configuration.Cardinality;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfiguration;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfigurationRelationOptions;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.re.export.configuration.ICardinality;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REToXGMMLWizardStep2 extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jUpperPanel;
	private JTabbedPane jTabbedPaneRelationOptions;
	private JPanel jPanelCardinalitty;
	private JPanel jPanelPolarity;
	private JPanel jPanelDirectionally;
	private JTable jTableCardinality;
	private JTable jTablePolarity;
	private JTable jTableDirectionally;
	
	public REToXGMMLWizardStep2(List<Object> param) {
		super(param);
		InitGUI();
		if(param.size() == 3)
		{
			fillWithPreviousSettings(param.get(2));
			getParam().remove(2);
		}
		this.setTitle("RE Process to Cytoscape (XGMML) File - Relation Properties Filter");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillWithPreviousSettings(Object object) {
		REToNetworkConfigurationRelationOptions relOpt = (REToNetworkConfigurationRelationOptions) object;	
		for(int i=0;i<jTableCardinality.getModel().getRowCount();i++)
		{
			ICardinality cardinallCardinality = (ICardinality) jTableCardinality.getValueAt(i, 0);
			if(relOpt.getCardinalitiesAllowed().contains(cardinallCardinality))
			{
				jTableCardinality.setValueAt(true, i, 2);
			}
			else
			{
				jTableCardinality.setValueAt(false, i, 2);
			}
		}
		for(int j=0;j<jTablePolarity.getModel().getRowCount();j++)
		{
			PolarityEnum polarity = (PolarityEnum) jTablePolarity.getValueAt(j, 0);
			if(relOpt.getPolaritiesAllowed().contains(polarity))
			{
				jTablePolarity.setValueAt(true, j, 2);
			}
			else
			{
				jTablePolarity.setValueAt(false, j, 2);
			}
		}
		for(int k=0;k<jTableDirectionally.getModel().getRowCount();k++)
		{
			DirectionallyEnum directionally = (DirectionallyEnum) jTableDirectionally.getValueAt(k, 0);
			if(relOpt.getDirectionallyAllowed().contains(directionally))
			{
				jTableDirectionally.setValueAt(true, k, 2);
			}
			else
			{
				jTableDirectionally.setValueAt(false, k, 2);
			}
		}
	}

	private void InitGUI() {
		setEnableDoneButton(false);	
	}

	@Override
	public JComponent getMainComponent() {
		if(jUpperPanel == null)
		{
			try {
				jUpperPanel = new JPanel();
				GridLayout jUpperPanelLayout = new GridLayout(1, 1);
				jUpperPanelLayout.setVgap(5);
				jUpperPanelLayout.setHgap(5);
				jUpperPanelLayout.setColumns(1);
				jUpperPanel.setLayout(jUpperPanelLayout);
				jUpperPanel.setBorder(BorderFactory.createTitledBorder("Filtering Options"));
				this.add(jUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jTabbedPaneRelationOptions = new JTabbedPane();
					jUpperPanel.add(jTabbedPaneRelationOptions);
					{
						jPanelCardinalitty = new JPanel();
						GridLayout jPanelCardinalittyLayout = new GridLayout(1, 1);
						jPanelCardinalittyLayout.setVgap(5);
						jPanelCardinalittyLayout.setHgap(5);
						jPanelCardinalittyLayout.setColumns(1);
						jPanelCardinalitty.setLayout(jPanelCardinalittyLayout);
						jTabbedPaneRelationOptions.addTab("Cardinality", null, jPanelCardinalitty, null);
						{
							jTableCardinality = new JTable();
							jTableCardinality.setModel(getTableCardinaliteTableModel());
							completeTable(jTableCardinality);
							jPanelCardinalitty.add(jTableCardinality);
						}
					}
					{
						jPanelPolarity = new JPanel();
						GridLayout jPanelPolarityLayout = new GridLayout(1, 1);
						jPanelPolarityLayout.setVgap(5);
						jPanelPolarityLayout.setHgap(5);
						jPanelPolarityLayout.setColumns(1);
						jPanelPolarity.setLayout(jPanelPolarityLayout);
						jTabbedPaneRelationOptions.addTab("Polarity", null, jPanelPolarity, null);
						{
							jTablePolarity = new JTable();

							jTablePolarity.setModel(getTableRElationPolarityTableModel(GlobalNames.relationPropertyPolarity));
							completeTable(jTablePolarity);

							jPanelPolarity.add(jTablePolarity);
						}
					}
					{
						jPanelDirectionally = new JPanel();
						GridLayout jPanelDirectionallyLayout = new GridLayout(1, 1);
						jPanelDirectionallyLayout.setVgap(5);
						jPanelDirectionallyLayout.setHgap(5);
						jPanelDirectionallyLayout.setColumns(1);
						jPanelDirectionally.setLayout(jPanelDirectionallyLayout);
						jTabbedPaneRelationOptions.addTab("Directionally", null, jPanelDirectionally, null);
						{
							jTableDirectionally = new JTable();
							try {
								jTableDirectionally.setModel(getTableRElationDirectionallyTableModel(GlobalNames.relationPropertyDirectionally));
								completeTable(jTableDirectionally);
							} catch (SQLException e ) {
								e.printStackTrace();
							}
							jPanelDirectionally.add(jTableDirectionally);
						}
					}
				}
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		}
		return jUpperPanel;
	}

	private TableModel getTableRElationPolarityTableModel(String relationpropertypolarity) throws SQLException, DatabaseLoadDriverException {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0 )
					return String.class;
				else if(columnIndex == 1)
					return Integer.class;
				else			
					return Boolean.class;
			}
		};
		String[] columns = new String[] {"Type", "Number of Occurrences",""};
		tableModel.setColumnIdentifiers(columns);	
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.processRelationProperty);
		ps.setInt(1, ((RESchema)getParam().get(0)).getID());
		ps.setString(2, relationpropertypolarity);
		ResultSet rs = ps.executeQuery();
		Object[] data = new Object[3];	
		while(rs.next())
		{
			data[0] = PolarityEnum.covertIntToPolarityEnum(rs.getInt(1));
			data[1] = rs.getInt(2);
			data[2] = Boolean.TRUE;
			tableModel.addRow(data);
		}
		rs.close();
		ps.close();
		return tableModel;
	}
	
	private TableModel getTableRElationDirectionallyTableModel(String relationpropertypolarity) throws SQLException, DatabaseLoadDriverException {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0 )
					return String.class;
				else if(columnIndex == 1)
					return Integer.class;
				else			
					return Boolean.class;
			}
		};
		String[] columns = new String[] {"Type", "Number of Occurrences",""};
		tableModel.setColumnIdentifiers(columns);	
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.processRelationProperty);
		ps.setInt(1, ((RESchema)getParam().get(0)).getID());
		ps.setString(2, relationpropertypolarity);
		ResultSet rs = ps.executeQuery();
		Object[] data = new Object[3];	
		while(rs.next())
		{
			data[0] = DirectionallyEnum.covertIntToDirectionallyEnum(rs.getInt(1));
			data[1] = rs.getInt(2);
			data[2] = Boolean.TRUE;
			tableModel.addRow(data);
		}
		rs.close();
		ps.close();
		return tableModel;
	}

	private TableModel getTableCardinaliteTableModel() throws SQLException, DatabaseLoadDriverException {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0 )
					return String.class;
				else if(columnIndex == 1)
					return Integer.class;
				else			
					return Boolean.class;
			}
		};
		TreeSet<ICardinality> result = REToNetworkConfiguration.getAllDefaultCardinalities();
		TreeMap<ICardinality,Integer> map = new TreeMap<ICardinality, Integer>();
		for(ICardinality cardinallity : result)
		{
			map.put(cardinallity, 0);
		}
		String[] columns = new String[] {"Type", "Number of Occurrences",""};
		tableModel.setColumnIdentifiers(columns);		
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.processRelationCardinalities);
		ps.setInt(1, ((RESchema)getParam().get(0)).getID());
		ResultSet rs = ps.executeQuery();
		int left = 0;
		int right = 0;	
		int annotationID = 0;
		int annotationIDDB = 0;
		int numberEntitiesSide = 0;
		String type = "";
		while(rs.next())
		{
			annotationIDDB = rs.getInt(1);
			if(annotationID == 0)
				annotationID = annotationIDDB;
			type = rs.getString(2);
			numberEntitiesSide = rs.getInt(3);
			if(annotationIDDB != annotationID)
			{
				ICardinality cardinality = new Cardinality(left, right);
				left = 0;
				right = 0;
				if(map.containsKey(cardinality))
				{
					int value = map.get(cardinality);
					value ++;
					map.put(cardinality, value);
				}
				annotationID = annotationIDDB;
			}
			if(type.equals("left"))
				left = transform(numberEntitiesSide);
			else
				right = transform(numberEntitiesSide);
		}
		rs.close();
		ps.close();
		if(left > 0 && right > 0)
		{
			ICardinality cardinality = new Cardinality(left, right);
			if(map.containsKey(cardinality))
			{
				int value = map.get(cardinality);
				value ++;
				map.put(cardinality, value);
			}
		}
		Object[] data = new Object[3];	
		for(ICardinality card:map.keySet())
		{
			if(map.get(card)>0)
			{
				data[0] = card;
				data[1] = map.get(card);
				data[2] = Boolean.TRUE;
				tableModel.addRow(data);
			}
		}
		return tableModel;
	}

	private int transform(int numberEntitiesSide) {
		if(numberEntitiesSide>1)
		{
			return 2;
		}
		return numberEntitiesSide;
	}

	private void completeTable(JTable mainTable) {
		mainTable.setRowHeight(20);
		mainTable.getColumnModel().getColumn(2).setMaxWidth(30);
		mainTable.getColumnModel().getColumn(2).setMinWidth(30);
		mainTable.getColumnModel().getColumn(2).setPreferredWidth(30);
	}

	@Override
	public void goNext() {
		SortedSet<ICardinality> getCardinalities = getCardinalitesSelected();
		EnumSet<PolarityEnum> getPolaries = getPolaritiesSelected();
		EnumSet<DirectionallyEnum> getDirectional = getDirectionaliesSelected();
		if(getCardinalities.size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one Cardinality Otpion");
			jTabbedPaneRelationOptions.setSelectedComponent(jPanelCardinalitty);
		}
		else if(getPolaries.size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one Polarity Option");
			jTabbedPaneRelationOptions.setSelectedComponent(jPanelPolarity);
		}
		else if(getDirectional.size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one Directionality Option");
			jTabbedPaneRelationOptions.setSelectedComponent(jPanelDirectionally);
		}
		else
		{
			REToNetworkConfigurationRelationOptions relOpt = new REToNetworkConfigurationRelationOptions(getPolaries, getDirectional, getCardinalities);
			getParam().add(relOpt);
			closeView();
			new REToXGMMLWizardStep3(getParam());
		}
		
	}

	private EnumSet<DirectionallyEnum> getDirectionaliesSelected() {
		EnumSet<DirectionallyEnum> result = EnumSet.noneOf(DirectionallyEnum.class);
		for(int i=0;i<this.jTableDirectionally.getRowCount();i++)
		{
			boolean selected = ((Boolean) jTableDirectionally.getValueAt(i,2)).booleanValue();
			if(selected)
			{
				result.add((DirectionallyEnum) jTableDirectionally.getValueAt(i,0));
			}	
		}	
		return result;
	}

	private EnumSet<PolarityEnum> getPolaritiesSelected() {
		EnumSet<PolarityEnum> result = EnumSet.noneOf(PolarityEnum.class);
		for(int i=0;i<this.jTablePolarity.getRowCount();i++)
		{
			boolean selected = ((Boolean) jTablePolarity.getValueAt(i,2)).booleanValue();
			if(selected)
			{
				result.add((PolarityEnum) jTablePolarity.getValueAt(i,0));
			}	
		}	
		return result;
	}

	private SortedSet<ICardinality> getCardinalitesSelected() {
		SortedSet<ICardinality> result = new TreeSet<ICardinality>();
		for(int i=0;i<this.jTableCardinality.getRowCount();i++)
		{
			boolean selected = ((Boolean) jTableCardinality.getValueAt(i,2)).booleanValue();
			if(selected)
			{
				result.add((ICardinality) jTableCardinality.getValueAt(i,0));
			}
			
		}	
		return result;
	}

	public void goBack() {
		closeView();
		try {
			new REToXGMMLWizardStep1(getParam());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}	
	}
	
	public void done() {}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"REProcess_Export_To_XGMML_File#Relation_Selection";
	}

}
