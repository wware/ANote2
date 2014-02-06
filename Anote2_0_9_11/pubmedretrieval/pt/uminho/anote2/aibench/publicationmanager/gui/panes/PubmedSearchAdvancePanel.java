package pt.uminho.anote2.aibench.publicationmanager.gui.panes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;

public class PubmedSearchAdvancePanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8522640776375735428L;
	private JPanel jPanelQueryProperties;
	private JLabel authorLabel;
	private JLabel journalLabel;
	private JLabel dateLabel;
	private JTextField authorTextField;
	private JTextField journalTextField;
	private JLabel jLabelType;
	private JComboBox jComboBoxType;
	private JComboBox fromComboBox;
	private JLabel toLabel;
	private JComboBox toComboBox;
	private JPanel jPanelRepositoryInformation;
	private JCheckBox jCheckBoxAllDocuments;
	private JCheckBox jCheckBoxMedline;
	private JCheckBox jCheckPubmed;
	private JPanel jPanelArticleDetails;
	private ButtonGroup groupButton;
	private JCheckBox jCheckBoxFreeFullText;
	private JCheckBox jCheckBoxAll;
	private PubmedSearchSimplePane jPanelSimpleOrganismAndKeywords;
	private JCheckBox jCheckBoxAvailableAbstract;
	private JCheckBox jCheckBoxFullText;
	
	public PubmedSearchAdvancePanel()
	{
		initPanel();
	}

	private void initPanel() {
		GridBagLayout jPanelAdvancePAnelLayout = new GridBagLayout();
		jPanelAdvancePAnelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
		jPanelAdvancePAnelLayout.rowHeights = new int[] {20, 7, 7};
		jPanelAdvancePAnelLayout.columnWeights = new double[] {0.1, 0.1};
		jPanelAdvancePAnelLayout.columnWidths = new int[] {7, 7};
		this.setLayout(jPanelAdvancePAnelLayout);
		this.add(getJPanelQueryProperties(), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJpanelRepositoryInformation(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJPanelArticleDetails(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));		
		this.add(getJPanel1(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	private JPanel getJPanelQueryProperties() {
		if(jPanelQueryProperties == null) {
			jPanelQueryProperties = new JPanel();
			GridBagLayout jPanelQueryPropertiesLayout = new GridBagLayout();
			jPanelQueryPropertiesLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelQueryPropertiesLayout.rowHeights = new int[] {7, 7, 7};
			jPanelQueryPropertiesLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1};
			jPanelQueryPropertiesLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelQueryProperties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query Properties", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelQueryProperties.setLayout(jPanelQueryPropertiesLayout);
			{
				authorLabel = new JLabel();
				jPanelQueryProperties.add(authorLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				authorLabel.setText("Author:");
			}
			{
				journalLabel = new JLabel();
				jPanelQueryProperties.add(journalLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				journalLabel.setText("Journal:");
			}
			{
				dateLabel = new JLabel();
				jPanelQueryProperties.add(dateLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				dateLabel.setText("Publication Date:");
			}
			{
				authorTextField = new JTextField();
				jPanelQueryProperties.add(authorTextField, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				authorTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				journalTextField = new JTextField();
				jPanelQueryProperties.add(journalTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				journalTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				jLabelType = new JLabel();
				jPanelQueryProperties.add(jLabelType, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelType.setText("Type :");
			}
			{
				ComboBoxModel jComboBoxTypeModel = 
					new DefaultComboBoxModel(
							new String[] { "All","Journal Article", "Review","Technical Report"});
				jComboBoxType = new JComboBox();
				jPanelQueryProperties.add(jComboBoxType, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jComboBoxType.setSelectedItem("All");
				jComboBoxType.setModel(jComboBoxTypeModel);
			}
			{
				ComboBoxModel fromComboBoxModel = dateComboModel(false);
				fromComboBox = new JComboBox();
				jPanelQueryProperties.add(fromComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				fromComboBox.setModel(fromComboBoxModel);
				fromComboBox.setSelectedIndex(0);
			}
			{
				toLabel = new JLabel();
				jPanelQueryProperties.add(toLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				toLabel.setText("to");
			}
			{
				ComboBoxModel toComboBoxModel = dateComboModel(true);
				toComboBox = new JComboBox();
				jPanelQueryProperties.add(toComboBox, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				toComboBox.setModel(toComboBoxModel);
				toComboBox.setSelectedIndex(0);
			}
		}
		

		return jPanelQueryProperties;
	}
	
	private DefaultComboBoxModel dateComboModel(boolean istodate){

		DefaultComboBoxModel model = new DefaultComboBoxModel();		
		int current_year = new GregorianCalendar().get(Calendar.YEAR);	
		if(istodate)
		{
			model.addElement("present");
			for(int year=current_year-1; year>=PublicationManager.searchYearStarting; year--)
				model.addElement(String.valueOf(year));
		}
		else
			for(int year=PublicationManager.searchYearStarting; year<=current_year; year++)
				model.addElement(String.valueOf(year));	
		return model;
	}
	
	private JPanel getJpanelRepositoryInformation() {
		if(jPanelRepositoryInformation == null)
		{
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanel1Layout.rowHeights = new int[] {7, 7, 7};
			jPanel1Layout.columnWeights = new double[] {0.1};
			jPanel1Layout.columnWidths = new int[] {7};
			jPanelRepositoryInformation = new JPanel();
			jPanelRepositoryInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Repository Information", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelRepositoryInformation.setLayout(jPanel1Layout);
			jPanelRepositoryInformation.add(getCheckPubmed(), new GridBagConstraints(-1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelRepositoryInformation.add(getCheckBoxMedline(), new GridBagConstraints(-1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));			
			jPanelRepositoryInformation.add(getJCheckBoxAllDocuments(), new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRepositoryInformation;
	}
	
	private JCheckBox getJCheckBoxAllDocuments() {
		if(jCheckBoxAllDocuments == null) {
			jCheckBoxAllDocuments = new JCheckBox();
			jCheckBoxAllDocuments.setText("All Documents");
			jCheckBoxAllDocuments.setSelected(true);
			jCheckBoxAllDocuments.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sellectAll();
				}
			});
		}
		return jCheckBoxAllDocuments;
	}
	
	private void sellectAll() {
		if(jCheckBoxAllDocuments.isSelected())
		{
			jCheckPubmed.setSelected(false);
			jCheckBoxMedline.setSelected(false);
		}	
	}

	private JCheckBox getCheckBoxMedline() {
		if(jCheckBoxMedline == null)
		{
			jCheckBoxMedline = new JCheckBox();
			jCheckBoxMedline.setText("MedLine");
			jCheckBoxMedline.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sellectMedLine();
				}
			});	
		}
		return jCheckBoxMedline;
	}

	private JCheckBox getCheckPubmed() {
		if(jCheckPubmed == null)
		{
			jCheckPubmed = new JCheckBox();
			jCheckPubmed.setText("Pubmed Central");
			jCheckPubmed.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sellectPubMEd();
				}
			});
		}
		return jCheckPubmed;
	}
	
	protected void sellectPubMEd() {
		if(jCheckPubmed.isSelected())
		{
			jCheckBoxAllDocuments.setSelected(false);
		}
		else
		{
			if(jCheckBoxMedline.isSelected())
			{
				
			}
			else
			{
				jCheckBoxAllDocuments.setSelected(true);
			}
		}
		
	}
	
	protected void sellectMedLine() {
		if(jCheckBoxMedline.isSelected())
		{
			jCheckBoxAllDocuments.setSelected(false);
		}
		else
		{
			if(jCheckPubmed.isSelected())
			{
				
			}
			else
			{
				jCheckBoxAllDocuments.setSelected(true);
			}
		}
		
	}
	
	private JPanel getJPanelArticleDetails() {
		if(jPanelArticleDetails == null) {
			jPanelArticleDetails = new JPanel();
			jPanelArticleDetails.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Article Details", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelArticleDetailsLayout = new GridBagLayout();
			jPanelArticleDetailsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelArticleDetailsLayout.rowHeights = new int[] {7, 7, 20, 7};
			jPanelArticleDetailsLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelArticleDetailsLayout.columnWidths = new int[] {7, 7};
			jPanelArticleDetails.setLayout(jPanelArticleDetailsLayout);
			{
				jPanelArticleDetails.add(getFreeFullText(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJCheckBoxAll(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJCheckBoxAvailableAbstract(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJCheckBoxFullText(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			groupButton = new ButtonGroup();
			groupButton.add(jCheckBoxAll);
			groupButton.add(jCheckBoxAvailableAbstract);
			groupButton.add(jCheckBoxFullText);
			groupButton.add(jCheckBoxFreeFullText);
		}
		return jPanelArticleDetails;
	}
	
	private JCheckBox getFreeFullText() {
		if(jCheckBoxFreeFullText == null)
		{
			jCheckBoxFreeFullText = new JCheckBox();
			jCheckBoxFreeFullText.setText("Open Access Articles");	
		}
		return jCheckBoxFreeFullText;
	}

	private JCheckBox getJCheckBoxAll() {
		if(jCheckBoxAll == null) {
			jCheckBoxAll = new JCheckBox();
			jCheckBoxAll.setText("All Documents");
			jCheckBoxAll.setSelected(true);
		}
		return jCheckBoxAll;
	}
	

	private JCheckBox getJCheckBoxAvailableAbstract() {
		if(jCheckBoxAvailableAbstract == null) {
			jCheckBoxAvailableAbstract = new JCheckBox();
			jCheckBoxAvailableAbstract.setText("Available Abstract");
		}
		return jCheckBoxAvailableAbstract;
	}

	
	private JCheckBox getJCheckBoxFullText() {
		if(jCheckBoxFullText == null) {
			jCheckBoxFullText = new JCheckBox();
			jCheckBoxFullText.setText("Available Full Text");
		}
		return jCheckBoxFullText;
	}
	
	
	public String getAuthors()
	{
		return authorTextField.getText();
	}
	
	public String getJournal()
	{
		return journalTextField.getText();
	}
	
	public String getFromDate()
	{
		return (String) ((DefaultComboBoxModel) fromComboBox.getModel()).getSelectedItem();
	}
	
	public String getToDate()
	{
		return (String) ((DefaultComboBoxModel) toComboBox.getModel()).getSelectedItem();
	}
	
	public String getArticleType()
	{
		return this.jComboBoxType.getSelectedItem().toString();
	}
	
	public boolean selectedJCheckBoxAll()
	{
		return jCheckBoxAll.isSelected();
	}
	
	public boolean selectjCheckBoxAvailableAbstract()
	{
		return jCheckBoxAvailableAbstract.isSelected();
	}
	
	public boolean selectJCheckBoxFreeFullText()
	{
		return jCheckBoxFreeFullText.isSelected();
	}
	
	public boolean selectJCheckBoxAllDocuments()
	{
		return jCheckBoxAllDocuments.isSelected();
	}
	
	public boolean selectJCheckPubmed()
	{
		return this.jCheckPubmed.isSelected();
	}
	
	public boolean selectJCheckBoxMedline()
	{
		return this.jCheckBoxMedline.isSelected();
	}
	
	public String getKeyWords()
	{
		return jPanelSimpleOrganismAndKeywords.getKeyWords();
	}
	
	public String getOrganism()
	{
		return jPanelSimpleOrganismAndKeywords.getOrganism();
	}
	
	private JPanel getJPanel1() {
		if(jPanelSimpleOrganismAndKeywords == null) {
			jPanelSimpleOrganismAndKeywords = new PubmedSearchSimplePane();
		}
		return jPanelSimpleOrganismAndKeywords;
	}

	public void updateFields(IIRSearchConfiguration pubmedSearchConfigutration) {
		jPanelSimpleOrganismAndKeywords.updateFields(pubmedSearchConfigutration);
		if(pubmedSearchConfigutration.getProperties().containsKey("authors"))
		{
			authorTextField.setText(pubmedSearchConfigutration.getProperties().getProperty("authors"));
		}
		if(pubmedSearchConfigutration.getProperties().containsKey("journal"))
		{
			journalTextField.setText(pubmedSearchConfigutration.getProperties().getProperty("journal"));
		}
		if(pubmedSearchConfigutration.getProperties().containsKey("fromDate"))
		{
			fromComboBox.setSelectedItem(pubmedSearchConfigutration.getProperties().getProperty("fromDate"));
		}
		if(pubmedSearchConfigutration.getProperties().containsKey("toDate"))
		{
			toComboBox.setSelectedItem(pubmedSearchConfigutration.getProperties().getProperty("toDate"));
		}
		if(pubmedSearchConfigutration.getProperties().containsKey("articletype"))
		{
			jComboBoxType.setSelectedItem(pubmedSearchConfigutration.getProperties().getProperty("articletype"));
		}
		if(!pubmedSearchConfigutration.getProperties().containsKey("articleDetails"))
		{
			jCheckBoxAll.setSelected(true);
		}
		else
		{
			jCheckBoxAll.setSelected(false);
			String tye  =pubmedSearchConfigutration.getProperties().getProperty("articleDetails").toString();
			if(tye.equals("abstract"))
			{
				jCheckBoxAvailableAbstract.setSelected(true);
			}
			else if(tye.equals("freefulltext"))
			{
				jCheckBoxFreeFullText.setSelected(true);
			}
			else
			{
				jCheckBoxFullText.setSelected(true);
			}
		}
		if(!pubmedSearchConfigutration.getProperties().containsKey("ArticleSource"))
		{
			jCheckBoxAllDocuments.setSelected(true);
		}
		else
		{
			jCheckBoxAllDocuments.setSelected(false);
			String t = pubmedSearchConfigutration.getProperties().getProperty("ArticleSource");
			if(t.equals("medpmc"))
			{
				jCheckBoxMedline.setSelected(true);
				jCheckPubmed.setSelected(true);
			}
			else if(t.equals("med"))
			{
				jCheckBoxMedline.setSelected(true);
			}
			else
			{
				jCheckPubmed.setSelected(true);
			}
		}
	}
}
