package pt.uminho.anote2.aibench.publicationmanager.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.core.report.processes.ir.IIRSearchProcessReport;
import pt.uminho.anote2.datastructures.utils.Utils;

public class IRSearchPanelReport extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5717376060257039828L;
	private JLabel jLabelDuration;
	private JTextField jTextFieldTime;
	private JTextField jTextFieldOrgnism;
	private JLabel jLabelOrganism;
	private JTextField jTextFieldKeywords;
	private JLabel jLabelKeywords;
	private JTextField jTextFieldDocuments;
	private JLabel jLabelNumberODocuments;
	private IIRSearchProcessReport report;

	public IRSearchPanelReport(IIRSearchProcessReport report)
	{
		this.report=report;
		initGUI();
	}

	private void initGUI() {
		GridBagLayout jPanelMainInfoLayout = new GridBagLayout();
		jPanelMainInfoLayout.rowWeights = new double[] {0.0, 0.0, 0.1, 0.1, 0.1};
		jPanelMainInfoLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
		jPanelMainInfoLayout.columnWeights = new double[] {0.05, 0.1, 0.1};
		jPanelMainInfoLayout.columnWidths = new int[] {7, 7, 7};
		putBorder();
		this.setLayout(jPanelMainInfoLayout);
		{
			jLabelNumberODocuments = new JLabel();
			this.add(jLabelNumberODocuments, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabelNumberODocuments.setText("Documents Retrieved :");
		}
		{
			jTextFieldDocuments = new JTextField();
			this.add(jTextFieldDocuments, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jTextFieldDocuments.setText(String.valueOf(report.getNumberOfDocuments()));
			jTextFieldDocuments.setEditable(false);
		}
		{
			jLabelDuration = new JLabel();
			this.add(jLabelDuration, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabelDuration.setText("Processing Time :");
		}
		{
			jTextFieldTime = new JTextField();
			this.add(jTextFieldTime, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			jTextFieldTime.setText(Utils.convertTimeToString(report.getTime()));
			jTextFieldTime.setEditable(false);
		}
		if(report.getKeywords()!=null)
		{
			{
				jLabelKeywords = new JLabel();
				this.add(jLabelKeywords, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelKeywords.setText("Keywords :");
			}
			{
				jTextFieldKeywords = new JTextField();
				this.add(jTextFieldKeywords, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 5), 0, 0));
				jTextFieldKeywords.setText(report.getKeywords());
				jTextFieldKeywords.setEditable(false);
			}
		}
		if(report.getOrganism()!=null)
		{
			{
				jLabelOrganism = new JLabel();
				this.add(jLabelOrganism, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelOrganism.setText("Organism :");
			}
			{
				jTextFieldOrgnism = new JTextField();
				this.add(jTextFieldOrgnism, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 5, 10, 5), 0, 0));
				jTextFieldOrgnism.setText(report.getOrganism());
				jTextFieldOrgnism.setEditable(false);
			}
		}		
	}

	protected void putBorder() {
		this.setBorder(BorderFactory.createTitledBorder(null, "Pubmed Search Report", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
	}
	
	protected void changeDocumentRetrieval(String newNAme)
	{
		jLabelNumberODocuments.setText(newNAme);
	}
	
	
	
}
