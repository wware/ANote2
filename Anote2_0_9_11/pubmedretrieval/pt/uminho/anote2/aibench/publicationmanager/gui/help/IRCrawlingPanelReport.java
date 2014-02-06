package pt.uminho.anote2.aibench.publicationmanager.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.report.processes.ir.IIRCrawlingProcessReport;
import pt.uminho.anote2.datastructures.utils.Utils;


public class IRCrawlingPanelReport extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IIRCrawlingProcessReport report;
	private JTabbedPane jTabbedPaneDownloadResults;
	private JList jListRestrictDownload;
	private JTextField jTextFieldAlreadyDownloaded;
	private JLabel jLabelAlreadyDownloaded;
	private JList jListWithoutDownloaded;
	private JList jListDownloads;
	private JScrollPane jScrollPaneDowloaded;
	private JTextField jTextFieldDuration;
	private JPanel jPanelDuration;
	private JLabel jLabelDuration;
	private JTextField jTextFieldRestrictedDownload;
	private JTextField jTextFieldWithouDownloades;
	private JTextField jTextPaneDownloades;
	private JLabel jLabelRestrictedDownload;
	private JLabel jLabelNotDownloaded;
	private JLabel jLabelDownload;
	private JPanel jPanelSummanry;
	private JPanel jPanelMainInformation;
	private JScrollPane jScrollPaneWithouDowloaded;
	private JScrollPane jScrollPaneREstrictedDowloaded;
	private JScrollPane jScrollPaneAlreadyDowloaded;
	private JList jListAlreadyDownload;

	public IRCrawlingPanelReport(IIRCrawlingProcessReport report)
	{
		this.report = report;
		initGUI();
		completeGUI();
	}

	private void completeGUI() {
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextPaneDownloades.setText(String.valueOf(report.getDocumentsRetrieval()));
		jTextFieldWithouDownloades.setText(String.valueOf(report.getListPublicationsNotDownloaded().size()));
		jTextFieldRestrictedDownload.setText(String.valueOf(report.getListPublicationRetrictedDownloaded().size()));
		jTextFieldAlreadyDownloaded.setText(String.valueOf(report.getListPublicationsAlreadyDownloaded().size()));
		Set<IPublication> listDownloads = report.getListPublicationsDownloaded();
		for(IPublication pub : listDownloads)
		{
			((DefaultComboBoxModel)jListDownloads.getModel()).addElement(pub);
		}
		Set<IPublication> withouDownloads = report.getListPublicationsNotDownloaded();
		for(IPublication pub : withouDownloads)
		{
			((DefaultComboBoxModel)jListWithoutDownloaded.getModel()).addElement(pub);
		}
		Set<IPublication> restrictedDownloads = report.getListPublicationRetrictedDownloaded();
		for(IPublication pub : restrictedDownloads)
		{
			((DefaultComboBoxModel)jListRestrictDownload.getModel()).addElement(pub);
		}
		Set<IPublication> alredyDownloaded = report.getListPublicationsAlreadyDownloaded();
		for(IPublication pub : alredyDownloaded)
		{
			((DefaultComboBoxModel)jListAlreadyDownload.getModel()).addElement(pub);
		}
	}

	private void initGUI() {
		{

			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(649, 430));
			thisLayout.rowWeights = new double[] {0.25, 0.75};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setBorder(BorderFactory.createTitledBorder(null, "Journal Retrieval Report", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			this.setLayout(thisLayout);
			{
				jTabbedPaneDownloadResults = new JTabbedPane();
				this.add(jTabbedPaneDownloadResults, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jScrollPaneDowloaded = new JScrollPane();
					jTabbedPaneDownloadResults.addTab("Downloaded", null, jScrollPaneDowloaded, null);
					{
						ListModel jListDownloadsModel = 
								new DefaultComboBoxModel();
						jListDownloads = new JList();
						jScrollPaneDowloaded.setViewportView(jListDownloads);
						jListDownloads.setModel(jListDownloadsModel);
					}
					jScrollPaneAlreadyDowloaded = new JScrollPane();
					jTabbedPaneDownloadResults.addTab("Previously Downloaded", null, jScrollPaneAlreadyDowloaded, null);
					{
						ListModel jListRestrictDownloadModel = 
								new DefaultComboBoxModel();
						jListAlreadyDownload = new JList();
						jScrollPaneAlreadyDowloaded.setViewportView(jListAlreadyDownload);
						jListAlreadyDownload.setModel(jListRestrictDownloadModel);
					}
					jScrollPaneWithouDowloaded = new JScrollPane();
					jTabbedPaneDownloadResults.addTab("Not Downloaded", null, jScrollPaneWithouDowloaded, null);
					{
						ListModel jListWithoutDownloadedModel = 
								new DefaultComboBoxModel();
						jListWithoutDownloaded = new JList();
						jScrollPaneWithouDowloaded.setViewportView(jListWithoutDownloaded);
						jListWithoutDownloaded.setModel(jListWithoutDownloadedModel);
					}
					jScrollPaneREstrictedDowloaded = new JScrollPane();
					jTabbedPaneDownloadResults.addTab("Restricted Access", null, jScrollPaneREstrictedDowloaded, null);
					{
						ListModel jListRestrictDownloadModel = 
								new DefaultComboBoxModel();
						jListRestrictDownload = new JList();
						jScrollPaneREstrictedDowloaded.setViewportView(jListRestrictDownload);
						jListRestrictDownload.setModel(jListRestrictDownloadModel);
					}

				}
			}
			{
				jPanelMainInformation = new JPanel();
				GridBagLayout jPanelMainInformationLayout = new GridBagLayout();
				this.add(jPanelMainInformation, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelMainInformationLayout.rowWeights = new double[] {0.1, 0.1};
				jPanelMainInformationLayout.rowHeights = new int[] {7, 7};
				jPanelMainInformationLayout.columnWeights = new double[] {0.1};
				jPanelMainInformationLayout.columnWidths = new int[] {7};
				jPanelMainInformation.setLayout(jPanelMainInformationLayout);
				{
					jPanelSummanry = new JPanel();
					GridBagLayout jPanelSummanryLayout = new GridBagLayout();
					jPanelMainInformation.add(jPanelSummanry, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelSummanryLayout.rowWeights = new double[] {0.1};
					jPanelSummanryLayout.rowHeights = new int[] {7};
					jPanelSummanryLayout.columnWeights = new double[] {0.05, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
					jPanelSummanryLayout.columnWidths = new int[] {7, 7, 20, 20, 20, 20, 7, 7};
					jPanelSummanry.setLayout(jPanelSummanryLayout);
					{
						jLabelDownload = new JLabel();
						jPanelSummanry.add(jLabelDownload, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelDownload.setText("Downloaded :");
					}
					{
						jLabelNotDownloaded = new JLabel();
						jPanelSummanry.add(jLabelNotDownloaded, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelNotDownloaded.setText("Not Downloaded :");
					}
					{
						jLabelRestrictedDownload = new JLabel();
						jPanelSummanry.add(jLabelRestrictedDownload, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelRestrictedDownload.setText("Restricted Access :");
					}
					{
						jTextPaneDownloades = new JTextField();
						jTextPaneDownloades.setEditable(false);
						jPanelSummanry.add(jTextPaneDownloades, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
					{
						jTextFieldWithouDownloades = new JTextField();
						jTextFieldWithouDownloades.setEditable(false);
						jPanelSummanry.add(jTextFieldWithouDownloades, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
					{
						jTextFieldRestrictedDownload = new JTextField();
						jTextFieldRestrictedDownload.setEditable(false);
						jPanelSummanry.add(jTextFieldRestrictedDownload, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
					{
						jLabelAlreadyDownloaded = new JLabel();
						jPanelSummanry.add(jLabelAlreadyDownloaded, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelAlreadyDownloaded.setText("Previously Downloaded");
					}
					{
						jTextFieldAlreadyDownloaded = new JTextField();
						jTextFieldAlreadyDownloaded.setEditable(false);
						jPanelSummanry.add(jTextFieldAlreadyDownloaded, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
				}
				{
					jPanelDuration = new JPanel();
					GridBagLayout jPanelDurationLayout = new GridBagLayout();
					jPanelMainInformation.add(jPanelDuration, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelDurationLayout.rowWeights = new double[] {0.1};
					jPanelDurationLayout.rowHeights = new int[] {7};
					jPanelDurationLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelDurationLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelDuration.setLayout(jPanelDurationLayout);
					{
						jLabelDuration = new JLabel();
						jPanelDuration.add(jLabelDuration, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabelDuration.setText("Processing Time :");
					}
					{
						jTextFieldDuration = new JTextField();
						jTextFieldDuration.setEditable(false);
						jPanelDuration.add(jTextFieldDuration, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
					}
				}
			}
		}

	}

	
	
}
