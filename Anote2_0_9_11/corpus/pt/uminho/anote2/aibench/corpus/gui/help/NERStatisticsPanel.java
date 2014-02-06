package pt.uminho.anote2.aibench.corpus.gui.help;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ClassTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.DocumentTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.DocumentsTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.EntityTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.MainTermTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoDatabaseIDTreeLabel;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoDatabaseIDTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoExternalIDsLabel;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoExternalIDsTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoPrimaryTermTreeLabel;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoPrimaryTermTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoStatusTreeLabel;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoStatusTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoSynonymsTreeLabel;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoSynonymsTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.ResourceInfoTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.SynonymTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treenodes.SynonymsTreeNode;
import pt.uminho.anote2.aibench.corpus.gui.help.treerenders.TreeAllTermsRenderer;
import pt.uminho.anote2.aibench.corpus.gui.help.treerenders.TreeClassTermsRenderer;
import pt.uminho.anote2.aibench.corpus.gui.help.treerenders.TreeDocumentClassRenderer;
import pt.uminho.anote2.aibench.corpus.stats.DocumentClassEntities;
import pt.uminho.anote2.aibench.corpus.stats.EntityAndSynonymsStats;
import pt.uminho.anote2.aibench.corpus.stats.EntityDocuments;
import pt.uminho.anote2.aibench.corpus.stats.NerStatitics;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.ExternalID;
import pt.uminho.anote2.datastructures.annotation.ner.SimpleEntity;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;



public class NERStatisticsPanel extends JTabbedPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1660540951015888492L;
	private int classPerLine;
	private boolean useResourceElementInformation;
	private NerStatitics nerStatistics;
	private JTree jTreeAllEntities;
	private JScrollPane jScrollPaneEntitiesDocument;
	private JScrollPane jScrollPaneEntitiesByClass;
	private JScrollPane jScrollPaneAllEntityAnnotations;
	private Dictionary resource = new Dictionary(1, new String(),new String());
	private JPanel termAnnotationPanel;
	private JPanel documentsAnnotationPanel;
	private Map<Integer,String> resourceIDResourceType;
	private Map<Integer,IResourceElementSet<IResourceElement>> termIDSynonymS;
	private Map<Integer,IResourceElement> termIDTerm;
	private Map<Integer,List<IExternalID>> termIDExternalIDs;
	


	public NERStatisticsPanel(NerStatitics nerStatistics,boolean useResourceElementInformation) throws SQLException, DatabaseLoadDriverException
	{
		this.nerStatistics = nerStatistics;
		this.useResourceElementInformation = useResourceElementInformation;
		this.resourceIDResourceType = new HashMap<Integer, String>();
		this.termIDSynonymS = new HashMap<Integer, IResourceElementSet<IResourceElement>>();
		this.termIDTerm = new HashMap<Integer, IResourceElement>();
		this.termIDExternalIDs = new HashMap<Integer, List<IExternalID>>();
		this.classPerLine = 4;
		initGUI();
	}

	public NERStatisticsPanel(NerStatitics nerStatistics,boolean useResourceElementInformation,int classPerLine) throws SQLException, DatabaseLoadDriverException
	{
		this.nerStatistics = nerStatistics;
		this.useResourceElementInformation = useResourceElementInformation;
		this.resourceIDResourceType = new HashMap<Integer, String>();
		this.termIDSynonymS = new HashMap<Integer, IResourceElementSet<IResourceElement>>();
		this.termIDTerm = new HashMap<Integer, IResourceElement>();
		this.termIDExternalIDs = new HashMap<Integer, List<IExternalID>>();
		this.classPerLine = classPerLine;
		initGUI();
	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			this.setPreferredSize(new java.awt.Dimension(721, 490));
			{
				jScrollPaneAllEntityAnnotations = new JScrollPane();
				this.addTab("All Entity Annotations", null, jScrollPaneAllEntityAnnotations, null);
				{
					jScrollPaneAllEntityAnnotations.setViewportView(getAllEntitiesTree());
				}
			}
			{
				jScrollPaneEntitiesByClass = new JScrollPane();
				this.addTab("Entities (Grouped by Class)", null, jScrollPaneEntitiesByClass, null);
				{
					jScrollPaneEntitiesByClass.setViewportView(getClassTreePanel());
				}
			}
			{
				jScrollPaneEntitiesDocument = new JScrollPane();
				this.addTab("Entities (Grouped By Document)", null, jScrollPaneEntitiesDocument, null);
				{
					jScrollPaneEntitiesDocument.setViewportView(getDocumentTreePanel());
				}
			}
		}
		
	}

	private JPanel getDocumentTreePanel() throws SQLException, DatabaseLoadDriverException {
		if(documentsAnnotationPanel == null)
		{
			documentsAnnotationPanel = new JPanel();
			documentsAnnotationPanel.setBorder(BorderFactory.createTitledBorder("Documents "+GlobalTextInfoSmall.statistics));
			GridBagLayout termAnnotationPanelLayout = new GridBagLayout();
			termAnnotationPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			termAnnotationPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			termAnnotationPanelLayout.columnWeights = getColumnWeights();
			termAnnotationPanelLayout.columnWidths = getColumnWidths();
			documentsAnnotationPanel.setLayout(termAnnotationPanelLayout);
			builtDocumentsClassTermss(); 
		}
		return documentsAnnotationPanel;
	}

	private void builtDocumentsClassTermss() throws SQLException, DatabaseLoadDriverException {
		int i=0;
		for(int docID : nerStatistics.getDocClasses().keySet())
		{
			JScrollPane scrollpane = new JScrollPane();
			scrollpane.setPreferredSize(new Dimension(60,200));
			documentsAnnotationPanel.add(scrollpane, new GridBagConstraints(i%classPerLine, i/classPerLine, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			final JTree tree = completeJtreePublication(docID);
			tree.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
					null));
			String doc = "Document ID :"+String.valueOf(docID);
			if(nerStatistics.getDocIDDocsOtherID().containsKey(docID))
				doc = "PUBMED : "+nerStatistics.getDocIDDocsOtherID().get(docID);
			scrollpane.setViewportView(tree);
			scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), doc  , TitledBorder.LEADING, TitledBorder.TOP, null, null));		
			tree.setCellRenderer(new TreeDocumentClassRenderer());
			tree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					try {
						linkoutExtenalIDs(tree,me);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			tree.setRootVisible(false);
			i++;
		}
	}

	private JTree completeJtreePublication(int docID) throws SQLException, DatabaseLoadDriverException {
		DocumentClassEntities docClasses = nerStatistics.getDocClasses().get(docID);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new DocumentTreeNode(docID,nerStatistics.getDocIDDocsOtherID().get(docID),nerStatistics.getDocAnnotations().get(docID)));
		DefaultMutableTreeNode classNode;
		for(Integer classID :docClasses.getClassIDEntities().keySet())
		{
			int count = docClasses.getClassIDCount().get(classID);
			String classe = ClassProperties.getClassIDClass().get(classID);
			classNode = new DefaultMutableTreeNode(new ClassTreeNode(classe, classID, count));
			updateClasse(classNode,classID,docClasses);
			root.add(classNode);
		}
		return new JTree(root);
	}

	private void updateClasse(DefaultMutableTreeNode classNode,Integer classID, DocumentClassEntities docClasses) throws SQLException, DatabaseLoadDriverException {
		Set<SimpleEntity> res = docClasses.getClassIDEntities().get(classID).getClassIDEntities().keySet();
		Iterator<SimpleEntity> it = res.iterator();
		while(it.hasNext())
		{
			SimpleEntity sp = it.next();
			EntityAndSynonymsStats info = docClasses.getClassIDEntities().get(classID).getClassIDEntities().get(sp);
			EntityTreeNode treeNode = new EntityTreeNode(sp.getName(), info.getOccurrencesNumber(), info.isHaveSynonyms());
			DefaultMutableTreeNode termTreeNod = new DefaultMutableTreeNode(treeNode);
			fillTErmDetailsAndPublications(termTreeNod,sp,info);
			classNode.add(termTreeNod);
		}
	}

	private JPanel getClassTreePanel() throws SQLException, DatabaseLoadDriverException {
		if(termAnnotationPanel == null)
		{
			termAnnotationPanel = new JPanel();
			termAnnotationPanel.setBorder(BorderFactory.createTitledBorder("Class "+GlobalTextInfoSmall.statistics));
			GridBagLayout termAnnotationPanelLayout = new GridBagLayout();
			termAnnotationPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			termAnnotationPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
			termAnnotationPanelLayout.columnWeights = getColumnWeights();
			termAnnotationPanelLayout.columnWidths = getColumnWidths();
			termAnnotationPanel.setLayout(termAnnotationPanelLayout);
			builtTreeClassTermss(); 
		}
		return termAnnotationPanel;
	}

	private void builtTreeClassTermss() throws SQLException, DatabaseLoadDriverException {
		int i=0;
		for(int classID :nerStatistics.getClassSimpleEntities().keySet())
		{
			JScrollPane scrollpane = new JScrollPane();
			scrollpane.setPreferredSize(new Dimension(60,200));
			termAnnotationPanel.add(scrollpane, new GridBagConstraints(i%classPerLine, i/classPerLine, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			final JTree tree = completeJtreeClass(classID);
			tree.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(BevelBorder.LOWERED), 
					null));
			scrollpane.setViewportView(tree);
			String color = CorporaProperties.getCorporaClassColor(classID).getColor();
			String classe = ClassProperties.getClassIDClass().get(classID);
			scrollpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), classe , TitledBorder.LEADING, TitledBorder.TOP, null, Color.decode(color)));		
			tree.setCellRenderer(new TreeClassTermsRenderer());
			tree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					try {
						linkoutExtenalIDs(tree,me);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			tree.setRootVisible(false);
			i++;
		}
	}

	private JTree completeJtreeClass(int classID) throws SQLException, DatabaseLoadDriverException {
		String classe = ClassProperties.getClassIDClass().get(classID);
		int count = nerStatistics.getClassOcurrences().get(classID);
		ClassTreeNode classNode = new ClassTreeNode(classe,classID,count);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(classNode);
		fillClassTerms(classID,root);
		return new JTree(root);
	}

	public double[] getColumnWeights()
	{
		double[] columnWeights = new double[classPerLine];
		for(int i=0;i<classPerLine;i++)
			columnWeights[i] = 0.1;
		return columnWeights;
	}
	
	public int[] getColumnWidths()
	{
		int[] columnWeights = new int[classPerLine];
		for(int i=0;i<classPerLine;i++)
			columnWeights[i] = 7;
		return columnWeights;
	}
	
	private JTree getAllEntitiesTree() throws SQLException, DatabaseLoadDriverException {
		if(jTreeAllEntities == null)
		{
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Class(es)");
			DefaultMutableTreeNode classeTreeNod;
			ClassTreeNode classNode;
			for(Integer classID :nerStatistics.getClassSimpleEntities().keySet())
			{
				String classe = ClassProperties.getClassIDClass().get(classID);
				int count = nerStatistics.getClassOcurrences().get(classID);
				classNode = new ClassTreeNode(classe,classID,count);
				classeTreeNod = new DefaultMutableTreeNode(classNode);
				fillClassTerms(classID,classeTreeNod);
				root.add(classeTreeNod);
			}
			jTreeAllEntities = new JTree(root);
			jTreeAllEntities.setCellRenderer(new TreeAllTermsRenderer());
			jTreeAllEntities.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					try {
						linkoutExtenalIDs(jTreeAllEntities,me);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jTreeAllEntities.setBorder(BorderFactory.createTitledBorder("All Entities "+GlobalTextInfoSmall.statistics));


		}
		return jTreeAllEntities;
	}
	
	protected void linkoutExtenalIDs(JTree jTreeAllEntities,MouseEvent me) throws SQLException, DatabaseLoadDriverException, IOException {
		TreePath tp = jTreeAllEntities.getPathForLocation(me.getX(), me.getY());
		if(tp!=null)
		{
			Object obj = tp.getLastPathComponent();
			if (tp != null)
			{
				if(obj instanceof DefaultMutableTreeNode)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
					Object userdata = node.getUserObject();
					if(userdata instanceof ResourceInfoExternalIDsLabel)
					{
						ResourceInfoExternalIDsLabel extIDLabel = (ResourceInfoExternalIDsLabel) userdata;
						IExternalID extID = extIDLabel.getExternal();
						List<String> urls = LinkOutsPropertiesGUI.getUrlsForSource(extID.getSourceID());
						if(urls.size()>0)
						{
							for(String url:urls)
								Help.internetAccess(url.replace("#", extID.getExternalID()));
						}
						else
						{
							String url = "http://www.google.com/search?q=#";
							Help.internetAccess(url.replace("#", extID.getExternalID()));
						}
					}
				}
			}		
		}
	}

	private void fillClassTerms(Integer classID,DefaultMutableTreeNode classeTreeNod) throws SQLException, DatabaseLoadDriverException {
		Set<SimpleEntity> termClass = nerStatistics.getClassSimpleEntities().get(classID);
		DefaultMutableTreeNode termTreeNod;
		for(SimpleEntity ent: termClass)
		{
			EntityAndSynonymsStats entStats = nerStatistics.getEntityStatistics().get(ent);
			if(entStats!=null)
			{
				String mainTerm = ent.getName();
				boolean haveSynonyms = entStats.isHaveSynonyms();
				int occurrences = entStats.getOccurrencesNumber();
				EntityTreeNode entTreeNode = new EntityTreeNode(mainTerm, occurrences, haveSynonyms);
				termTreeNod = new DefaultMutableTreeNode(entTreeNode);
				fillTErmDetailsAndPublications(termTreeNod,ent,entStats);
				classeTreeNod.add(termTreeNod);
			}
			else
			{
				System.err.println(ent.toString());
			}
		}
	}

	private void fillTErmDetailsAndPublications(DefaultMutableTreeNode termTreeNod, SimpleEntity ent, EntityAndSynonymsStats entStats) throws SQLException, DatabaseLoadDriverException {
		IResourceElement elem = null;
		if(useResourceElementInformation && ent.getResourceElementID() != 0)
		{
			if(!termIDTerm.containsKey(ent.getResourceElementID()))
				termIDTerm.put(ent.getResourceElementID(), this.resource.getTerm(ent.getResourceElementID()));
		}
		elem = termIDTerm.get(ent.getResourceElementID());
		updateMainTerm(termTreeNod,ent,entStats);
		boolean isrule = updateSynonyms(termTreeNod,ent,entStats,elem);
		updatePublications(termTreeNod,ent);
		if(elem!=null)
		{
			updateResourceInfo(termTreeNod,ent,entStats,elem,isrule);
		}
 
	}

	private void updateResourceInfo(DefaultMutableTreeNode termTreeNod,SimpleEntity ent, EntityAndSynonymsStats entStats,IResourceElement elem, boolean isrule) throws SQLException, DatabaseLoadDriverException {
		 DefaultMutableTreeNode resourceInfo = new DefaultMutableTreeNode(new ResourceInfoTreeNode());
		 termTreeNod.add(resourceInfo);
		 if(!this.termIDSynonymS.containsKey(ent.getResourceElementID()))
			 this.termIDSynonymS.put(ent.getResourceElementID(), resource.getTermSynomns(Integer.valueOf(ent.getResourceElementID())));
		 if(!this.termIDTerm.containsKey(ent.getResourceElementID()))
			 this.termIDTerm.put(ent.getResourceElementID(), resource.getTerm(ent.getResourceElementID()));
		 if(!this.termIDExternalIDs.containsKey(ent.getResourceElementID()))
			 this.termIDExternalIDs.put(ent.getResourceElementID(), resource.getexternalIDandSorceIDandSource(Integer.valueOf(ent.getResourceElementID())));
		if(this.termIDTerm.get(ent.getResourceElementID())!=null)
		 {
			 IResourceElement elemTerm = this.termIDTerm.get(ent.getResourceElementID());
			 DefaultMutableTreeNode primaryTreeNode = new DefaultMutableTreeNode(new ResourceInfoPrimaryTermTreeNode(isrule));
			 resourceInfo.add(primaryTreeNode);
			 DefaultMutableTreeNode primaryTreeLabel = new DefaultMutableTreeNode(new ResourceInfoPrimaryTermTreeLabel(elemTerm.getTerm()));
			 resourceInfo.add(primaryTreeLabel);
			 DefaultMutableTreeNode databseIDTreeNode = new DefaultMutableTreeNode(new ResourceInfoDatabaseIDTreeNode());
			 resourceInfo.add(databseIDTreeNode);
			 DefaultMutableTreeNode databseIDTreeLabel = new DefaultMutableTreeNode(new ResourceInfoDatabaseIDTreeLabel(ent.getResourceElementID()));
			 resourceInfo.add(databseIDTreeLabel);
		 }
		 if( this.termIDSynonymS.get(ent.getResourceElementID()).getElements().size() > 0)
		 { 
			DefaultMutableTreeNode synonyms = new DefaultMutableTreeNode(new ResourceInfoSynonymsTreeNode(isrule));
			resourceInfo.add(synonyms);
			for(IResourceElement elemSynonym :this.termIDSynonymS.get(ent.getResourceElementID()).getElements())
			{
				DefaultMutableTreeNode synonym = new DefaultMutableTreeNode(new ResourceInfoSynonymsTreeLabel(elemSynonym.getTerm()));
				resourceInfo.add(synonym);
			}
		 }
		 if(this.termIDExternalIDs.get(ent.getResourceElementID()).size()>0)
		 {
			DefaultMutableTreeNode externalIds = new DefaultMutableTreeNode(new ResourceInfoExternalIDsTreeNode());
			resourceInfo.add(externalIds);
			DefaultMutableTreeNode externalId;
			 for(IExternalID id :this.termIDExternalIDs.get(ent.getResourceElementID()))
			 {
				IExternalID extID = new ExternalID(id.getExternalID(), id.getSource() , id.getSourceID());
				externalId = new DefaultMutableTreeNode(new ResourceInfoExternalIDsLabel(extID ));
				resourceInfo.add(externalId);
			 }
		 }
		 if(elem!=null)
		 {
			 IResourceElement elemTerm = this.termIDTerm.get(ent.getResourceElementID());
			 DefaultMutableTreeNode statusTreeNode = new DefaultMutableTreeNode(new ResourceInfoStatusTreeNode());
			 resourceInfo.add(statusTreeNode);
			 DefaultMutableTreeNode statusTreeLabel = new DefaultMutableTreeNode(new ResourceInfoStatusTreeLabel(elemTerm.isActive()));
			 resourceInfo.add(statusTreeLabel);
		 }

	}

	private void updatePublications(DefaultMutableTreeNode termTreeNod,SimpleEntity ent) {
		EntityDocuments docs = nerStatistics.getEntityPerDocuments().get(ent);
		if(docs!=null)
		{
			DefaultMutableTreeNode documentsTreeNode = new DefaultMutableTreeNode(new DocumentsTreeNode(docs.getDocumnentIDOcurreences().size()));
			termTreeNod.add(documentsTreeNode);
			DefaultMutableTreeNode documentTreeNode;
			for(int docID :docs.getDocumnentIDOcurreences().keySet())
			{
				int occurrences = docs.getDocumnentIDOcurreences().get(docID);
				String pmid = nerStatistics.getDocIDDocsOtherID().get(docID);
				documentTreeNode = new DefaultMutableTreeNode(new DocumentTreeNode(docID, pmid, occurrences));
				documentsTreeNode.add(documentTreeNode);
			}
		}
	}

	private void updateMainTerm(DefaultMutableTreeNode termTreeNod,SimpleEntity ent, EntityAndSynonymsStats entStats) {
		if(entStats.isHaveSynonyms())
		{
			DefaultMutableTreeNode mainTerm = new DefaultMutableTreeNode(new MainTermTreeNode(ent.getName(), entStats.getMainTermAnnotations()));
			termTreeNod.add(mainTerm);
		}
	}

	private boolean updateSynonyms(DefaultMutableTreeNode termTreeNod,SimpleEntity ent, EntityAndSynonymsStats entStats,IResourceElement elem) throws SQLException, DatabaseLoadDriverException {
		if(entStats.isHaveSynonyms())
		{
			DefaultMutableTreeNode synonymsNode,synonymNode;
			boolean resourceISARule = false;
			if(elem !=  null)
			{
				if(!this.resourceIDResourceType.containsKey(ent.getResourceElementID()))
					this.resourceIDResourceType.put(ent.getResourceElementID(), resource.getResourceType(ent.getResourceElementID()));
				resourceISARule = resourceIDResourceType.get(ent.getResourceElementID()).equals(GlobalOptions.resourcesRuleSetName); 
			}
			int diferentSynonyms = entStats.getSynonymOccurences().size();
			if(resourceISARule)
			{
				synonymsNode = new DefaultMutableTreeNode(new SynonymsTreeNode("Other Rule Variations :",diferentSynonyms));
			}
			else
			{
				synonymsNode = new DefaultMutableTreeNode(new SynonymsTreeNode("Synonyms :",diferentSynonyms));
			}
			termTreeNod.add(synonymsNode);
			for(String synonym:entStats.getSynonymOccurences().keySet())
			{
				int occurrences = entStats.getSynonymOccurences().get(synonym);
				synonymNode = new DefaultMutableTreeNode(new SynonymTreeNode(synonym, occurrences ));
				synonymsNode.add(synonymNode);
			}
			return resourceISARule;
		}
		return false;
	}


}
