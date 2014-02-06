package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PDF;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.RelevancePanel;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.textprocessing.TermSeparator;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

/**
 * 
* GUI for View a Publication details
*/
public class PublicationResumeGUI extends DialogGenericViewOkButtonOnly implements Observer{

	private static final long serialVersionUID = -664381864632621746L;
	
	private JButton otheridTextField;
	private JEditorPane titleTextPane;
	private JLabel dateLable;
	private JLabel journalLabel;
	private JTextField journalTextField;
	private JLabel statusLabel;
	private JTextField statusTextField;
	private JTextField dateTextField;
	private JLabel authorsLabel;
	private JEditorPane authorsTextPane;
	private JLabel titleLabel;
	private JLabel volumeLabel;
	private JLabel abstractLabel;
	private JButton jButtonUpdateExternalLink;
	private JTextField jTextFieldExternalLink;
	private JLabel jLabelExternalLink;
	private JLabel jLabelOtherID;
	private JButton jButtonAddPdf;
	private JLabel jLabelAddMAualPdf;
	private JTextField jTextFieldPages;
	private JLabel jLabelJournalRetrieval;
	private JLabel jLabelView;
	private JPanel jPanelOperations;
	private JPanel jPanelSecondInformation;
	private JScrollPane jScrollPaneAuthors;
	private JScrollPane jScrollPaneAbstract;
	private JScrollPane jScrollPaneTitle;
	private JPanel jPanelWRelevance;
	private JPanel jPanelPublicationInformation;
	private JEditorPane abstractTextPane;
	private JButton jButtonLoadPdf;
	private JPanel relevancePanel;
	private JButton pdfButton;
	private JLabel pagesLabel;
	private JLabel issueLabel;
	private JTextField issueField;
	private JTextField volumeField1;	
	private IPublication publication;
	private QueryInformationRetrievalExtension query;
	protected static final String SEPARATOR = "[ '<>/(),\n\\.]+";


	protected boolean cancel;

	private JFileChooser filePath;

	private RelevancePanel relvancePAnel;

	public PublicationResumeGUI(QueryInformationRetrievalExtension query,IPublication publication){
		super("Publication - " + publication.getOtherID());
		this.publication = publication;
		this.query=query;
		initFileChooser();
		initGUI();
		this.setSize(GlobalOptions.superWidth, GlobalOptions.superHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.8, 0.2};
				thisLayout.rowHeights = new int[] {7, 7};
				thisLayout.columnWeights = new double[] {0.1};
				thisLayout.columnWidths = new int[] {7};
				getContentPane().setLayout(thisLayout);
				getContentPane().add(getJPanelPublicationInformation(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				getContentPane().add(getJPanelOperations(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
	
	
	private void loadPdf(){
		this.query.addObserver(this);
		ArrayList<IPublication> toDownload = new ArrayList<IPublication>();
		toDownload.add(publication);
		
		ParamSpec[] paramsSpec = new ParamSpec[]{ 
				new ParamSpec("query",QueryInformationRetrievalExtension.class,this.query, null),
				new ParamSpec("publication",ArrayList.class,toDownload, null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.journalretrievalmuldoc")){			
				Workbench.getInstance().executeOperation(def, paramsSpec);
				return;
			}
		}
		

	}
	
	
	private JPanel getJPanelPublicationInformation() throws SQLException, DatabaseLoadDriverException {
		if(jPanelPublicationInformation == null) {
			jPanelPublicationInformation = new JPanel();
			GridBagLayout jPanelPublicationInformationLayout = new GridBagLayout();
			jPanelPublicationInformationLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.1, 0.0, 0.0};
			jPanelPublicationInformationLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 20};
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
						jPanelPublicationInformation.add(getJButtonUpdateExternalLink(), new GridBagConstraints(7, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						authorsLabel.setText("Authors:");
					}
					abstractLabel.setText("Abstract :");
				}
				titleLabel.setText("Title :");
			}
		}
		return jPanelPublicationInformation;
	}
	
	private JPanel getJPanelWRelevance() throws SQLException, DatabaseLoadDriverException {
		if(jPanelWRelevance == null) {
			jPanelWRelevance = new JPanel();
			{
				relevancePanel = new JPanel();
				jPanelWRelevance.add(relevancePanel);
				GridBagLayout relevancePanelLayout = new GridBagLayout();
				relevancePanelLayout.rowWeights = new double[] {0.1};
				relevancePanelLayout.rowHeights = new int[] {7};
				relevancePanelLayout.columnWeights = new double[] {0.0};
				relevancePanelLayout.columnWidths = new int[] {16};
				relevancePanel.setLayout(relevancePanelLayout);

				relevancePanel.setSize(50, 30);
				relevancePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Weight Relevance", TitledBorder.LEADING, TitledBorder.TOP));
				relevancePanel.add(getJPanelRelevance(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jPanelWRelevance;
	}
	
	private RelevancePanel getJPanelRelevance() throws SQLException, DatabaseLoadDriverException {
		if(relvancePAnel == null)
		{
			relvancePAnel = new RelevancePanel(query, publication);
		}
		return relvancePAnel;
	}

	private JScrollPane getJScrollPaneTitle() {
		if(jScrollPaneTitle == null) {
			jScrollPaneTitle = new JScrollPane();
			{
				titleTextPane = new JEditorPane();
				jScrollPaneTitle.setViewportView(titleTextPane);
				titleTextPane.setContentType("text/html");
				titleTextPane.setEditable(false);
				if(query.isFromPubmedSearch())
				{
					String[] wordsToHighLited = getWordsToHiglithed();
					String htmlCode = getHTMLCode(this.publication.getTitle(),wordsToHighLited);
					titleTextPane.setText(htmlCode);
				}
				else
				{
					titleTextPane.setText(this.publication.getTitle());
				}
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

	private String getHTMLCode(String title, String[] wordsToHighLited) {
		if(title==null)
		{
			return "<DIV></DIV>";
		}
		String htmlCOde = TermSeparator.termSeparator(title);
		for(String wordToHighLited:wordsToHighLited)
		{
			Pattern p =  Pattern.compile(SEPARATOR + "("+wordToHighLited+")" + SEPARATOR, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(htmlCOde);
			List<Integer[]> list = new ArrayList<Integer[]>();
			if(htmlCOde.toLowerCase().startsWith(wordToHighLited.toLowerCase()))
			{
				Integer[] offset = new Integer[2];
				offset[0] = 0;
				offset[1] = wordToHighLited.length();
				list.add(offset);
			}
			if(htmlCOde.toLowerCase().endsWith(wordToHighLited.toLowerCase()))
			{
				Integer[] offset = new Integer[2];
				offset[0] = htmlCOde.length()-1-wordToHighLited.length();
				offset[1] = htmlCOde.length()-1;
				list.add(offset);
			}
			while(m.find())
			{
				Integer[] offset = new Integer[2];
				offset[0] = m.start(1);
				offset[1] = m.end(1);
				list.add(offset);
			}
			for(int i=list.size()-1;i>=0;i--)
			{
				htmlCOde = htmlCOde.substring(0, list.get(i)[0]) + 
						"<b  bgcolor=\""+OtherConfigurations.getHighlightBackGroundColor()+"\">"+htmlCOde.substring(list.get(i)[0], list.get(i)[1])+"</b>" +
						htmlCOde.substring(list.get(i)[1]);
			}
		}
		return "<DIV>" + htmlCOde + "</DIV>";
	}
	


	private String[] getWordsToHiglithed() {
		String query = this.query.getKeyWords();
		String organism = this.query.getOrganism();
		query = query + " " + organism; 
		query = query.replaceAll("AND", "");
		query = query.replaceAll("OR", " ");
		query = query.replaceAll("\"", " ");	
		query = query.replaceAll("\\)", " ");	
		query = query.replaceAll("\\(", " ");
		query = query.replaceAll("\\[", " ");
		query = query.replaceAll("\\]", " ");
		query = query.replaceAll("'", " ");
		query = query.replaceAll(";", " ");
		query = query.replaceAll(":", " ");
		query = query.replaceAll("\\+", " ");
		query = query.replaceAll("\\*", " ");
		query = query.replaceAll("\\-", " ");
		query = query.replaceAll("\\/", " ");
		query = query.replaceAll("\\{", " ");
		query = query.replaceAll("\\}", " ");
		query = query.replaceAll("\\s+", " ");
		
		if(query.length()>0)
		{
			return query.split("\\s");
		}		
		return new String[0];
	}
	
	private String[] getWordsToHiglithedAuthors() {
		String query = String.valueOf(this.query.getProperties().get("authors"));
		query = query.replaceAll("AND", " ");
		query = query.replaceAll("OR", " ");
		query = query.replaceAll("\"", " ");	
		query = query.replaceAll("\\)", " ");	
		query = query.replaceAll("\\(", " ");
		query = query.replaceAll("\\[", " ");
		query = query.replaceAll("\\]", " ");
		query = query.replaceAll("'", " ");
		query = query.replaceAll(";", " ");
		query = query.replaceAll(":", " ");
		query = query.replaceAll("\\+", " ");
		query = query.replaceAll("\\*", " ");
		query = query.replaceAll("\\-", " ");
		query = query.replaceAll("\\/", " ");
		query = query.replaceAll("\\{", " ");
		query = query.replaceAll("\\}", " ");
		query = query.replaceAll("\\s+", " ");
		
		if(query.length()>0)
		{
			return query.split("\\s");
		}		
		return new String[0];
	}

	private JScrollPane getJScrollPaneAbstract() {
		if(jScrollPaneAbstract == null) {
			jScrollPaneAbstract = new JScrollPane();
			{
				abstractTextPane = new JEditorPane();
				jScrollPaneAbstract.setViewportView(abstractTextPane);
				abstractTextPane.setContentType("text/html");
				abstractTextPane.setEditable(false);
				if(query.isFromPubmedSearch())
				{
					String[] wordsToHighLited = getWordsToHiglithed();
					String htmlCode = getHTMLCode(this.publication.getAbstractSection(),wordsToHighLited);
					abstractTextPane.setText(htmlCode);
				}
				else
				{
					abstractTextPane.setText(this.publication.getAbstractSection());
				}
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
				authorsTextPane = new JEditorPane();
				authorsTextPane.setContentType("text/html");
				jScrollPaneAuthors.setViewportView(authorsTextPane);
				authorsTextPane.setEditable(false);
				if(query.isFromPubmedSearch())
				{
					String[] wordsToHighLited = getWordsToHiglithedAuthors();
					String htmlCode = getHTMLCode(this.publication.getAuthors(),wordsToHighLited);
					authorsTextPane.setText(htmlCode);
				}
				else
				{
					authorsTextPane.setText(this.publication.getAuthors());
				}
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
				String otherID = publication.getOtherID();
				if(otherID == null)
					otherID = new String();
				otheridTextField = new JButton();
				otheridTextField.setText(otherID);
				otheridTextField.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String  externalLink = publication.getExternalLink();
						if(externalLink ==null || externalLink.length() < 1)
						{
							Workbench.getInstance().warn("The document does not contains external link... Please update External Link");
						}
						else
						{
							try {
								Help.internetAccess(externalLink);
							} catch (IOException e1) {
								TreatExceptionForAIbench.treatExcepion(e1);
							}
						}
					}
				});
				jPanelSecondInformation.add(otheridTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				otheridTextField.setText(String.valueOf(this.publication.getOtherID()));
				otheridTextField.setBackground(new java.awt.Color(250,250,250));
				otheridTextField.setBorder(BorderFactory.createCompoundBorder(
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
	
	private JPanel getJPanelOperations() {
		if(jPanelOperations == null) {
			jPanelOperations = new JPanel();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperationsLayout.rowWeights = new double[] {0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7, 20, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			jPanelOperations.add(getJLabelView(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			{
				pdfButton = new JButton();
				jPanelOperations.add(pdfButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jPanelOperations.add(getJLabelJournalRetrieval(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				{
					jButtonLoadPdf = new JButton();
					jPanelOperations.add(jButtonLoadPdf, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonLoadPdf.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/acroreadViewLoad.png")));
					jButtonLoadPdf.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							loadPdf();
						}
					});
				}
				{
					jPanelOperations.add(getButtonsPanel(), new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelOperations.add(getJLabelAddMAualPdf(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelOperations.add(getJButtonAddPdf(), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				}
				pdfButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/acroreadView.png")));

				
				pdfButton.addActionListener(new ActionListener(){		
					public void actionPerformed(ActionEvent arg0) {
						viewPdf();
					}
				});
				changePdfIcons();
			}
		}
		return jPanelOperations;
	}

	private void changePdfIcons() {
		if(this.publication.isPDFAvailable())
		{
			pdfButton.setEnabled(true);
			jButtonLoadPdf.setEnabled(false);
			jButtonAddPdf.setEnabled(false);
		}
		else
		{		
			pdfButton.setEnabled(false);
			jButtonLoadPdf.setEnabled(true);
			jButtonAddPdf.setEnabled(true);
		}
	}
	
	private JLabel getJLabelView() {
		if(jLabelView == null) {
			jLabelView = new JLabel();
			jLabelView.setText("View PDF :");
		}
		return jLabelView;
	}
	
	private JLabel getJLabelJournalRetrieval() {
		if(jLabelJournalRetrieval == null) {
			jLabelJournalRetrieval = new JLabel();
			jLabelJournalRetrieval.setText("Download PDF :");
		}
		return jLabelJournalRetrieval;
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

	public void update(Observable arg0, Object arg1) {
		if(this.publication.isPDFAvailable())
		{
			new ShowMessagePopup("File Added Sucessfully");
		}
		else
		{	
			Workbench.getInstance().warn("Unable to Download PDF File");
		}	
		this.query.deleteObserver(this);
		changePdfIcons();
	}
	
	private JLabel getJLabelAddMAualPdf() {
		if(jLabelAddMAualPdf == null) {
			jLabelAddMAualPdf = new JLabel();
			jLabelAddMAualPdf.setText("Add PDF :");
		}
		return jLabelAddMAualPdf;
	}
	
	private JButton getJButtonAddPdf() {
		if(jButtonAddPdf == null) {
			jButtonAddPdf = new JButton();
			jButtonAddPdf.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/acroreadAdd.png")));
			jButtonAddPdf.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					openAddViewDialog();
				}
			});
		}
		return jButtonAddPdf;
	}
	
	private void initFileChooser()
	{
        filePath = new FileChooserExtensionPdf();
	}

	protected void openAddViewDialog() {
		this.query.addObserver(this); 
        int rel = filePath.showOpenDialog(this); 
        if(rel == JFileChooser.CANCEL_OPTION)
        {
        	
        }
        else if(rel == JFileChooser.APPROVE_OPTION)
        {
        	
        	File save_path = filePath.getSelectedFile();
        	SessionSettings.getSessionSettings().setSearchDirectory(save_path.getParent());
        	ParamSpec[] paramsSpec = new ParamSpec[]{ 
        			new ParamSpec("Publication",IPublication.class,publication, null),
        			new ParamSpec("Query",QueryInformationRetrievalExtension.class,this.query, null),
        			new ParamSpec("File",File.class,save_path, null)
        	};

        	for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
        		if (def.getID().equals("operations.addfile")){			
        			Workbench.getInstance().executeOperation(def, paramsSpec);
        		}
        	}
        }
	}
	
	private JLabel getJLabelOtherID() throws SQLException, DatabaseLoadDriverException {
		if(jLabelOtherID == null) {
			jLabelOtherID = new JLabel();
			jLabelOtherID.setText(publication.getType());
		}
		return jLabelOtherID;
	}

	@Override
	protected void okButtonAction() {
		finish();		
	}

	@Override
	protected String getHelpLink() {
		return 	GlobalOptions.wikiGeneralLink+"Publication_Resume";
	}
	
	private JLabel getJLabelExternalLink() {
		if(jLabelExternalLink == null) {
			jLabelExternalLink = new JLabel();
			jLabelExternalLink.setText("External Link :");
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
	
	private JButton getJButtonUpdateExternalLink() {
		if(jButtonUpdateExternalLink == null) {
			jButtonUpdateExternalLink = new JButton();
			jButtonUpdateExternalLink.setText("Update");
			jButtonUpdateExternalLink.addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						publication.setExternalLink(jTextFieldExternalLink.getText());
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (DatabaseLoadDriverException e) {
						e.printStackTrace();
					}
				}
			});
		}
		return jButtonUpdateExternalLink;
	}

}
