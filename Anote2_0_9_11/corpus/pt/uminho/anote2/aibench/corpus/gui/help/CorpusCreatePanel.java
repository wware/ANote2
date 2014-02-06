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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.corpora.ICorpusCreateReport;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.process.IE.IIEProcess;


public class CorpusCreatePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ICorpusCreateReport report;
	private JTabbedPane jTabbedPaneIEProcesses;
	private JTextField jTextField1TextType;
	private JTextField jTextFieldCorpusName;
	private JLabel jLabelName;
	private JList jListRE;
	private JList jListNER;
	private JScrollPane jScrollPaneREPRocesses;
	private JScrollPane jScrollPaneBERProcesses;
	private JTextField jTextFieldDuration;
	private JLabel jLabelDuration;
	private JPanel jPanelDuration;
	private JTextField jTextFieldProcesses;
	private JTextField jTextFieldDocuments;
	private JLabel jLabelType;
	private JLabel jLabelProcesses;
	private JLabel jLabelPublications;
	private JPanel jPanelSummaryStats;
	private JPanel jPanelSummaryPanel;

	public CorpusCreatePanel(ICorpusCreateReport report) throws SQLException, DatabaseLoadDriverException
	{
		this.report = report;
		initGUI();
		completeGUI();
	}

	private void completeGUI() throws SQLException, DatabaseLoadDriverException {
		jTextFieldCorpusName.setText(report.getName());
		jTextFieldDocuments.setText(String.valueOf(report.getDocumentSize()));
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextFieldProcesses.setText(String.valueOf(report.getProcesses().size()));
		jTextField1TextType.setText(report.getCorpusTextType().toString());
		
		for(IIEProcess process : report.getProcesses())
		{
			if(process.getType().equals(GlobalNames.ner))
				((DefaultComboBoxModel)jListNER.getModel()).addElement(process);
			else
				((DefaultComboBoxModel)jListRE.getModel()).addElement(process);
		}		
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(643, 417));
			thisLayout.rowWeights = new double[] {0.25, 0.75};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.setBorder(BorderFactory.createTitledBorder("Create Corpus Report"));
			{
				jTabbedPaneIEProcesses = new JTabbedPane();
				this.add(jTabbedPaneIEProcesses, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				if(report.getProcesses().size() > 0)
				{
					{
						jScrollPaneBERProcesses = new JScrollPane();
						jTabbedPaneIEProcesses.addTab("NER Processes", null, jScrollPaneBERProcesses, null);
						{
							ListModel jListNERModel = 
									new DefaultComboBoxModel();
							jListNER = new JList();
							jScrollPaneBERProcesses.setViewportView(jListNER);
							jListNER.setModel(jListNERModel);
						}
					}
					{
						jScrollPaneREPRocesses = new JScrollPane();
						jTabbedPaneIEProcesses.addTab("RE Processes", null, jScrollPaneREPRocesses, null);
						{
							ListModel jListREModel = 
									new DefaultComboBoxModel();
							jListRE = new JList();
							jScrollPaneREPRocesses.setViewportView(jListRE);
							jListRE.setModel(jListREModel);
						}
					}
				}
			}
			{
				jPanelSummaryPanel = new JPanel();
				GridBagLayout jPanelSummaryPanelLayout = new GridBagLayout();
				this.add(jPanelSummaryPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSummaryPanelLayout.rowWeights = new double[] {0.1, 0.1};
				jPanelSummaryPanelLayout.rowHeights = new int[] {7, 7};
				jPanelSummaryPanelLayout.columnWeights = new double[] {0.1};
				jPanelSummaryPanelLayout.columnWidths = new int[] {7};
				jPanelSummaryPanel.setLayout(jPanelSummaryPanelLayout);
				{
					jPanelSummaryStats = new JPanel();
					GridBagLayout jPanelSummaryStatsLayout = new GridBagLayout();
					jPanelSummaryPanel.add(jPanelSummaryStats, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelSummaryStatsLayout.rowWeights = new double[] {0.1};
					jPanelSummaryStatsLayout.rowHeights = new int[] {7};
					jPanelSummaryStatsLayout.columnWeights = new double[] {0.05, 0.1, 0.1, 0.1, 0.1, 0.1};
					jPanelSummaryStatsLayout.columnWidths = new int[] {7, 7, 7, 20, 20, 7};
					jPanelSummaryStats.setLayout(jPanelSummaryStatsLayout);
					{
						jLabelPublications = new JLabel();
						jPanelSummaryStats.add(jLabelPublications, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelPublications.setText("Documents :");
					}
					{
						jLabelProcesses = new JLabel();
						jPanelSummaryStats.add(jLabelProcesses, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelProcesses.setText("Processes :");
					}
					{
						jLabelType = new JLabel();
						jPanelSummaryStats.add(jLabelType, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelType.setText("Text Type:");
					}
					{
						jTextFieldDocuments = new JTextField();
						jTextFieldDocuments.setEditable(false);
						jPanelSummaryStats.add(jTextFieldDocuments, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
					{
						jTextFieldProcesses = new JTextField();
						jTextFieldProcesses.setEditable(false);
						jPanelSummaryStats.add(jTextFieldProcesses, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
					{
						jTextField1TextType = new JTextField();
						jTextField1TextType.setEditable(false);
						jPanelSummaryStats.add(jTextField1TextType, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
				}
				{
					jPanelDuration = new JPanel();
					GridBagLayout jPanelDurationLayout = new GridBagLayout();
					jPanelSummaryPanel.add(jPanelDuration, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelDurationLayout.rowWeights = new double[] {0.1};
					jPanelDurationLayout.rowHeights = new int[] {7};
					jPanelDurationLayout.columnWeights = new double[] {0.05, 0.1, 0.05, 0.1};
					jPanelDurationLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelDuration.setLayout(jPanelDurationLayout);
					{
						jLabelDuration = new JLabel();
						jPanelDuration.add(jLabelDuration, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelDuration.setText("Processing Time :");
					}
					{
						jTextFieldDuration = new JTextField();
						jPanelDuration.add(jTextFieldDuration, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
						jTextFieldDuration.setEditable(false);
					}
					{
						jLabelName = new JLabel();
						jPanelDuration.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelName.setText("Name :");
					}
					{
						jTextFieldCorpusName = new JTextField();
						jTextFieldCorpusName.setEditable(false);
						jPanelDuration.add(jTextFieldCorpusName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
		}

	}
}
