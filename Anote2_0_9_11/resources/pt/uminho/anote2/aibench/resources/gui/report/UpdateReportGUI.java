package pt.uminho.anote2.aibench.resources.gui.report;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceUpdateReport;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.Utils;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class UpdateReportGUI extends DialogGenericViewOkButtonOnly{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jTextFieldDuration;
	private JLabel jLabelTime;
	private JTextField jTextFieldClassesAdded;
	private JTextField jTextFieldFile;
	private JLabel jLabelFile;
	private JTextField jTextFieldExternalIDsAdded;
	private JTextField jTextFieldSynonymsAdded;
	private JTextField jTextFieldTermsAdded;
	private JTextField jTextFieldConflits;
	private JLabel jLabelConflits;
	private JLabel jLabelClassesAdded;
	private JLabel jLabelExternalIds;
	private JLabel jLabel1SynonymsAdded;
	private JLabel jLabelTermsAdded;
	private JTextField jTextFieldFont;
	private JTextField jTextFieldDestinyresource;
	private JLabel jLabelSource;
	private JLabel jLabelDestinyResource;
	private JPanel jPanelReportInformation;
	private JList jlistSameTerms;
	private JList jlistSameTermsWhitDiferentClasses;
	private JPanel sameTerms;
	private JPanel sameTermsWhitDiferentClasses;
	private JScrollPane jscrollpanelSameTerms;
	private JScrollPane jscrollpanelSameTermsWhitDiferentClasses;
	private JTabbedPane jtabbedPaneConflist;
	private JPanel sameSynonyms;
	private JScrollPane jscrollpanelSameSynonyms;
	private JList JlistSameSynonyms;
	private JPanel sameExternalID;
	private JScrollPane jscrollpanelSameExternalID;
	private JList JlistSameExternalID;
	private IResourceUpdateReport report;

	public UpdateReportGUI(IResourceUpdateReport report)
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
				jPanelReportInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7};
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
					jLabelSource.setText("Source : ");
				}
				{
					jTextFieldDestinyresource = new JTextField();
					jTextFieldDestinyresource.setEditable(false);
					jPanelReportInformation.add(jTextFieldDestinyresource, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jTextFieldFont = new JTextField();
					jTextFieldFont.setEditable(false);
					jPanelReportInformation.add(jTextFieldFont, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
					jPanelReportInformation.add(jLabelConflits, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelConflits.setText("Conflicts : ");
				}
				{
					jTextFieldConflits = new JTextField();
					jTextFieldConflits.setEditable(false);
					jPanelReportInformation.add(jTextFieldConflits, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
					jPanelReportInformation.add(getJLabelFile(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelReportInformation.add(getJTextFieldFile(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
		// TODO Auto-generated method stub	
	}
	
	private void fillGUI() {
		if(report.isPerformed())
		{
			jTextFieldDestinyresource.setText(report.getResourceDestination().toString());
			jTextFieldConflits.setText(String.valueOf(report.getNumberConflits()));
			jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
			jTextFieldTermsAdded.setText(String.valueOf(report.getTermsAdding()));
			jTextFieldSynonymsAdded.setText(String.valueOf(report.getSynonymsAdding()));
			jTextFieldClassesAdded.setText(String.valueOf(report.getClassesAdding()));
			jTextFieldExternalIDsAdded.setText(String.valueOf(report.getExternalIDs()));
			jTextFieldFile.setText(report.getFile());
			jTextFieldFont.setText(report.getDataFont());
		}
		else
		{
			Workbench.getInstance().warn("Merge Error");
			finishing();
		}
	}
	
	protected void finishing() {
		this.setModal(false);
		this.setVisible(false);
		this.dispose();
	}

	
	public JComponent setConfltitsPanel()
	{
		jtabbedPaneConflist = new JTabbedPane();
		sameTerms = new JPanel();
		sameTerms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Replicated Terms", TitledBorder.LEADING, TitledBorder.TOP));
		jtabbedPaneConflist.add("Same Terms", sameTerms);
		BorderLayout thisLayout1 = new BorderLayout();
		sameTerms.setLayout(thisLayout1);
		jscrollpanelSameTerms = new JScrollPane();
		sameTerms.add(jscrollpanelSameTerms, BorderLayout.CENTER);
		jlistSameTerms = new JList();
		jlistSameTerms.setModel(getModal());
		jscrollpanelSameTerms.setViewportView(jlistSameTerms);
		sameTermsWhitDiferentClasses = new JPanel();
		sameTermsWhitDiferentClasses.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Terms with different Classes", TitledBorder.LEADING, TitledBorder.TOP));
		jtabbedPaneConflist.add("Different Classes", sameTermsWhitDiferentClasses);
		BorderLayout thisLayout2 = new BorderLayout();
		sameTermsWhitDiferentClasses.setLayout(thisLayout2);
		jscrollpanelSameTermsWhitDiferentClasses = new JScrollPane();
		sameTermsWhitDiferentClasses.add(jscrollpanelSameTermsWhitDiferentClasses,BorderLayout.CENTER);	
		jlistSameTermsWhitDiferentClasses = new JList();
		jlistSameTermsWhitDiferentClasses.setModel(getModalClassConflits());
		sameTermsWhitDiferentClasses.add(jscrollpanelSameTermsWhitDiferentClasses, BorderLayout.CENTER);
		jscrollpanelSameTermsWhitDiferentClasses.setViewportView(jlistSameTermsWhitDiferentClasses);	
		sameSynonyms = new JPanel();
		sameSynonyms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Replicated Synonyms", TitledBorder.LEADING, TitledBorder.TOP));
		jtabbedPaneConflist.add("Same Synonyms", sameSynonyms);
		BorderLayout thisLayout3 = new BorderLayout();
		sameSynonyms.setLayout(thisLayout3);
		jscrollpanelSameSynonyms = new JScrollPane();
		sameSynonyms.add(jscrollpanelSameSynonyms, BorderLayout.CENTER);
		JlistSameSynonyms = new JList();
		JlistSameSynonyms.setModel(getModalSynonyms());
		jscrollpanelSameSynonyms.setViewportView(JlistSameSynonyms);
		sameExternalID = new JPanel();
		sameExternalID.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Replicated External ID's", TitledBorder.LEADING, TitledBorder.TOP));
		jtabbedPaneConflist.add("Same External ID's", sameExternalID);
		BorderLayout thisLayout4 = new BorderLayout();
		sameExternalID.setLayout(thisLayout4);
		jscrollpanelSameExternalID = new JScrollPane();
		sameExternalID.add(jscrollpanelSameExternalID, BorderLayout.CENTER);
		JlistSameExternalID = new JList();
		JlistSameExternalID.setModel(getModalExternalID());
		jscrollpanelSameExternalID.setViewportView(JlistSameExternalID);
		return jtabbedPaneConflist;
	}

	public IResourceUpdateReport getReport() {
		return report;
	}

	public void setReport(IResourceUpdateReport report) {
		this.report = report;
	}
	
	private ListModel getModalExternalID() {
		DefaultListModel listmodel = new DefaultListModel();
		for(IInsertConflits conflit: getReport().getConflicts())
		{
			if(conflit.getConflitType()==ConflitsType.AlteradyHaveExternalID)
			{
				List<IExternalID> extIDs = conflit.getOriginalTerm().getExtenalIDs();
				for(IExternalID extID :extIDs)
					listmodel.addElement(extID);
			}
		}
		return listmodel;
	}

	private ListModel getModalSynonyms() {
		DefaultListModel listmodel = new DefaultListModel();
		for(IInsertConflits conflit: getReport().getConflicts())
		{
			if(conflit.getConflitType()==ConflitsType.AlreadyHaveSynonyms)
				listmodel.addElement(conflit.getOriginalTerm().getTerm());
		}
		return listmodel;
	}

	private ListModel getModal() {
		DefaultListModel listmodel = new DefaultListModel();
		for(IInsertConflits conflit: getReport().getConflicts())
		{
			if(conflit.getConflitType()==ConflitsType.AlreadyHaveTerm)
				listmodel.addElement(conflit.getOriginalTerm().getTerm());
		}
		return listmodel;
	}
	
	private ListModel getModalClassConflits() {
		DefaultListModel listmodel = new DefaultListModel();
		for(IInsertConflits conflit: getReport().getConflicts())
		{
			if(conflit.getConflitType()==ConflitsType.TermInDiferentClasses)
				listmodel.addElement(conflit.getOriginalTerm().getTerm()+ " ( " + ClassProperties.getClassIDClass().get(conflit.getOriginalTerm().getTermClassID()) + " <- "+ClassProperties.getClassIDClass().get(conflit.getConflitTerm().getTermClassID())+" )");
		}
		return listmodel;
	}

	protected void okButtonAction() {
		finishing();
		
	}
	
	private JLabel getJLabelFile() {
		if(jLabelFile == null) {
			jLabelFile = new JLabel();
			jLabelFile.setText("File : ");
		}
		return jLabelFile;
	}
	
	private JTextField getJTextFieldFile() {
		if(jTextFieldFile == null) {
			jTextFieldFile = new JTextField();
			jTextFieldFile.setEditable(false);
		}
		return jTextFieldFile;
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Resources_Update_Report";
	}

}
