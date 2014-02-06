package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.RelevancePanel;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.documents.Publication;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
 * 
* GUI for View a Publication details
*/
public class AddNewPublicationGUI extends DialogGenericViewInputGUI{

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
	private JTextField jTextFieldType;
	private JLabel jLabelNewType;
	private JLabel jLabelID;
	private RelevancePanel relevancePane;
	private JTextField jTextFieldUrl;
	private JLabel jLabelURL;
	private JComboBox jComboPublicationType;
	private JButton jButtonAddFile;
	private JTextPane jTextPaneFile;
	private JTextField jTextFieldPages;
	private JPanel jPanelOperations;
	private JPanel jPanelSecondInformation;
	private JScrollPane jScrollPaneAuthors;
	private JScrollPane jScrollPaneAbstract;
	private JScrollPane jScrollPaneTitle;
	private JPanel jPanelPublicationInformation;
	private JTextPane abstractTextPane;
	private JLabel pagesLabel;
	private JLabel issueLabel;
	private JTextField issueField;
	private JTextField volumeField1;

	protected boolean cancel;

	private FileChooserExtensionPdf filePath;
	private File file = null;
	private QueryInformationRetrievalExtension query;

	public AddNewPublicationGUI(){
		super("Add a New Publication to the Query");
		initFileChooser();
		initGUI();
		changeDocumentType();
		this.setModal(true);
	}
	
	private void initGUI() {
		try {
			{
				List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(QueryInformationRetrievalExtension.class);
				if(cl.size()==0)
				{
					Workbench.getInstance().warn("Query not selected");
					finish();
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
					finish();
					return;
				}		
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.0, 0.0};
				thisLayout.rowHeights = new int[] {9, 30, 30, 30, 30, 20, 20, 30, 20};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1};
				thisLayout.columnWidths = new int[] {65, 20, 73, 469, 18, 86, 84, 7};
				getContentPane().setLayout(thisLayout);
				getContentPane().add(getJPanelPublicationInformation(), new GridBagConstraints(-1, 0, 8, 7, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getJPanelOperations(), new GridBagConstraints(-1, 7, 8, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 8, 9, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	private JPanel getJPanelPublicationInformation() throws SQLException, DatabaseLoadDriverException {
		if(jPanelPublicationInformation == null) {
			jPanelPublicationInformation = new JPanel();
			GridBagLayout jPanelPublicationInformationLayout = new GridBagLayout();
			jPanelPublicationInformationLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0};
			jPanelPublicationInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 20, 7};
			jPanelPublicationInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.1};
			jPanelPublicationInformationLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
			jPanelPublicationInformation.setLayout(jPanelPublicationInformationLayout);
			jPanelPublicationInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Information", TitledBorder.LEADING, TitledBorder.TOP));

			{
				titleLabel = new JLabel();
				jPanelPublicationInformation.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPublicationInformation.add(getJScrollPaneTitle(), new GridBagConstraints(1, 0, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPublicationInformation.add(getJPanelSecondInformation(), new GridBagConstraints(8, 1, 2, 6, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					abstractLabel = new JLabel();
					jPanelPublicationInformation.add(abstractLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPublicationInformation.add(getJScrollPaneAbstract(), new GridBagConstraints(1, 1, 7, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						authorsLabel = new JLabel();
						jPanelPublicationInformation.add(authorsLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJScrollPaneAuthors(), new GridBagConstraints(1, 4, 7, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJLabelURL(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJTextFieldUrl(), new GridBagConstraints(1, 6, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJPanel1(), new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						authorsLabel.setText("Authors:");
					}
					abstractLabel.setText("Abstract :");
				}
				titleLabel.setText("Title :");
			}
		}
		return jPanelPublicationInformation;
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
	
	private JPanel getJPanelSecondInformation() throws SQLException, DatabaseLoadDriverException {
		if(jPanelSecondInformation == null) {
			jPanelSecondInformation = new JPanel();
			jPanelSecondInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Details", TitledBorder.LEADING, TitledBorder.TOP));

			GridBagLayout jPanelSecondInformationLayout = new GridBagLayout();
			jPanelSecondInformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelSecondInformationLayout.rowHeights = new int[] {7, 20, 20, 7, 7, 7, 7, 7, 7};
			jPanelSecondInformationLayout.columnWeights = new double[] {0.0, 0.1};
			jPanelSecondInformationLayout.columnWidths = new int[] {7, 7};
			jPanelSecondInformation.setLayout(jPanelSecondInformationLayout);
			{
				pmidTextField = new JTextField();
				jPanelSecondInformation.add(pmidTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				
				pmidTextField.setBackground(new java.awt.Color(250,250,250));
				pmidTextField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
			{
				dateLable = new JLabel();
				jPanelSecondInformation.add(dateLable, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				dateLable.setText("Date:");
			}
			{
				dateTextField = new JTextField();
				jPanelSecondInformation.add(dateTextField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				dateTextField.setBackground(new java.awt.Color(250,250,250));
				dateTextField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
			{
				journalLabel = new JLabel();
				jPanelSecondInformation.add(journalLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				journalLabel.setText("Journal:");
			}
			{
				journalTextField = new JTextField();
				jPanelSecondInformation.add(journalTextField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				journalTextField.setBackground(new java.awt.Color(250,250,250));
				journalTextField.setBorder(BorderFactory
						.createCompoundBorder(BorderFactory
								.createEtchedBorder(BevelBorder.LOWERED), null));
			}
			{
				statusLabel = new JLabel();
				jPanelSecondInformation.add(statusLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				statusLabel.setText("Status:");
			}
			{
				statusTextField = new JTextField();
				jPanelSecondInformation.add(statusTextField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				statusTextField.setBackground(new java.awt.Color(250,250,250));
				statusTextField.setBorder(BorderFactory
						.createCompoundBorder(BorderFactory
								.createEtchedBorder(BevelBorder.LOWERED), null));
			}
			{
				pagesLabel = new JLabel();
				jPanelSecondInformation.add(pagesLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				pagesLabel.setText("Pages:");
			}
			{
				volumeLabel = new JLabel();
				jPanelSecondInformation.add(volumeLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				volumeLabel.setText("Volume:");
			}
			{
				volumeField1 = new JTextField();
				jPanelSecondInformation.add(volumeField1, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				
				volumeField1.setBackground(new java.awt.Color(250,250,250));
				volumeField1.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
			{
				issueLabel = new JLabel();
				jPanelSecondInformation.add(issueLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				issueLabel.setText("Issue:");
			}
			{
				issueField = new JTextField();
				jPanelSecondInformation.add(issueField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJTextFieldPages(), new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJComboPublicationType(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJLabelID(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJLabelNewType(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelSecondInformation.add(getJTextFieldType(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

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
        filePath = new FileChooserExtensionPdf();
	}

	protected void openAddViewDialog() { 
        int rel = filePath.showOpenDialog(this); 
        if(rel == JFileChooser.CANCEL_OPTION)
        {
        	file = null;
        	jTextPaneFile.setText("");
        }
        else if(rel == JFileChooser.APPROVE_OPTION)
        {
        	file = filePath.getSelectedFile();
        	jTextPaneFile.setText(file.getAbsolutePath());
        	SessionSettings.getSessionSettings().setSearchDirectory(file.getParent());
        }
        else
        {
        	file = null;
        	jTextPaneFile.setText("");
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
		if(titleTextPane.getText() == null || titleTextPane.getText().length() < 1)
		{
			Workbench.getInstance().warn("Please fill Title");
		}
		else if(file==null && (abstractTextPane.getText()==null || abstractTextPane.getText().length() < 1))
		{
			Workbench.getInstance().warn("Please fill Abstract or insert a pdf file");
		}
		else if(jComboPublicationType.getSelectedItem().toString().equals("other") && jTextFieldType.getText().length() < 1)
		{
			Workbench.getInstance().warn("Please fill Document Type ");
		}
		else
		{
			String publicationType  = jComboPublicationType.getSelectedItem().toString();
			if(publicationType.equals("other"))
				publicationType = jTextFieldType.getText();
			IPublication publication = new Publication(-1, 
												pmidTextField.getText(), 
												titleTextPane.getText(), 
												authorsTextPane.getText(), 
												dateTextField.getText(), 
												statusTextField.getText(), 
												journalTextField.getText(),
												volumeField1.getText(), 
												issueField.getText(), 
												jTextFieldPages.getText(), 
												jTextFieldUrl.getText(), 
												abstractTextPane.getText(),
												false);
			List list = new ArrayList();
			list.add(publication);
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
					new ParamSpec("query",QueryInformationRetrievalExtension.class,query,null),
					new ParamSpec("pub",List.class,list,null),
					new ParamSpec("type",String.class,publicationType,null),
					new ParamSpec("file",File.class,file,null),
					new ParamSpec("relevance",String.class,relevancePane.getRelevance().toString(),null)
				});
			}
		
	}
	
	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		this.paramsRec = arg0;
		this.setSize(GlobalOptions.superWidth,GlobalOptions.superHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}
	
	private JComboBox getJComboPublicationType() throws SQLException, DatabaseLoadDriverException {
		if(jComboPublicationType == null) {
			DefaultComboBoxModel jComboPublicationTypeModel = getDocumentTypesModel();
			jComboPublicationTypeModel.addElement("other");
			jComboPublicationType = new JComboBox();
			jComboPublicationType.setModel(jComboPublicationTypeModel);
			jComboPublicationType.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					changeDocumentType();
				}
			});
		}
		return jComboPublicationType;
	}
	
	private DefaultComboBoxModel getDocumentTypesModel() throws SQLException, DatabaseLoadDriverException {
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		PreparedStatement ps = Configuration.getDatabase().getConnection().prepareStatement(QueriesPublication.selectAllDocumentTypes);
		ResultSet rs = ps.executeQuery();
		while(rs.next())
		{
			model.addElement(rs.getString(1));
		}
		rs.close();
		ps.close();

		return model;
	}

	protected void changeDocumentType() {
		if(jComboPublicationType.getSelectedItem().equals("other"))
		{
			jLabelNewType.setEnabled(true);
			jTextFieldType.setEnabled(true);
		}
		else
		{
			jLabelNewType.setEnabled(false);
			jTextFieldType.setEnabled(false);
		}
	}

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Query_Add_Publication";
	}
	
	private JLabel getJLabelURL() {
		if(jLabelURL == null) {
			jLabelURL = new JLabel();
			jLabelURL.setText("URL :");
		}
		return jLabelURL;
	}
	
	private JTextField getJTextFieldUrl() {
		if(jTextFieldUrl == null) {
			jTextFieldUrl = new JTextField();
		}
		return jTextFieldUrl;
	}
	
	private JPanel getJPanel1() {
		if(relevancePane == null) {
			relevancePane = new RelevancePanel(query);
			relevancePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Weight Relevance", TitledBorder.LEADING, TitledBorder.TOP));
		}
		return relevancePane;
	}
	
	private JLabel getJLabelID() {
		if(jLabelID == null) {
			jLabelID = new JLabel();
			jLabelID.setText("Type (ID) :");
		}
		return jLabelID;
	}
	
	private JLabel getJLabelNewType() {
		if(jLabelNewType == null) {
			jLabelNewType = new JLabel();
			jLabelNewType.setText("Type :");
		}
		return jLabelNewType;
	}
	
	private JTextField getJTextFieldType() {
		if(jTextFieldType == null) {
			jTextFieldType = new JTextField();
		}
		return jTextFieldType;
	}

}
