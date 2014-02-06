package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.Help;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * 
 * AibenchGUI for PubmedSearchOperation
 * 
 */
public class PubmedSearchGUI extends DialogGenericView {



	private static final long serialVersionUID = -1255672985699352343L;
	private JPanel fieldsPanel;
	private JLabel authorLabel;
	private JLabel toLabel;
	private JCheckBox jCheckPubmed;
	private JCheckBox jCheckBoxFullText;
	private JCheckBox jCheckBoxAvailableAbstract;
	private JCheckBox jCheckBoxAll;
	private JPanel jPanelArticleDetails;
	private JCheckBox jCheckBoxAllDocuments;
	private JPanel jPanelQueryProperties;
	private JPanel jPanelQueryMainInformation;
	private JButton jButtonHelp;
	private JCheckBox jCheckBoxFreeFullText;
	private JCheckBox jCheckBoxMedline;
	private JPanel jPanelRepositoryInformation;
	private JComboBox jComboBoxType;
	private JLabel jLabelType;
	private JTextField organismTextField;
	private JLabel organismLabel;
	private JTextField keywordsTextField;
	private JLabel keywordsLabel;
	private JComboBox toComboBox;
	private JComboBox fromComboBox;
	private JLabel dateLabel;
	private JTextField journalTextField;
	private JLabel journalLabel;
	private JTextField authorTextField;
	
	private boolean finish = false;
	
	public PubmedSearchGUI() throws MissingDatatypesException {
		super("Pubmed Search");	
		this.setModal(true);
		initGUI();	
	}

	protected void okButtonAction() {
		String from = (String) ((DefaultComboBoxModel) fromComboBox.getModel()).getSelectedItem();
		String to = (String) ((DefaultComboBoxModel) toComboBox.getModel()).getSelectedItem();
		
		if(!to.equals("present") && Integer.decode(to)<Integer.decode(from))
		{
			Workbench.getInstance().warn("Please select a valid date interval.");
			return;
		}	
		if(this.keywordsTextField.getText().compareTo("")!=0 || this.organismTextField.getText().compareTo("")!=0)
		{
			Properties properties = new Properties();
			if(!this.authorTextField.getText().equals(""))
			{
				properties.put("authors",authorTextField.getText());
			}
			if(!this.journalTextField.getText().equals(""))
			{
				properties.put("journal",this.journalTextField.getText());
			}
			properties.put("fromDate",(String) this.fromComboBox.getSelectedItem());
			if(!((String) this.toComboBox.getSelectedItem()).equals("present"))
			{
				properties.put("toDate",(String) this.toComboBox.getSelectedItem());
			}
			if(!this.jComboBoxType.getSelectedItem().equals("All"))
			{
				properties.put("articletype", this.jComboBoxType.getSelectedItem());
			}
			if(this.jCheckBoxAll.isSelected())
			{
				
			}
			else
			{
				if(jCheckBoxAvailableAbstract.isSelected())
				{
					properties.put("articleDetails","abstract");
					
				}
				else if(jCheckBoxFreeFullText.isSelected())
				{
					properties.put("articleDetails","freefulltext");
				
				}
				else
				{
					properties.put("articleDetails","fulltext");
						
				}
			}
			if(this.jCheckBoxAllDocuments.isSelected())
			{
				
			}
			else
			{
				if(this.jCheckPubmed.isSelected())
				{
					if(this.jCheckBoxMedline.isSelected())
					{
						properties.put("ArticleSource","medpmc");
					}
					else
					{
						properties.put("ArticleSource","med");
					}
				}
				else
				{
					properties.put("ArticleSource","pmc");
				}
			}
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
			
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
				new ParamSpec("catalogue",PublicationManager.class,(PublicationManager) cl.get(0).getUserData(),null),
				new ParamSpec("keywords",String.class,this.keywordsTextField.getText(),null),
				new ParamSpec("organism",String.class,this.organismTextField.getText(),null),
				new ParamSpec("properties",Properties.class,properties,null),
			});
		}
		else
			Workbench.getInstance().warn("Please insert keywords or a organism so search!");
	}
	
	private void initGUI() throws MissingDatatypesException {
		{
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
			if(cl.size()==0)
			{
				Workbench.getInstance().warn("Publication Manager not inited");
				finish = true;
				return;
			}
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				fieldsPanel = new JPanel();
				GridBagLayout fieldsPanelLayout = new GridBagLayout();
				getContentPane().add(fieldsPanel, new GridBagConstraints(0, 0, 7, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				fieldsPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
				fieldsPanelLayout.rowHeights = new int[] {20, 20, 20, 20, 20, 20};
				fieldsPanelLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1};
				fieldsPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
				fieldsPanel.setLayout(fieldsPanelLayout);
				{
					jPanelRepositoryInformation = new JPanel();
					jPanelRepositoryInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Repository Information", TitledBorder.LEADING, TitledBorder.TOP));
					GridBagLayout jPanel1Layout = new GridBagLayout();
					fieldsPanel.add(jPanelRepositoryInformation, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					fieldsPanel.add(getJPanelQueryMainInformation(), new GridBagConstraints(0, 0, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					fieldsPanel.add(getJPanelQueryProperties(), new GridBagConstraints(0, 2, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					fieldsPanel.add(getJPanelArticleDetails(), new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1};
					jPanel1Layout.rowHeights = new int[] {7, 7, 7};
					jPanel1Layout.columnWeights = new double[] {0.1};
					jPanel1Layout.columnWidths = new int[] {7};
					jPanelRepositoryInformation.setLayout(jPanel1Layout);
					{
						jCheckPubmed = new JCheckBox();
						jPanelRepositoryInformation.add(jCheckPubmed, new GridBagConstraints(-1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jCheckPubmed.setText("Pubmed Central");
						jCheckPubmed.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								sellectPubMEd();
							}
						});
					}
					{
						jCheckBoxMedline = new JCheckBox();
						jPanelRepositoryInformation.add(jCheckBoxMedline, new GridBagConstraints(-1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jCheckBoxMedline.setText("MedLine");
						jCheckBoxMedline.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								sellectMedLine();
							}
						});
					}
					{
						jPanelRepositoryInformation.add(getJCheckBoxAllDocuments(), new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
			}
		}
		{
			getContentPane().add(this.getButtonsPanel(), new GridBagConstraints(0, 4, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
		}
		{
			this.setSize(600, 425);
		}
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

	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Pubmed_Search");
				}
			});
		}
		return jButtonHelp;
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
	
	private JPanel getJPanelQueryMainInformation() {
		if(jPanelQueryMainInformation == null) {
			jPanelQueryMainInformation = new JPanel();
			GridBagLayout jPanelQueryMainInformationLayout = new GridBagLayout();
			jPanelQueryMainInformationLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelQueryMainInformationLayout.rowHeights = new int[] {7, 7};
			jPanelQueryMainInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1};
			jPanelQueryMainInformationLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelQueryMainInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Query Main Information", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelQueryMainInformation.setLayout(jPanelQueryMainInformationLayout);
			{
				keywordsLabel = new JLabel();
				jPanelQueryMainInformation.add(keywordsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				keywordsLabel.setText("Keywords:");
			}
			{
				organismLabel = new JLabel();
				jPanelQueryMainInformation.add(organismLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				organismLabel.setText("Organism:");
			}
			{
				keywordsTextField = new JTextField();
				jPanelQueryMainInformation.add(keywordsTextField, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				keywordsTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
			{
				organismTextField = new JTextField();
				jPanelQueryMainInformation.add(organismTextField, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				organismTextField.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			}
		}
		return jPanelQueryMainInformation;
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
	
	@Override
	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		
		if(finish)
			finish();
		else
		{
			this.paramsRec = arg0;
			this.setTitle("Pubmed Search");
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);		
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
		else
		{
			
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
				jCheckBoxFreeFullText = new JCheckBox();
				jPanelArticleDetails.add(jCheckBoxFreeFullText, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJButtonHelp(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJCheckBoxAll(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJCheckBoxAvailableAbstract(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelArticleDetails.add(getJCheckBoxFullText(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jCheckBoxFreeFullText.setText("Available Free Full Text");
				jCheckBoxFreeFullText.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						sellectFreeFullText();
					}
				});
				
			}
			
		}
		return jPanelArticleDetails;
	}
	
	private JCheckBox getJCheckBoxAll() {
		if(jCheckBoxAll == null) {
			jCheckBoxAll = new JCheckBox();
			jCheckBoxAll.setText("All Documents");
			jCheckBoxAll.setSelected(true);
			jCheckBoxAll.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sellectAllDocs();
				}
			});
		}
		return jCheckBoxAll;
	}
	
	protected void sellectAllDocs() {
		if(jCheckBoxAll.isSelected())
		{
			jCheckBoxAvailableAbstract.setSelected(false);
			jCheckBoxFreeFullText.setSelected(false);
		}
		else
		{
			jCheckBoxAll.setSelected(true);
		}
		
	}

	private JCheckBox getJCheckBoxAvailableAbstract() {
		if(jCheckBoxAvailableAbstract == null) {
			jCheckBoxAvailableAbstract = new JCheckBox();
			jCheckBoxAvailableAbstract.setText("Available Abstract");
			jCheckBoxAvailableAbstract.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sellectAbstract();
				}
			});
		}
		return jCheckBoxAvailableAbstract;
	}

	protected void sellectAbstract() {
		
		if(jCheckBoxAvailableAbstract.isSelected())
		{
			jCheckBoxAll.setSelected(false);
			jCheckBoxFreeFullText.setSelected(false);
			jCheckBoxFullText.setSelected(false);
		}
		else
		{
			jCheckBoxAll.setSelected(true);
		}
	}
	
	protected void sellectFreeFullText() {
		
		if(jCheckBoxFreeFullText.isSelected())
		{
			jCheckBoxAll.setSelected(false);
			jCheckBoxAvailableAbstract.setSelected(false);
			jCheckBoxFullText.setSelected(false);
		}
		else
		{
			jCheckBoxAll.setSelected(true);
		}
	}
	
	private JCheckBox getJCheckBoxFullText() {
		if(jCheckBoxFullText == null) {
			jCheckBoxFullText = new JCheckBox();
			jCheckBoxFullText.setText("Available Full Text");
			jCheckBoxFullText.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					sellectFullText();
				}
			});
		}
		return jCheckBoxFullText;
	}

	protected void sellectFullText() {
		if(jCheckBoxFullText.isSelected())
		{
			jCheckBoxAll.setSelected(false);
			jCheckBoxAvailableAbstract.setSelected(false);
			jCheckBoxFreeFullText.setSelected(false);
		}
		else
		{
			jCheckBoxAll.setSelected(true);
		}
		
	}

}
