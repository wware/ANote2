package pt.uminho.anote2.aibench.resources.gui.report;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public abstract class MergeReportGUI  extends DialogGenericViewOkButtonOnly{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jTextFieldDuration;
	private JLabel jLabelTime;
	private JTextField jTextFieldClassesAdded;
	private JTextField jTextFieldExternalIDsAdded;
	private JTextField jTextFieldSynonymsAdded;
	private JTextField jTextFieldTermsAdded;
	private JTextField jTextFieldConflits;
	private JLabel jLabelConflits;
	private JLabel jLabelClassesAdded;
	private JTextField jTextFieldSource2;
	private JLabel jLabelExternalIds;
	private JLabel jLabel1SynonymsAdded;
	private JLabel jLabelTermsAdded;
	private JTextField jTextFieldSource;
	private JTextField jTextFieldDestinyresource;
	private JLabel jLabelSourceResource2;
	private JLabel jLabelSource;
	private JLabel jLabelDestinyResource;
	private JPanel jPanelReportInformation;
	private IResourceMergeReport report;

	public MergeReportGUI(IResourceMergeReport report)
	{
		this.setReport(report);
		initGUI();
		fillGUI();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		this.setTitle(report.getTitle());
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.05, 0.2, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				getContentPane().add(setConfltitsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelReportInformation = new JPanel();
				jPanelReportInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Report Information", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelReportInformationLayout = new GridBagLayout();
				getContentPane().add(jPanelReportInformation, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelReportInformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
				jPanelReportInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 20};
				jPanelReportInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				jPanelReportInformationLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelReportInformation.setLayout(jPanelReportInformationLayout);
				{
					jLabelDestinyResource = new JLabel();
					jPanelReportInformation.add(jLabelDestinyResource, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelDestinyResource.setText("Destiny Resource : ");
				}
				{
					jLabelSource = new JLabel();
					jPanelReportInformation.add(jLabelSource, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelSource.setText("Source Resource : ");
				}
				{
					jLabelSourceResource2 = new JLabel();
					jPanelReportInformation.add(jLabelSourceResource2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelSourceResource2.setText("Other Source Resource *  : ");
				}
				{
					jTextFieldDestinyresource = new JTextField();
					jTextFieldDestinyresource.setEditable(false);
					jPanelReportInformation.add(jTextFieldDestinyresource, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldSource = new JTextField();
					jTextFieldSource.setEditable(false);
					jPanelReportInformation.add(jTextFieldSource, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldSource2 = new JTextField();
					jTextFieldSource2.setEditable(false);
					jPanelReportInformation.add(jTextFieldSource2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jLabelTermsAdded = new JLabel();
					jPanelReportInformation.add(jLabelTermsAdded, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelTermsAdded.setText("Terms Added : ");
				}
				{
					jLabel1SynonymsAdded = new JLabel();
					jPanelReportInformation.add(jLabel1SynonymsAdded, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel1SynonymsAdded.setText("Synonyms Added : ");
				}
				{
					jLabelExternalIds = new JLabel();
					jPanelReportInformation.add(jLabelExternalIds, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelExternalIds.setText("External ID's Added : ");
				}
				{
					jLabelClassesAdded = new JLabel();
					jPanelReportInformation.add(jLabelClassesAdded, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelClassesAdded.setText("Classes Added : ");
				}
				{
					jLabelConflits = new JLabel();
					jPanelReportInformation.add(jLabelConflits, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelConflits.setText("Conflits : ");
				}
				{
					jTextFieldConflits = new JTextField();
					jTextFieldConflits.setEditable(false);
					jPanelReportInformation.add(jTextFieldConflits, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldTermsAdded = new JTextField();
					jTextFieldTermsAdded.setEditable(false);
					jPanelReportInformation.add(jTextFieldTermsAdded, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldSynonymsAdded = new JTextField();
					jTextFieldSynonymsAdded.setEditable(false);
					jPanelReportInformation.add(jTextFieldSynonymsAdded, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldExternalIDsAdded = new JTextField();
					jTextFieldExternalIDsAdded.setEditable(false);
					jPanelReportInformation.add(jTextFieldExternalIDsAdded, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldClassesAdded = new JTextField();
					jTextFieldClassesAdded.setEditable(false);
					jPanelReportInformation.add(jTextFieldClassesAdded, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jLabelTime = new JLabel();
					jPanelReportInformation.add(jLabelTime, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelTime.setText("Duration : ");
				}
				{
					jTextFieldDuration = new JTextField();
					jTextFieldDuration.setEditable(false);
					jPanelReportInformation.add(jTextFieldDuration, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
		// TODO Auto-generated method stub	
	}
	
	private void fillGUI() {
		if(report.isPerformed())
		{
			if(report.isCreatedNewResource())
			{		
				jTextFieldSource2.setText(report.getResourceSource2().toString());
			}
			jTextFieldDestinyresource.setText(report.getResourceDestination().toString());
			jTextFieldSource.setText(report.getResourceSource().toString());
			jTextFieldConflits.setText(String.valueOf(report.getNumberConflits()));
			jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
			jTextFieldTermsAdded.setText(String.valueOf(report.getTermsAdding()));
			jTextFieldSynonymsAdded.setText(String.valueOf(report.getSynonymsAdding()));
			jTextFieldClassesAdded.setText(String.valueOf(report.getClassesAdding()));
			jTextFieldExternalIDsAdded.setText(String.valueOf(report.getExternalIDs()));
		}
		else
		{
			Workbench.getInstance().warn("Merge Error");
			finish();
		}
	}
	
	public abstract JComponent setConfltitsPanel();

	public IResourceMergeReport getReport() {
		return report;
	}

	public void setReport(IResourceMergeReport report) {
		this.report = report;
	}
	
	

}
