package pt.uminho.anote2.carrot.linkage.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.carrot.linkage.datastructures.CarrotClusterAlgorithmsEnum;
import pt.uminho.anote2.carrot.linkage.datastructures.ClusterAlgorithmsFields;
import pt.uminho.anote2.carrot.linkage.datastructures.ClusterAlgorithmsPropertiesNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class ClusteringGUI extends DialogGenericViewInputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelSelectAlgorithm;
	private JPanel jPanelSTCGeneral;
	private JSlider jSliderSTCScoreWeight;
	private JPanel jPanelSTCSLiders;
	private JCheckBox jCheckBoxLingoUseQueryInformation;
	private JPanel jPanelLingoClusterBAseANdUseQuery;
	private JLabel jLabelDesiredClusterCountBase;
	private JSpinner jSpinnerDesiredClusterCountBase;
	private JSlider jSliderLingoScoreWeight;
	private JPanel jPanelLingoSettings;
	private JCheckBox jCheckBoxDimensionReduction;
	private JCheckBox jCheckBoxSTCUseQueryInformation;
	private JSpinner jSpinnerDocumentCountBoost;
	private JLabel jLabelDocumentCountBoost;
	private JSpinner jSpinnerOptimalPhraseLength;
	private JLabel jLabelOptimalPhraseLength;
	private JSpinner jSpinnerSTCSingleTermBoost;
	private JLabel jLabelSTCSingleTermBoost;
	private JSpinner jSpinnerSTCMaxBaseClusters;
	private JLabel jLabelSTCMaxBaseClusters;
	private JPanel jPanelSpinners;
	private JSlider jSliderSTCMinBaseClusterScore;
	private JSlider jSliderSTCMinBaseClusters;
	private JSlider jSliderLabelCount;
	private JSpinner jSpinnerKmeansClusterCount;
	private JSlider jSliderPartionCount;
	private JSpinner jSpinnerKmeansMaxInteractions;
	private JLabel jLabelKmeansMAxIterations;
	private JLabel jLabelKmeansClusterCount;
	private JPanel jPanelKmeansSettings;
	private JComboBox jComboBoxAlgorithm;
	private JComboBox jComboBoxQueries;
	private JTabbedPane jTabbedPaneKmeansAlgorith;
	private JPanel jPanelSelectQueryPanel;
	private JPanel jPanelButtonsPAnel;
	private JTabbedPane jTabbedPaneLingoAlgorith;
	private JTabbedPane jTabbedPaneSTCAlgorith;

	public ClusteringGUI()
	{
		super("Clustering");
		initGUI();
		this.setModal(true);
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.15, 0.15, 0.7};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelButtonsPAnel = new JPanel();
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTabbedPaneKmeansAlgorith = new JTabbedPane();
				jTabbedPaneKmeansAlgorith.addTab("K-Means Settings", null, getJPanelKmeansSettings(), null);
				jTabbedPaneKmeansAlgorith.setBorder(BorderFactory.createTitledBorder(""));
				jTabbedPaneKmeansAlgorith.setVisible(false);
				jTabbedPaneLingoAlgorith = new JTabbedPane();
				jTabbedPaneLingoAlgorith.addTab("Lingo Settings", null, getJPanelLingoSettings(), null);
				jTabbedPaneLingoAlgorith.setBorder(BorderFactory.createTitledBorder(""));
				jTabbedPaneLingoAlgorith.setVisible(false);
				jTabbedPaneSTCAlgorith = new JTabbedPane();
				jTabbedPaneSTCAlgorith.addTab("STC Settings", null, getJPanelSTCGeneral(), null);
				jTabbedPaneSTCAlgorith.setBorder(BorderFactory.createTitledBorder(""));
				jTabbedPaneSTCAlgorith.setVisible(true);
				getContentPane().add(jTabbedPaneKmeansAlgorith, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(jTabbedPaneLingoAlgorith, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(jTabbedPaneSTCAlgorith, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

			}
			{
				jPanelSelectQueryPanel = new JPanel();
				GridBagLayout jPanelSelectQueryPanelLayout = new GridBagLayout();
				getContentPane().add(jPanelSelectQueryPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSelectQueryPanel.setBorder(BorderFactory.createTitledBorder("Select Query"));
				jPanelSelectQueryPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelSelectQueryPanelLayout.rowHeights = new int[] {7, 7, 7};
				jPanelSelectQueryPanelLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelSelectQueryPanelLayout.columnWidths = new int[] {7, 7, 7};
				jPanelSelectQueryPanel.setLayout(jPanelSelectQueryPanelLayout);
				{
					jPanelSelectQueryPanel.add(getComboBoxQueries(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));;
				}
			}
			{
				jPanelSelectAlgorithm = new JPanel();
				GridBagLayout jPanelSelectAlgorithmLayout = new GridBagLayout();
				getContentPane().add(jPanelSelectAlgorithm, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSelectAlgorithm.setBorder(BorderFactory.createTitledBorder("Select Algorithm"));
				jPanelSelectAlgorithmLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelSelectAlgorithmLayout.rowHeights = new int[] {7, 7, 7};
				jPanelSelectAlgorithmLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelSelectAlgorithmLayout.columnWidths = new int[] {7, 7, 7};
				jPanelSelectAlgorithm.setLayout(jPanelSelectAlgorithmLayout);
				{

					jPanelSelectAlgorithm.add(getComboBoxAlgorithmS(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}

		}

	}

	private JComboBox getComboBoxAlgorithmS() {
		if(jComboBoxAlgorithm==null)
		{
			jComboBoxAlgorithm = new JComboBox();
			jComboBoxAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jComboBoxAlgorithmActionPerformed();
				}
			});
			for(CarrotClusterAlgorithmsEnum enu: CarrotClusterAlgorithmsEnum.values())
			{
				jComboBoxAlgorithm.addItem(enu);
			}
		}
		return jComboBoxAlgorithm;
	}

	private JComboBox getComboBoxQueries() {
		if(jComboBoxQueries==null)
		{
			jComboBoxQueries = new JComboBox();
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class);
			for (ClipboardItem item : cl) {
				jComboBoxQueries.addItem((QueryInformationRetrievalExtension)item.getUserData());
			}
			if (jComboBoxQueries.getModel().getSize() > 0)
			{
				Object obj = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
				QueryInformationRetrievalExtension query = (QueryInformationRetrievalExtension) obj;
				jComboBoxQueries.setSelectedItem(query);
			}
		}
		return jComboBoxQueries;
	}



	
	private JPanel getJPanelKmeansSettings() {
		if(jPanelKmeansSettings == null) {
			jPanelKmeansSettings = new JPanel();
			jPanelKmeansSettings.setVisible(false);
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanel1Layout.rowHeights = new int[] {7, 7, 7};
			jPanel1Layout.columnWeights = new double[] {0.1, 0.1, 0.05, 0.1, 0.1};
			jPanel1Layout.columnWidths = new int[] {7, 7, 7, 20, 7};
			jPanelKmeansSettings.setLayout(jPanel1Layout);
			jPanelKmeansSettings.add(getJSpinnerKmeansClusterCount(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelKmeansSettings.add(getJLabelKmeansClusterCount(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelKmeansSettings.add(getJLabelKmeansMAxIterations(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelKmeansSettings.add(getJSpinnerKmeansMaxInteractions(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelKmeansSettings.add(getJSliderPartionCount(), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelKmeansSettings.add(getJSliderLabelCount(), new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelKmeansSettings.add(getJCheckBoxDimensionReduction(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelKmeansSettings;
	}
	
	private JSpinner getJSpinnerKmeansClusterCount() {
		if(jSpinnerKmeansClusterCount == null) {
			SpinnerNumberModel jSpinnerKmeansClusterCountModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.kmeansClusterCountDefaultValue,ClusterAlgorithmsFields.kmeansClusterCountMin,
							ClusterAlgorithmsFields.kmeansClusterCountMax,ClusterAlgorithmsFields.kmeansClusterCountStep);
			jSpinnerKmeansClusterCount = new JSpinner();
			jSpinnerKmeansClusterCount.setModel(jSpinnerKmeansClusterCountModel);
		}
		return jSpinnerKmeansClusterCount;
	}
	
	private JLabel getJLabelKmeansClusterCount() {
		if(jLabelKmeansClusterCount == null) {
			jLabelKmeansClusterCount = new JLabel();
			jLabelKmeansClusterCount.setText("Cluster Count :");
		}
		return jLabelKmeansClusterCount;
	}
	
	private JLabel getJLabelKmeansMAxIterations() {
		if(jLabelKmeansMAxIterations == null) {
			jLabelKmeansMAxIterations = new JLabel();
			jLabelKmeansMAxIterations.setText("Max Iterations :");
		}
		return jLabelKmeansMAxIterations;
	}
	
	private JSpinner getJSpinnerKmeansMaxInteractions() {
		if(jSpinnerKmeansMaxInteractions == null) {
			SpinnerNumberModel jSpinnerKmeansMaxInteractionsModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.kmeansClusterCountDefaultValue,ClusterAlgorithmsFields.kmeansClusterMaxIterationsMin,ClusterAlgorithmsFields.kmeansClusterMaxIterationsMax,
							ClusterAlgorithmsFields.kmeansClusterMaxIterationsStep);
			jSpinnerKmeansMaxInteractions = new JSpinner();
			jSpinnerKmeansMaxInteractions.setModel(jSpinnerKmeansMaxInteractionsModel);
		}
		return jSpinnerKmeansMaxInteractions;
	}
	
	private JSlider getJSliderPartionCount() {
		if(jSliderPartionCount == null) {
			jSliderPartionCount = new JSlider(JSlider.HORIZONTAL,ClusterAlgorithmsFields.kmeansClusterPartitionCountMin, ClusterAlgorithmsFields.kmeansClusterPartitionCountMax, ClusterAlgorithmsFields.kmeansClusterPartitionCountDefaultValue);
			jSliderPartionCount.setBorder(BorderFactory.createTitledBorder("Partition Count"));
			jSliderPartionCount.setMajorTickSpacing(2);
			jSliderPartionCount.setMinorTickSpacing(1);
			jSliderPartionCount.setPaintTicks(true);
			jSliderPartionCount.setPaintLabels(true);	
		}
		return jSliderPartionCount;
	}
	
	private JSlider getJSliderLabelCount() {
		if(jSliderLabelCount == null) {
			jSliderLabelCount = new JSlider(JSlider.HORIZONTAL,ClusterAlgorithmsFields.kmeansClusterLabelCountMin, ClusterAlgorithmsFields.kmeansClusterLabelCountMax, ClusterAlgorithmsFields.kmeansClusterLabelCountDefaultValue);
			jSliderLabelCount.setBorder(BorderFactory.createTitledBorder("Labels Count"));
			jSliderLabelCount.setMajorTickSpacing(2);
			jSliderLabelCount.setMinorTickSpacing(1);
			jSliderLabelCount.setPaintTicks(true);
			jSliderLabelCount.setPaintLabels(true);	
		}
		return jSliderLabelCount;
	}
	
	private JCheckBox getJCheckBoxDimensionReduction() {
		if(jCheckBoxDimensionReduction == null) {
			jCheckBoxDimensionReduction = new JCheckBox();
			jCheckBoxDimensionReduction.setText("Use Dimension Reduction");
			jCheckBoxDimensionReduction.setSelected(ClusterAlgorithmsFields.kmeansClusterMaxIterationsDimensionalityReduction);
		}
		return jCheckBoxDimensionReduction;
	}
	
	private JPanel getJPanelLingoSettings() {
		if(jPanelLingoSettings == null) {
			jPanelLingoSettings = new JPanel();
			jPanelLingoSettings.setVisible(false);
			GridBagLayout jPanelLingoSettingsLayout = new GridBagLayout();
			jPanelLingoSettingsLayout.rowWeights = new double[] {0.1};
			jPanelLingoSettingsLayout.rowHeights = new int[] {7};
			jPanelLingoSettingsLayout.columnWeights = new double[] {0.0, 0.1, 0.05, 0.1};
			jPanelLingoSettingsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelLingoSettings.setLayout(jPanelLingoSettingsLayout);
			jPanelLingoSettings.add(getJSliderScoreWeight(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLingoSettings.add(getJPanelLingoClusterBAseANdUseQuery(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLingoSettings;
	}
	
	private JSlider getJSliderScoreWeight() {
		if(jSliderLingoScoreWeight == null) {
			jSliderLingoScoreWeight = new JSlider(JSlider.HORIZONTAL,ClusterAlgorithmsFields.LingoScoreWeightMin,
					ClusterAlgorithmsFields.LingoScoreWeightMax,ClusterAlgorithmsFields.LingoScoreWeightDefaultValue);
			jSliderLingoScoreWeight.setBorder(BorderFactory.createTitledBorder("Score Weight"));
			jSliderLingoScoreWeight.setLabelTable( ClusterAlgorithmsFields.LingoScoreWeightLabels);
			jSliderLingoScoreWeight.setPaintLabels(true);
		}
		return jSliderLingoScoreWeight;
	}
	
	private JSpinner getJSpinnerDesiredClusterCountBase() {
		if(jSpinnerDesiredClusterCountBase == null) {
			SpinnerNumberModel jSpinnerDesiredClusterCountBaseModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.LingoDesiredClusterCountBaseDefaultValue,ClusterAlgorithmsFields.LingoDesiredClusterCountBaseMin,
							ClusterAlgorithmsFields.LingoDesiredClusterCountBaseMax,ClusterAlgorithmsFields.LingoDesiredClusterCountBaseStep);
			jSpinnerDesiredClusterCountBase = new JSpinner();
			jSpinnerDesiredClusterCountBase.setModel(jSpinnerDesiredClusterCountBaseModel);
		}
		return jSpinnerDesiredClusterCountBase;
	}
	
	private JLabel getJLabelDesiredClusterCountBase() {
		if(jLabelDesiredClusterCountBase == null) {
			jLabelDesiredClusterCountBase = new JLabel();
			jLabelDesiredClusterCountBase.setText("Desired Cluster Count Base");
		}
		return jLabelDesiredClusterCountBase;
	}
	
	private JPanel getJPanelLingoClusterBAseANdUseQuery() {
		if(jPanelLingoClusterBAseANdUseQuery == null) {
			jPanelLingoClusterBAseANdUseQuery = new JPanel();
			GridBagLayout jPanelLingoClusterBAseANdUseQueryLayout = new GridBagLayout();
			jPanelLingoClusterBAseANdUseQueryLayout.rowWeights = new double[] {0.1, 0.05};
			jPanelLingoClusterBAseANdUseQueryLayout.rowHeights = new int[] {7, 7};
			jPanelLingoClusterBAseANdUseQueryLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelLingoClusterBAseANdUseQueryLayout.columnWidths = new int[] {7, 7};
			jPanelLingoClusterBAseANdUseQuery.setLayout(jPanelLingoClusterBAseANdUseQueryLayout);
			jPanelLingoClusterBAseANdUseQuery.add(getJLabelDesiredClusterCountBase(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLingoClusterBAseANdUseQuery.add(getJSpinnerDesiredClusterCountBase(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelLingoClusterBAseANdUseQuery.add(getJCheckBoxLingoUseQueryInformation(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelLingoClusterBAseANdUseQuery;
	}
	
	
	private JCheckBox getJCheckBoxLingoUseQueryInformation() {
		if(jCheckBoxLingoUseQueryInformation == null) {
			jCheckBoxLingoUseQueryInformation = new JCheckBox();
			jCheckBoxLingoUseQueryInformation.setText("Use Query Information");
			jCheckBoxLingoUseQueryInformation.setSelected(ClusterAlgorithmsFields.LingoUSeQueryInformation);
		}
		return jCheckBoxLingoUseQueryInformation;
	}
	
	private JPanel getJPanelSTCGeneral() {
		if(jPanelSTCGeneral == null) {
			jPanelSTCGeneral = new JPanel();
			GridLayout jPanelSTCGeneralLayout = new GridLayout(1, 2);
			jPanelSTCGeneralLayout.setColumns(2);
			jPanelSTCGeneralLayout.setHgap(5);
			jPanelSTCGeneralLayout.setVgap(5);
			jPanelSTCGeneral.setLayout(jPanelSTCGeneralLayout);
			jPanelSTCGeneral.add(getJPanelSpinners());
			jPanelSTCGeneral.add(getJPanelSTCSpinners());
		}
		return jPanelSTCGeneral;
	}
	
	private JPanel getJPanelSTCSpinners() {
		if(jPanelSTCSLiders == null) {
			jPanelSTCSLiders = new JPanel();
			GridLayout jPanelSTCSpinnersLayout = new GridLayout(3, 1);
			jPanelSTCSpinnersLayout.setColumns(1);
			jPanelSTCSpinnersLayout.setHgap(5);
			jPanelSTCSpinnersLayout.setVgap(5);
			jPanelSTCSpinnersLayout.setRows(3);
			jPanelSTCSLiders.setLayout(jPanelSTCSpinnersLayout);
			jPanelSTCSLiders.add(getJSliderSTCScoreWeight());
			jPanelSTCSLiders.add(getJSliderSTCMinBaseClustersSize());
			jPanelSTCSLiders.add(getJSliderMinBaseClusterScore());
		}
		return jPanelSTCSLiders;
	}
	
	private JSlider getJSliderSTCScoreWeight() {
		if(jSliderSTCScoreWeight == null) {
			jSliderSTCScoreWeight = new JSlider(JSlider.HORIZONTAL,ClusterAlgorithmsFields.STCScoreWeightMin,
					ClusterAlgorithmsFields.STCScoreWeightMax,ClusterAlgorithmsFields.STCScoreWeightDefaultValue);
			jSliderSTCScoreWeight.setBorder(BorderFactory.createTitledBorder("Score Weight"));
			jSliderSTCScoreWeight.setLabelTable( ClusterAlgorithmsFields.STCScoreWeightLabels);
			jSliderSTCScoreWeight.setPaintLabels(true);
		}
		return jSliderSTCScoreWeight;
	}
	
	private JSlider getJSliderSTCMinBaseClustersSize() {
		if(jSliderSTCMinBaseClusters == null) {
			jSliderSTCMinBaseClusters = new JSlider(JSlider.HORIZONTAL,ClusterAlgorithmsFields.STCMinBaseClusterSizeMin,
					ClusterAlgorithmsFields.STCMinBaseClusterSizeMax,ClusterAlgorithmsFields.STCMinBaseClusterSizeDefaultValue);
			jSliderSTCMinBaseClusters.setBorder(BorderFactory.createTitledBorder("Min Base Clusters Size"));
			jSliderSTCMinBaseClusters.setMajorTickSpacing(2);
			jSliderSTCMinBaseClusters.setMinorTickSpacing(1);
			jSliderSTCMinBaseClusters.setPaintTicks(true);
			jSliderSTCMinBaseClusters.setPaintLabels(true);	
		}
		return jSliderSTCMinBaseClusters;
	}
	
	private JSlider getJSliderMinBaseClusterScore() {
		if(jSliderSTCMinBaseClusterScore == null) {
			jSliderSTCMinBaseClusterScore = new JSlider(JSlider.HORIZONTAL,ClusterAlgorithmsFields.STCMinBaseClusterScoreMin,
					ClusterAlgorithmsFields.STCMinBaseClusterScoreMax,ClusterAlgorithmsFields.STCMinBaseClusterScoreDefaultValue);
			jSliderSTCMinBaseClusterScore.setBorder(BorderFactory.createTitledBorder("Min Base Cluster Score"))	;
			jSliderSTCMinBaseClusterScore.setMajorTickSpacing(2);
			jSliderSTCMinBaseClusterScore.setMinorTickSpacing(2);
			jSliderSTCMinBaseClusterScore.setPaintTicks(true);
			jSliderSTCMinBaseClusterScore.setPaintLabels(true);
		}
		return jSliderSTCMinBaseClusterScore;
	}
	
	private JPanel getJPanelSpinners() {
		if(jPanelSpinners == null) {
			jPanelSpinners = new JPanel();
			GridBagLayout jPanelSpinnersLayout = new GridBagLayout();
			jPanelSpinnersLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelSpinnersLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
			jPanelSpinnersLayout.columnWeights = new double[] {0.0, 0.1};
			jPanelSpinnersLayout.columnWidths = new int[] {7, 7};
			jPanelSpinners.setLayout(jPanelSpinnersLayout);
			jPanelSpinners.add(getJLabelSTCMaxBaseClusters(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 4), 0, 0));
			jPanelSpinners.add(getJSpinnerSTCMaxBaseClusters(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJLabelSTCSingleTermBoost(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJSpinnerSTCSingleTermBoost(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJLabelOptimalPhraseLength(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJSpinnerOptimalPhraseLength(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJLabelDocumentCountBoost(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJSpinnerDocumentCountBoost(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpinners.add(getJCheckBoxSTCUseQueryInformation(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSpinners;
	}
	
	private JLabel getJLabelSTCMaxBaseClusters() {
		if(jLabelSTCMaxBaseClusters == null) {
			jLabelSTCMaxBaseClusters = new JLabel();
			jLabelSTCMaxBaseClusters.setText("Max Base Clusters :");
		}
		return jLabelSTCMaxBaseClusters;
	}
	
	private JSpinner getJSpinnerSTCMaxBaseClusters() {
		if(jSpinnerSTCMaxBaseClusters == null) {
			SpinnerNumberModel jSpinnerSTCMaxBaseClustersModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.STCMaxBaseClustersDefaultValue,ClusterAlgorithmsFields.STCMaxBaseClustersMin,
							ClusterAlgorithmsFields.STCMaxBaseClustersMax,ClusterAlgorithmsFields.STCMaxBaseClustersStep);		
			jSpinnerSTCMaxBaseClusters = new JSpinner();
			jSpinnerSTCMaxBaseClusters.setModel(jSpinnerSTCMaxBaseClustersModel);
		}
		return jSpinnerSTCMaxBaseClusters;
	}
	
	private JLabel getJLabelSTCSingleTermBoost() {
		if(jLabelSTCSingleTermBoost == null) {
			jLabelSTCSingleTermBoost = new JLabel();
			jLabelSTCSingleTermBoost.setText("Single Term Boost :");
		}
		return jLabelSTCSingleTermBoost;
	}
	
	private JSpinner getJSpinnerSTCSingleTermBoost() {
		if(jSpinnerSTCSingleTermBoost == null) {
			SpinnerNumberModel jSpinnerSTCSingleTermBoostModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.STCSingleTermBoostDefaultValue,ClusterAlgorithmsFields.STCSingleTermBoostMin,
							ClusterAlgorithmsFields.STCSingleTermBoostMax,ClusterAlgorithmsFields.STCSingleTermBoostStep);
			jSpinnerSTCSingleTermBoost = new JSpinner();
			jSpinnerSTCSingleTermBoost.setModel(jSpinnerSTCSingleTermBoostModel);
		}
		return jSpinnerSTCSingleTermBoost;
	}
	
	private JLabel getJLabelOptimalPhraseLength() {
		if(jLabelOptimalPhraseLength == null) {
			jLabelOptimalPhraseLength = new JLabel();
			jLabelOptimalPhraseLength.setText("Optimal Phrase Length :");
		}
		return jLabelOptimalPhraseLength;
	}
	
	private JSpinner getJSpinnerOptimalPhraseLength() {
		if(jSpinnerOptimalPhraseLength == null) {
			SpinnerNumberModel jSpinnerOptimalPhraseLengthModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.STCOptimalPhraseLengthDefaultValue,ClusterAlgorithmsFields.STCOptimalPhraseLengthMin,
							ClusterAlgorithmsFields.STCOptimalPhraseLengthMax,ClusterAlgorithmsFields.STCOptimalPhraseLengthStep);
			jSpinnerOptimalPhraseLength = new JSpinner();
			jSpinnerOptimalPhraseLength.setModel(jSpinnerOptimalPhraseLengthModel);
		}
		return jSpinnerOptimalPhraseLength;
	}
	
	private JLabel getJLabelDocumentCountBoost() {
		if(jLabelDocumentCountBoost == null) {
			jLabelDocumentCountBoost = new JLabel();
			jLabelDocumentCountBoost.setText("Document Count Boost :");
		}
		return jLabelDocumentCountBoost;
	}
	
	private JSpinner getJSpinnerDocumentCountBoost() {
		if(jSpinnerDocumentCountBoost == null) {
			SpinnerNumberModel jSpinnerDocumentCountBoostModel = 
					new SpinnerNumberModel(ClusterAlgorithmsFields.STCDocumentCountBoostDefaultValue,ClusterAlgorithmsFields.STCDocumentCountBoostMin,
							ClusterAlgorithmsFields.STCDocumentCountBoostMax,ClusterAlgorithmsFields.STCDocumentCountBoostStep);
			jSpinnerDocumentCountBoost = new JSpinner();
			jSpinnerDocumentCountBoost.setModel(jSpinnerDocumentCountBoostModel);
		}
		return jSpinnerDocumentCountBoost;
	}
	
	private JCheckBox getJCheckBoxSTCUseQueryInformation() {
		if(jCheckBoxSTCUseQueryInformation == null) {
			jCheckBoxSTCUseQueryInformation = new JCheckBox();
			jCheckBoxSTCUseQueryInformation.setText("Use Query Information");
			jCheckBoxSTCUseQueryInformation.setSelected(ClusterAlgorithmsFields.STCUseQueryInformation);
		}
		return jCheckBoxSTCUseQueryInformation;
	}
	
	private void jComboBoxAlgorithmActionPerformed() {
		CarrotClusterAlgorithmsEnum algorithm = (CarrotClusterAlgorithmsEnum) jComboBoxAlgorithm.getSelectedItem();
		if(algorithm.toString().equals("STC"))
		{
			jTabbedPaneKmeansAlgorith.setVisible(false);
			jTabbedPaneLingoAlgorith.setVisible(false);
			jTabbedPaneSTCAlgorith.setVisible(true);
		}
		else if(algorithm.toString().equals("Lingo"))
		{
			jTabbedPaneKmeansAlgorith.setVisible(false);
			jTabbedPaneLingoAlgorith.setVisible(true);
			jTabbedPaneSTCAlgorith.setVisible(false);
		}
		else
		{
			jTabbedPaneKmeansAlgorith.setVisible(true);
			jTabbedPaneLingoAlgorith.setVisible(false);
			jTabbedPaneSTCAlgorith.setVisible(false);
		}
	}
	
	private Properties getProperties() {
		Properties pro = new Properties();
		CarrotClusterAlgorithmsEnum algorithm = (CarrotClusterAlgorithmsEnum) jComboBoxAlgorithm.getSelectedItem();
		if(algorithm.toString().equals("STC"))
		{
			Boolean useQueryInformation = jCheckBoxSTCUseQueryInformation.isSelected();
			pro.put(ClusterAlgorithmsPropertiesNames.queryInformation, useQueryInformation);
			int minBaseClusterScore = jSliderLabelCount.getValue();
			double doubleMinBaseClusterScore = Double.valueOf(minBaseClusterScore);
			pro.put(ClusterAlgorithmsPropertiesNames.minBaseClusterScore, doubleMinBaseClusterScore);
			int maxBaseClusters = (Integer) jSpinnerSTCMaxBaseClusters.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.maxBaseClusters, maxBaseClusters);
			int minBaseClusterSize = jSliderSTCMinBaseClusters.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.minBaseClusterSize, minBaseClusterSize);
			double singleTermBoost = (Double) jSpinnerSTCSingleTermBoost.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.singleTermBoost, singleTermBoost);
			int optimalPhraseLength = (Integer) jSpinnerOptimalPhraseLength.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.optimalPhraseLength, optimalPhraseLength);
			int documentCountBoost = (Integer) jSpinnerDocumentCountBoost.getValue();
			double doubleDocumentCountBoost = Double.valueOf(documentCountBoost);
			pro.put(ClusterAlgorithmsPropertiesNames.documentCountBoost, doubleDocumentCountBoost);
			int scoreWeight = jSliderLingoScoreWeight.getValue();
			double doublescoreWeight = (double) scoreWeight / (double) ClusterAlgorithmsFields.STCScoreWeightDivisionNumber;
			pro.put(ClusterAlgorithmsPropertiesNames.scoreWeight, doublescoreWeight);

		}
		else if(algorithm.toString().equals("Lingo"))
		{
			Boolean useQueryInformation = jCheckBoxLingoUseQueryInformation.isSelected();
			pro.put(ClusterAlgorithmsPropertiesNames.queryInformation, useQueryInformation);
			int scoreWeight = jSliderLingoScoreWeight.getValue();
			double doublescoreWeight = (double) scoreWeight / (double) ClusterAlgorithmsFields.LingoScoreWeightDivisionNumber;
			pro.put(ClusterAlgorithmsPropertiesNames.scoreWeight, doublescoreWeight);
			int desiredClusterCountBase = (Integer) jSpinnerDesiredClusterCountBase.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.desiredClusterCountBase, desiredClusterCountBase);
		}
		else
		{
			int clusterCount = (Integer) jSpinnerKmeansClusterCount.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.clusterCount, clusterCount);
			int maxIterations = (Integer) jSpinnerKmeansMaxInteractions.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.maxIterations, maxIterations);
			Boolean useDimensionalityReduction = jCheckBoxDimensionReduction.isSelected();
			pro.put(ClusterAlgorithmsPropertiesNames.useDimensionalityReduction, useDimensionalityReduction);
			int partitionCount = jSliderPartionCount.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.partitionCount, partitionCount);
			int labelCount = jSliderLabelCount.getValue();
			pro.put(ClusterAlgorithmsPropertiesNames.labelCount, labelCount);
		}
		return pro;
	}

	@Override
	protected void okButtonAction() {
		Properties prop = getProperties();
		paramsRec.paramsIntroduced(new ParamSpec[]{
				new ParamSpec("query",QueryInformationRetrievalExtension.class,(QueryInformationRetrievalExtension) jComboBoxQueries.getSelectedItem() ,null),
				new ParamSpec("algorithm",CarrotClusterAlgorithmsEnum.class,(CarrotClusterAlgorithmsEnum) jComboBoxAlgorithm.getSelectedItem() ,null),
				new ParamSpec("properties",Properties.class,prop,null)
		});		
	}

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Query_Create_Cluster";
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Query Itens on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}
	}

}
