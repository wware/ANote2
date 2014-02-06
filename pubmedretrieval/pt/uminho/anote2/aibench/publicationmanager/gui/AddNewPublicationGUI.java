package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.documents.Publication;
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
* GUI for View a Publication details
*/
public class AddNewPublicationGUI extends DialogGenericView{

	private static final long serialVersionUID = -664381864632621746L;
	
	private JTextField pmidTextField;
	private JTextPane titleTextPane;
	private JLabel dateLable;
	private JLabel journalLabel;
	private JTextField journalTextField;
	private JLabel statusLabel;
	private JTextField statusTextField;
	private JTextField dateTextField;
	private JLabel authorsLabel;
	private JTextPane authorsTextPane;
	private JLabel titleLabel;
	private JLabel volumeLabel;
	private JLabel abstractLabel;
	private JComboBox jComboPublicationType;
	private JButton jButtonAddFile;
	private JTextPane jTextPaneFile;
	private JTextField jTextFieldPages;
	private JPanel jPanelOperations;
	private JPanel jPanelSecondInformation;
	private JScrollPane jScrollPaneAuthors;
	private JScrollPane jScrollPaneAbstract;
	private JScrollPane jScrollPaneTitle;
	private JPanel jPanelWRelevance;
	private JPanel jPanelPublicationInformation;
	private JTextPane abstractTextPane;
	private JButton jButtonHelp;
	private ButtonGroup buttonGroup;
	private JButton relevanceButton3;
	private JButton relevanceButton2;
	private JButton relevanceButton1;
	private JPanel relevancePanel;
	private JLabel pagesLabel;
	private JLabel issueLabel;
	private JTextField issueField;
	private JTextField volumeField1;

	protected boolean cancel;

	private JFileChooser filePath;
	private String relevance=null;
	private File file = null;
	private QueryInformationRetrievalExtension query;

	public AddNewPublicationGUI(){
		super("Pubmed Search");	
		this.setModal(true);
		initFileChooser();
		{
			this.setTitle("Add a New Publication For Query");
			this.setSize(900, 500);
		}
		initGUI();
		refreshRelevanceButtons();	
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	}
	
	private void initGUI() {
		try {
			{
				List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class);
				if(cl.size()==0)
				{
					Workbench.getInstance().warn("Query not selected");
					return;
				}
				if(HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class)!=null)
				{
					Object data = HelpAibench.getSelectedItem(QueryInformationRetrievalExtension.class);
					query = (QueryInformationRetrievalExtension)data;
				}
				else
				{
					Workbench.getInstance().warn("Query not selected");
					return;
				}		
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.0, 0.0};
				thisLayout.rowHeights = new int[] {9, 30, 30, 30, 30, 20, 20, 30, 20};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {22, 57, 65, 20, 73, 469, 18, 86, 84, 20, 7};
				getContentPane().setLayout(thisLayout);
				getContentPane().add(getJPanelPublicationInformation(), new GridBagConstraints(1, 0, 8, 7, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getJPanelOperations(), new GridBagConstraints(0, 7, 9, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(super.getButtonsPanel(), new GridBagConstraints(0, 8, 9, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

		} catch(Exception e) {
			e.printStackTrace();
		}	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Query_Add_Publication");
				}
			});
		}
		return jButtonHelp;
	}
	
	private void refreshRelevanceButtons(){
		
		if(relevance == null)
		{
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
		}
		else if(relevance.compareTo("irrelevant")==0)
		{
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
		}
		else if(relevance.compareTo("related")==0)
		{
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
		}
		else
		{
			this.relevanceButton1.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton1.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton2.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
			this.relevanceButton3.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star1.png")));
			this.relevanceButton3.setRolloverIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Star2.png")));
		}	
	}
	
	
	private JPanel getJPanelPublicationInformation() {
		if(jPanelPublicationInformation == null) {
			jPanelPublicationInformation = new JPanel();
			GridBagLayout jPanelPublicationInformationLayout = new GridBagLayout();
			jPanelPublicationInformationLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.0};
			jPanelPublicationInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			jPanelPublicationInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.1};
			jPanelPublicationInformationLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
			jPanelPublicationInformation.setLayout(jPanelPublicationInformationLayout);
			jPanelPublicationInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Information", TitledBorder.LEADING, TitledBorder.TOP));

			{
				titleLabel = new JLabel();
				jPanelPublicationInformation.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPublicationInformation.add(getJPanelWRelevance(), new GridBagConstraints(8, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPublicationInformation.add(getJScrollPaneTitle(), new GridBagConstraints(1, 0, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPublicationInformation.add(getJPanelSecondInformation(), new GridBagConstraints(8, 1, 2, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					abstractLabel = new JLabel();
					jPanelPublicationInformation.add(abstractLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPublicationInformation.add(getJScrollPaneAbstract(), new GridBagConstraints(1, 1, 7, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						authorsLabel = new JLabel();
						jPanelPublicationInformation.add(authorsLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJScrollPaneAuthors(), new GridBagConstraints(1, 4, 7, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						authorsLabel.setText("Authors:");
					}
					abstractLabel.setText("Abstract :");
				}
				titleLabel.setText("Title :");
			}
		}
		return jPanelPublicationInformation;
	}
	
	private JPanel getJPanelWRelevance() {
		if(jPanelWRelevance == null) {
			jPanelWRelevance = new JPanel();
			{
				relevancePanel = new JPanel();
				jPanelWRelevance.add(relevancePanel);
				GridBagLayout relevancePanelLayout = new GridBagLayout();
				relevancePanelLayout.rowWeights = new double[] {0.1};
				relevancePanelLayout.rowHeights = new int[] {7};
				relevancePanelLayout.columnWeights = new double[] {0.0, 0.0, 0.0};
				relevancePanelLayout.columnWidths = new int[] {16, 16, 16};
				relevancePanel.setLayout(relevancePanelLayout);

				relevancePanel.setSize(50, 30);
				relevancePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Weight Relevance", TitledBorder.LEADING, TitledBorder.TOP));
				{
					buttonGroup = new ButtonGroup();
				}
				{
					relevanceButton1 = new JButton();
					relevancePanel.add(relevanceButton1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					buttonGroup.add(relevanceButton1);
					relevanceButton1.setRolloverEnabled(true);
					relevanceButton1.setBorderPainted(false);
					relevanceButton1.setSize(18, 22);
					relevanceButton1.setContentAreaFilled(false);
					relevanceButton1.setToolTipText("Irrelevant");
					relevanceButton1.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							relevanceButtonPerformedIrrelevant();
						}
					});	
				}
				{
					relevanceButton2 = new JButton();
					relevancePanel.add(relevanceButton2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					buttonGroup.add(relevanceButton2);
					relevanceButton2.setRolloverEnabled(true);
					relevanceButton2.setBorderPainted(false);
					relevanceButton2.setSize(18, 22);
					relevanceButton2.setContentAreaFilled(false);
					relevanceButton2.setToolTipText("Related");
					relevanceButton2.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							relevanceButtonPerformedRelated();
						}
					});
				}
				{
					relevanceButton3 = new JButton();
					relevancePanel.add(relevanceButton3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					buttonGroup.add(relevanceButton3);
					relevanceButton3.setRolloverEnabled(true);
					relevanceButton3.setBorderPainted(false);
					relevanceButton3.setSize(18, 22);
					relevanceButton3.setContentAreaFilled(false);
					relevanceButton3.setToolTipText("Relevant");
					relevanceButton3.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							relevanceButtonPerformed();
						}
					});
				}
			}
		}
		return jPanelWRelevance;
	}
	
	protected void relevanceButtonPerformedIrrelevant(){
		relevance = "irrelevant";
		refreshRelevanceButtons();
	}
	protected void relevanceButtonPerformedRelated(){
		relevance = "related";
		refreshRelevanceButtons();
	}
	
	protected void relevanceButtonPerformed(){
		relevance = "relevant";
		refreshRelevanceButtons();
	}

	private JScrollPane getJScrollPaneTitle() {
		if(jScrollPaneTitle == null) {
			jScrollPaneTitle = new JScrollPane();
			{
				titleTextPane = new JTextPane();
				jScrollPaneTitle.setViewportView(titleTextPane);
				titleTextPane.setBackground(new java.awt.Color(250,250,250));
				titleTextPane.setPreferredSize(new java.awt.Dimension(469, 55));
				titleTextPane
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
		}
		return jScrollPaneTitle;
	}
	
	private JScrollPane getJScrollPaneAbstract() {
		if(jScrollPaneAbstract == null) {
			jScrollPaneAbstract = new JScrollPane();
			{
				abstractTextPane = new JTextPane();
				jScrollPaneAbstract.setViewportView(abstractTextPane);
				abstractTextPane.setBackground(new java.awt.Color(250,250,250));
				abstractTextPane.setBorder(BorderFactory
						.createCompoundBorder(BorderFactory
								.createEtchedBorder(BevelBorder.LOWERED), null));
			}
		}
		return jScrollPaneAbstract;
	}
	
	private JScrollPane getJScrollPaneAuthors() {
		if(jScrollPaneAuthors == null) {
			jScrollPaneAuthors = new JScrollPane();
			{
				authorsTextPane = new JTextPane();
				jScrollPaneAuthors.setViewportView(authorsTextPane);
				authorsTextPane.setBackground(new java.awt.Color(250,250,250));
				authorsTextPane
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
		}
		return jScrollPaneAuthors;
	}
	
	private JPanel getJPanelSecondInformation() {
		if(jPanelSecondInformation == null) {
			jPanelSecondInformation = new JPanel();
			jPanelSecondInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Details", TitledBorder.LEADING, TitledBorder.TOP));

			GridBagLayout jPanelSecondInformationLayout = new GridBagLayout();
			jPanelSecondInformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelSecondInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7};
			jPanelSecondInformationLayout.columnWeights = new double[] {0.0, 0.1};
			jPanelSecondInformationLayout.columnWidths = new int[] {7, 7};
			jPanelSecondInformation.setLayout(jPanelSecondInformationLayout);
			{
				pmidTextField = new JTextField();
				jPanelSecondInformation.add(pmidTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				
				pmidTextField.setBackground(new java.awt.Color(250,250,250));
				pmidTextField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
			{
				dateLable = new JLabel();
				jPanelSecondInformation.add(dateLable, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				dateLable.setText("Date:");
			}
			{
				dateTextField = new JTextField();
				jPanelSecondInformation.add(dateTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				dateTextField.setBackground(new java.awt.Color(250,250,250));
				dateTextField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
			{
				journalLabel = new JLabel();
				jPanelSecondInformation.add(journalLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				journalLabel.setText("Journal:");
			}
			{
				journalTextField = new JTextField();
				jPanelSecondInformation.add(journalTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				journalTextField.setBackground(new java.awt.Color(250,250,250));
				journalTextField.setBorder(BorderFactory
						.createCompoundBorder(BorderFactory
								.createEtchedBorder(BevelBorder.LOWERED), null));
			}
			{
				statusLabel = new JLabel();
				jPanelSecondInformation.add(statusLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				statusLabel.setText("Status:");
			}
			{
				statusTextField = new JTextField();
				jPanelSecondInformation.add(statusTextField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				statusTextField.setBackground(new java.awt.Color(250,250,250));
				statusTextField.setBorder(BorderFactory
						.createCompoundBorder(BorderFactory
								.createEtchedBorder(BevelBorder.LOWERED), null));
			}
			{
				pagesLabel = new JLabel();
				jPanelSecondInformation.add(pagesLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				pagesLabel.setText("Pages:");
			}
			{
				volumeLabel = new JLabel();
				jPanelSecondInformation.add(volumeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				volumeLabel.setText("Volume:");
			}
			{
				volumeField1 = new JTextField();
				jPanelSecondInformation.add(volumeField1, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				
				volumeField1.setBackground(new java.awt.Color(250,250,250));
				volumeField1.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
			{
				issueLabel = new JLabel();
				jPanelSecondInformation.add(issueLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				issueLabel.setText("Issue:");
			}
			{
				issueField = new JTextField();
				jPanelSecondInformation.add(issueField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJTextFieldPages(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJComboPublicationType(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

				issueField.setBackground(new java.awt.Color(250,250,250));
				issueField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
		}
		return jPanelSecondInformation;
	}
	
	private JPanel getJPanelOperations() {
		if(jPanelOperations == null) {
		jPanelOperations = new JPanel();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication File", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperationsLayout.rowWeights = new double[] {0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 20, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			jPanelOperations.add(getJButtonHelp(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOperations.add(getJTextPaneFile(), new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOperations.add(getJButtonAddFile(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelOperations;
	}

	private JTextField getJTextFieldPages() {
		if(jTextFieldPages == null) {
			jTextFieldPages = new JTextField();
			jTextFieldPages = new JTextField();
			jTextFieldPages.setBackground(new java.awt.Color(250,250,250));
			jTextFieldPages.setBorder(BorderFactory.createCompoundBorder(
					null, 
					null));
		}
		return jTextFieldPages;
	}

	private void initFileChooser()
	{
        filePath = new JFileChooser();
        filePath.setApproveButtonText("Select");
        filePath.setFileFilter(new FileFilter(){

            @Override
            public boolean accept(File file) {
                return (file.getName().toLowerCase().endsWith(".pdf") || file.isDirectory());
            }

            @Override
            public String getDescription() {
                return ".pdf";
            }
           
        });
	}

	protected void openAddViewDialog() { 
        int rel = filePath.showOpenDialog(new JFrame()); 
        if(rel == JFileChooser.CANCEL_OPTION)
        {
        	file = null;
        	jTextPaneFile.setText("");
        }
        else
        {
        	file = filePath.getSelectedFile();
        	jTextPaneFile.setText(file.getAbsolutePath());
        }
	}
	
	private JTextPane getJTextPaneFile() {
		if(jTextPaneFile == null) {
			jTextPaneFile = new JTextPane();
			jTextPaneFile.setEditable(false);
		}
		return jTextPaneFile;
	}
	
	private JButton getJButtonAddFile() {
		if(jButtonAddFile == null) {
			jButtonAddFile = new JButton();
			jButtonAddFile.setText("Add File");
			jButtonAddFile.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					openAddViewDialog();
				}
			});	
		}
		return jButtonAddFile;
	}

	protected void okButtonAction() {
		if(file==null&&abstractTextPane.getText()==null)
		{
			Workbench.getInstance().warn("Please put abstract or insert a pdf file for new publication");
		}
		else
		{
			int publicationIDType = HelpDatabase.initArticleTypeID(query.getPubManager().getDb(),(String) jComboPublicationType.getSelectedItem());
			
			IPublication pub = new Publication(publicationIDType, 
												pmidTextField.getText(), 
												titleTextPane.getText(), 
												authorsTextPane.getText(), 
												dateTextField.getText(), 
												statusTextField.getText(), 
												journalTextField.getText(),
												volumeField1.getText(), 
												issueField.getText(), 
												jTextFieldPages.getText(), 
												null, 
												abstractTextPane.getText(),
												false);
			
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
					new ParamSpec("query",QueryInformationRetrievalExtension.class,query,null),
					new ParamSpec("pub",IPublication.class,pub,null),
					new ParamSpec("file",File.class,file,null),
					new ParamSpec("relevance",String.class,relevance,null)
				});
			}
		
	}
	
	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		this.paramsRec = arg0;
		this.setSize(900,600);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}
	
	private JComboBox getJComboPublicationType() {
		if(jComboPublicationType == null) {
			ComboBoxModel jComboPublicationTypeModel = 
				new DefaultComboBoxModel(
						new String[] { "other", "pmid","isbn","doi"});
			jComboPublicationType = new JComboBox();
			jComboPublicationType.setModel(jComboPublicationTypeModel);
		}
		return jComboPublicationType;
	}

}
