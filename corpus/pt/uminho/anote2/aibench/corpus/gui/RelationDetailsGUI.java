package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.aibench.corpus.databasemanagement.CorporaManagement;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.REProcess;
import pt.uminho.anote2.aibench.corpus.structures.AnnotatedDocumentProperties;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.JTreeCellRenderer;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.textprocessing.Tokenizer;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.process.IE.IIEProcess;
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
public class RelationDetailsGUI extends JDialog{

	private static final long serialVersionUID = -664381864632621746L;

	private JTextField jTextVerb;
	private JEditorPane jEditorPaneSentence;
	private JTextField jTextLemmaVerb;
	private JScrollPane jScrollRightEntities;
	private JTextField jTextFieldDirectionaly;
	private JTextField jTextFieldPolarity;
	private JLabel jLabelDirecyionaly;
	private JLabel jLabelPolaridade;
	private JButton jButtonHelp;
	private JPanel jPanelRightEntities;
	private JPanel jPanelLeftEntities;
	private JScrollPane jScrollLeftEntities;
	private JScrollPane jScrollSentence;
	private JButton jButtonCancel;
	private JLabel jLabelLemmaVerb;
	private JLabel jLabelVerb;
	private JPanel downPanel;
	private JTree tree1;
	private JTree tree2;
	private IEventAnnotation event;
	private AnnotatedDocumentProperties properties;
	private String text;
	private IIEProcess process;
	private SortedMap<Long, IAnnotation> annotationSorted;
	private static int min = 0;
	private int max = 0;
	private int intervalstep = 50;
	private int maxTotal = 0;
	private JButton jButtonDown;
	private JButton jButtonUp;
	private int sentenceLimitInf = 0;
	private int sentenceLimitSup = 0;
	
	public RelationDetailsGUI(IEventAnnotation event,Map<Integer, String> classIDclass,IIEProcess process){
		super(Workbench.getInstance().getMainFrame());
		this.event=event;
		text = null;
		this.process=process;
		initGUI();
		initDownPanel();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void initGUI() {
		try {
			
			GridBagLayout downPanelLayout = new GridBagLayout();
				downPanelLayout.rowWeights = new double[] {0.05, 0.08, 0.05, 0.08, 0.1, 0.15, 0.05, 0.4, 0.1};
				downPanelLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7};
				downPanelLayout.columnWeights = new double[] {0.05, 0.08, 0.08, 0.0, 0.0, 0.0, 0.08, 0.08, 0.05};
				downPanelLayout.columnWidths = new int[] {7, 7, 7, 35, 140, 40, 7, 7, 7};			
				{				
					downPanel = new JPanel();
					downPanel.setLayout(downPanelLayout);
					this.add(downPanel);			
				}
				{
					jTextVerb = new JTextField();
					downPanel.add(jTextVerb, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jTextVerb.setEditable(false);
					jTextVerb.setBackground(Color.WHITE);
				}
				{
					jTextLemmaVerb = new JTextField();
					jTextLemmaVerb.setEditable(false);
					jTextLemmaVerb.setBackground(Color.WHITE);
					downPanel.add(jTextLemmaVerb, new GridBagConstraints(6, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
				{
					jLabelVerb = new JLabel();
					downPanel.add(jLabelVerb, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelVerb.setText("Clue");
				}
				{
					jLabelLemmaVerb = new JLabel();
					downPanel.add(jLabelLemmaVerb, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelLemmaVerb.setText("Lemma (Clue)");
				}
				{
					jButtonCancel = new JButton();
					downPanel.add(jButtonCancel, new GridBagConstraints(8, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonCancel.setText("Cancel");
					jButtonCancel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
					jButtonCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							closeButtonActionPerformed(evt);
						}
					});
				}
				{
					jScrollSentence = new JScrollPane();
					downPanel.add(jScrollSentence, new GridBagConstraints(1, 7, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jEditorPaneSentence = new JEditorPane();
						jScrollSentence.setViewportView(jEditorPaneSentence);
						jEditorPaneSentence.setEditable(false);
						jEditorPaneSentence.setBackground(Color.WHITE);
					}
				}
				{
					jScrollLeftEntities = new JScrollPane();
					downPanel.add(jScrollLeftEntities, new GridBagConstraints(0, 1, 3, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jPanelLeftEntities = new JPanel();
						jScrollLeftEntities.setViewportView(jPanelLeftEntities);
					}
					{
						tree1 = new JTree();
						tree1.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
								null));
						jScrollLeftEntities.setViewportView(tree1);
						jScrollLeftEntities.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Entities At Left", TitledBorder.LEADING, TitledBorder.TOP, null));
						
					}
				}
				{
					jScrollRightEntities = new JScrollPane();
					downPanel.add(jScrollRightEntities, new GridBagConstraints(6, 1, 3, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					{
						jPanelRightEntities = new JPanel();
						jScrollRightEntities.setViewportView(jPanelRightEntities);
					}
					{
						tree2 = new JTree();
						tree2.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
								null));
						jScrollRightEntities.setViewportView(tree2);
						jScrollRightEntities.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Entities At Right", TitledBorder.LEADING, TitledBorder.TOP, null));
						
					}
				}
				{
					jButtonHelp = new JButton();
					downPanel.add(jButtonHelp, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
					jButtonHelp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							Help.internetAcess(GlobalOptions.wikiGeneralLink+"Relations_Details_GUI");
						}
					});
					jButtonHelp.setEnabled(true);
					jButtonHelp.setVisible(true);
				}
				{
					jLabelPolaridade = new JLabel();
					downPanel.add(jLabelPolaridade, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelPolaridade.setText("Polarity");
				}
				{
					jLabelDirecyionaly = new JLabel();
					downPanel.add(jLabelDirecyionaly, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jLabelDirecyionaly.setText("Directionaly");
				}
				{
					jTextFieldPolarity = new JTextField();
					downPanel.add(jTextFieldPolarity, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldPolarity.setEditable(false);
					jTextFieldPolarity.setBackground(Color.WHITE);
					int polarity = -5;
					if(event.getEventProperties().getPolarity()!=null)
						polarity = event.getEventProperties().getPolarity();
					if(polarity==1)
					{
						jTextFieldPolarity.setText("Positive (+)");
					}
					else if(polarity==-1)
					{
						jTextFieldPolarity.setText("Negative (-)");
					}
					else if(polarity==0)
					{
						jTextFieldPolarity.setText("Condicional (?)");
					}
					else
					{
						jTextFieldPolarity.setText("Unknown (?)");
					}
				}
				{
					jTextFieldDirectionaly = new JTextField();
					downPanel.add(jTextFieldDirectionaly, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jTextFieldDirectionaly.setEditable(false);
					jTextFieldDirectionaly.setBackground(Color.WHITE);				
					if(event.getEventProperties().isDirectionally()!=null)
					{
						if(event.getEventProperties().isDirectionally())
						{
							jTextFieldDirectionaly.setText("Right -> Left");
						}
						else
						{
							jTextFieldDirectionaly.setText("Left -> Right");

						}
					}
					else
					{
						jTextFieldDirectionaly.setText("Unknown");
					}
				}
				{
					jButtonUp = new JButton();
					downPanel.add(jButtonUp, new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonUp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag+.png")));
					jButtonUp.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag+Pressed.png")));
					jButtonUp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							moreText(true);
						}
					});

				}
				{
					jButtonDown = new JButton();
					downPanel.add(jButtonDown, new GridBagConstraints(5, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonDown.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag-.png")));
					jButtonDown.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag-Pressed.png")));
					jButtonDown.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							moreText(false);
						}
					});
					
				}
				this.setSize(1024, 680);
				this.setVisible(true);
				Utilities.centerOnOwner(this);				
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void moreText(boolean b) {
		if(b)
		{
			sentenceLimitInf = sentenceLimitInf - intervalstep;
			if(sentenceLimitInf<0)
				sentenceLimitInf = 0;
			sentenceLimitSup = sentenceLimitSup + intervalstep;
			if(sentenceLimitSup>this.maxTotal)
				sentenceLimitSup = maxTotal;	
		}
		else
		{
			sentenceLimitInf = sentenceLimitInf + intervalstep;
			if(sentenceLimitInf>min)
				sentenceLimitInf = min;
			sentenceLimitSup = sentenceLimitSup - intervalstep;
			if(sentenceLimitSup<max)
				sentenceLimitSup = max;	
		}
		jEditorPaneSentence.setText(convertSentenceInHTMLFormat());	
		jEditorPaneSentence.updateUI();
	}
	
	private void initDownPanel()
	{
		String lemma = "";
		if(event.getEventProperties().getLemma()!=null)
			lemma = event.getEventProperties().getLemma();
		String eventClue = "";
		if(event.getEventClue()!=null)
			eventClue = event.getEventClue();
		jTextVerb.setText(eventClue);
		jTextLemmaVerb.setText(lemma);
		jEditorPaneSentence.setContentType("text/html");
		jEditorPaneSentence.setText(convertSentenceInHTMLFormat());	
		tree1.setModel(this.getTreeModelEntities(event.getEntitiesAtLeft(),"L"));
		tree1.setCellRenderer(new JTreeCellRenderer());
		tree2.setModel(this.getTreeModelEntities(event.getEntitiesAtRight(),"R"));
		tree2.setCellRenderer(new JTreeCellRenderer());
	}
	
	public String convertSentenceInHTMLFormat()
	{
		if(this.text==null)
		{
			GenericPair<Integer,Integer> publicationCorpusForEvent =  CorporaManagement.getEventInformation(((Corpus)((REProcess) process).getCorpus()).getCorpora().getDb(), event.getID());
			IPublication pub = ((REProcess) process).getAllProcessDocs().get(publicationCorpusForEvent.getY());
			String textType = ((REProcess) process).getCorpus().getProperties().getProperty("textType");
			String intNer = (String) process.getProperties().get("nerprocess");
			String name = new String();
			if(intNer!=null)
			{
				name =  CorporaManagement.getNERProcessDesignation(((Corpus)((REProcess) process).getCorpus()).getCorpora().getDb(),intNer);
			}
			if(textType!=null)
			{
				if(textType.equals("abstract"))
				{
					if(name.equals("Anote NER"))
					{
						text = Tokenizer.tokenizer(pub.getAbstractSection());
					}
					else
					{
						text = pub.getAbstractSection();
					}
				}
				else if(textType.equals("full text"))
				{
					if(name.equals("Anote NER"))
					{
						text = Tokenizer.tokenizer(pub.getFullTextFromDatabase(((Corpus)((REProcess) process).getCorpus()).getCorpora().getDb()));
					}
					else
					{
						text = pub.getFullTextFromDatabase(((Corpus)((REProcess) process).getCorpus()).getCorpora().getDb());
					}
				}
			}
			properties = new AnnotatedDocumentProperties(((REProcess) process).getAllProcessDocs().get(publicationCorpusForEvent.getX()),((Corpus)((REProcess) process).getCorpus()).getCorpora().getDb());
			this.maxTotal = text.length();
			this.annotationSorted =  getOrderAnnotations();
		}
		return getHtmlStream();
	}
	


	private String getHtmlStream() {
		String html = "<html>\n\t<body>\n\t";
		StringBuffer htmlText = new StringBuffer();
		String text = this.text.substring(this.sentenceLimitInf, this.sentenceLimitSup);
		long pointer=0;
		for(IAnnotation pos : annotationSorted.values())
		{
			Integer start = (int) pos.getStartOffset();
			Integer end = (int) pos.getEndOffset(); 
			Integer difI = start - sentenceLimitInf;
			Integer difS = end - sentenceLimitInf;
			if(pos instanceof IEventAnnotation)
			{
				if(pointer<difI-1)
					htmlText.append(text.substring((int) pointer , difI-1));			
				String entity = "<FONT COLOR=\"#CC0000\"><b>";;
				htmlText.append(entity);
				htmlText.append(text.substring(difI-1, difS));
				htmlText.append("</b></FONT>");
				pointer = difS;
			}
			else
			{
				IEntityAnnotation annot = (IEntityAnnotation) pos;
				if(pointer<difI)
					htmlText.append(text.substring((int) pointer, difI));			
				String entity = "<FONT COLOR=" + properties.getTasks().get(properties.getClassIDclass().get(annot.getClassAnnotationID())).getColor() + "><b>";;
				htmlText.append(entity);
				htmlText.append(text.substring(difI, difS));
				htmlText.append("</b></FONT>");
				pointer = difS;
			}
		}
		if(pointer<sentenceLimitSup-sentenceLimitInf)
			htmlText.append(text.substring((int) pointer,sentenceLimitSup-sentenceLimitInf));
		html = html.concat("\n\t</body>\n</html>");
		return htmlText.toString();
	}

	private SortedMap<Long, IAnnotation> getOrderAnnotations() {
		SortedMap<Long, IAnnotation> annotations = new TreeMap<Long, IAnnotation>();
		min = (int) event.getStartOffset();
		max = (int) event.getEndOffset();
		annotations.put(event.getStartOffset(), event);
		List<IEntityAnnotation> eventLeft = event.getEntitiesAtLeft();
		for(IEntityAnnotation el:eventLeft)
		{
			annotations.put(el.getStartOffset(), el);
			if(min>el.getStartOffset())
			{
				min = (int) el.getStartOffset();
			}
		}
		List<IEntityAnnotation> eventRight = event.getEntitiesAtRight();
		for(IEntityAnnotation er:eventRight)
		{
			annotations.put(er.getStartOffset(), er);
			if(max<er.getEndOffset())
			{
				max = (int) er.getEndOffset();
			}
		}
		sentenceLimitInf = min - 50;
		if(sentenceLimitInf<0)
			sentenceLimitInf = 0;
		sentenceLimitSup = max +50;
		if(sentenceLimitSup>this.maxTotal)
			sentenceLimitSup = maxTotal;	
		return annotations;
	}

	private DefaultTreeModel getTreeModelEntities(List<IEntityAnnotation> list,String LR){
		
		DefaultMutableTreeNode parentnode;
		if(LR.equals("L"))
		{
			parentnode = new DefaultMutableTreeNode("Entities At Left");
		}
		else
		{
			parentnode = new DefaultMutableTreeNode("Entities At Right");
		}
		for(IEntityAnnotation ent:list)
		{
			String entityName = ent.getAnnotationValue();
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(entityName+"("+ this.properties.getClassIDclass().get(ent.getClassAnnotationID()) +")");
			parentnode.add(child);
		}		
		DefaultTreeModel model = new DefaultTreeModel(parentnode);
		return model;
	}

	
	private void closeButtonActionPerformed(ActionEvent evt){
		this.setVisible(false);
		this.dispose();
	}
}
