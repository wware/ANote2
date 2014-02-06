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

import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

public class MergeLexicalWordsReportGUI extends MergeReportGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel conflits;
	private JScrollPane jscrollPane;
	private JList jlist;
	
	public MergeLexicalWordsReportGUI(IResourceMergeReport report) {
		super(report);
	}

	@Override
	public JComponent setConfltitsPanel() {
		conflits = new JPanel();
		conflits.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "List of Conflits (replicated Terms)", TitledBorder.LEADING, TitledBorder.TOP));
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1};
		thisLayout.rowHeights = new int[] {7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		conflits.setLayout(thisLayout);
		jscrollPane = new JScrollPane();
		conflits.add(jscrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
		jlist = new JList();
		jlist.setModel(getModal());
		jscrollPane.setViewportView(jlist);	
		return conflits;
	}

	private ListModel getModal() {
		DefaultListModel listmodel = new DefaultListModel();
		for(IInsertConflits conflit: getReport().getConflicts())
		{
			listmodel.addElement(conflit.getOriginalTerm().getTerm());
		}
		return listmodel;
	}

	@Override
	protected void okButtonAction() {
		finish();
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LexicalWords_Merge#Report";
	}
	
	

}
