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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import pt.uminho.anote2.aibench.corpus.gui.help.LinkOutsPropertiesGUI;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.ShortAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.datastructures.document.parts.CuratorDocumentAnnotations;
import pt.uminho.anote2.aibench.curator.datastructures.document.parts.CuratorDocumentPreviousState;
import pt.uminho.anote2.aibench.curator.document.ParsingUtils;
import pt.uminho.anote2.aibench.curator.gui.ChangeResourcesGUI;
import pt.uminho.anote2.aibench.curator.gui.CorrectSelectionDialog;
import pt.uminho.anote2.aibench.curator.gui.NewClassGUI;
import pt.uminho.anote2.aibench.curator.menu.externallinks.ExternalLinksMenu;
import pt.uminho.anote2.aibench.curator.settings.CuratorDefaultSettings;
import pt.uminho.anote2.aibench.curator.view.panes.tree.TreeNodeText;
import pt.uminho.anote2.aibench.curator.view.panes.tree.renderer.CuratorTermTreeCellRenderer;
import pt.uminho.anote2.aibench.curator.view.panes.tree.term.TermNode;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.IExternalID;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.datastructures.resources.lookuptable.LookupTable;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.generic.genericpanel.view.JPanelWait;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class NERCuratorPane extends JPanel implements Observer {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JPanelWait jWaitPanelWait;
	private JEditorPane editPanel;
	private JScrollPane textScroll;
	private JScrollPane statisticsScroll;
	private JToolBar toolBar;	
	private JPanel annotationsPanel;
	private JSplitPane mainSplitPane;
	private JButton jButtonHelp;
	private JButton zooIn;
	private JButton zoomOut;
	private JButton undoButton;
	private JButton searchButton;
	private JCheckBox allInsertorRemove;
	private JTextField searchTextField;
	private JPopupMenu menu;
	private CorrectSelectionDialog correctSelectionForm;	
	private JTree treeTerms; // JTree with the annotated classes and terms
	private int caretPosition;
	private String selectedText;
	private String textToFind;
	private int lastFound;
	private PanelTextEditor panelTextEditor;
	private CuratorDocument curatorDocument;
	private LookupTable look = null;
	private JButton lookuptableButton;
	private Map<Integer,DefaultMutableTreeNode> databaseIDNode;
	private JPanel jPanelToolBAr;
	private JCheckBox caseSensative;

	public NERCuratorPane(CuratorDocument curatorDocument){
		super();
		try {
			this.curatorDocument = curatorDocument;
			databaseIDNode = new HashMap<Integer, DefaultMutableTreeNode>();
			editPanel = new JEditorPane();		
			initGUI();
			refreshStatistics();
			if(curatorDocument.getDocumentText().getClearText().length()<1)
			{
				Workbench.getInstance().warn("Unknown abstract or Full text for documente with ID "+curatorDocument.getAnnotatedDocument().getID());
				editPanel.setEnabled(false);	
			}
			else
			{
				synchronized(editPanel){editPanel.setText(curatorDocument.getDocumentText().getHtmlTextForEntities());}
				synchronized(editPanel){editPanel.setEditable(false);} 
				menu = getPopup();
				editPanel.setComponentPopupMenu(menu);    
				initiateCorrectForm(); 
				curatorDocument.getDocumentText().setClearText(getclearText());
				panelTextEditor = new PanelTextEditor(this);
			}
			mainSplitPane.setResizeWeight(0.85);
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

				statisticsScroll = new JScrollPane();
				{
					treeTerms = new JTree();
					statisticsScroll.setViewportView(treeTerms);
					treeTerms.setCellRenderer(new CuratorTermTreeCellRenderer());
					treeTerms.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							try {
								linkoutExtenalIDs(me);
							} catch (DatabaseLoadDriverException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							} catch (IOException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							} catch (SQLException e) {
								TreatExceptionForAIbench.treatExcepion(e);
							}
						}
					});
					statisticsScroll.setMinimumSize(new Dimension(100,400));
				}
				statisticsScroll.setViewportView(treeTerms);


				this.add(getJSplitPane3(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));					
				this.add(getJWaitPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));					

				mainSplitPane.setLeftComponent(getAnnotationsPanel());
				annotationsPanel.add(textScroll, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				annotationsPanel.add(getJpanelToolBar(), new GridBagConstraints(-1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				mainSplitPane.setRightComponent(statisticsScroll);

				editPanel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						editPanel.getHighlighter().removeAllHighlights();
					}
				});
			}
		}
	}

	private JPanel getJpanelToolBar() throws SQLException, DatabaseLoadDriverException {
		if(jPanelToolBAr == null)
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.5};
			thisLayout.columnWidths = new int[] {7};
			jPanelToolBAr = new JPanel();
			jPanelToolBAr.setLayout(thisLayout);
			jPanelToolBAr.add(getToolBar(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelToolBAr;
	}

	private Component getToolBar() {
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

	private void linkoutExtenalIDs(MouseEvent me) throws DatabaseLoadDriverException, IOException, SQLException {
		TreePath tp = treeTerms.getPathForLocation(me.getX(), me.getY());
		if(tp!=null)
		{
			Object obj = tp.getLastPathComponent();
			if (tp != null)
			{
				if(obj instanceof DefaultMutableTreeNode)
				{
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
					Object userdata = node.getUserObject();
					if(userdata instanceof IExternalID)
					{
						IExternalID extID = (IExternalID) userdata;
						List<String> urls = LinkOutsPropertiesGUI.getUrlsForSource(extID.getSourceID());
						if(urls.size()>0)
						{
							for(String url:urls)
								Help.internetAccess(url.replace("#", extID.getExternalID()));

						}
						else if(!Boolean.valueOf(PropertiesManager.getPManager().getProperty(CuratorDefaultSettings.USING_TERM_NAME_LINKOUT_SEARCH).toString()))
						{
							String url = "http://www.google.com/search?q=#";
							Help.internetAccess(url.replace("#", extID.getExternalID()));
						}
					}
				}
			}		
		}
	}

	private JPanel getJWaitPanel() {
		if(jWaitPanelWait==null)
		{
			jWaitPanelWait = new JPanelWait();
		}
		return jWaitPanelWait;
	}

	private void constructToolBar(){

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
	    
	    lookuptableButton = new JButton();
	    lookuptableButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/list.png")));
	    lookuptableButton.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/list.png")));
	    lookuptableButton.setToolTipText("Resource");
	    lookuptableButton.setBorderPainted(false);
	    lookuptableButton.setContentAreaFilled(false);
	    
	    lookuptableButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent evt) {    		
	    		openLookuptableGUI();
			}

	    });
	    toolBar.add(lookuptableButton);  
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
	    allInsertorRemove = new JCheckBox();
	    allInsertorRemove.setText("All Text  ");
	    allInsertorRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				caseSensative.setVisible(allInsertorRemove.isSelected());
			};
		});
	    toolBar.add(allInsertorRemove);	
	    caseSensative = new JCheckBox();
	    caseSensative.setText("Case sensitive");
	    caseSensative.setSelected(true);
	    caseSensative.setVisible(false);
	    toolBar.add(caseSensative);
	    toolBar.add(getJButtonHelp());
	}
	
	private void addNewClass(String classe)
	{
		JMenuItem mm = ((JMenuItem) menu.getComponent(1));
		mm.add(addItem(classe,Boolean.TRUE));
	}
	
	private JPopupMenu getPopup() throws SQLException, DatabaseLoadDriverException{
		JPopupMenu menu = new JPopupMenu("Options");
			
		menu.add(new ExternalLinksMenu(this.editPanel,curatorDocument));
		menu.add(getMenuTag());

		
		JMenuItem item = new JMenuItem("Correct Annotation");
		item.setBackground(Color.decode("#F0F0F0"));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				try {
					showCorrectForm(evt);
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Remove Annotation");
		item.setBackground(Color.decode("#F0F0F0"));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				try {
					panelTextEditor.removeTagButtonActionPerformed(evt,allInsertorRemove.isSelected(),caseSensative.isSelected(),look);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (ManualCurationException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
			}
		});
		menu.add(item);
			
		return menu;
	}
	
	private JMenu getMenuTag(){
		JMenu menutags = new JMenu("Add Annotation");
		menutags.setBackground(Color.decode("#E0E0E0"));
		Boolean dark = true;
		if(ClassProperties.getClassIDClass()!=null)
		{
			addNewClassOption(menutags, dark);		
			for(final int classID: ClassProperties.getClassIDClass().keySet())
			{
					String className = ClassProperties.getClassIDClass().get(classID);
					JMenuItem item = addItem(className,dark);
					menutags.add(item);			
			}
		}
		return menutags;
	}
	
	private JMenuItem addItem(final String name,Boolean dark)
	{
		JMenuItem item = new JMenuItem(name);
		
		if(dark)
		{
			item.setBackground(Color.decode("#E0E0E0"));
			dark=false;
		}
		else
		{
			item.setBackground(Color.decode("#F0F0F0"));
			dark=true;
		}
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					panelTextEditor.addTagButtonActionPerformed(evt,ClassProperties.getClassClassID().get(name),allInsertorRemove.isSelected(),caseSensative.isSelected(),getLookupTable());
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (ManualCurationException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
			}
		});
		return item;
	}

	private void addNewClassOption(JMenuItem menutags, boolean dark) {
		JMenuItem item = new JMenuItem("New Class");	
		if(dark)
		{
			item.setBackground(Color.decode("#E0E0E0"));
			dark=false;
		}
		else
		{
			item.setBackground(Color.decode("#F0F0F0"));
			dark=true;
		}
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addTermWhithNewClass(evt);
			}
		});
		menutags.add(item);
	}
	
	private void addTermWhithNewClass(ActionEvent evt) {

		final NewClassGUI resourcesChange = new NewClassGUI();
		resourcesChange.addWindowListener(new WindowListener(){

			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
			public void windowClosed(WindowEvent arg0) {
				if(resourcesChange.isNewClass())
				{
					int classID = -1;
					try {
						classID = ClassProperties.insertNewClass(resourcesChange.getClasse());
						panelTextEditor.addTagButtonActionPerformed(null,classID,allInsertorRemove.isSelected(),caseSensative.isSelected(),getLookupTable());
						addNewClass(resourcesChange.getClasse().toLowerCase());
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (ManualCurationException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}

				}
			}
		});
	}

	/**
	 * Zoom in html
	 */
	private synchronized void zoomIn(){
		this.curatorDocument.getCuratorView().setZoom(true);
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
		curatorDocument.getDocumentText().setHtmlTextForEntities(curatorDocument.getDocumentText().getHtmlTextForEntities().replace(last, next));		
		synchronized(editPanel){editPanel.setText(curatorDocument.getDocumentText().getHtmlTextForEntities());}
		goToCaret(getSelectedText());
	}
	
	/**
	 * Zoom out html
	 */
	private synchronized void zoomOut(){
		this.curatorDocument.getCuratorView().setZoom(false);
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
			annotationsPanelLayout.rowWeights = new double[] {0.0, 1.0};
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
		treeTerms.setModel(this.getTreeTermsModel());
	}
	
	private DefaultTreeModel getTreeTermsModel() throws SQLException, DatabaseLoadDriverException{
		databaseIDNode = new HashMap<Integer, DefaultMutableTreeNode>();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Class(es)");		
		String taux;
		int naux;	
		CuratorDocumentAnnotations annotations = curatorDocument.getAnnotations();
		ShortAnnotation annotation;
		Dictionary dic = new Dictionary(1, new String(),new String());
		for(String tag : annotations.getClassTermAnnotations().keySet())
		{
			DefaultMutableTreeNode child = new DefaultMutableTreeNode();	
			int classID = ClassProperties.getClassClassID().get(tag);
			child.setUserObject((AnnotationColorStyleProperty) CorporaProperties.getCorporaClassColor(classID));
			for(String term : annotations.getClassTermAnnotations().get(tag).keySet())
			{
				annotation = annotations.getClassTermAnnotations().get(tag).get(term);
				naux = annotation.getCount();
				taux = annotation.getCommonName();	
				if(taux==null)
				{
					annotation.setCommonName(term);
				}
				DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(new TermNode(annotation.getTerm(), naux, annotation.getAnnotationResourceAssociatedID()));
				DefaultMutableTreeNode subsubchild;
				if(annotation.getAnnotationResourceAssociatedID()>0)
				{
					if(databaseIDNode.containsKey(annotation.getAnnotationResourceAssociatedID()))
					{
						DefaultMutableTreeNode father = databaseIDNode.get(annotation.getAnnotationResourceAssociatedID());
						father.add(subchild);
						((TermNode) father.getUserObject()).addCount(naux);
					}
					else
					{
						subsubchild = new DefaultMutableTreeNode(new TreeNodeText("DatabaseID:"));
						subchild.add(subsubchild);	
						subsubchild = new DefaultMutableTreeNode(annotation.getAnnotationResourceAssociatedID());
						subchild.add(subsubchild);	
						IResourceElementSet<IResourceElement> list = dic.getTermSynomns(Integer.valueOf(annotation.getAnnotationResourceAssociatedID()));
						IResourceElement elemTerm = dic.getTerm(annotation.getAnnotationResourceAssociatedID());
						String resourceType = dic.getResourceType(annotation.getAnnotationResourceAssociatedID());
						boolean resourceISARule = resourceType.equals(GlobalOptions.resourcesRuleSetName);
						if(!elemTerm.getTerm().equals(term) || list.size()>0)
						{
							if(resourceISARule)
							{
								subsubchild = new DefaultMutableTreeNode(new TreeNodeText("Main Rule:"));
							}
							else
							{
								subsubchild = new DefaultMutableTreeNode(new TreeNodeText("Synonyms:"));

							}
							subchild.add(subsubchild);
							if(!elemTerm.getTerm().equals(term) )
							{
								subsubchild = new DefaultMutableTreeNode(elemTerm.getTerm());
								subchild.add(subsubchild);
							}
							for(IResourceElement elem :list.getElements())
							{
								if(!elem.getTerm().equals(term))
								{
									subsubchild = new DefaultMutableTreeNode(elem.getTerm());
									subchild.add(subsubchild);
								}
							}
						}
						List<IExternalID> externalIDSource = dic.getexternalIDandSorceIDandSource(Integer.valueOf(annotation.getAnnotationResourceAssociatedID()));
						if(externalIDSource.size()>0)
						{
							subsubchild = new DefaultMutableTreeNode(new TreeNodeText("ExternalIDs:"));
							subchild.add(subsubchild);
							for(IExternalID id :externalIDSource)
							{
								subsubchild = new DefaultMutableTreeNode(id);
								subchild.add(subsubchild);
							}
						}
						subsubchild = new DefaultMutableTreeNode(new TreeNodeText("Status :"));
						subchild.add(subsubchild);
						if(elemTerm.isActive())
						{
							subsubchild = new DefaultMutableTreeNode("Active");
						}
						else
						{
							subsubchild = new DefaultMutableTreeNode("Inactive");
						}
						subchild.add(subsubchild);
						if(resourceISARule)
						{
							subsubchild = new DefaultMutableTreeNode(new TreeNodeText("Other Rule Variation :"));
						}
						else
						{
							subsubchild = new DefaultMutableTreeNode(new TreeNodeText("Annotated Synonyms :"));
						}
						subchild.add(subsubchild);
						databaseIDNode.put(annotation.getAnnotationResourceAssociatedID(), subchild);	
						child.add(subchild);
					}
				}
				else
				{
					child.add(subchild);
				}
			}		
			node.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	
	
	
	private void initiateCorrectForm(){
		correctSelectionForm = new CorrectSelectionDialog();
		correctSelectionForm.setEnabled(false);
		correctSelectionForm.addActionListnerToOkButton(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					panelTextEditor.applyCorrectSelectionButtonActionPerfomed(evt, correctSelectionForm,allInsertorRemove.isSelected(),caseSensative.isSelected(),getLookupTable());
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (ManualCurationException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
			}
		});
	}
	
	private void showCorrectForm(ActionEvent evt) throws SQLException, DatabaseLoadDriverException{
		synchronized(editPanel){setSelectedText(editPanel.getSelectedText());}
		String term = getSelectedText();
		int start = editPanel.getSelectionStart()-PanelTextEditor.error;
		int end = editPanel.getSelectionEnd()-PanelTextEditor.error;

		AnnotationPosition posC = new AnnotationPosition(start,end);
		if(term==null)
		{
			Workbench.getInstance().warn("Please select the text you want to correct! Ignoring...");
		}
		else if(!curatorDocument.getNERAnnotations().containsKey(posC))
		{
			Workbench.getInstance().warn("Please select an annotation of text! Ignoring...");
		}
		else
		{	
			correctSelectionForm.getNewTermTextArea().setText(term);
			correctSelectionForm.getTagList().clearSelection();
			String currentAnnotarion = curatorDocument.getAnnotations().getTermClass().get(term);
			if(currentAnnotarion!=null)
			{
				correctSelectionForm.getCurrentTagTextArea().setText(currentAnnotarion);
			}
			else
			{
				correctSelectionForm.getCurrentTagTextArea().setText("");
			}
			correctSelectionForm.setEnabled(true);
			correctSelectionForm.updateClassModel();
			Utilities.centerOnOwner(correctSelectionForm);
			correctSelectionForm.setModal(true);
			correctSelectionForm.setVisible(true);
		}
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

	public void changed() throws SQLException, DatabaseLoadDriverException{
		CuratorDocument doc = this.curatorDocument;
		CuratorDocumentPreviousState previousData = new CuratorDocumentPreviousState(doc.getDocumentText(),doc.getAnnotations(),doc.getNERAnnotations());
		doc.setPreviousData(previousData);
	}
	
	private void openLookuptableGUI() {
		final ChangeResourcesGUI chResouGUI = new ChangeResourcesGUI(curatorDocument.getAnnotatedDocument(), getLookupTable()); 		
			chResouGUI.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
			
			public void windowClosed(WindowEvent e) {
				if(chResouGUI.getResource()!=null)
				{
					setLookupTable((LookupTable)chResouGUI.getResource());
				}
			}
			
		});
	}
	
	public JButton gtUnButton(){return this.undoButton;}
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
			synchronized(editPanel){editPanel.setText(curatorDocument.getDocumentText().getHtmlTextForEntities());}
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

	public LookupTable getLookupTable() {
		return look;
	}

	public void setLookupTable(LookupTable look) {
		this.look = look;
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();	
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Curator_View#Entities_View");
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
	
	public CuratorDocument getCuratorDocument() {
		return curatorDocument;
	}

	

}
