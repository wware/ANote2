package pt.uminho.anote2.aibench.corpus.gui.help;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import es.uvigo.ei.aibench.workbench.Workbench;

import pt.uminho.anote2.aibench.corpus.datatypes.NERSchema;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.processes.INERProcessReport;
import pt.uminho.anote2.datastructures.utils.Utils;


public class NERAdvancePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private INERProcessReport report;
	private NERSchema nerSchema;
	private JPanel jPanelGeneralInfo;
	private JTabbedPane jPanelAnnotationDetails;
	private JButton jButtonExportToCSV;
	private JPanel jPanelOperations;
	private JTextField jTextFieldDuration;
	private JLabel jLabelDuration;
	private JTextField jTextFieldEntities;
	private JLabel jLabelEntities;

	public NERAdvancePanel(INERProcessReport report) throws SQLException, DatabaseLoadDriverException
	{
		this.report = report;
		this.nerSchema = new NERSchema(report.getNERProcess().getID(), report.getNERProcess().getCorpus()
				, report.getNERProcess().getName(), report.getNERProcess().getType(), report.getNERProcess().getProperties());
		initGUI();
		completeGUI();
	}

	private void completeGUI() {
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextFieldEntities.setText(String.valueOf(report.getNumberOFEntities()));
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(749, 590));
			thisLayout.rowWeights = new double[] {0.0, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelGeneralInfo = new JPanel();
				GridBagLayout jPanelGeneralInfoLayout = new GridBagLayout();
				this.add(jPanelGeneralInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelGeneralInfoLayout.rowWeights = new double[] {0.1};
				jPanelGeneralInfoLayout.rowHeights = new int[] {7};
				jPanelGeneralInfoLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.0, 0.1};
				jPanelGeneralInfoLayout.columnWidths = new int[] {7, 7, 7, 20, 7};
				jPanelGeneralInfo.setLayout(jPanelGeneralInfoLayout);
				jPanelGeneralInfo.setBorder(BorderFactory.createTitledBorder("General Information"));
				{
					jLabelEntities = new JLabel();
					jPanelGeneralInfo.add(jLabelEntities, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					jLabelEntities.setText("Entities :");
				}
				{
					jTextFieldEntities = new JTextField();
					jTextFieldEntities.setEditable(false);
					jPanelGeneralInfo.add(jTextFieldEntities, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
				}
				{
					jLabelDuration = new JLabel();
					jPanelGeneralInfo.add(jLabelDuration, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					jLabelDuration.setText("Processing Time :");
				}
				{
					jTextFieldDuration = new JTextField();
					jTextFieldDuration.setEditable(false);
					jPanelGeneralInfo.add(jTextFieldDuration, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 10), 0, 0));
				}
			}
			{
				jPanelOperations = new JPanel();
				GridBagLayout jPanelOperationsLayout = new GridBagLayout();
				this.add(jPanelOperations, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperationsLayout.rowWeights = new double[] {0.1};
				jPanelOperationsLayout.rowHeights = new int[] {7};
				jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
				jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7};
				jPanelOperations.setLayout(jPanelOperationsLayout);
				jPanelOperations.setBorder(BorderFactory.createTitledBorder("Export Options"));
				{
					jButtonExportToCSV = new JButton();
					jPanelOperations.add(jButtonExportToCSV, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonExportToCSV.setText("Export to TSV");
					ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/saveAll.png"));
					jButtonExportToCSV.setIcon(icon);
					jButtonExportToCSV.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								if(nerSchema.getAllEntities().size()>0)
								{
									new NERAdvanceExportCSVGUI(nerSchema);
								}
								else
								{
									Workbench.getInstance().warn("No Entities to Export");
								}
							} catch (SQLException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							} catch (DatabaseLoadDriverException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							}
						}
					});
				}
			}
			{
				jPanelAnnotationDetails = new NERStatisticsPanel(nerSchema.getStatistics(),true,2);
				this.add(jPanelAnnotationDetails, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelAnnotationDetails.setBorder(BorderFactory.createTitledBorder(null, "Entity Annotations Details", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			}
		}

	}

	public void refreshMemory() throws SQLException, DatabaseLoadDriverException {
		report = null;
		nerSchema.getStatistics().freeMemory();
	}

}
