package pt.uminho.anote2.aibench.resources.gui.report;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class MergeDictionariesReportGUI extends MergeReportGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public MergeDictionariesReportGUI(IResourceMergeReport report) {
		super(report);
	}

	public JComponent setConfltitsPanel() {
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
		sameTermsWhitDiferentClasses.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Terms whit diferent Classes", TitledBorder.LEADING, TitledBorder.TOP));
		jtabbedPaneConflist.add("Diferent Classes", sameTermsWhitDiferentClasses);
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
		finish();
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Resources_Update_Report#Conflit_Details";
	}

}
