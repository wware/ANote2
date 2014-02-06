package pt.uminho.anote2.aibench.resources.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeModelOntology;
import pt.uminho.anote2.aibench.resources.datastructures.tree.TreeNodeOntology;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.JTreeCellRenderer;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;


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
public class OntologyView extends JPanel implements Observer{
	
	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPane1;
	private JPanel jPanelOperations;
	private JButton jButtonUpdateOntology;
	private JList jListSyns;
	private JButton jButtonUpdate12;
	private JTextArea jTextAreaID;
	private JLabel jLabelID;
	private JTree jTreeOntology;
	private JTextArea jTextPaneNotes;
	private JLabel jLabelNotes;
	private JTextArea jTextAreaNAme;
	private JLabel jLabelNAme;
	private JPanel jPanelHeader;
	private JButton jButtonHelp;
	private JPanel SelectGOandRelationSet;
	private JPanel auxiliarPanel;
	private JScrollPane jp;
	private OntologyAibench ontology;
	private JPanel jpanelTermInfo;

	public OntologyView(OntologyAibench ontology) throws SQLException{
		this.ontology=ontology;//.getRelationCorpus().getAnnotations().getOntologies().getOntology();
		this.ontology.addObserver(this);
		initGUI();	
		createViewTrees();
	}
	
	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.15, 0.65, 0.0};
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
						jPanelHeader.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Ontology Information", TitledBorder.LEADING, TitledBorder.TOP));
						GridBagLayout jPanelHeaderLayout = new GridBagLayout();
						auxiliarPanel.add(jPanelHeader, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelHeaderLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
						jPanelHeaderLayout.rowHeights = new int[] {7, 7, 7};
						jPanelHeaderLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
						jPanelHeaderLayout.columnWidths = new int[] {20, 7, 20, 7, 20, 20};
						jPanelHeader.setLayout(jPanelHeaderLayout);
						{
							jLabelNAme = new JLabel();
							jPanelHeader.add(jLabelNAme, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelNAme.setText("Name:");
						}
						{
							jTextAreaNAme = new JTextArea();
							jTextAreaNAme.setText(ontology.getName());
							jTextAreaNAme.setEditable(false);
							jPanelHeader.add(jTextAreaNAme, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						}
						{
							jLabelNotes = new JLabel();
							jPanelHeader.add(jLabelNotes, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelNotes.setText("Notes:");
						}
						{
							jTextPaneNotes = new JTextArea();
							jTextPaneNotes.setEditable(false);
							jTextPaneNotes.setText(ontology.getInfo());
							jPanelHeader.add(jTextPaneNotes, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						}
						{
							jLabelID = new JLabel();
							jPanelHeader.add(jLabelID, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jLabelID.setText("ID:");
						}
						{
							jTextAreaID = new JTextArea();
							jTextAreaID.setText(String.valueOf(ontology.getId()));
							jTextAreaID.setEditable(false);
							jPanelHeader.add(jTextAreaID, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
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
						jPanelOperations = new JPanel();
						jPanelOperations.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Ontology Operations", TitledBorder.LEADING, TitledBorder.TOP));
						GridBagLayout jPanelOperationsLayout = new GridBagLayout();
						SelectGOandRelationSet.add(jPanelOperations, new GridBagConstraints(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelOperationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
						jPanelOperationsLayout.rowHeights = new int[] {7, 7, 7};
						jPanelOperationsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
						jPanelOperationsLayout.columnWidths = new int[] {7, 7, 7, 7};
						jPanelOperations.setLayout(jPanelOperationsLayout);
						{
							jButtonHelp = new JButton();
							jPanelOperations.add(jButtonHelp, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
							jButtonHelp.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e){
									Help.internetAcess(GlobalOptions.wikiGeneralLink+"Ontology_Update");
								}
							});
						}
						{
							jButtonUpdateOntology = new JButton();
							jPanelOperations.add(jButtonUpdateOntology, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jButtonUpdateOntology.setText("Update (OBO 1.0) Files");
							jButtonUpdateOntology.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh22.png")));
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
							jPanelOperations.add(jButtonUpdate12, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
							jButtonUpdate12.setText("Update (OBO 1.2) Files");
							jButtonUpdate12.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Refresh22.png")));
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	private JPopupMenu getPopupForTreeOntology(){
//		JPopupMenu menu = new JPopupMenu("Options");
//			
//		JMenuItem item = new JMenuItem("View Synonyms");
//		item.setBackground(Color.decode("#F0F0F0"));
//		item.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent evt) {
//				
//			}
//		});
//		menu.add(item);			
//		menu.addSeparator();
//		JMenuItem item2 = new JMenuItem("View External ID");
//		item2.setBackground(Color.decode("#F0F0F0"));
//		item2.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent evt) {
//				
//			}
//		});
//		menu.add(item);			
//		menu.addSeparator();
//		return menu;		
//	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createViewTrees() throws SQLException
	{		
		 if(ontology.isOntologyFill())
		 {
			 jTreeOntology = new JTree(new TreeModelOntology(ontology.getOntologyTree()));
			 jTreeOntology.addMouseListener(new MouseAdapter() {
			      public void mouseClicked(MouseEvent me) {
			        doMouseClicked(me);
			      }
			    });
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
	
	private void doMouseClicked(MouseEvent me) {
		TreeNodeOntology<IResourceElement> node = (TreeNodeOntology<IResourceElement>) this.jTreeOntology.getSelectionPath().getLastPathComponent();	
		{
			IResourceElement elem = ((IResourceElement) node.getValue());
			String term = String.valueOf(elem.getID());
			changeSyn(elem);	
		}
	}
	
	private void changeSyn(IResourceElement term) {	
		String[] synosyms = getSynonyms(term);
		ListModel jListSynsModel =  new DefaultComboBoxModel( synosyms);
		jListSyns.setModel(jListSynsModel);
		jListSyns.updateUI();
	}

	private String[] getSynonyms(IResourceElement term) {
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
			createViewTrees();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}
