package pt.uminho.anote2.aibench.curator.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewOkButtonOnly;
import pt.uminho.anote2.aibench.utils.gui.JTreeCellRenderer;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColors;
import pt.uminho.anote2.datastructures.documents.AnnotatedDocument;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class CuratorRelationDetailsGUI extends DialogGenericViewOkButtonOnly{

	private static final long serialVersionUID = 1L;

	private JTextField jTextVerb;
	private JPanel jPanelRelationSideEntities;
	private JPanel jPanelRelationProperties;
	private JPanel jPanelRelationClueAndLemma;
	private JEditorPane jEditorPaneSentence;
	private JTextField jTextLemmaVerb;
	private JScrollPane jScrollRightEntities;
	private JTextField jTextFieldDirectionaly;
	private JTextField jTextFieldPolarity;
	private JLabel jLabelDirecyionaly;
	private JLabel jLabelPolaridade;
	private JPanel jPanelRightEntities;
	private JPanel jPanelLeftEntities;
	private JScrollPane jScrollLeftEntities;
	private JScrollPane jScrollSentence;
	private JLabel jLabelLemmaVerb;
	private JLabel jLabelVerb;
	private JPanel downPanel;
	private JTree tree1;
	private JTree tree2;
	private IEventAnnotation event;
	private SortedMap<Long, IAnnotation> annotationSorted;
	private String text;
	private AnnotatedDocument annotatedDocument;

	private int min;
	private JPanel jPanelRelationClueAndProperties;
	private int max;
	
	public CuratorRelationDetailsGUI(IEventAnnotation event,IIEProcess process,boolean editable) throws SQLException, DatabaseLoadDriverException, RelationDelimiterExeption{
		super("Relation Details : "+event.getID());
		GenericPair<Integer,Integer> publicationCorpusForEvent =  CorporaDatabaseManagement.getEventInformation(event.getID());
		IPublication pub = ((RESchema) process).getAllProcessDocs().get(publicationCorpusForEvent.getY());
		annotatedDocument = new AnnotatedDocument(pub.getID(), pub, process, ((RESchema) process).getCorpus());
		this.event=event;
		initGUI();
		initDownPanel();
		this.setSize(GlobalOptions.superWidth, GlobalOptions.superHeight);
		Utilities.centerOnOwner(this);			
		initHTMLrelation();	
		this.setModal(true);
		this.setVisible(true);	
	}
	
	private void initGUI() {
		try {
			
			GridBagLayout downPanelLayout = new GridBagLayout();
				downPanelLayout.rowWeights = new double[] {0.15, 0.4, 0.4, 0.0};
				downPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
				downPanelLayout.columnWeights = new double[] {0.05};
				downPanelLayout.columnWidths = new int[] {7};			
				{				
					downPanel = new JPanel();
					downPanel.setLayout(downPanelLayout);
					this.add(downPanel);			
				}
				{
					jScrollSentence = new JScrollPane();
					downPanel.add(jScrollSentence, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
					{
						jEditorPaneSentence = new JEditorPane();
						jScrollSentence.setViewportView(jEditorPaneSentence);
						jEditorPaneSentence.setEditable(false);
						jEditorPaneSentence.setBackground(Color.WHITE);
					}
				}
				{
					jPanelRelationClueAndProperties = new JPanel();
					GridBagLayout jPanelRelationClueAndPropertiesLayout = new GridBagLayout();
					downPanel.add(jPanelRelationClueAndProperties, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelRelationClueAndPropertiesLayout.rowWeights = new double[] {0.1};
					jPanelRelationClueAndPropertiesLayout.rowHeights = new int[] {7};
					jPanelRelationClueAndPropertiesLayout.columnWeights = new double[] {0.1, 0.1};
					jPanelRelationClueAndPropertiesLayout.columnWidths = new int[] {7, 7};
					jPanelRelationClueAndProperties.setLayout(jPanelRelationClueAndPropertiesLayout);
					{
						jPanelRelationClueAndLemma = new JPanel();
						GridBagLayout jPanelRelationClueAndLemmaLayout = new GridBagLayout();
						jPanelRelationClueAndProperties.add(jPanelRelationClueAndLemma, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelRelationClueAndLemmaLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
						jPanelRelationClueAndLemmaLayout.rowHeights = new int[] {7, 7, 7, 7};
						jPanelRelationClueAndLemmaLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
						jPanelRelationClueAndLemmaLayout.columnWidths = new int[] {7, 7, 7};
						jPanelRelationClueAndLemma.setLayout(jPanelRelationClueAndLemmaLayout);
						jPanelRelationClueAndLemma.setBorder(BorderFactory.createTitledBorder(""));
						{
							jLabelVerb = new JLabel();
							jPanelRelationClueAndLemma.add(jLabelVerb, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
							jLabelVerb.setText("Clue :");
						}
						{
							jTextVerb = new JTextField();
							jPanelRelationClueAndLemma.add(jTextVerb, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 10), 0, 0));
							jTextVerb.setEditable(false);
							jTextVerb.setBackground(Color.WHITE);
						}
						{
							jLabelLemmaVerb = new JLabel();
							jPanelRelationClueAndLemma.add(jLabelLemmaVerb, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
							jLabelLemmaVerb.setText("Lemma (Clue)");
						}
						{
							jTextLemmaVerb = new JTextField();
							jPanelRelationClueAndLemma.add(jTextLemmaVerb, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 10), 0, 0));
							jTextLemmaVerb.setEditable(false);
							jTextLemmaVerb.setBackground(Color.WHITE);
						}
					}
					{
						jPanelRelationProperties = new JPanel();
						GridBagLayout jPanelRelationPropertiesLayout = new GridBagLayout();
						jPanelRelationClueAndProperties.add(jPanelRelationProperties, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelRelationPropertiesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
						jPanelRelationPropertiesLayout.rowHeights = new int[] {7, 7, 7, 7};
						jPanelRelationPropertiesLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
						jPanelRelationPropertiesLayout.columnWidths = new int[] {7, 7, 7};
						jPanelRelationProperties.setLayout(jPanelRelationPropertiesLayout);
						jPanelRelationProperties.setBorder(BorderFactory.createTitledBorder(""));
						{
							jLabelPolaridade = new JLabel();
							jPanelRelationProperties.add(jLabelPolaridade, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
							jLabelPolaridade.setText("Polarity :");
						}
						{
							jTextFieldPolarity = new JTextField();
							jPanelRelationProperties.add(jTextFieldPolarity, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
							jTextFieldPolarity.setEditable(false);
							jTextFieldPolarity.setBackground(Color.WHITE);
							jTextFieldPolarity.setText(event.getEventProperties().getPolarity().toString());
						}
						{
							jLabelDirecyionaly = new JLabel();
							jPanelRelationProperties.add(jLabelDirecyionaly, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
							jLabelDirecyionaly.setText("Directionally :");
						}
						{
							jTextFieldDirectionaly = new JTextField();
							jPanelRelationProperties.add(jTextFieldDirectionaly, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 10), 0, 0));
							jTextFieldDirectionaly.setEditable(false);
							jTextFieldDirectionaly.setBackground(Color.WHITE);				
							jTextFieldDirectionaly.setText(event.getEventProperties().getDirectionally().toString());
						}
					}
				}
				{
					jPanelRelationSideEntities = new JPanel();
					GridBagLayout jPanelRelationSideEntitiesLayout = new GridBagLayout();
					downPanel.add(jPanelRelationSideEntities, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelRelationSideEntitiesLayout.rowWeights = new double[] {0.1};
					jPanelRelationSideEntitiesLayout.rowHeights = new int[] {7};
					jPanelRelationSideEntitiesLayout.columnWeights = new double[] {0.1, 0.1};
					jPanelRelationSideEntitiesLayout.columnWidths = new int[] {7, 7};
					jPanelRelationSideEntities.setLayout(jPanelRelationSideEntitiesLayout);
					{
						jScrollLeftEntities = new JScrollPane();
						jPanelRelationSideEntities.add(jScrollLeftEntities, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
						jPanelRelationSideEntities.add(jScrollRightEntities, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
				}
				{
					downPanel.add(getButtonsPanel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}

		} catch(Exception e) {
			e.printStackTrace();
		}
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
		tree1.setModel(this.getTreeModelEntities(event.getEntitiesAtLeft(),"L"));
		tree1.setCellRenderer(new JTreeCellRenderer());
		tree2.setModel(this.getTreeModelEntities(event.getEntitiesAtRight(),"R"));
		tree2.setCellRenderer(new JTreeCellRenderer());
	}

	private void initHTMLrelation() throws RelationDelimiterExeption, SQLException, DatabaseLoadDriverException {
		jEditorPaneSentence.setText(convertSentenceInHTMLFormat());
	}
	
	public String convertSentenceInHTMLFormat() throws RelationDelimiterExeption, SQLException, DatabaseLoadDriverException
	{
		text = ((AnnotatedDocument) annotatedDocument).getDocumetAnnotationText();
		annotationSorted =  getOrderAnnotations();	
		List<ISentence> sentences = annotatedDocument.getSentencesText();
		ISentence sentenceInit = findSentence(sentences,min);	
		ISentence sentenceEnd = findSentence(sentences,max);
		if(sentenceInit == null || sentenceEnd == null || sentenceInit.getStartOffset()>=sentenceEnd.getEndOffset())
		{
			throw new RelationDelimiterExeption();
		}
		return getHtmlStream((int)sentenceInit.getStartOffset(),(int) sentenceEnd.getEndOffset());
		
	}

	private ISentence findSentence(List<ISentence> sentences, int offset) {
		for(ISentence set:sentences)
		{
			if(set.getStartOffset() <= offset && offset <= set.getEndOffset())
			{
				return set;
			}
		}
		return null;
	}

	private String getHtmlStream(int startsentence, int endSetence) throws SQLException, DatabaseLoadDriverException {
		String html = "<html>\n\t<body>\n\t";
		StringBuffer htmlText = new StringBuffer();
		String text = this.text.substring(startsentence, endSetence);
		long pointer=0;
		for(IAnnotation pos : annotationSorted.values())
		{
			Integer difI = (int) pos.getStartOffset() - startsentence;
			Integer difS = (int) pos.getEndOffset() - startsentence;
			if(pos instanceof IEventAnnotation)
			{
				
				if(pointer<difI)
					htmlText.append(text.substring((int) pointer , difI));			
				String entity = "<FONT COLOR=\""+OtherConfigurations.getVerbColor()+"\"><b bgcolor=\""+OtherConfigurations.getVerbColorBackGround()+"\">";
				htmlText.append(entity);
				if(difI>=0)
				{
					htmlText.append(text.substring(difI, difS));
				}
				else
				{
					htmlText.append(text.substring(difI, difS));
				}
				htmlText.append("</b></FONT>");
				pointer = difS;
			}
			else
			{
				IEntityAnnotation annot = (IEntityAnnotation) pos;
				if(pointer<difI)
					htmlText.append(text.substring((int) pointer, difI));	
				String entity = new String();
				if(annot.getClassAnnotationID()>=0)
				{
					entity = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(annot.getClassAnnotationID()).getColor() + "><b>";
				}
				else
				{
					entity = AnnotationColors.getDefaultColor();
				}
				htmlText.append(entity);
				htmlText.append(text.substring(difI, difS));
				htmlText.append("</b></FONT>");
				pointer = difS;
			}
		}
		if(pointer<endSetence-startsentence)
			htmlText.append(text.substring((int) pointer,endSetence-startsentence));
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
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(entityName+"( "+ ClassProperties.getClassIDClass().get(ent.getClassAnnotationID()) +" )");
			parentnode.add(child);
		}		
		DefaultTreeModel model = new DefaultTreeModel(parentnode);
		return model;
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
