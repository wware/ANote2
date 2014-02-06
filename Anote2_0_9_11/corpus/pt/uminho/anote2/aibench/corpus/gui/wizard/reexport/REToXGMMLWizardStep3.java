package pt.uminho.anote2.aibench.corpus.gui.wizard.reexport;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.Stats.NERQueriesStats;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfigurationAdvanceOptions;
import pt.uminho.anote2.datastructures.process.re.export.configuration.REToNetworkConfigurationEntityOptions;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.network.XGMMLPolygnos;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAdvanceOptions;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REToXGMMLWizardStep3 extends WizardStandard {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jUpperPanel;
	private JTabbedPane jTabbedPaneAdvanceOptions;
	private JPanel jPanelOptions;
	private JCheckBox jCheckBoxAllowLonelyNodes;
	private JTable jTableClassPolygons;
	private JScrollPane jScrollPaneClassXGMMLPolygnos;
	private JCheckBox jCheckBoxUseGraphicWeights;
	private JPanel jPanelUseGraphicWeights;
	private JPanel jPanelLayoutSettings;
	private JCheckBox jCheckBoxUseRelationSentenceDetails;
	private JCheckBox jCheckBoxdirected;
	private JCheckBox jCheckBoxEntitiesRepresentedByprimaryNAme;
	private JCheckBox jCheckBoxIgnoreClues;
	private JPanel jPanelLemmaSelection;
	private JPanel jPanelRelationsType;
	private TableSearchPanel jPanelSearchLemma;
	private TableSearchPanel jPanelSearchRelationsType;
	private JButton jButtonAll;
	private boolean jButtonAllSelected = true;
	private JButton jButtonAll2;
	private boolean jButtonAllSelected2 = true;

	public REToXGMMLWizardStep3(List<Object> param) {
		super(param);
		InitGUI();
		if(param.size() == 4)
		{
			fillWithPreviousSettings(param.get(3));
			param.remove(3);
		}
		this.setTitle("RE Process to Cytoscape (XGMML) File - Advanced Options");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillWithPreviousSettings(Object object) {
		IREToNetworkConfigurationAdvanceOptions advanced = (IREToNetworkConfigurationAdvanceOptions) object;
		jCheckBoxAllowLonelyNodes.setSelected(advanced.allowLonelyNodes());
		jCheckBoxdirected.setSelected(advanced.isDirectedNetwork());
		jCheckBoxEntitiesRepresentedByprimaryNAme.setSelected(advanced.isEntitiesRepresentedByPrimaryName());
		jCheckBoxIgnoreClues.setSelected(advanced.ignoreClues());
		jCheckBoxUseRelationSentenceDetails.setSelected(advanced.exportRelationsDetails());
		jCheckBoxUseGraphicWeights.setSelected(advanced.useGraphicWeights());
		for(int i=0;i<jPanelSearchLemma.getMainTable().getModel().getRowCount();i++)
		{
			String lemma = (String) jPanelSearchLemma.getMainTable().getValueAt(i, 0);
			if(advanced.getLemmaVerbsAllow().contains(lemma))
			{
				jPanelSearchLemma.getMainTable().setValueAt(true, i, 2);
			}
			else
			{
				jPanelSearchLemma.getMainTable().setValueAt(false, i, 2);
			}
		}
		for(int i=0;i<jPanelSearchRelationsType.getMainTable().getModel().getRowCount();i++)
		{
			String leftClasse = (String) jPanelSearchRelationsType.getMainTable().getValueAt(i, 0);
			String rightClasse = (String) jPanelSearchRelationsType.getMainTable().getValueAt(i, 1);
			IRelationsType rt = new RelationType(ClassProperties.getClassClassID().get(leftClasse), ClassProperties.getClassClassID().get(rightClasse));	
			if(advanced.getRelationsType().contains(rt))
			{
				jPanelSearchRelationsType.getMainTable().setValueAt(true, i, 3);
			}
			else
			{
				jPanelSearchRelationsType.getMainTable().setValueAt(false, i, 3);
			}
		}
		for(int i=0;i<jTableClassPolygons.getModel().getRowCount();i++)
		{
			String classe = (String) jTableClassPolygons.getValueAt(i, 0);
			int classID = ClassProperties.getClassClassID().get(classe);			
			jTableClassPolygons.setValueAt(advanced.getXGMMLPolygnos().get(classID), i, 1);
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
				jUpperPanel.setBorder(BorderFactory.createTitledBorder("Advanced Options"));
				GridLayout jUpperPanelLayout = new GridLayout(1, 1);
				jUpperPanelLayout.setVgap(5);
				jUpperPanelLayout.setHgap(5);
				jUpperPanelLayout.setColumns(1);
				jUpperPanel.setLayout(jUpperPanelLayout);
				{
					jTabbedPaneAdvanceOptions = new JTabbedPane();
					jUpperPanel.add(jTabbedPaneAdvanceOptions);
					{
						jPanelOptions = new JPanel();
						GridBagLayout jPanelOptionsLayout = new GridBagLayout();
						jTabbedPaneAdvanceOptions.addTab("General", null, jPanelOptions, null);
						jPanelOptionsLayout.rowWeights = new double[] {0.2, 0.05, 0.05, 0.05, 0.05, 0.05, 0.25};
						jPanelOptionsLayout.rowHeights = new int[] {20, 7, 7, 7, 7, 20, 20};
						jPanelOptionsLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
						jPanelOptionsLayout.columnWidths = new int[] {7, 7, 7};
						jPanelOptions.setLayout(jPanelOptionsLayout);
						{
							jCheckBoxAllowLonelyNodes = new JCheckBox();
							jPanelOptions.add(jCheckBoxAllowLonelyNodes, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jCheckBoxAllowLonelyNodes.setText("Allow Isolated Nodes");
						}
						{
							jCheckBoxdirected = new JCheckBox();
							jPanelOptions.add(jCheckBoxdirected, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jCheckBoxdirected.setText("Directed Graph");
							jCheckBoxdirected.setSelected(true);
						}
						{
							jCheckBoxEntitiesRepresentedByprimaryNAme = new JCheckBox();
							jPanelOptions.add(jCheckBoxEntitiesRepresentedByprimaryNAme, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jCheckBoxEntitiesRepresentedByprimaryNAme.setText("Group terms By primary IDs (In a single node)");
							jCheckBoxEntitiesRepresentedByprimaryNAme.setSelected(true);
						}
						{
							jCheckBoxIgnoreClues = new JCheckBox();
							jPanelOptions.add(jCheckBoxIgnoreClues, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jPanelOptions.add(getJCheckBoxUseRelationSentenceDetails(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jCheckBoxIgnoreClues.setText("Ignore Clues (Verbs)");
						}
					}
					{
						jPanelLemmaSelection = new JPanel();
						GridBagLayout jPanelLemmaSelectionLayout = new GridBagLayout();
						jPanelLemmaSelectionLayout.rowWeights = new double[] {0.1, 0.0};
						jPanelLemmaSelectionLayout.rowHeights = new int[] {7, 7};
						jPanelLemmaSelectionLayout.columnWeights = new double[] {0.1};
						jPanelLemmaSelectionLayout.columnWidths = new int[] {7};
						jPanelLemmaSelection.setLayout(jPanelLemmaSelectionLayout);
						jPanelLemmaSelection.add(getChangeButton(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jTabbedPaneAdvanceOptions.addTab("Lemma", null, jPanelLemmaSelection, null);
						jPanelSearchLemma = new TableSearchPanel(getLemmaTebalModel(),false);
						jPanelLemmaSelection.add(jPanelSearchLemma, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						completeTable(jPanelSearchLemma.getMainTable());
					}
					{
						jPanelRelationsType = new JPanel();
						GridBagLayout jPanelRelationsTypeLayout = new GridBagLayout();
						jPanelRelationsTypeLayout.rowWeights = new double[] {0.1, 0.0};
						jPanelRelationsTypeLayout.rowHeights = new int[] {7, 7};
						jPanelRelationsTypeLayout.columnWeights = new double[] {0.1};
						jPanelRelationsTypeLayout.columnWidths = new int[] {7};
						jPanelRelationsType.setLayout(jPanelRelationsTypeLayout);
						jPanelRelationsType.add(getChangeButton2(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jTabbedPaneAdvanceOptions.addTab("Relations Type", null, jPanelRelationsType, null);
						jTabbedPaneAdvanceOptions.addTab("Layout Settings", null, getJPanelLayoutSettings(), null);
						jPanelSearchRelationsType = new TableSearchPanel(getRelationTypeModal(),false);

						jPanelRelationsType.add(jPanelSearchRelationsType, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						completeTable2(jPanelSearchRelationsType.getMainTable());
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

	private JButton getChangeButton2() {
		if(jButtonAll2 == null)
		{
			jButtonAll2 = new JButton();
			jButtonAll2.setText("Unselect");
			jButtonAll2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					changeOriginalTableModel2();
				}
			});
		}
		return jButtonAll2;
	}

	protected void changeOriginalTableModel2() {
		boolean selectedAll = !jButtonAllSelected2  ;
		for(int i=0;i<this.jPanelSearchRelationsType.getOriginalTableModel().getRowCount();i++)
		{
			this.jPanelSearchRelationsType.getOriginalTableModel().setValueAt(selectedAll, i, 3);
		}			
		if(selectedAll)
			jButtonAll2.setText("Unselect");
		else
			jButtonAll2.setText("Select All");
		jButtonAllSelected2 = selectedAll;
		jPanelSearchRelationsType.updateUI();
	}

	private JButton getChangeButton() {
		if(jButtonAll == null)
		{
			jButtonAll = new JButton();
			jButtonAll.setText("Unselect");
			jButtonAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					changeOriginalTableModel();
				}
			});
		}
		return jButtonAll;
	}

	protected void changeOriginalTableModel() {
		boolean selectedAll = !jButtonAllSelected ;
		for(int i=0;i<this.jPanelSearchLemma.getOriginalTableModel().getRowCount();i++)
		{
			this.jPanelSearchLemma.getOriginalTableModel().setValueAt(selectedAll, i, 2);
		}			
		if(selectedAll)
			jButtonAll.setText("Unselect");
		else
			jButtonAll.setText("Select All");
		jButtonAllSelected = selectedAll;
		jPanelSearchLemma.updateUI();
	}

	private TableModel getRelationTypeModal() throws SQLException, DatabaseLoadDriverException {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				if(columnIndex == 0 || columnIndex == 1)
					return String.class;
				else if(columnIndex == 2)
					return Integer.class;
				else			
					return Boolean.class;
			}
		};
		String[] columns = new String[] {"Class At Left","Class At Right", "Number of Occurrences",""};
		tableModel.setColumnIdentifiers(columns);	
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.processRelationsTypes);
		ps.setInt(1, ((RESchema) getParam().get(0)).getID());
		ResultSet rs = ps.executeQuery();
		TreeMap<IRelationsType,Integer> map = new TreeMap<IRelationsType, Integer>();
		Set<Integer> classesAtLeft = new HashSet<Integer>();
		Set<Integer> classesAtRight = new HashSet<Integer>();
		int annotationID = 0;
		int annotationIDDB = 0;
		int classID;
		String direction = "";
		while(rs .next())
		{
			annotationIDDB = rs.getInt(1);
			direction = rs.getString(3);
			classID = rs.getInt(4);
			if(annotationID == 0)
				annotationID = annotationIDDB;
			if(annotationIDDB != annotationID)
			{
				for(int clLeft :classesAtLeft)
				{
					for(int clRight : classesAtRight)
					{
						IRelationsType rType = new RelationType(clLeft, clRight);
						if(map.containsKey(rType))
						{
							int value = map.get(rType);
							value ++;
							map.put(rType, value);
						}
						else
						{
							map.put(rType, 1);
						}
					}
				}
				classesAtLeft = new HashSet<Integer>();
				classesAtRight = new HashSet<Integer>();
				annotationID = annotationIDDB;
			}
			if(!((REToNetworkConfigurationEntityOptions) getParam().get(1)).getClassIdsAllowed().contains(classID))
			{
			}
			else if(direction.equals("left"))
			{
				classesAtLeft.add(classID);
			}
			else
				classesAtRight.add(classID);
			
		}
		rs.close();
		ps.close();
		Object[] data = new Object[4];	
		for(IRelationsType relType:map.keySet())
		{
				data[0] = ClassProperties.getClassIDClass().get(relType.getLeftClassID());
				data[1] = ClassProperties.getClassIDClass().get(relType.getRightClassID());
				data[2] = map.get(relType);
				data[3] = Boolean.TRUE;
				tableModel.addRow(data);
		}
		return tableModel;
	}

	private TableModel getLemmaTebalModel() throws SQLException, DatabaseLoadDriverException {
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
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(NERQueriesStats.processRelationsLemmas);
		ps.setInt(1, ((RESchema)getParam().get(0)).getID());
		ResultSet rs = ps.executeQuery();
		Object[] data = new Object[3];	
		while(rs.next())
		{
			data[0] = rs.getString(1);
			data[1] = rs.getInt(2);
			data[2] = Boolean.TRUE;
			tableModel.addRow(data);
		}
		rs.close();
		ps.close();
		return tableModel;
	}
	
	private void completeTable(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setMinWidth(150);
		jtable.getColumnModel().getColumn(2).setMaxWidth(30);
		jtable.getColumnModel().getColumn(2).setMinWidth(30);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(30);
	}
	
	private void completeTable2(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setMinWidth(150);
		jtable.getColumnModel().getColumn(3).setMaxWidth(30);
		jtable.getColumnModel().getColumn(3).setMinWidth(30);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(30);
	}

	@Override
	public void goNext() {
		Set<String> lemaSelection = getLemmaSelection();
		SortedSet<IRelationsType> relationTypes = getRelationTypes();
		if(!jCheckBoxIgnoreClues.isSelected() && lemaSelection .size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one lemma or select ignore clues ");
		}
		else if(relationTypes.size() == 0)
		{
			Workbench.getInstance().warn("Please select at least one relation Type ");
		}
		else
		{
			Map<Integer, XGMMLPolygnos> classPolignos = getClassPolignos();
			boolean useGraphicWeighs = jCheckBoxUseGraphicWeights.isSelected();
			boolean useRelationDetais = jCheckBoxUseRelationSentenceDetails.isSelected();
			IREToNetworkConfigurationAdvanceOptions advanced = new REToNetworkConfigurationAdvanceOptions(lemaSelection,jCheckBoxAllowLonelyNodes.isSelected(),jCheckBoxdirected.isSelected(),
					jCheckBoxEntitiesRepresentedByprimaryNAme.isSelected(),jCheckBoxIgnoreClues.isSelected(),relationTypes,useRelationDetais,useGraphicWeighs,classPolignos);
			getParam().add(advanced);
			closeView();
			new REToXGMMLWizardStep4(getParam());
		}
	}

	private Map<Integer, XGMMLPolygnos> getClassPolignos() {
		Map<Integer, XGMMLPolygnos> result = new HashMap<Integer, XGMMLPolygnos>();
		for(int i=0;i<jTableClassPolygons.getRowCount();i++)
		{
			int classID = ClassProperties.getClassClassID().get((String)jTableClassPolygons.getValueAt(i, 0));
			XGMMLPolygnos polygno = (XGMMLPolygnos) jTableClassPolygons.getValueAt(i, 1);
			result.put(classID, polygno);
		}
		return result;
	}

	private SortedSet<IRelationsType> getRelationTypes() {
		SortedSet<IRelationsType> relationsType = new TreeSet<IRelationsType>();
		String classIDLeft;
		String classIDRight;
		for(int i=0;i<jPanelSearchRelationsType.getOriginalTableModel().getRowCount();i++)
		{
			boolean selected = ((Boolean) jPanelSearchRelationsType.getValueAt(i,3)).booleanValue();
			if(selected)
			{
				classIDLeft = (String) jPanelSearchRelationsType.getValueAt(i,0);
				classIDRight = (String) jPanelSearchRelationsType.getValueAt(i,1);
				relationsType.add(new RelationType(ClassProperties.getClassClassID().get(classIDLeft),ClassProperties.getClassClassID().get(classIDRight)));
			}	
		}	
		return relationsType;
	}

	private Set<String> getLemmaSelection() {
		Set<String> lemmas = new HashSet<String>();
		for(int i=0;i<jPanelSearchLemma.getOriginalTableModel().getRowCount();i++)
		{
			boolean selected = ((Boolean) jPanelSearchLemma.getValueAt(i,2)).booleanValue();
			if(selected)
			{
				lemmas.add((String) jPanelSearchLemma.getValueAt(i,0));
			}	
		}	
		return lemmas;
	}

	@Override
	public void goBack() {
		closeView();
		new REToXGMMLWizardStep2(getParam());
	}

	public void done() {}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"REProcess_Export_To_XGMML_File#Advanced_Options";
	}
	
	private JCheckBox getJCheckBoxUseRelationSentenceDetails() {
		if(jCheckBoxUseRelationSentenceDetails == null) {
			jCheckBoxUseRelationSentenceDetails = new JCheckBox();
			jCheckBoxUseRelationSentenceDetails.setText("Export Relations Sentence Details");
			jCheckBoxUseRelationSentenceDetails.setSelected(true);
		}
		return jCheckBoxUseRelationSentenceDetails;
	}
	
	private JPanel getJPanelLayoutSettings() {
		if(jPanelLayoutSettings == null) {
			jPanelLayoutSettings = new JPanel();
			GridBagLayout jPanelLayoutSettingsLayout = new GridBagLayout();
			jPanelLayoutSettingsLayout.rowWeights = new double[] {0.1, 0.0};
			jPanelLayoutSettingsLayout.rowHeights = new int[] {7, 7};
			jPanelLayoutSettingsLayout.columnWeights = new double[] {0.1};
			jPanelLayoutSettingsLayout.columnWidths = new int[] {7};
			jPanelLayoutSettings.setLayout(jPanelLayoutSettingsLayout);
			jPanelLayoutSettings.add(getJPanelUseGraphicWeights(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLayoutSettings.add(getJScrollPaneClassXGMMLPolygnos(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLayoutSettings;
	}
	
	private JPanel getJPanelUseGraphicWeights() {
		if(jPanelUseGraphicWeights == null) {
			jPanelUseGraphicWeights = new JPanel();
			GridBagLayout jPanelUseGraphicWeightsLayout = new GridBagLayout();
			jPanelUseGraphicWeights.setBorder(BorderFactory.createTitledBorder("Graphic Weights"));
			jPanelUseGraphicWeightsLayout.rowWeights = new double[] {0.1};
			jPanelUseGraphicWeightsLayout.rowHeights = new int[] {7};
			jPanelUseGraphicWeightsLayout.columnWeights = new double[] {0.1};
			jPanelUseGraphicWeightsLayout.columnWidths = new int[] {7};
			jPanelUseGraphicWeights.setLayout(jPanelUseGraphicWeightsLayout);
			jPanelUseGraphicWeights.add(getJCheckBoxUseGraphicWeights(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 20, 5, 5), 0, 0));
		}
		return jPanelUseGraphicWeights;
	}
	
	private JCheckBox getJCheckBoxUseGraphicWeights() {
		if(jCheckBoxUseGraphicWeights == null) {
			jCheckBoxUseGraphicWeights = new JCheckBox();
			jCheckBoxUseGraphicWeights.setText("Use Graphic Weights");
		}
		return jCheckBoxUseGraphicWeights;
	}
	
	private JScrollPane getJScrollPaneClassXGMMLPolygnos() {
		if(jScrollPaneClassXGMMLPolygnos == null) {
			jScrollPaneClassXGMMLPolygnos = new JScrollPane();
			jScrollPaneClassXGMMLPolygnos.setBorder(BorderFactory.createTitledBorder("Class - Polygons Representation"));
			jScrollPaneClassXGMMLPolygnos.setViewportView(getJTableClassPolygons());
		}
		return jScrollPaneClassXGMMLPolygnos;
	}
	
	private JTable getJTableClassPolygons() {
		if(jTableClassPolygons == null) {
			jTableClassPolygons = new JTable();
			jTableClassPolygons.setModel(getJTableClassPolygonsModel());
			completeJTablePolygons(jTableClassPolygons);
		}
		return jTableClassPolygons;
	}

	private void completeJTablePolygons(JTable jTableClassPolygons2) {
		jTableClassPolygons2.getColumnModel().getColumn(1).setMaxWidth(250);
		jTableClassPolygons2.getColumnModel().getColumn(1).setMinWidth(250);
		jTableClassPolygons2.getColumnModel().getColumn(1).setPreferredWidth(250);	
		jTableClassPolygons2.getColumn("Polygons").setCellRenderer(new ButtonAnnotationsRenderer());
		jTableClassPolygons2.getColumn("Polygons").setCellEditor(new ButtonAnnotationsCellEditor());
		jTableClassPolygons2.setRowHeight(20);
	}

	private TableModel getJTableClassPolygonsModel() {
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int col) {
			     switch (col) {
			         case 1:
			             return true;
			         default:
			             return false;
			      }
			}
		};
		String[] columns = new String[] {"Class", "Polygons"};
		tableModel.setColumnIdentifiers(columns);
		Object[] data = new Object[2];
		REToNetworkConfigurationEntityOptions entityoptions = ((REToNetworkConfigurationEntityOptions) getParam().get(1));
		int classEntities = entityoptions.getClassIdsAllowed().size();
		int sum = 0;
		Random rand = new Random();
		for(int classID : entityoptions.getClassIdsAllowed())
		{
			data[0] = ClassProperties.getClassIDClass().get(classID);
			if(classEntities < 10)
			{
				data[1] = XGMMLPolygnos.values()[sum];
				sum++;
			}
			else
			{
				int randomNum = rand.nextInt(9);
				data[1] = XGMMLPolygnos.values()[randomNum];
			}
			tableModel.addRow(data);
		}
		return tableModel;
	}
	
	class ButtonAnnotationsRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(XGMMLPolygnos.values());
			JComboBox viewButton = new JComboBox(model);
			viewButton.setSelectedItem(value);
			return viewButton;
		}
		
		public void whenClick() {
		}

	}

	class ButtonAnnotationsCellEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 1L;
		  
		public ButtonAnnotationsCellEditor() {
		        super(new JComboBox(XGMMLPolygnos.values()));
		  }

	}

}
