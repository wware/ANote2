package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.structures.PDF;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
 * 
* GUI for View a Publication details
*/
public class PublicationResumeGUI extends DialogGenericViewOkButtonOnly{

	private static final long serialVersionUID = -664381864632621746L;
	
	private JButton otherIDTextField;
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
	private JButton jButtonUpdate;
	private JTextField jTextFieldExternalLink;
	private JLabel jLabelExternalLink;
	private JPanel jPanel1;
	private JLabel jLabelOtherID;
	private JTextField jTextFieldPages;
	private JPanel jPanelSecondInformation;
	private JScrollPane jScrollPaneAuthors;
	private JScrollPane jScrollPaneAbstract;
	private JScrollPane jScrollPaneTitle;
	private JPanel jPanelWRelevance;
	private JPanel jPanelPublicationInformation;
	private JTextPane abstractTextPane;
	private JButton pdfButton;
	private JLabel pagesLabel;
	private JLabel issueLabel;
	private JTextField issueField;
	private JTextField volumeField1;
	
	private IPublication publication;

	public PublicationResumeGUI(IPublication publication){
		super();
		this.publication = publication;
		this.setTitle("Publication - " + this.publication.getOtherID());
		this.setSize(900, 500);
		Utilities.centerOnOwner(this);
		initGUI();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.0};
				thisLayout.rowHeights = new int[] {9, 30, 30, 30, 30, 20, 20, 30};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1};
				thisLayout.columnWidths = new int[] {57, 65, 20, 73, 469, 18, 86, 84, 7};
				getContentPane().setLayout(thisLayout);
				getContentPane().add(getJPanelPublicationInformation(), new GridBagConstraints(0, 0, 9, 7, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(-1, 7, 9, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

		} catch(Exception e) {
			e.printStackTrace();
		}	}

	private void viewPdf(){
		String filePath = GlobalOptions.saveDocDirectoty;
		String byotherID = filePath + publication.getOtherID() + ".pdf";
		String byid = filePath + "id" + publication.getID() + ".pdf";
		String id_path_otherID = filePath+"id"+this.publication.getID()+"-"+this.publication.getOtherID()+".pdf";
		File file = new File(byotherID);
		if(file.exists())
		{
			PDF pdf = new PDF(file);
			JFrame frame = new JFrame();
			PDFViewGUI gui = new PDFViewGUI(pdf);
			frame.add(gui);
			frame.setEnabled(true);
			frame.pack();
			frame.setVisible(true);
		}
		file = new File(byid);
		if(file.exists())
		{
			PDF pdf = new PDF(file);
			JFrame frame = new JFrame();
			PDFViewGUI gui = new PDFViewGUI(pdf);
			frame.add(gui);
			frame.setEnabled(true);
			frame.pack();
			frame.setVisible(true);
		}
		file = new File(id_path_otherID);
		if(file.exists())
		{
			PDF pdf = new PDF(file);
			JFrame frame = new JFrame();
			PDFViewGUI gui = new PDFViewGUI(pdf);
			frame.add(gui);
			frame.setEnabled(true);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	private JPanel getJPanelPublicationInformation() throws SQLException, DatabaseLoadDriverException {
		if(jPanelPublicationInformation == null) {
			jPanelPublicationInformation = new JPanel();
			GridBagLayout jPanelPublicationInformationLayout = new GridBagLayout();
			jPanelPublicationInformationLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0};
			jPanelPublicationInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 20, 7};
			jPanelPublicationInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.0, 0.0};
			jPanelPublicationInformationLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
			jPanelPublicationInformation.setLayout(jPanelPublicationInformationLayout);
			jPanelPublicationInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Information", TitledBorder.LEADING, TitledBorder.TOP));

			{
				titleLabel = new JLabel();
				jPanelPublicationInformation.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPublicationInformation.add(getJPanelWRelevance(), new GridBagConstraints(8, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
						jPanelPublicationInformation.add(getJLabelExternalLink(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJTextFieldExternalLink(), new GridBagConstraints(1, 6, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jPanelPublicationInformation.add(getJButtonUpdate(), new GridBagConstraints(7, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
				pdfButton = new JButton();
				pdfButton.setText("View Pdf");
				jPanelWRelevance.add(pdfButton);
				pdfButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/acroreadView.png")));
				
				
				pdfButton.addActionListener(new ActionListener(){		
					public void actionPerformed(ActionEvent arg0) {
						viewPdf();
					}
				});
				changePdfIcons();
			}
		}
		return jPanelWRelevance;
	}
	
	private JScrollPane getJScrollPaneTitle() {
		if(jScrollPaneTitle == null) {
			jScrollPaneTitle = new JScrollPane();
			{
				titleTextPane = new JTextPane();
				jScrollPaneTitle.setViewportView(titleTextPane);
				titleTextPane.setText(this.publication.getTitle());
				titleTextPane.setEditable(false);
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
			jScrollPaneAbstract.setAutoscrolls(false);
			{
				abstractTextPane = new JTextPane();
				jScrollPaneAbstract.setViewportView(abstractTextPane);
				abstractTextPane.setText(this.publication.getAbstractSection());
				abstractTextPane.setEditable(false);
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
				authorsTextPane.setText(this.publication.getAuthors());
				authorsTextPane.setEditable(false);
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
			jPanelSecondInformationLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelSecondInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7};
			jPanelSecondInformationLayout.columnWeights = new double[] {0.0, 0.1};
			jPanelSecondInformationLayout.columnWidths = new int[] {7, 7};
			jPanelSecondInformation.setLayout(jPanelSecondInformationLayout);
			{
				
	
			}
			{
				otherIDTextField = new JButton();
				jPanelSecondInformation.add(otherIDTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				otherIDTextField.setText(this.publication.getOtherID());
				if(this.publication.getExternalLink()!=null)
				{
					otherIDTextField.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							try {
								Help.internetAccess(publication.getExternalLink());
							} catch (IOException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							}					
						}
					});
					otherIDTextField.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
							null));
				}
			}
			{
				dateLable = new JLabel();
				jPanelSecondInformation.add(dateLable, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				dateLable.setText("Date:");
			}
			{
				dateTextField = new JTextField();
				jPanelSecondInformation.add(dateTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				dateTextField.setText(String.valueOf(this.publication.getYearDate()));
				dateTextField.setEditable(false);
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
				journalTextField.setText(this.publication.getJournal());
				journalTextField.setEditable(false);
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
				statusTextField.setText(this.publication.getStatus());
				statusTextField.setEditable(false);
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
				volumeField1.setText(this.publication.getVolume());
				volumeField1.setEditable(false);
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
				jPanelSecondInformation.add(getJLabelOtherID(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				issueField.setText(this.publication.getIssue());
				issueField.setEditable(false);
				issueField.setBackground(new java.awt.Color(250,250,250));
				issueField.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
			}
		}
		return jPanelSecondInformation;
	}


	private void changePdfIcons() {
		if(publication.isPDFAvailable())
		{
			pdfButton.setEnabled(true);
		}
		else
		{		
			pdfButton.setEnabled(false);
		}
	}

	private JTextField getJTextFieldPages() {
		if(jTextFieldPages == null) {
			jTextFieldPages = new JTextField();
			jTextFieldPages = new JTextField();
			jTextFieldPages.setText(this.publication.getPages());
			jTextFieldPages.setEditable(false);
			jTextFieldPages.setBackground(new java.awt.Color(250,250,250));
			jTextFieldPages.setBorder(BorderFactory.createCompoundBorder(
					null, 
					null));
		}
		return jTextFieldPages;
	}
	
	private JLabel getJLabelOtherID() throws SQLException, DatabaseLoadDriverException {
		if(jLabelOtherID == null) {
			jLabelOtherID = new JLabel();
			jLabelOtherID.setText(publication.getType()+" :");
		}
		return jLabelOtherID;
	}
	
	private JLabel getJLabelExternalLink() {
		if(jLabelExternalLink == null) {
			jLabelExternalLink = new JLabel();
			jLabelExternalLink.setText("External Link");
		}
		return jLabelExternalLink;
	}
	
	private JTextField getJTextFieldExternalLink() {
		if(jTextFieldExternalLink == null) {
			jTextFieldExternalLink = new JTextField();
			jTextFieldExternalLink.setText(publication.getExternalLink());
		}
		return jTextFieldExternalLink;
	}
	
	private JButton getJButtonUpdate() {
		if(jButtonUpdate == null) {
			jButtonUpdate = new JButton();
			jButtonUpdate.setText("Update");
			jButtonUpdate.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						publication.setExternalLink(jTextFieldExternalLink.getText());
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {						
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonUpdate;
	}

	@Override
	protected void okButtonAction() {
		finish();
	}

	@Override
	protected String getHelpLink() {
		return null;
	}

}
