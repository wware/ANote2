package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.NERProcess;
import pt.uminho.anote2.aibench.corpus.gui.EntitieStatisticsView;
import pt.uminho.anote2.aibench.corpus.others.EntityNERStats;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.MyTreeCellRenderer;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.document.IAnnotatedDocument;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.Utils;



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
 * @author Hugo Costa,Rafael Carreira
 * 
 * Objectivo : Visualizar estatisticas das anotações presentes no corpus
 *
 */
public class NEREntityStatisticsView extends JPanel implements Observer{

	private static final long serialVersionUID = 6866512520510402222L;
	private JLabel totalLabel;
	private JLabel ndocsLabel;
	private JTextField totalTextField;
	private JTextField ndocsTextField;
	private JButton jButtonHelp;
	private JButton jButtonRefresh;
	private JPanel termAnnotationPanel;
	private JRadioButton termAnnotationRadioButton;
	private JRadioButton docAnnotationRadioButton;
	private JPanel entitiesPanel;
	private JList top10List;
	private JScrollPane top10ScrollPane;
	private JPanel corpusStatPanel;
	private JPanel entStatsPanel;
	private JSplitPane jSplitPane1;
	private JScrollPane docFreqScrollPane;
	private JScrollPane termAnnotationScrollPane;
	private JPanel downPanel;
	private JScrollPane entStatsScrollPane;

	private NerStatitics relStatitcsNer;
	private Map<String,Stats> doc_stats; // class -> (doc,frequency on doc)
	private Map<String,Double> corpus_stats; // class -> frequency on corpus
	private Map<String,ArrayList<GenericPair<String,String>>> term_stats; // class -> (term, frequency on corpus)
	private String[] top10_terms;
	private DecimalFormat round = new DecimalFormat("0.00");	
	private NERProcess nerProcess;
	private Map<Integer,String> classes;
	private Map<String,Integer> classesClassesID;

	@SuppressWarnings("unchecked")
	public NEREntityStatisticsView(NERProcess process) throws IOException, SQLException
	{
		this.doc_stats=new HashMap<String, Stats>();
		this.nerProcess=process;
		this.corpus_stats = new HashMap<String, Double>();
		this.term_stats = new HashMap<String,ArrayList<GenericPair<String,String>>>();
		this.top10_terms = new String[10];
		classes = ResourcesHelp.getClassIDClassOnDatabase(process.getDB());
		classesClassesID = (Map<String, Integer>) Utils.swapHashElements(classes);
		calcStatistics();
		convertData();
		initGUI();
		termAnnotationScrollPane.setVisible(false);
	}
	
	private void initGUI() throws IOException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(655, 172));
			thisLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {100, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				corpusStatPanel = new JPanel();
				GridBagLayout corpusStatPanelLayout = new GridBagLayout();
				corpusStatPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
				corpusStatPanelLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
				corpusStatPanelLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
				corpusStatPanelLayout.columnWidths = new int[] {100, 7, 7};
				corpusStatPanel.setLayout(corpusStatPanelLayout);
				{
					totalLabel = new JLabel();
					corpusStatPanel.add(totalLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 2, 0), 0, 0));
					totalLabel.setText("Total Corpus Annotations:");
				}
				{
					totalTextField = new JTextField();
					corpusStatPanel.add(totalTextField, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 0, 0), 0, 0));
					
					totalTextField.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
							null));
					totalTextField.setEditable(false);
					totalTextField.setBackground(Color.white);
					changesTotal();
				}
				{
					ndocsLabel = new JLabel();
					corpusStatPanel.add(ndocsLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 2, 0), 0, 0));
					ndocsLabel.setText("Documents:");
				}
				{
					ndocsTextField = new JTextField();
					corpusStatPanel.add(ndocsTextField, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 0, 0), 0, 0));
					ndocsTextField.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
							null));
					ndocsTextField.setEditable(false);
					ndocsTextField.setBackground(Color.white);
					changesNdocs();
				}
				{
					entStatsScrollPane = new JScrollPane();
					corpusStatPanel.add(entStatsScrollPane, new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 0), 0, 0));
					entStatsScrollPane.setEnabled(false);
					entStatsScrollPane.setBorder(BorderFactory.createTitledBorder("Entities Corpus Statistics"));
					{
						entStatsPanel = new JPanel();
						GridBagLayout entStatsPanelLayout = new GridBagLayout();
						entStatsScrollPane.setViewportView(entStatsPanel);
						entStatsPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
						entStatsPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
						entStatsPanelLayout.columnWeights = new double[] {0.0, 0.1};
						entStatsPanelLayout.columnWidths = new int[] {70, 7};
						entStatsPanel.setLayout(entStatsPanelLayout);
					}
				}
				{
					top10ScrollPane = new JScrollPane();
					corpusStatPanel.add(top10ScrollPane, new GridBagConstraints(2, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 0, 5), 0, 0));
					top10ScrollPane.setBorder(BorderFactory.createTitledBorder("Top 10 Terms"));
					{
						fillTop10terms();
						top10List.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					}
				}
				{
					jButtonRefresh = new JButton();
					corpusStatPanel.add(jButtonRefresh, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonRefresh.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/exec16.png")));
					jButtonRefresh.setText("Refresh");
					jButtonRefresh.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0) {
							update();							
						}
					});
				}
				{
					jButtonHelp = new JButton();
					corpusStatPanel.add(jButtonHelp, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
					jButtonHelp.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							Help.internetAcess(GlobalOptions.wikiGeneralLink+"Process_Entity_Details_View");
						}
					});
				}

			}
			{
				jSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				jSplitPane1.setOneTouchExpandable(true);
				jSplitPane1.setResizeWeight(0.25);
				this.add(jSplitPane1, new GridBagConstraints(0, 0, 4, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					downPanel = new JPanel();
					GridBagLayout downPanelLayout = new GridBagLayout();
					downPanelLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
					downPanelLayout.rowHeights = new int[] {20, 7, 7, 7};
					downPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					downPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
					downPanel.setLayout(downPanelLayout);
					{
						docAnnotationRadioButton = new JRadioButton();
						downPanel.add(docAnnotationRadioButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
						docAnnotationRadioButton.setText("Document Frequency");
						docAnnotationRadioButton.setSelected(true);
						docAnnotationRadioButton.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
								changePannel();							
							}
						});
					}
					{
						termAnnotationRadioButton = new JRadioButton();
						downPanel.add(termAnnotationRadioButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 0, 0), 0, 0));
						termAnnotationRadioButton.setText("Term Annotation");
						termAnnotationRadioButton.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
								changePannel();							
							}
						});
					}
					{
						ButtonGroup group = new ButtonGroup();
						group.add(termAnnotationRadioButton);
						group.add(docAnnotationRadioButton);
					}
					{
						docFreqScrollPane = new JScrollPane();
						termAnnotationScrollPane = new JScrollPane();
					}
					{
						downPanel.add(docFreqScrollPane, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						downPanel.add(termAnnotationScrollPane, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						getEntitiePanel();
						docFreqScrollPane.setViewportView(entitiesPanel);
					}
					{
						getTermPanel();
						termAnnotationScrollPane.setViewportView(termAnnotationPanel);
					}
					constructTrees();
				}
				this.jSplitPane1.setBottomComponent(downPanel);
				this.jSplitPane1.setTopComponent(corpusStatPanel);
			
			}
		}	
	}

	private void getTermPanel() {
		termAnnotationPanel = new JPanel();
		termAnnotationPanel.setBorder(BorderFactory.createTitledBorder("Terms Statistics"));
		GridBagLayout termAnnotationPanelLayout = new GridBagLayout();
		termAnnotationPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		termAnnotationPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
		termAnnotationPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		termAnnotationPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		termAnnotationPanel.setLayout(termAnnotationPanelLayout);
	}

	private void getEntitiePanel() {
		entitiesPanel = new JPanel();
		entitiesPanel.setBorder(BorderFactory.createTitledBorder("Corpus Statistics"));
		GridBagLayout entitiesPanelLayout = new GridBagLayout();
		entitiesPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		entitiesPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
		entitiesPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		entitiesPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		entitiesPanel.setLayout(entitiesPanelLayout);
	}

	private void fillTop10terms() {
		ListModel top10ListModel = 
			new DefaultComboBoxModel(top10_terms);
		top10List = new JList();
		top10ScrollPane.setViewportView(top10List);
		top10List.setModel(top10ListModel);
	}

	private DefaultTreeModel getTreeModel(String cls){
				
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(cls);
		for(GenericPair<String, Double> stat: this.doc_stats.get(cls).getStats())
			node.add(new DefaultMutableTreeNode(stat.getX() + " (" + round.format(stat.getY()) + ")"));
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}	
	
	private DefaultTreeModel getTreeModel2(String cls){
		
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(cls);
		for(GenericPair<String, String> stat: this.term_stats.get(cls))
		{
			node.add(new DefaultMutableTreeNode(stat.getX() + " (" + stat.getY() + ")"));
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}	
	
	private void calcStatistics() throws IOException{
		relStatitcsNer = new NerStatitics(nerProcess.getAllProcessDocs());
		Map<Integer,ArrayList<String>> hashEntities = new TreeMap<Integer, ArrayList<String>>();
		Map<Integer, ArrayList<String>> nerHash = this.relStatitcsNer.getHashClassNER();
		Map<String, EntityNERStats> entHash = this.relStatitcsNer.getHashEntitiesNER();
		Iterator<Integer> itclasseID = nerHash.keySet().iterator();
		while(itclasseID.hasNext())
		{
			int ocorrencesNumber=0;
			Integer classeID = itclasseID.next();
			String classe = classes.get(classeID);
			ArrayList<String> entities = nerHash.get(classeID);
			for(int i=0;i<entities.size();i++)
			{
				String entity = entities.get(i);
				int entityOcurrences = entHash.get(entity).getOccurrencesNumber();
				ocorrencesNumber+=entityOcurrences;	
				// Na hash dos termos
				if(this.term_stats.containsKey(classe))
				{
					GenericPair<String, String> pair = new GenericPair<String, String>(entity,String.valueOf(entityOcurrences));
					term_stats.get(classe).add(pair);
				}
				else
				{
					ArrayList<GenericPair<String,String>> arrayGen = new ArrayList<GenericPair<String,String>>();
					GenericPair<String, String> pair = new GenericPair<String, String>(entity,String.valueOf(entityOcurrences));
					arrayGen.add(pair);
					term_stats.put(classe,arrayGen);
				}			
				// Na Hash colocar aki a entidade
				// para o top twn de entidades
				if(hashEntities.containsKey(entityOcurrences))
				{
					hashEntities.get(entityOcurrences).add(entity);
				}
				else
				{
					ArrayList<String> newArray = new ArrayList<String>();
					newArray.add(entity);
					hashEntities.put(entityOcurrences,newArray);
				}
				
			}
			corpus_stats.put(classe,(double)ocorrencesNumber/(double) nerProcess.getAllProcessDocs().size());
		}
		// Colocar o top 10
		int i=0;
		Iterator<Integer> itNumber = hashEntities.keySet().iterator();
		ArrayList<Integer> array = new ArrayList<Integer>(hashEntities.size());
		while(itNumber.hasNext())
		{
			array.add(itNumber.next());
		}
		
		for(int k=array.size()-1;k>=0;k--)
		{
			Integer number = array.get(k);
			ArrayList<String> arrayStrings = hashEntities.get(number);
			for(int j=0;j<arrayStrings.size()&&i<10;j++)
			{
				top10_terms[i]=arrayStrings.get(j)+" ("+round.format((double)number/(double) nerProcess.getAllProcessDocs().size())+")";
				i++;
			}
		}
	}

	private void changePannel(){
		boolean flag = false;	
		if(docAnnotationRadioButton.isSelected())
			flag = true;
		
		docFreqScrollPane.setEnabled(flag);
		docFreqScrollPane.setVisible(flag);
		termAnnotationScrollPane.setEnabled(!flag);
		termAnnotationScrollPane.setVisible(!flag);
		downPanel.updateUI();
			
	}
	
	private void constructTrees()
	{
		{
			int i=0;
			
			for(String cls: this.corpus_stats.keySet())
			{
				JScrollPane scrollpane = new JScrollPane();
				scrollpane.setPreferredSize(new Dimension(40,200));
				entitiesPanel.add(scrollpane, new GridBagConstraints(i%4, i/4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					JTree tree = new JTree();
					tree.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
							null));
					scrollpane.setViewportView(tree);
					String color = "#FF00FF";
					try {
						color = Corpora.getColorForClass(classesClassesID.get(cls), nerProcess.getDB());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//this.nerBox.getPublicationSet().getOwner().getProjectPreferences().getTask().getProperties().get(cls).getColor();
					scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), cls, TitledBorder.LEADING, TitledBorder.TOP, null, Color.decode(color)));
					tree.setModel(this.getTreeModel(cls));
					((DefaultTreeCellRenderer) tree.getCellRenderer()).setFont(new Font("Arial", Font.PLAIN, 12));
					((DefaultTreeCellRenderer) tree.getCellRenderer()).setIcon(null);
					
					tree.setCellRenderer(new MyTreeCellRenderer());
					
					tree.addTreeSelectionListener(new TreeSelectionListener(){

						public void valueChanged(TreeSelectionEvent arg0) {
							
							String node = arg0.getPath().getLastPathComponent().toString();
							
							if(node.contains("("))
							{
//								// Perguntar ao rafa a utilidade
//								int pmid = Integer.decode(node.substring(0,node.indexOf("(")-1));
//								
//								JOptionPane option_pane = new JOptionPane("Open document \"" + pmid + "\" ?");
//								Object[] options = new String[]{"Yes", "No"};
//								
//								option_pane.setOptions(options);
//								option_pane.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_question.png")));
//								
//								JDialog dialog = option_pane.createDialog(Workbench.getInstance().getMainFrame(), "@Note interrogation");
//								dialog.setVisible(true);
//								
//								Object choice = option_pane.getValue();
//											
//								if(choice.equals("Yes"))
//								{
//									//returnDoc(pmid);
//								}
							}
							else // class
							{
								TreePath[] p = arg0.getPaths();
								String entity = p[0].toString();
								entity = entity.substring(1,entity.length()-1); // remove "[]"
								new EntitieStatisticsView(entity,term_stats.get(entity));
							}
						}
						
					});
					
										
					JLabel label = new JLabel(cls + ":");
					entStatsPanel.add(label, new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					// Mudar formato
					JTextField field = new JTextField(round.format(this.corpus_stats.get(cls)));
					field.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
							null));
					field.setEditable(false);
					field.setBackground(Color.white);
					entStatsPanel.add(field, new GridBagConstraints(1, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
				i++;
			}
			
			i=0;
			
			for(String cls: this.term_stats.keySet())
			{
				JScrollPane scrollpane = new JScrollPane();
				scrollpane.setPreferredSize(new Dimension(40,200));
				termAnnotationPanel.add(scrollpane, new GridBagConstraints(i%4, i/4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					JTree tree = new JTree();
					tree.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
							null));
					scrollpane.setViewportView(tree);
					String color = "#FF00FF";
					try {
						color = Corpora.getColorForClass(classesClassesID.get(cls), nerProcess.getDB());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), cls, TitledBorder.LEADING, TitledBorder.TOP, null, Color.decode(color)));
					tree.setModel(this.getTreeModel2(cls));
					((DefaultTreeCellRenderer) tree.getCellRenderer()).setFont(new Font("Arial", Font.PLAIN, 12));
					((DefaultTreeCellRenderer) tree.getCellRenderer()).setIcon(null);
					
					tree.setCellRenderer(new MyTreeCellRenderer());
				}
				i++;
			}	
		}
	}


	private void changesNdocs() {
		ndocsTextField.setText(String.valueOf(nerProcess.getAllProcessDocs().size()));
	}


	private void changesTotal() {
		totalTextField.setText(String.valueOf(this.relStatitcsNer.getNerAnnotations()));
	}

	private void convertData()
	{
		Map<Integer, HashMap<String,Integer>> docStats = relStatitcsNer.getDocStats();
		Iterator<Integer> it = docStats.keySet().iterator();
		while(it.hasNext())
		{
			int classID = it.next();
			String classe = classes.get(classID);
			HashMap<String,Integer> pmids = docStats.get(classID);
			Iterator<String> itpmids = pmids.keySet().iterator();
			Stats stats = new Stats();
			while(itpmids.hasNext())
			{
				String pmid = itpmids.next();
				Integer freq = pmids.get(pmid);
				stats.addStat(pmid, freq);
			}
			this.doc_stats.put(classe, stats);
		}
	}

	
	public static class NerStatitics implements Serializable
	{	
		private static final long serialVersionUID = -6033591072760041316L;
		protected int nerAnnotations;
		protected Map<String,EntityNERStats> hashEntitiesNER;
		protected Map<Integer,ArrayList<String>> hashClassNER;	
		private Map<Integer, HashMap<String,Integer>> docStats;
		protected Map<Integer,IAnnotatedDocument> docsAnnot;
		
		public NerStatitics(Map<Integer,IAnnotatedDocument> docsAnnot)
		{
			this.docsAnnot=docsAnnot;
			this.nerAnnotations=0;
			this.hashClassNER=new HashMap<Integer,ArrayList<String>>();
			this.hashEntitiesNER=new HashMap<String, EntityNERStats>();
			this.docStats=new HashMap<Integer, HashMap<String,Integer>>();
			getnerstatitics();
		}

		public Map<String, EntityNERStats> getHashEntitiesNER() {
			return hashEntitiesNER;
		}

		public Map<Integer, ArrayList<String>> getHashClassNER() {
			return hashClassNER;
		}

		public void addEntity(String entity,Integer classeID,String id)
		{
			if(this.hashEntitiesNER.containsKey(entity))
			{
				this.hashEntitiesNER.get(entity).add();
			}
			else
			{
				
				if(this.hashClassNER.containsKey(classeID))
				{
					this.hashClassNER.get(classeID).add(entity);
				}
				else
				{
					ArrayList<String> arrayGen = new ArrayList<String>();
					arrayGen.add(entity);
					this.hashClassNER.put(classeID,arrayGen);
				}		
				EntityNERStats stats = new EntityNERStats();
				stats.add();
				this.hashEntitiesNER.put(entity, stats);
			}
			nerAnnotations++;
			
			if(docStats.containsKey(classeID))
			{
				HashMap<String,Integer> aux = docStats.get(classeID);
				if(aux.containsKey(id))
				{
					int freq = aux.get(id);
					aux.put(id, ++freq);
				}
				else
				{
					aux.put(id,1);
				}
			}
			else
			{
				HashMap<String,Integer> aux2 = new HashMap<String, Integer>();
				aux2.put(id, 1);
				docStats.put(classeID,aux2);
			}
			
		}
		/**
		 * Method that gerate a ner statitics
		 * 
		 * @param relationCorpus
		 */
		private void getnerstatitics()
		{
			Iterator<Integer> itPmids = docsAnnot.keySet().iterator();
			IAnnotatedDocument doc;
			while(itPmids.hasNext())
			{
				Integer id = itPmids.next();
				doc = docsAnnot.get(id);
				for(IEntityAnnotation entAnnot:doc.getEntitiesAnnotations())
				{
					addEntity(entAnnot.getAnnotationValue(),entAnnot.getClassAnnotationID(),String.valueOf(id));
				}
 			}
		}		

		public int getNerAnnotations() {
			return nerAnnotations;
		}

		public Map<Integer, HashMap<String, Integer>> getDocStats() {
			return docStats;
		}
	}

	class Stats{
		
		private ArrayList<GenericPair<String,Double>> stats;
		
		public Stats(ArrayList<GenericPair<String, Double>> stats) {
			this.stats = stats;
		}
		
		public Stats(){
			this.stats = new ArrayList<GenericPair<String,Double>>();
		}
			
		public ArrayList<GenericPair<String, Double>> getStats() {
			return stats;
		}
		
		public void addStat(String pmid, double count){
			this.stats.add(new GenericPair<String, Double>(pmid,count));
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		update();
	}

	@SuppressWarnings("unchecked")
	private void update() {
		this.doc_stats=new HashMap<String, Stats>();
		this.corpus_stats = new HashMap<String, Double>();
		this.term_stats = new HashMap<String,ArrayList<GenericPair<String,String>>>();
		this.top10_terms = new String[10];
		try {
			classes = ResourcesHelp.getClassIDClassOnDatabase(getNerProcess().getDB());
			classesClassesID = (Map<String, Integer>) Utils.swapHashElements(classes);
			calcStatistics();
		} catch (IOException e) {
		} catch (SQLException e) {
		}
		convertData();
		fillTop10terms();
		getEntitiePanel();
		getTermPanel();
		constructTrees();
		entitiesPanel.updateUI();
		termAnnotationPanel.updateUI();
	}
	
	public NERProcess getNerProcess() {
		return nerProcess;
	}
	
}
