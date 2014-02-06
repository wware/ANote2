package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
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

import pt.uminho.anote2.aibench.publicationmanager.dataStructures.PDF;
import pt.uminho.anote2.aibench.publicationmanager.dataStructures.RelevanceChanged;
import pt.uminho.anote2.aibench.publicationmanager.dataStructures.RelevanceData;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.database.queries.documents.QueriesPublication;
import pt.uminho.anote2.datastructures.database.queries.process.QueriesIRProcess;
import pt.uminho.anote2.datastructures.utils.MathUtils;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
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
public class PublicationResumeGUI extends JDialog implements Observer{

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
	private JTextPane abstractTextPane;
	private JButton closeButton;
	private JButton jButtonHelp;
	private JButton jButtonLoadPdf;
	private ButtonGroup buttonGroup;
	private JButton relevanceButton3;
	private JButton relevanceButton2;
	private JButton relevanceButton1;
	private JPanel relevancePanel;
	private JButton pdfButton;
	private JLabel pagesLabel;
	private JLabel issueLabel;
	private JTextField issueField;
	private JTextField volumeField1;
	
	private IPublication publication;
	private ArrayList<RelevanceData> queryKeywords;
	private RelevanceChanged rel_changed;
	private QueryInformationRetrievalExtension query;

	protected boolean cancel;

	private JFileChooser filePath;

	public PublicationResumeGUI(QueryInformationRetrievalExtension query,IPublication publication, RelevanceChanged rel_changed){
		super(Workbench.getInstance().getMainFrame());
		this.publication = publication;
		this.query=query;
		this.rel_changed = rel_changed;	
		initFileChooser();
		try {
			this.constructQueryKeywords();
		} catch (NonExistingConnection e) {
			e.printStackTrace();
		}
		{
			this.setVisible(true);
			this.setTitle("Publication - " + this.publication.getOtherID());
			this.setSize(900, 500);
			Utilities.centerOnOwner(this);
			//this.setModal(true);
		}
		initGUI();
		refreshRelevanceButtons();	
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
				getContentPane().add(getJPanelOperations(), new GridBagConstraints(0, 7, 9, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}

		} catch(Exception e) {
			e.printStackTrace();
		}	}
	
	private void closeButtonActionPerformed(ActionEvent evt){
		//this.setModal(false);
		this.setVisible(false);
		this.dispose();
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Publication_Resume");
				}
			});
		}
		return jButtonHelp;
	}
	
	private void viewPdf(){
		String filePath = PublicationManager.saveDocs;
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
	
	
	
	private void relevanceButtonPerformed(){
		
		(new RelevanceGUI(query.getPubManager().getDb(),this.publication, this.queryKeywords, this.rel_changed)).addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent arg0) {}
			
			public void windowClosing(WindowEvent arg0) {}
			
			public void windowDeactivated(WindowEvent arg0) {}
			
			public void windowDeiconified(WindowEvent arg0) {}
			
			public void windowIconified(WindowEvent arg0) {}
			
			public void windowOpened(WindowEvent arg0) {}
			
			public void windowClosed(WindowEvent arg0) {
				refreshRelevanceButtons();
			}
			
		});
				
	}

	
	private void constructQueryKeywords() throws NonExistingConnection{
		this.queryKeywords = new ArrayList<RelevanceData>();
		query.getPubManager().getDb().openConnection();	
		try {						
			PreparedStatement ps = (PreparedStatement) query.getPubManager().getDb().getConnection().prepareStatement(QueriesIRProcess.selectQueryByID);    
			ps.setInt(1,this.publication.getID());		
			ResultSet rs = ps.executeQuery();
			ResultSet rs2;
			
			while(rs.next())
			{	
				PreparedStatement ps2 = (PreparedStatement) query.getPubManager().getDb().getConnection().prepareStatement(QueriesIRProcess.selectQueryProperties);    
				ps2.setInt(1,rs.getInt(1));		
				rs2 = ps2.executeQuery();
				String queryTo = rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3);
				while(rs2.next())
				{
					queryTo = queryTo.concat(rs2.getString(1)+" "+rs2.getString(2)+" ");
				}
				RelevanceData pair = new RelevanceData(this.publication, rs.getInt(1),queryTo );
				this.queryKeywords.add(pair);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		query.getPubManager().getDb().closeConnection();
	}
	
	private void refreshRelevanceButtons(){
		String relevance = getAverageRelevance();
		
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
	
	private String getAverageRelevance(){
		
		if(this.queryKeywords.size()==0)
			return null;
		
		boolean notVoted = true;
		
		int[] relevances = new int[this.queryKeywords.size()];
				
		for(int i=0;i<this.queryKeywords.size();i++)
		{
			if(this.queryKeywords.get(i).getRelevance()!=null)
			{	
				notVoted=false;
				if(this.queryKeywords.get(i).getRelevance().compareTo("irrelevant")==0)
					relevances[i]=1;
				else if(this.queryKeywords.get(i).getRelevance().compareTo("related")==0)
					relevances[i]=2;
				else
					relevances[i]=3;
			}
		}
		
		if(notVoted) // There is no relevation voted for the publication to any query
			return null;
		
		int relevance = MathUtils.getAverage(relevances);
		
		switch(relevance){
			case 1:
				return "irrelevant";
			case 2:
				return "related";
			default:
				return "relevant";
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
							relevanceButtonPerformed();
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
							relevanceButtonPerformed();
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
			query.getPubManager().getDb().openConnection();
			PreparedStatement ps = (PreparedStatement) query.getPubManager().getDb().getConnection().prepareStatement(QueriesPublication.selectPublicationType);    
			ps.setInt(1,publication.getID());	
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				jLabelOtherID.setText(rs.getString(1)+":");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				       	
	}

	private JPanel getJPanelOperations() {
		if(jPanelOperations == null) {
			jPanelOperations = new JPanel();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Publication Operation", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperationsLayout.rowWeights = new double[] {0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7, 20, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			jPanelOperations.add(getJButtonHelp(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
					closeButton = new JButton();
					jPanelOperations.add(closeButton, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelOperations.add(getJLabelAddMAualPdf(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jPanelOperations.add(getJButtonAddPdf(), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					closeButton.setText("Close");
					closeButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
					closeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							closeButtonActionPerformed(evt);
						}
					});
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
		
		String id_path = PublicationManager.saveDocs+"id"+this.publication.getID()+".pdf";
		String id_path_otherID = PublicationManager.saveDocs+"id"+this.publication.getID()+"-"+this.publication.getOtherID()+".pdf";
		String pdf_path = PublicationManager.saveDocs+this.publication.getOtherID()+".pdf";
		if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
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
			jLabelView.setText("ViewPDF :");
		}
		return jLabelView;
	}
	
	private JLabel getJLabelJournalRetrieval() {
		if(jLabelJournalRetrieval == null) {
			jLabelJournalRetrieval = new JLabel();
			jLabelJournalRetrieval.setText("DownloadPDF :");
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
		String id_path = PublicationManager.saveDocs+"id"+this.publication.getID()+".pdf";
		String id_path_otherID = PublicationManager.saveDocs+"id"+this.publication.getID()+"-"+this.publication.getOtherID()+".pdf";
		String pdf_path = PublicationManager.saveDocs+this.publication.getOtherID()+".pdf";
		if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
		{
			new ShowMessagePopup("File Adding Sucess");
		}
		else
		{	
			Workbench.getInstance().warn("Unable to Download Pdf File");
		}	
		this.query.deleteObserver(this);
		changePdfIcons();
	}
	
	private JLabel getJLabelAddMAualPdf() {
		if(jLabelAddMAualPdf == null) {
			jLabelAddMAualPdf = new JLabel();
			jLabelAddMAualPdf.setText("Add Pdf :");
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
		this.query.addObserver(this); 
        int rel = filePath.showOpenDialog(new JFrame()); 
        if(rel == JFileChooser.CANCEL_OPTION)
        {
        	
        }
        else
        {
        	File save_path = filePath.getSelectedFile();
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
	
	private JLabel getJLabelOtherID() {
		if(jLabelOtherID == null) {
			jLabelOtherID = new JLabel();
			jLabelOtherID.setText("pmid :");
			fillType();
		}
		return jLabelOtherID;
	}

}
