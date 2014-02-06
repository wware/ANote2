package pt.uminho.anote2.aibench.curator.view.panes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.document.ParsingUtils;
import pt.uminho.anote2.aibench.curator.view.panes.tree.EventNode;
import pt.uminho.anote2.aibench.curator.view.panes.tree.TreeNodeTextBold;
import pt.uminho.anote2.aibench.curator.view.panes.tree.clues.ClueNode;
import pt.uminho.anote2.aibench.curator.view.panes.tree.listener.RECuratorEditPaneMouseAdapter;
import pt.uminho.anote2.aibench.curator.view.panes.tree.listener.TreeClueRelationsMouseAdapter;
import pt.uminho.anote2.aibench.curator.view.panes.tree.listener.TreeEntitiesRelationMouseAdaptar;
import pt.uminho.anote2.aibench.curator.view.panes.tree.renderer.TreeCellRenderEntitiesRelations;
import pt.uminho.anote2.aibench.curator.view.panes.tree.renderer.TreeCellRenderVerbsRelations;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.generic.genericpanel.view.JPanelWait;
import es.uvigo.ei.aibench.workbench.Workbench;

public class RECuratorPane extends JPanel implements Observer {

	private static final long serialVersionUID = 9155059515796055999L;

	private JPanelWait jWaitPanelWait;
	private JEditorPane editPanel;
	private JScrollPane textScroll;
	private JScrollPane statisticsScroll;
	private JScrollPane tasksColorScroll;
	private JToolBar toolBar;	
	private JPanel annotationsPanel;
	private JSplitPane mainSplitPane;
	private JButton jButtonHelp;
	private JButton zooIn;
	private JButton zoomOut;
	private JButton undoButton;
	private JButton searchButton;
	private JTextField searchTextField;
	private JTree treeEntitiesRelations; // JTree with the annotated classes and terms
	private JTree treeCluesRelations; // JTree with the annotated complex tags
	private int caretPosition;
	private String selectedText;
	private String textToFind;
	private int lastFound;
	private IResource<IResourceElement> dic = null;
	private CuratorDocument curatorDocument;

	private JPanel jPanelToolBAr;

	public RECuratorPane(CuratorDocument curatorDocument){
		super();
		try {
			this.curatorDocument = curatorDocument;
			editPanel = new JEditorPane();	
			initGUI();
			refreshStatistics();
			if(this.curatorDocument.getDocumentText().getClearText().length()<1)
			{
				Workbench.getInstance().warn("Unknown abstract or Full text for documente whit ID "+this.curatorDocument.getAnnotatedDocument().getID());
				editPanel.setEnabled(false);	
			}
			else
			{
				synchronized(editPanel){editPanel.setText(this.curatorDocument.getDocumentText().getHtmlTextForEntitiesAndClues());}
				synchronized(editPanel){editPanel.setEditable(false);} 
				this.curatorDocument.getDocumentText().setClearText(getclearText());
			}
			treeEntitiesRelations.setCellRenderer(new TreeCellRenderEntitiesRelations());
			treeEntitiesRelations.addMouseListener(new TreeEntitiesRelationMouseAdaptar(treeEntitiesRelations,this));
			treeCluesRelations.setCellRenderer(new TreeCellRenderVerbsRelations());
			treeCluesRelations.addMouseListener(new TreeClueRelationsMouseAdapter(treeCluesRelations,this));	
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.5};
			thisLayout.rowHeights = new int[] {20};
			thisLayout.columnWeights = new double[] {0.5};
			thisLayout.columnWidths = new int[] {7};		
			this.setLayout(thisLayout);
			{
				editPanel.setContentType("text/html");
				textScroll = new JScrollPane(editPanel);	
				this.add(getJSplitPane3(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));					
				this.add(getJWaitPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));					
				mainSplitPane.setLeftComponent(getAnnotationsPanel());
				annotationsPanel.add(textScroll, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				annotationsPanel.add(getJpanelToolBar(), new GridBagConstraints(-1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));			
				mainSplitPane.setRightComponent(getJtabbedPaneRelations());				
				editPanel.addMouseListener(new RECuratorEditPaneMouseAdapter(this, curatorDocument));
			}
		}

	}
	
	private Component getJtabbedPaneRelations() {
		JTabbedPane relationsIndex = new JTabbedPane();
	   	statisticsScroll = new JScrollPane(treeEntitiesRelations);
		{
			treeEntitiesRelations = new JTree();
			statisticsScroll.setViewportView(treeEntitiesRelations);
			statisticsScroll.setMinimumSize(new Dimension(100,400));
		}
		
		tasksColorScroll = new JScrollPane(treeCluesRelations);
		{
			treeCluesRelations = new JTree();
			tasksColorScroll.setViewportView(treeCluesRelations);
			tasksColorScroll.setMinimumSize(new Dimension(100,100));
		}
		relationsIndex.add("Order By Clue", tasksColorScroll);
		relationsIndex.add("Order By Term", statisticsScroll);
		return relationsIndex;
	}

	private JPanel getJpanelToolBar() throws SQLException, DatabaseLoadDriverException {
		if(jPanelToolBAr == null)
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.5, 0.1};
			thisLayout.columnWidths = new int[] {7, 7};
			jPanelToolBAr = new JPanel();
			jPanelToolBAr.setLayout(thisLayout);
			jPanelToolBAr.add(getToolBar(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelToolBAr;
	}

	private JToolBar getToolBar() {
		if(toolBar == null)
		{
			toolBar = new JToolBar();
			toolBar.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
					null));
		}
		constructToolBar();
		toolBar.setFloatable(false);
		return toolBar;
	}
	
	private JPanel getJWaitPanel() {
		if(jWaitPanelWait==null)
		{
			jWaitPanelWait = new JPanelWait();
		}
		return jWaitPanelWait;
	}

	public void constructToolBar(){

	    zooIn = new JButton();
	    zooIn.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag+.png")));
	    zooIn.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag+Pressed.png")));
	    zooIn.setToolTipText("Zoom In");
	    zooIn.setBorderPainted(false);
	    zooIn.setContentAreaFilled(false);
	    zooIn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent evt) {
				zoomIn();
			}
	    });
	    toolBar.add(zooIn);
	    toolBar.addSeparator();
	    
	    zoomOut = new JButton();
	    zoomOut.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag-.png")));
	    zoomOut.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag-Pressed.png")));
	    zoomOut.setToolTipText("Zoom Out");
	    zoomOut.setBorderPainted(false);
	    zoomOut.setContentAreaFilled(false);
	    zoomOut.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent evt) {
				zoomOut();
			}
	    });
	    toolBar.add(zoomOut);	 
	    toolBar.addSeparator();   
	    searchTextField = new JTextField();
		searchTextField.setText("");
		searchTextField.setPreferredSize(new java.awt.Dimension(200, 34));
		searchTextField.addKeyListener(new KeyAdapter() {	
			public void keyTyped(KeyEvent evt) {
				search(evt);
			}
		});
	    toolBar.add(searchTextField);    
	    searchButton = new JButton();
		searchButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/down.png")));
		searchButton.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/downPressed.png")));
		searchButton.setToolTipText("Find");
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setEnabled(false);
		searchButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				find(searchTextField.getText());
			}
			
		});
	    toolBar.add(searchButton);	    
	    toolBar.add(getJButtonHelp());
	}

	/**
	 * Zoom in html
	 */
	private synchronized void zoomIn(){
		this.curatorDocument.getCuratorView().setZoom(true);

	}
	
	/**
	 * Zoom out html
	 */
	private synchronized void zoomOut(){
		this.curatorDocument.getCuratorView().setZoom(false);
	}

	public void changeZoom(boolean zoomin) {
		synchronized(editPanel){setCaretPosition(editPanel.getCaretPosition());}	
		int zoom = this.curatorDocument.getCuratorView().getZoom();
		if(zoomin)
			zoom--;
		else
			zoom++;
		String last = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + zoom + "pt\" align=\"justify\">";
		String next = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + this.curatorDocument.getCuratorView().getZoom() + "pt\" align=\"justify\">";	
		curatorDocument.getDocumentText().setHtmlTextForEntitiesAndClues(curatorDocument.getDocumentText().getHtmlTextForEntitiesAndClues().replace(last, next));
		synchronized(editPanel){editPanel.setText(curatorDocument.getDocumentText().getHtmlTextForEntitiesAndClues());}
		goToCaret(getSelectedText());
	}
	
	private void search(KeyEvent evt){
		boolean found;
		
		if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE || evt.getKeyChar() == KeyEvent.VK_ENTER)
		{
			if(searchTextField.getText().compareTo("")!=0)
			{
				found = find(searchTextField.getText());
				searchButton.setEnabled(found);
			}
			else
			{
				editPanel.getHighlighter().removeAllHighlights();
				searchButton.setEnabled(false);
				found = true;
			}
		}
		else
		{
			found = find(searchTextField.getText()+evt.getKeyChar());
			searchButton.setEnabled(found);
		}
		if(found)
		{
			this.searchTextField.setForeground(Color.BLACK);
			this.searchTextField.setBackground(Color.WHITE);
		}		
		else
		{
			this.searchTextField.setForeground(new java.awt.Color(255,0,0));
			searchTextField.setBackground(new java.awt.Color(174,174,174));
		}		
	}
	
	private boolean find(String text){

		if(text.equals(""))
			return true;		
		
		if(!text.equals(textToFind) || lastFound==-1)
		{
			lastFound = 0;
			textToFind = text;
		}
		
		lastFound = curatorDocument.getDocumentText().getClearText().indexOf(text, lastFound);
		
		if(lastFound!=-1)
		{
			editPanel.setCaretPosition(lastFound);
			int caretEnd = lastFound + text.length();
			
			try {
				editPanel.getHighlighter().removeAllHighlights();
				editPanel.getHighlighter().addHighlight(lastFound, caretEnd, DefaultHighlighter.DefaultPainter);
				
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			lastFound = caretEnd+1;
			return true;
		}
		return false;
	}
	
	private JPanel getAnnotationsPanel() {
		if(annotationsPanel == null) {
			annotationsPanel = new JPanel();
			GridBagLayout annotationsPanelLayout = new GridBagLayout();
			annotationsPanelLayout.rowWeights = new double[] {0.0005, 0.0995};
			annotationsPanelLayout.rowHeights = new int[] {7, 7};
			annotationsPanelLayout.columnWeights = new double[] {0.1};
			annotationsPanelLayout.columnWidths = new int[] {7};
			annotationsPanel.setLayout(annotationsPanelLayout);
		}
		return annotationsPanel;
	}
	
	private JSplitPane getJSplitPane3() {
		if(mainSplitPane == null) {
			mainSplitPane = new JSplitPane();
			mainSplitPane.setOneTouchExpandable(true);
			mainSplitPane.setResizeWeight(0.9);
		}
		return mainSplitPane;
	}
	
	public synchronized void refreshStatistics() throws SQLException, DatabaseLoadDriverException{
		treeEntitiesRelations.setModel(this.getTreeEntitiesRelationModel());
		treeCluesRelations.setModel(this.getClueRelationsStructure());
	}
	
	private DefaultTreeModel getTreeEntitiesRelationModel() throws SQLException, DatabaseLoadDriverException{
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Entities (Relations)");		
		Map<AnnotationPosition, Set<IEventAnnotation>> entityRelations = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityRelationSet();
		DefaultMutableTreeNode subchild;
		for(AnnotationPosition termPosition : entityRelations.keySet())
		{
			IAnnotation annot = curatorDocument.getAnnotatedDocument().getEntityAnnotationPositions().get(termPosition);
			if(annot instanceof IEntityAnnotation)
			{	
				IEntityAnnotation ent = (IEntityAnnotation) annot;

				DefaultMutableTreeNode child = new DefaultMutableTreeNode(ent);
				for(IEventAnnotation event : entityRelations.get(termPosition))
				{
					subchild = new DefaultMutableTreeNode(new EventNode(event));
					completeRelationTree(subchild, event);
					child.add(subchild);
				}		
				node.add(child);
			}
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	private DefaultTreeModel getClueRelationsStructure(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Verbs (Clues)");	
		Map<AnnotationPosition, Set<IEventAnnotation>> clueRelations = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getVerbRelations();
		DefaultMutableTreeNode subchild;
		for(AnnotationPosition cluePosition : clueRelations.keySet())
		{
			String clue = this.curatorDocument.getDocumentText().getClearText().substring(cluePosition.getStart(), cluePosition.getEnd()+1);
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(new ClueNode(cluePosition, clue));
			for(IEventAnnotation event : clueRelations.get(cluePosition))
			{
				subchild = new DefaultMutableTreeNode(new EventNode(event));
				completeRelationTree(subchild, event);
				child.add(subchild);
			}		
			node.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}

	private void completeRelationTree(DefaultMutableTreeNode subchild,IEventAnnotation event) {
		DefaultMutableTreeNode subsubchild;
		String clue;
		clue = event.getEventClue();
		if(clue==null || clue.length()==0)
		{
			clue = "Not Exist";
		}
		subsubchild = new DefaultMutableTreeNode(new TreeNodeTextBold("Clue :"));
		subchild.add(subsubchild);
		subsubchild = new DefaultMutableTreeNode(clue);
		subchild.add(subsubchild);
		subsubchild = new DefaultMutableTreeNode(new TreeNodeTextBold("Entities At left :"));
		subchild.add(subsubchild);
		for(IEntityAnnotation entLeft : event.getEntitiesAtLeft())
		{
			subsubchild = new DefaultMutableTreeNode(entLeft);
			subchild.add(subsubchild);
		}
		subsubchild = new DefaultMutableTreeNode(new TreeNodeTextBold("Entities At Right :"));
		subchild.add(subsubchild);
		for(IEntityAnnotation entLeft : event.getEntitiesAtRight())
		{
			subsubchild = new DefaultMutableTreeNode(entLeft);
			subchild.add(subsubchild);
		}
		subsubchild = new DefaultMutableTreeNode(new TreeNodeTextBold("Properties :"));
		subchild.add(subsubchild);
		String lemma = event.getEventProperties().getLemma();
		if(!lemma.isEmpty())
		{
			DefaultMutableTreeNode subsubsubchildNode = new DefaultMutableTreeNode(new TreeNodeTextBold("Clue (Lemma) :"));
			subsubchild.add(subsubsubchildNode);
			subsubsubchildNode = new DefaultMutableTreeNode(lemma);
			subsubchild.add(subsubsubchildNode);
		}
		DirectionallyEnum dir = event.getEventProperties().getDirectionally();
		DefaultMutableTreeNode subsubsubchildNode = new DefaultMutableTreeNode(new TreeNodeTextBold("Directionally :"));
		subsubchild.add(subsubsubchildNode);
		subsubsubchildNode = new DefaultMutableTreeNode(dir);
		subsubchild.add(subsubsubchildNode);
		PolarityEnum polarity = event.getEventProperties().getPolarity();
		subsubsubchildNode = new DefaultMutableTreeNode(new TreeNodeTextBold("Polarity :"));
		subsubchild.add(subsubsubchildNode);
		subsubsubchildNode = new DefaultMutableTreeNode(polarity);
		subsubchild.add(subsubsubchildNode);
	}

	
	public synchronized void goToCaret(String text){
		synchronized(editPanel){
			int pos = getCaretPosition() - 200;
			if(pos<0)
				pos=0;
			editPanel.setCaretPosition(pos);
		}
	}
	
	private String getclearText(){
		Color aux = editPanel.getSelectionColor();
		editPanel.setSelectionColor(Color.WHITE);
		editPanel.selectAll();
		String cleanText = editPanel.getSelectedText();
		int p = this.getCaretPosition();
		this.editPanel.select(p, p);
		editPanel.setSelectionColor(aux);
		return cleanText;
	}
	
	public JButton gtUnButton(){return this.undoButton;}
	public CuratorDocument getAiDocument(){return this.curatorDocument;}
	synchronized public JEditorPane getEditPanel() {return editPanel;}
	public synchronized int getCaretPosition() {return caretPosition;}
	public synchronized void setCaretPosition(int caretPosition) {this.caretPosition = caretPosition;}
	public synchronized String getSelectedText() {return selectedText;}
	
	public synchronized void setSelectedText(String selectedtext) {
		this.selectedText = ParsingUtils.removeBoundarieSpaces(selectedtext);
	}
	
	public void update(Observable o, Object arg) {
		startWaitPAnel();
		try {
			curatorDocument.rebuildHtml(this.curatorDocument.getCuratorView().getZoom());
			synchronized(editPanel){editPanel.setText(curatorDocument.getDocumentText().getHtmlTextForEntitiesAndClues());}
			goToCaret(getSelectedText());
			refreshStatistics();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

		stopWaitPAnel();
	}
	
	private void stopWaitPAnel() {
		
		jWaitPanelWait.setVisible(false);	
		jWaitPanelWait.setEnabled(false);
		mainSplitPane.setEnabled(true);
		mainSplitPane.setVisible(true);	
	}

	private void startWaitPAnel()
	{
		mainSplitPane.setVisible(false);
		mainSplitPane.setEnabled(false);
		jWaitPanelWait.setEnabled(true);
		jWaitPanelWait.setVisible(true);	
	}
	

	public IResource<IResourceElement> getDic() {
		return dic;
	}

	public void setLookupTable(IResource<IResourceElement> dic) {
		this.dic = dic;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Curator_View#Relation.2FEvent_View");
					} catch (IOException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}

	public void removeEvent(IEventAnnotation event) throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		removeEventFromStructures(event);
		
	}

	private void removeEventFromStructures(IEventAnnotation event) throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		curatorDocument.removeEventAnnotation(event);		
	}

	public void removeAllEventsFromClue(AnnotationPosition position) throws ManualCurationException, SQLException, DatabaseLoadDriverException {
		curatorDocument.removeAllEventFromClue(position);
		
	}
	
	public CuratorDocument getCuratorDocument() {
		return curatorDocument;
	}
	
}
