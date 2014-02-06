package pt.uminho.anote2.aibench.corpus.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERMergeProcess;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.process.IE.IIEProcess;



public class NERMergePanel extends JTabbedPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private INERMergeProcess report;
	private JPanel jPanelGeneralInfo;
	private JTextField jTextFieldDuration;
	private JTextField jTextFieldAnnotations;
	private JLabel jLabelDuration;
	private JLabel jLabelTotalAnnotations;
	private JTextField jTextFieldNERSchemasMerged;
	private JLabel jLabelNERSchemasMerged;
	private JTextField jTextFieldNERSchema;
	private JLabel jLabelResultProcess;
	private JList jListNERSchemas;
	private JPanel jPanelMergedProcesses;
	private JPanel jPanelSummaryStats;
	private NERSchema nerProcess;

	public NERMergePanel(INERMergeProcess report) throws SQLException, DatabaseLoadDriverException
	{
		this.report = report;
		nerProcess = new NERSchema(report.getNERSchema().getID(),report.getNERSchema().getCorpus(),report.getNERSchema().getName(),report.getNERSchema().getType(),report.getNERSchema().getProperties());
		initGUI();
		completeGUI();
	}

	private void completeGUI() throws SQLException, DatabaseLoadDriverException {
		jTextFieldNERSchema.setText(report.getNERSchema().toString());
		jTextFieldNERSchemasMerged.setText(String.valueOf(report.nerProcessesMerged().size()));
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextFieldAnnotations.setText(String.valueOf(nerProcess.getStatistics().getNerAnnotations()));
		jListNERSchemas.setModel(getNERSchemasMerged());

	}

	private ListModel getNERSchemasMerged() {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(IIEProcess nerPro: report.nerProcessesMerged())
		{
			model.addElement(nerPro);
		}
		return model;
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			this.setPreferredSize(new java.awt.Dimension(691, 532));
			{
				jPanelGeneralInfo = new JPanel();
				GridBagLayout jPanelGeneralInfoLayout = new GridBagLayout();
				this.addTab("Summary", null, jPanelGeneralInfo, null);
				jPanelGeneralInfoLayout.rowWeights = new double[] {0.0, 0.75};
				jPanelGeneralInfoLayout.rowHeights = new int[] {7, 7};
				jPanelGeneralInfoLayout.columnWeights = new double[] {0.1};
				jPanelGeneralInfoLayout.columnWidths = new int[] {7};
				jPanelGeneralInfo.setLayout(jPanelGeneralInfoLayout);
				{
					jPanelSummaryStats = new JPanel();
					GridBagLayout jPanelSummaryStatsLayout = new GridBagLayout();
					jPanelGeneralInfo.add(jPanelSummaryStats, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelSummaryStats.setBorder(BorderFactory.createTitledBorder("SummaryDetails"));
					jPanelSummaryStatsLayout.rowWeights = new double[] {0.1, 0.1};
					jPanelSummaryStatsLayout.rowHeights = new int[] {7, 7};
					jPanelSummaryStatsLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
					jPanelSummaryStatsLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelSummaryStats.setLayout(jPanelSummaryStatsLayout);
					{
						jLabelResultProcess = new JLabel();
						jPanelSummaryStats.add(jLabelResultProcess, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jLabelResultProcess.setText("NER Schema (Created) :");
					}
					{
						jTextFieldNERSchema = new JTextField();
						jTextFieldNERSchema.setEditable(false);
						jPanelSummaryStats.add(jTextFieldNERSchema, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
					}
					{
						jLabelNERSchemasMerged = new JLabel();
						jPanelSummaryStats.add(jLabelNERSchemasMerged, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jLabelNERSchemasMerged.setText("NER Schemas Merged :");
					}
					{
						jTextFieldNERSchemasMerged = new JTextField();
						jTextFieldNERSchemasMerged.setEditable(false);
						jPanelSummaryStats.add(jTextFieldNERSchemasMerged, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
					}
					{
						jLabelTotalAnnotations = new JLabel();
						jPanelSummaryStats.add(jLabelTotalAnnotations, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jLabelTotalAnnotations.setText("Annotations :");
					}
					{
						jLabelDuration = new JLabel();
						jPanelSummaryStats.add(jLabelDuration, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						jLabelDuration.setText("Duration :");
					}
					{
						jTextFieldAnnotations = new JTextField();
						jTextFieldAnnotations.setEditable(false);
						jPanelSummaryStats.add(jTextFieldAnnotations, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
					}
					{
						jTextFieldDuration = new JTextField();
						jTextFieldDuration.setEditable(false);
						jPanelSummaryStats.add(jTextFieldDuration, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
					}
				}
				{
					jPanelMergedProcesses = new JPanel();
					GridBagLayout jPanelMergedProcessesLayout = new GridBagLayout();
					jPanelGeneralInfo.add(jPanelMergedProcesses, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelMergedProcessesLayout.rowWeights = new double[] {0.1};
					jPanelMergedProcessesLayout.rowHeights = new int[] {7};
					jPanelMergedProcessesLayout.columnWeights = new double[] {0.1};
					jPanelMergedProcessesLayout.columnWidths = new int[] {7};
					jPanelMergedProcesses.setLayout(jPanelMergedProcessesLayout);
					jPanelMergedProcesses.setBorder(BorderFactory.createTitledBorder("NER Schemas Merged"));
					{
						jListNERSchemas = new JList();
						jPanelMergedProcesses.add(jListNERSchemas, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
			{
				this.addTab("Entity Details", null, new NERStatisticsPanel(nerProcess.getStatistics(),true,3));
			}
		}

	}

}
