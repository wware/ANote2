package pt.uminho.anote2.aibench.resources.gui.report;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class MergeRulesSerReportGUI extends MergeReportGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private JPanel conflits;
	private JList jlistSameTerms;
	private JList jlistSameTermsWhitDiferentClasses;
	private JPanel sameTerms;
	private JPanel sameTermsWhitDiferentClasses;
	private JScrollPane jscrollpanelSameTerms;
	private JScrollPane jscrollpanelSameTermsWhitDiferentClasses;
	
	public MergeRulesSerReportGUI(IResourceMergeReport report) {
		super(report);		
	}
	
	
	@Override
	public JComponent setConfltitsPanel() {
		conflits = new JPanel();
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1,0.1};
		thisLayout.rowHeights = new int[] {7,7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		conflits.setLayout(thisLayout);
		sameTerms = new JPanel();
		GridBagLayout thisLayoutSameTerms = new GridBagLayout();
		thisLayoutSameTerms.rowWeights = new double[] {0.1};
		thisLayoutSameTerms.rowHeights = new int[] {7};
		thisLayoutSameTerms.columnWeights = new double[] {0.1};
		thisLayoutSameTerms.columnWidths = new int[] {7};
		sameTerms.setLayout(thisLayoutSameTerms);
		jscrollpanelSameTerms = new JScrollPane();
		sameTerms.add(jscrollpanelSameTerms, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jlistSameTerms = new JList();
		jlistSameTerms.setModel(getModal());
		jscrollpanelSameTerms.setViewportView(jlistSameTerms);
		sameTerms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "List of Conflits (replicated Terms)", TitledBorder.LEADING, TitledBorder.TOP));
		conflits.add(sameTerms, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		sameTermsWhitDiferentClasses = new JPanel();
		sameTermsWhitDiferentClasses.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "List of Conflits (Terms whit diferent classes)", TitledBorder.LEADING, TitledBorder.TOP));
		GridBagLayout thisLayoutSameTermsDifClasses = new GridBagLayout();
		thisLayoutSameTermsDifClasses.rowWeights = new double[] {0.1};
		thisLayoutSameTermsDifClasses.rowHeights = new int[] {7};
		thisLayoutSameTermsDifClasses.columnWeights = new double[] {0.1};
		thisLayoutSameTermsDifClasses.columnWidths = new int[] {7};
		sameTermsWhitDiferentClasses.setLayout(thisLayoutSameTermsDifClasses);
		jscrollpanelSameTermsWhitDiferentClasses = new JScrollPane();
		sameTermsWhitDiferentClasses.add(jscrollpanelSameTermsWhitDiferentClasses, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
		jlistSameTermsWhitDiferentClasses = new JList();
		jlistSameTermsWhitDiferentClasses.setModel(getModalClassConflits());
		jscrollpanelSameTermsWhitDiferentClasses.setViewportView(jlistSameTermsWhitDiferentClasses);
		conflits.add(sameTermsWhitDiferentClasses, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		return conflits;
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

	@Override
	protected void okButtonAction() {
		finish();
	}


	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"RulesSet_Merge#Report";
	}
	
	

}
