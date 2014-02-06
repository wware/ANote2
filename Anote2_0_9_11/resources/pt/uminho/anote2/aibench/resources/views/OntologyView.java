package pt.uminho.anote2.aibench.resources.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeModelOntology;
import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeNodeOntology;
import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeOntology;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.resources.datatypes.Resources;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.JTreeCellRenderer;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.exceptions.resources.ontologies.OntologyMissingRootExeption;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;

public class OntologyView extends JPanel implements Observer{
	
	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPane1;
	private JPanel jPanelOperations;
	private JButton jButtonUpdateOntology;
	private JList jListSyns;
	private JButton jButtonUpdate12;
	private JTextField jTextFieldID;
	private JLabel jLabelID;
	private JTree jTreeOntology;
	private JTextField jTextPaneNotes;
	private JLabel jLabelNotes;
	private JTextField jTextFieldNAme;
	private JLabel jLabelNAme;
	private JPanel jPanelHeader;
	private JButton jButtonHelp;
	private JPanel SelectGOandRelationSet;
	private JPanel auxiliarPanel;
	private JScrollPane jp;
	private OntologyAibench ontology;
	private JPanel jpanelTermInfo;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanelOntoInfo;
	private JPanel jPanelOntoStatsInfo;
	private JLabel jLabelTerms;
	private JLabel jLabelSyn;
	private JLabel jLabelAveregeSynForTerm;
	private JLabel jLabelClassesContent;
	private JTextField jTextFieldTerms;
	private JTextField jTextFieldSyn;
	private JTextField jTextFieldAverageTermsForSyn;
	private JTextField jTextFieldNumberOfClasses;
	private int numberTerms;
	private int numberSyn;
	DecimalFormat dec = new DecimalFormat("0.00");
	private SortedMap<Integer, String> classes; // classeID,classe

	public OntologyView(OntologyAibench ontology){
		this.ontology=ontology;//.getRelationCorpus().getAnnotations().getOntologies().getOntology();
		this.ontology.addObserver(this);
		try {
			classes = Resources.getResourceContentClasses(ontology.getID());
			initGUI();	
			createViewTrees();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (OntologyMissingRootExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.7, 0.05};
				thisLayout.rowHeights = new int[] {7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1};
				thisLayout.columnWidths = new int[] {7};
				this.setLayout(thisLayout);
				this.setPreferredSize(new java.awt.Dimension(569, 268));

				{
					auxiliarPanel = new JPanel();
					GridBagLayout auxiliarPanelLayout = new GridBagLayout();
					this.add(auxiliarPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					auxiliarPanel.setLayout(auxiliarPanelLayout);
					{
						jPanelHeader = new JPanel();
						jPanelHeader.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Information", TitledBorder.LEADING, TitledBorder.TOP));
						GridBagLayout jPanelHeaderLayout = new GridBagLayout();
						auxiliarPanel.add(jPanelHeader, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelHeaderLayout.rowWeights = new double[] {0.1};
						jPanelHeaderLayout.rowHeights = new int[] {7};
						jPanelHeaderLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
						jPanelHeaderLayout.columnWidths = new int[] {20, 7, 20, 7, 20, 20};
						jPanelHeader.setLayout(jPanelHeaderLayout);
						{
							jLabelNAme = new JLabel();
							jPanelHeader.add(jLabelNAme, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelNAme.setText("Name:");
						}
						{
							jTextFieldNAme = new JTextField();
							jTextFieldNAme.setText(ontology.getName());
							jTextFieldNAme.setEditable(false);
							jPanelHeader.add(jTextFieldNAme, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						}
						{
							jLabelNotes = new JLabel();
							jPanelHeader.add(jLabelNotes, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelNotes.setText("Description:");
						}
						{
							jTextPaneNotes = new JTextField();
							jTextPaneNotes.setEditable(false);
							jTextPaneNotes.setText(ontology.getInfo());
							jPanelHeader.add(jTextPaneNotes, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						}
						{
							jLabelID = new JLabel();
							jPanelHeader.add(jLabelID, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelID.setText("ID:");
						}
						{
							jTextFieldID = new JTextField();
							jTextFieldID.setText(String.valueOf(ontology.getID()));
							jTextFieldID.setEditable(false);
							jPanelHeader.add(jTextFieldID, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						}
					}
					auxiliarPanelLayout.rowWeights = new double[] {0.1};
					auxiliarPanelLayout.rowHeights = new int[] {7};
					auxiliarPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					auxiliarPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
				}
				{
					SelectGOandRelationSet = new JPanel();
					GridBagLayout SelectGOandRelationSetLayout = new GridBagLayout();
					this.add(SelectGOandRelationSet, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					SelectGOandRelationSetLayout.rowWeights = new double[] {};
					SelectGOandRelationSetLayout.rowHeights = new int[] {};
					SelectGOandRelationSetLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
					SelectGOandRelationSetLayout.columnWidths = new int[] {7, 7, 7, 7, 7};
					SelectGOandRelationSet.setLayout(SelectGOandRelationSetLayout);
					{ 
						SelectGOandRelationSet.add(getJTabbedPane1(), new GridBagConstraints(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						
						jSplitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(), new JScrollPane());
						jSplitPane1.setDividerSize(10);
						jSplitPane1.setResizeWeight(0.8);
						jSplitPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Ontology Tree", TitledBorder.LEADING, TitledBorder.TOP));
						this.add(jSplitPane1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				 jp= new JScrollPane();
			}
	}

	private JPanel getOperationsPane() throws SQLException, DatabaseLoadDriverException {
		if(jPanelOperations == null)
		{
			jPanelOperations = new JPanel();
			jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Operations", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelOperationsLayout = new GridBagLayout();
			jPanelOperationsLayout.rowWeights = new double[] {0.1};
			jPanelOperationsLayout.rowHeights = new int[] {7};
			jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelOperations.setLayout(jPanelOperationsLayout);
			{
				jButtonHelp = new JButton();
				jPanelOperations.add(jButtonHelp, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
				jButtonHelp.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						try {
							Help.internetAccess(GlobalOptions.wikiGeneralLink+"Ontology_Update");
						} catch (IOException e1) {
							TreatExceptionForAIbench.treatExcepion(e1);
						}
					}
				});
			}
			{
				jButtonUpdateOntology = new JButton();
				jPanelOperations.add(jButtonUpdateOntology, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButtonUpdateOntology.setText("Update (OBO 1.0) Files");
				jButtonUpdateOntology.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
				jButtonUpdateOntology.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.updateontology")){			
								Workbench.getInstance().executeOperation(def);
								return;
							}
						}
					}
				});

			}
			{
				jButtonUpdate12 = new JButton();
				jPanelOperations.add(jButtonUpdate12, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jButtonUpdate12.setText("Update (OBO 1.2) Files");
				jButtonUpdate12.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh.png")));
				jButtonUpdate12.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.updateontology12")){			
								Workbench.getInstance().executeOperation(def);
								return;
							}
						}
					}
				});
			}
			if(ontology.isOntologyFill())
			{
				jButtonUpdateOntology.setEnabled(false);
				jButtonUpdate12.setEnabled(false);
			}
		}
		return jPanelOperations;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createViewTrees() throws SQLException, OntologyMissingRootExeption, DatabaseLoadDriverException
	{		
		 if(ontology.isOntologyFill())
		 {
			 TreeOntology<IResourceElement> getTree = ontology.getOntologyTree();
			 TreeModelOntology ontoTree =  new TreeModelOntology(getTree);
			 jTreeOntology = new JTree(ontoTree);
			 jTreeOntology.addMouseListener(new MouseAdapter() {
				 public void mouseClicked(MouseEvent me) {
					 try {
						 doMouseClicked();
					 } catch (SQLException e) {
						 TreatExceptionForAIbench.treatExcepion(e);
					 } catch (DatabaseLoadDriverException e) {
						 TreatExceptionForAIbench.treatExcepion(e);
					 };
				 }
			 });
			 jTreeOntology.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent arg0) {
					
				}
				
				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void keyPressed(KeyEvent arg0) {
			        try {
						doMouseClicked();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}					
				}
			});
			 jTreeOntology.setRootVisible(false);
			 jp.setViewportView(jTreeOntology);	
			 jSplitPane1.setLeftComponent(jp);
			 jTreeOntology.setCellRenderer(new JTreeCellRenderer());
			 jpanelTermInfo = new JPanel();
			 GridBagLayout jpanelTermInfoLayout = new GridBagLayout();
			 jSplitPane1.setRightComponent(jpanelTermInfo);
			 jpanelTermInfoLayout.rowWeights = new double[] {0.1};
			 jpanelTermInfoLayout.rowHeights = new int[] {7};
			 jpanelTermInfoLayout.columnWeights = new double[] {0.1};
			 jpanelTermInfoLayout.columnWidths = new int[] {7};
			 jpanelTermInfo.setLayout(jpanelTermInfoLayout);
			 jpanelTermInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Synomyms", TitledBorder.LEADING, TitledBorder.TOP));
			 {
				ListModel jListSynsModel =  new DefaultComboBoxModel( new String[] {});
				jListSyns = new JList();
				jpanelTermInfo.add(jListSyns, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jListSyns.setModel(jListSynsModel);
			 }
		 }

	}
	
	private void doMouseClicked() throws SQLException, DatabaseLoadDriverException {
		TreeNodeOntology<IResourceElement> node = (TreeNodeOntology<IResourceElement>) this.jTreeOntology.getSelectionPath().getLastPathComponent();	
		{
			IResourceElement elem = ((IResourceElement) node.getValue());
			changeSyn(elem);	
		}
	}
	
	private void changeSyn(IResourceElement term) throws SQLException, DatabaseLoadDriverException {	
		String[] synosyms = getSynonyms(term);
		ListModel jListSynsModel =  new DefaultComboBoxModel( synosyms);
		jListSyns.setModel(jListSynsModel);
		jListSyns.updateUI();
		if(ontology.isOntologyFill())
		{
			jButtonUpdateOntology.setEnabled(false);
			jButtonUpdate12.setEnabled(false);
		}
	}

	private String[] getSynonyms(IResourceElement term) throws SQLException, DatabaseLoadDriverException {
		IResourceElementSet<IResourceElement> terms = ontology.getTermSynomns(term.getID());
		String[] synTerm = new String[terms.size()];
		int i=0;
		for(IResourceElement elem: terms.getElements())
		{
			synTerm[i] = elem.getTerm();
			i++;
		}
		return synTerm;
	}

	public void update(Observable arg0, Object arg1) {
		try {
			classes = Resources.getResourceContentClasses(ontology.getID());
			createViewTrees();
			numberTerms = Resources.getnumberTerms(ontology.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			numberSyn = Resources.getNumberSynonyms(ontology.getID());
			jTextFieldSyn.setText(String.valueOf(numberSyn));
			float averageTermsSyn = (float) numberSyn /(float) numberTerms ;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));
			jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
			jTextFieldNAme.setText(ontology.getName());

		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (OntologyMissingRootExeption e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}		
	}
	
	private JTabbedPane getJTabbedPane1() throws SQLException, DatabaseLoadDriverException {
		if(jTabbedPane1 == null) {
			jTabbedPane1 = new JTabbedPane();
			jTabbedPane1.addTab("Operations",getOperationsPane());
			jTabbedPane1.addTab(GlobalTextInfoSmall.statistics,getOntologyStatistics());
		}
		return jTabbedPane1;
	}
	
	private JPanel getOntologyStatistics() throws DatabaseLoadDriverException, SQLException {
		if(jPanelOntoInfo==null)
		{
			jPanelOntoInfo = new JPanel();
			GridBagLayout jPanelDicInfoLayout = new GridBagLayout();
			jPanelOntoInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),GlobalTextInfoSmall.statistics, TitledBorder.LEADING,TitledBorder.TOP));
			jPanelDicInfoLayout.rowWeights = new double[] {0.1};
			jPanelDicInfoLayout.rowHeights = new int[] {7};
			jPanelDicInfoLayout.columnWeights = new double[] {0.05, 0.1};
			jPanelDicInfoLayout.columnWidths = new int[] {7, 7};
			jPanelOntoInfo.setLayout(jPanelDicInfoLayout);
			jPanelOntoInfo.add(getJPanelOntoStats(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelOntoInfo;
	}
	
	private JPanel getJPanelOntoStats() throws DatabaseLoadDriverException, SQLException {
		if(jPanelOntoStatsInfo == null) {
			jPanelOntoStatsInfo = new JPanel();
			GridBagLayout jPanelDicStatsLayout = new GridBagLayout();
			jPanelDicStatsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelDicStatsLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelDicStatsLayout.columnWeights = new double[] {0.0, 0.05};
			jPanelDicStatsLayout.columnWidths = new int[] {7, 7};
			jPanelOntoStatsInfo.setLayout(jPanelDicStatsLayout);
			jPanelOntoStatsInfo.add(getJLabelTerms(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJLabelSyn(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJTextFieldTerms(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJTextFieldSyn(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJLabelAveregeSynForTerm(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJTextFieldAverageTermsForSyn(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJLabelClassesContent(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelOntoStatsInfo.add(getJTextFieldNumberOfClasses(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelOntoStatsInfo;
	}
	
	private JLabel getJLabelTerms() {
		if(jLabelTerms == null) {
			jLabelTerms = new JLabel();
			jLabelTerms.setText("Terms");
		}
		return jLabelTerms;
	}
	
	private JLabel getJLabelSyn() {
		if(jLabelSyn == null) {
			jLabelSyn = new JLabel();
			jLabelSyn.setText("Synonyms");
		}
		return jLabelSyn;
	}
	
	private JTextField getJTextFieldTerms() throws SQLException, DatabaseLoadDriverException {
		if(jTextFieldTerms == null) {
			jTextFieldTerms = new JTextField();
			jTextFieldTerms.setMinimumSize(new Dimension(60,25));
			numberTerms = Resources.getnumberTerms(ontology.getID());
			jTextFieldTerms.setText(String.valueOf(numberTerms));
			jTextFieldTerms.setEditable(false);
		}
		return jTextFieldTerms;
	}
	
	private JTextField getJTextFieldSyn() throws DatabaseLoadDriverException, SQLException {
		if(jTextFieldSyn == null) {
			jTextFieldSyn = new JTextField();
			jTextFieldSyn.setMinimumSize(new Dimension(60,25));
			jTextFieldSyn.setEditable(false);
			numberSyn = Resources.getNumberSynonyms(ontology.getID());
			jTextFieldSyn.setText(String.valueOf(numberSyn));
		}
		return jTextFieldSyn;
	}
	
	private JLabel getJLabelAveregeSynForTerm() {
		if(jLabelAveregeSynForTerm == null) {
			jLabelAveregeSynForTerm = new JLabel();
			jLabelAveregeSynForTerm.setText("Average Term/Syn");
		}
		return jLabelAveregeSynForTerm;
	}
	
	private JTextField getJTextFieldAverageTermsForSyn() {
		if(jTextFieldAverageTermsForSyn == null) {
			jTextFieldAverageTermsForSyn = new JTextField();
			jTextFieldAverageTermsForSyn.setMinimumSize(new Dimension(60,25));
			jTextFieldAverageTermsForSyn.setEditable(false);
			float averageTermsSyn = (float) numberSyn /(float) numberTerms ;
			jTextFieldAverageTermsForSyn.setText(dec.format(averageTermsSyn));	
		}
		return jTextFieldAverageTermsForSyn;
	}
	
	private JLabel getJLabelClassesContent() {
		if(jLabelClassesContent == null) {
			jLabelClassesContent = new JLabel();
			jLabelClassesContent.setText("Number of classes :");
		}
		return jLabelClassesContent;
	}
	
	private JTextField getJTextFieldNumberOfClasses() {
		if(jTextFieldNumberOfClasses == null) {
			jTextFieldNumberOfClasses = new JTextField();
			jTextFieldNumberOfClasses.setMinimumSize(new Dimension(60,25));
			jTextFieldNumberOfClasses.setEditable(false);
			jTextFieldNumberOfClasses.setText(String.valueOf(classes.size()));
		}
		return jTextFieldNumberOfClasses;
	}
}
