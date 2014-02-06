package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.structures.PDF;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


/**
 * 
* GUI for View a Publication details
*/
public class PublicationResumeGUI extends JDialog{

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
	private JLabel jLabelOtherID;
	private JTextField jTextFieldPages;
	private JPanel jPanelSecondInformation;
	private JScrollPane jScrollPaneAuthors;
	private JScrollPane jScrollPaneAbstract;
	private JScrollPane jScrollPaneTitle;
	private JPanel jPanelWRelevance;
	private JPanel jPanelPublicationInformation;
	private JTextPane abstractTextPane;
	private JButton closeButton;
	private JButton pdfButton;
	private JLabel pagesLabel;
	private JLabel issueLabel;
	private JTextField issueField;
	private JTextField volumeField1;
	
	private IPublication publication;
	private IDatabase database;

	public PublicationResumeGUI(IPublication publication,IDatabase database){
		super(Workbench.getInstance().getMainFrame());
		this.publication = publication;
		this.setVisible(true);
		this.database=database;
		this.setTitle("Publication - " + this.publication.getOtherID());
		this.setSize(900, 500);
		Utilities.centerOnOwner(this);
		initGUI();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1, 0.0};
				thisLayout.rowHeights = new int[] {9, 30, 30, 30, 30, 20, 20, 30};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {22, 57, 65, 20, 73, 469, 18, 86, 84, 20, 7};
				getContentPane().setLayout(thisLayout);
				getContentPane().add(getJPanelPublicationInformation(), new GridBagConstraints(1, 0, 8, 7, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					closeButton = new JButton();
					getContentPane().add(closeButton, new GridBagConstraints(8, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					closeButton.setText("Close");
					closeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Cancel.png")));
					closeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							closeButtonActionPerformed(evt);
						}
					});
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
		}	}
	
	private void closeButtonActionPerformed(ActionEvent evt){
		//this.setModal(false);
		this.setVisible(false);
		this.dispose();
	}

	private void viewPdf(){
		String filePath = Corpora.saveDocs;
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
	
	private JPanel getJPanelPublicationInformation() {
		if(jPanelPublicationInformation == null) {
			jPanelPublicationInformation = new JPanel();
			GridBagLayout jPanelPublicationInformationLayout = new GridBagLayout();
			jPanelPublicationInformationLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.0};
			jPanelPublicationInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
			jPanelPublicationInformationLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.0};
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
				
	
			}
			{
				pmidTextField = new JTextField();
				jPanelSecondInformation.add(pmidTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				pmidTextField.setText(String.valueOf(this.publication.getOtherID()));
				pmidTextField.setEditable(false);
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
				dateTextField.setText(String.valueOf(this.publication.getDate()));
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
	
	private void fillType() {
		try {
			PreparedStatement prep = database.getConnection().prepareStatement(QueriesPublication.selectPublicationsTypeForPublication);
			prep.setInt(1,publication.getID());
			ResultSet rs = prep.executeQuery();
			if(rs.next())
			{
				jLabelOtherID.setText(rs.getString(1)+":");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
				       	
	}

	private void changePdfIcons() {
		
		String id_path = Corpora.saveDocs+"id"+this.publication.getID()+".pdf";
		String id_path_otherID = Corpora.saveDocs+"id"+this.publication.getID()+"-"+this.publication.getOtherID()+".pdf";
		String pdf_path = Corpora.saveDocs+this.publication.getOtherID()+".pdf";
		if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
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
	
	private JLabel getJLabelOtherID() {
		if(jLabelOtherID == null) {
			jLabelOtherID = new JLabel();
			jLabelOtherID.setText("pmid :");
			fillType();
		}
		return jLabelOtherID;
	}

}
