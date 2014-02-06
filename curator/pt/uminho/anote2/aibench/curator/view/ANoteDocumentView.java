package pt.uminho.anote2.aibench.curator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
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
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.gui.ChooseColorGUI;
import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.curator.datastructures.ANoteAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.ANoteDocument;
import pt.uminho.anote2.aibench.curator.datastructures.ANoteDocumentAnnotations;
import pt.uminho.anote2.aibench.curator.datastructures.ANoteDocumentPreviousData;
import pt.uminho.anote2.aibench.curator.datastructures.AiANoteDocument;
import pt.uminho.anote2.aibench.curator.exeption.ANoteFileNotExistsException;
import pt.uminho.anote2.aibench.curator.gui.ChangeResourcesGUI;
import pt.uminho.anote2.aibench.curator.gui.CorrectSelectionDialog;
import pt.uminho.anote2.aibench.curator.gui.FilterClassesGUI;
import pt.uminho.anote2.aibench.curator.gui.NewClassGUI;
import pt.uminho.anote2.aibench.curator.gui.PanelTextEditor;
import pt.uminho.anote2.aibench.curator.utils.ExternalLinksMenu;
import pt.uminho.anote2.aibench.curator.utils.MyTreeCellRenderer;
import pt.uminho.anote2.aibench.curator.utils.ParsingUtils;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.datastructures.annotation.AnnotationColorStyleProperty;
import pt.uminho.anote2.datastructures.resources.Dictionary.Dictionary;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.dictionary.IDictionary;
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
public class ANoteDocumentView extends JPanel implements Observer {

	private static final long serialVersionUID = 9155059515796055999L;

	private JEditorPane editPanel;
	private JScrollPane textScroll;
	private JScrollPane statisticsScroll;
	private JScrollPane colorsPanel;
	private JScrollPane tasksColorScroll;
	private JToolBar toolBar;	
	private JPanel annotationsPanel;
	private JSplitPane mainSplitPane;
	private JButton jButtonHelp;
	private JSplitPane statistSplitPane;
	private JSplitPane textSplitPane;
	private JPanel tasksColorPanel;
	private JButton zooIn;
	private JButton zoomOut;
	private JButton undoButton;
	private JButton searchButton;
	private JCheckBox allInsertorRemove;
	private JTextField searchTextField;
	private JButton filterButton;
	private JPopupMenu menu;
	private CorrectSelectionDialog correctSelectionForm;	
	private JTree treeTerms; // JTree with the annotated classes and terms
	private JTree treeStructure; // JTree with the annotated complex tags
	private int zoom = 12;
	private int caretPosition;
	private String selectedText;
	private String textToFind;
	private int lastFound;
	private String[] tags;
	private PanelTextEditor panelTextEditor;
	private NERDocumentAnnotation nerDoc;
	private AiANoteDocument aiDocument;
	private int column=1;
	private int line=0;
	private IResource<IResourceElement> dic = null;
	private JButton lookuptableButton;

	public ANoteDocumentView(NERDocumentAnnotation nerDoc) throws IOException, ANoteFileNotExistsException,SQLException{
		super();
		this.nerDoc=nerDoc;
		this.aiDocument = new AiANoteDocument(nerDoc);
		editPanel = new JEditorPane();				
		initGUI();
		synchronized(editPanel){editPanel.setText(aiDocument.getDocument().getDocumentText().getHtmlText());}
	    synchronized(editPanel){editPanel.setEditable(false);} 
	    refreshStatistics();
	    menu = getPopup();
	    editPanel.setComponentPopupMenu(menu);    
	    initiateCorrectForm(); 
		aiDocument.getDocument().getDocumentText().setClearText(getclearText());
		insertButton();
		panelTextEditor = new PanelTextEditor(this);
		aiDocument.setOpened(true);
	    aiDocument.addObserver(this);
	}

	public int getZoom() {
		return zoom;
	}

	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.5};
				thisLayout.rowHeights = new int[] {20};
				thisLayout.columnWeights = new double[] {0.5};
				thisLayout.columnWidths = new int[] {7};
				
				this.setLayout(thisLayout);
				{
					toolBar = new JToolBar();
					toolBar.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(BevelBorder.LOWERED),
						null));
				}
				{

					editPanel.setContentType("text/html");
					textScroll = new JScrollPane(editPanel);

				   	statisticsScroll = new JScrollPane(treeTerms);
					{
						treeTerms = new JTree();
						statisticsScroll.setViewportView(treeTerms);
						treeTerms.setCellRenderer(new MyTreeCellRenderer());
						statisticsScroll.setMinimumSize(new Dimension(100,400));
					}
					
					tasksColorScroll = new JScrollPane(treeStructure);
					{
						treeStructure = new JTree();
						tasksColorScroll.setViewportView(treeStructure);
						tasksColorScroll.setMinimumSize(new Dimension(100,100));
					}
					
					this.add(getJSplitPane3(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));					
					
					mainSplitPane.setLeftComponent(getAnnotationsPanel());
					annotationsPanel.add(getJSplitPane1(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					annotationsPanel.add(toolBar, new GridBagConstraints(-1, -1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					
					colorsPanel = new JScrollPane();
					colorsPanel.setViewportView(getTasksColorPanel());
					this.textSplitPane.setBottomComponent(colorsPanel);
					
					this.textSplitPane.setTopComponent(textScroll);
					
					mainSplitPane.setRightComponent(getJSplitPane2());
					statistSplitPane.setBottomComponent(tasksColorScroll);
					statistSplitPane.setTopComponent(statisticsScroll);
					
					editPanel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							editPanel.getHighlighter().removeAllHighlights();
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		constructToolBar();
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
	    
		undoButton = new JButton();
		undoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/undo.png")));
		undoButton.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/undoPressed.png")));
		undoButton.setToolTipText("Undo");
	    undoButton.setBorderPainted(false);
	    undoButton.setContentAreaFilled(false);
	    undoButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent evt) {
				undo();
			}
	    });
	    toolBar.add(undoButton);
	    undoButton.setEnabled(false);

	    toolBar.addSeparator();
	    
	    filterButton = new JButton();
	    filterButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/misc_2_22.png")));
	    filterButton.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/misc_2_22Pressed.png")));
	    filterButton.setToolTipText("Filter Entities");
	    filterButton.setBorderPainted(false);
	    filterButton.setContentAreaFilled(false);
	    
	    filterButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent evt) {
	    		Point p = filterButton.getLocation();
	    		{
	    			Point po = getLocationOnScreen();
	    			double xaux=po.getX()+p.getX()+20;
	    			double yaux=po.getY()+p.getY()+20;
	    			p.setLocation(xaux, yaux);
	    		}
	    		final FilterClassesGUI cls_gui = new FilterClassesGUI(aiDocument.getDocument().getProperties().getTasks(), p);
	    		
	    		cls_gui.addWindowListener(new WindowListener(){

					public void windowActivated(WindowEvent arg0) {}
					public void windowClosing(WindowEvent arg0) {}
					public void windowDeactivated(WindowEvent arg0) {}
					public void windowDeiconified(WindowEvent arg0) {}
					public void windowIconified(WindowEvent arg0) {}
					public void windowOpened(WindowEvent arg0) {}
					
					public void windowClosed(WindowEvent e) {
						List<String> entities = cls_gui.getEnt_selected();
						if(entities!=null)
							filterEntities(entities);
					}
					
				});
			}
	    });
	    
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
	    allInsertorRemove.setText("All Text");
	    toolBar.add(allInsertorRemove);	 
	    toolBar.add(getJButtonHelp());
	}
	
	private void addNewClass(String classe)
	{
		JMenuItem mm = ((JMenuItem) menu.getComponent(1));
		mm.add(addItem(classe,Boolean.TRUE));
		/**
		 * Ver como passar o New para ultimo depois
		 */
		//addNewClassOption(mm,Boolean.TRUE);	
	}
	
	private JPopupMenu getPopup(){
		JPopupMenu menu = new JPopupMenu("Options");
			
		menu.add(new ExternalLinksMenu(this.editPanel));	
		menu.add(getMenuTag());

		
		JMenuItem item = new JMenuItem("Correct Annotation");
		item.setBackground(Color.decode("#F0F0F0"));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				showCorrectForm(evt);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Remove Annotation");
		item.setBackground(Color.decode("#F0F0F0"));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				panelTextEditor.removeTagButtonActionPerformed(evt,allInsertorRemove.isSelected());
			}
		});
		menu.add(item);
		
//		item = new JMenuItem("Add Relation");
//		item.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent evt) {
//				panelTextEditor.addRelationButtonActionPerfomed(evt);
//			}
//		});
//		menu.add(item);
			
		return menu;
	}
	
	private JMenu getMenuTag(){
		JMenu menutags = new JMenu("Add Annotation");
		menutags.setBackground(Color.decode("#E0E0E0"));
		Boolean dark = true;
		if(aiDocument.getDocument().getProperties().getTasks()!=null)
		{
			int i = 0;
			this.tags = new String[aiDocument.getDocument().getProperties().getTasks().size()];
			for(final AnnotationColorStyleProperty property: aiDocument.getDocument().getProperties().getTasks().values())
			{
				if(property.getColor().compareTo("none")!=0)
				{
					this.tags[i++]=property.getName();
					JMenuItem item = addItem(property.getName(),dark);
					menutags.add(item);			
				}		
			}
			addNewClassOption(menutags, dark);		
		}
		return menutags;
	}
	
	public JMenuItem addItem(final String name,Boolean dark)
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
				panelTextEditor.addTagButtonActionPerformed(evt,aiDocument.getDocument().getProperties().getClassClassID().get(name),allInsertorRemove.isSelected(),getDic());
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
	
	public void addTermWhithNewClass(ActionEvent evt) {

			final NewClassGUI resourcesChange = new NewClassGUI(getAiDocument().getDocument().getProperties().getClassClassID().keySet());
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
						IDatabase db = ((Corpus)nerDoc.getCorpus()).getCorpora().getDb();
						IDictionary lookup = new Dictionary(db,0,"","");
						int classID = lookup.addElementClass(resourcesChange.getClasse());
						getAiDocument().getDocument().getProperties().getClassClassID().put(resourcesChange.getClasse(), classID);
						getAiDocument().getDocument().getProperties().getClassIDclass().put(classID, resourcesChange.getClasse());
						String color = null;
						try {
							color = Corpora.getColorForClass(classID,db);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						getAiDocument().getDocument().getProperties().getTasks().put(resourcesChange.getClasse(), new AnnotationColorStyleProperty(resourcesChange.getClasse(),color,""));
						panelTextEditor.addTagButtonActionPerformed(null,classID,allInsertorRemove.isSelected(),getDic());
						addNewClass(resourcesChange.getClasse());
						insertButton(resourcesChange.getClasse(),color);
						colorsPanel.updateUI();
					}
				}
			});
	}

	/**
	 * Zoom in html
	 */
	public synchronized void zoomIn(){
		synchronized(editPanel){setCaretPosition(editPanel.getCaretPosition());}		
		String last = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + this.zoom + "pt\" align=\"justify\">";
		this.zoom++;
		String next = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + this.zoom + "pt\" align=\"justify\">";
		aiDocument.getDocument().getDocumentText().setHtmlText(aiDocument.getDocument().getDocumentText().getHtmlText().replace(last, next));		
		synchronized(editPanel){editPanel.setText(aiDocument.getDocument().getDocumentText().getHtmlText());}
		goToCaret(getSelectedText());
	}
	
	/**
	 * Zoom out html
	 */
	public synchronized void zoomOut(){
		synchronized(editPanel){setCaretPosition(editPanel.getCaretPosition());}	
		String last = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + this.zoom + "pt\" align=\"justify\">";
		this.zoom--;
		String next = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + this.zoom + "pt\" align=\"justify\">";	
		aiDocument.getDocument().getDocumentText().setHtmlText(aiDocument.getDocument().getDocumentText().getHtmlText().replace(last, next));
		synchronized(editPanel){editPanel.setText(aiDocument.getDocument().getDocumentText().getHtmlText());}
		goToCaret(getSelectedText());
	}
		
	private synchronized void filterEntities(List<String> entities){
		synchronized(editPanel){setCaretPosition(editPanel.getCaretPosition());}	
		String htmlText = aiDocument.getDocument().getDocumentText().getHtmlText();
		htmlText = aiDocument.getDocument().getAnnotations().filterTags(htmlText, entities, aiDocument.getDocument().getProperties().getTasks());
		aiDocument.getDocument().getDocumentText().setHtmlText(htmlText);		
		synchronized(editPanel){editPanel.setText(htmlText);}
		goToCaret(getSelectedText());
	}
	
	private synchronized void undo(){
//		synchronized(editPanel){setCaretPosition(editPanel.getCaretPosition());}	
//		ANoteDocument doc = this.aiDocument.getDocument();
//		doc.setDocumentText(doc.getPreviousData().getDocumentText());
//		doc.setAnnotations(doc.getPreviousData().getAnnotations());	
//		refreshStatistics();
//		synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlText());}	
//		goToCaret(getSelectedText());
//		undoButton.setEnabled(false);
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
		
		lastFound = aiDocument.getDocument().getDocumentText().getClearText().indexOf(text, lastFound);
		
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
	
	private JPanel getTasksColorPanel() {
		if (tasksColorPanel == null) {
			tasksColorPanel = new JPanel();
			GridBagLayout tasksColorPanelLayout = new GridBagLayout();
			tasksColorPanelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			tasksColorPanelLayout.rowHeights = new int[] {7, 7, 7};
			tasksColorPanelLayout.columnWeights = new double[] {0.0, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.0, 0.1};
			tasksColorPanelLayout.columnWidths = new int[] {18, 30, 10, 30, 10, 30, 10, 30, 10, 30, 10};
			tasksColorPanel.setLayout(tasksColorPanelLayout);
			tasksColorPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return tasksColorPanel;
	}
	
	private JSplitPane getJSplitPane1() {
		if(textSplitPane == null) {
			textSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			textSplitPane.setOneTouchExpandable(true);
			textSplitPane.setResizeWeight(0.95);
		}
		return textSplitPane;
	}
	
	private JSplitPane getJSplitPane2() {
		if(statistSplitPane == null) {
			statistSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			statistSplitPane.setOneTouchExpandable(true);
			statistSplitPane.setResizeWeight(0.8);
		}
		return statistSplitPane;
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

	private void insertButton(){

		String color;
		for(final String task: aiDocument.getDocument().getProperties().getTasks().keySet())
		{
			color = aiDocument.getDocument().getProperties().getTasks().get(task).getColor();
			if(!color.equals("none"))
			{
				insertButton(task,color);
			}
		}
	}
	
	private void insertButton(final String task,String color)
	{
		final JButton b = new JButton();
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				changeColor(task, b);
			}
		});
		b.setBackground(Color.decode(color));
		tasksColorPanel.add(b, new GridBagConstraints(column, line, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		JLabel l = new JLabel(task);
		tasksColorPanel.add(l, new GridBagConstraints(column+1, line, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 0), 0, 0));
		
		if(++line==3)
		{
			line=0;
			column+=2;
		}
	}
	
	
	
	private void changeColor(String task, Component cmp){
		final String tasks = task;
		final Component button = cmp;
		final String colorNow = aiDocument.getDocument().getProperties().getTasks().get(task).getColor();
		int classID = aiDocument.getDocument().getProperties().getClassClassID().get(task);
		final ChooseColorGUI color = new ChooseColorGUI(classID, colorNow,((Corpus) nerDoc.getCorpus()).getCorpora().getDb());
		color.addWindowListener(
				new WindowListener(){
			public void windowActivated(WindowEvent arg0) {}
			public void windowClosing(WindowEvent arg0) {}
			public void windowDeactivated(WindowEvent arg0) {}
			public void windowDeiconified(WindowEvent arg0) {}
			public void windowIconified(WindowEvent arg0) {}
			public void windowOpened(WindowEvent arg0) {}
									
			public void windowClosed(WindowEvent arg0) {
				if(!color.getColor().equals(colorNow))
				{
						synchronized(editPanel){setCaretPosition(editPanel.getCaretPosition());}
						aiDocument.getDocument().getProperties().getTasks().get(tasks).setColor(color.getColor());
						aiDocument.getDocument().rebuildHtml(getZoom());
						refreshStatistics();
						synchronized(editPanel){editPanel.setText(aiDocument.getDocument().getDocumentText().getHtmlText());}
						button.setBackground(Color.decode(aiDocument.getDocument().getProperties().getTasks().get(tasks).getColor()));
						goToCaret(getSelectedText());
				}
				else
				{
				}
			}
		});	
	}
	
	
	public synchronized void refreshStatistics(){
		treeTerms.setModel(this.getTreeTermsModel());
		treeStructure.setModel(this.getTreeStructureModel());
		((DefaultTreeCellRenderer) treeStructure.getCellRenderer()).setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
	private DefaultTreeModel getTreeTermsModel(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("bio_classes");		
		String taux;
		int naux;	
		ANoteDocumentAnnotations annotations = aiDocument.getDocument().getAnnotations();
		ANoteAnnotation annotation;
		for(String tag : annotations.getClassTermAnnotations().keySet())
		{
			DefaultMutableTreeNode child = new DefaultMutableTreeNode();	
			child.setUserObject((AnnotationColorStyleProperty) aiDocument.getDocument().getProperties().getTasks().get(tag));
			for(String term : annotations.getClassTermAnnotations().get(tag).keySet())
			{
				annotation = annotations.getClassTermAnnotations().get(tag).get(term);
				naux = annotation.getCount();
				taux = annotation.getCommonName();	
				if(taux==null)
				{
					annotation.setCommonName(term);
				}				
				DefaultMutableTreeNode subchild = new DefaultMutableTreeNode(annotation.getTerm() + " (" + naux + ")");
				child.add(subchild);
			}		
			node.add(child);
		}
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	private DefaultTreeModel getTreeStructureModel(){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("Struct");	
		int section_index = 0;
		boolean not_found = true;
		for(String complexTag: aiDocument.getDocument().getDocumentStructure().getPieces().keySet())
		{
			if(complexTag.contains("SECTION"))
			{
				if(not_found)
				{
					node.add(new DefaultMutableTreeNode("SECTIONS"));
					not_found=false;
				}
				((DefaultMutableTreeNode) node.getChildAt(section_index)).add(new DefaultMutableTreeNode(complexTag.substring(8)));
			}
			else	
				node.add(new DefaultMutableTreeNode(complexTag));
			
			if(not_found)
				section_index++;
		}
		treeStructure.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				goToTextPos(e.getPath().getLastPathComponent());					
			}
		});
		DefaultTreeModel model = new DefaultTreeModel(node);
		return model;
	}
	
	
	private void initiateCorrectForm(){
		correctSelectionForm = new CorrectSelectionDialog(Workbench.getInstance().getMainFrame(),this.tags);
		correctSelectionForm.getAplyButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panelTextEditor.applyCorrectSelectionButtonActionPerfomed(evt, correctSelectionForm,allInsertorRemove.isSelected(),getDic());
			}
		});
	}
	
	private void showCorrectForm(ActionEvent evt){
		synchronized(editPanel){setSelectedText(editPanel.getSelectedText());}
		String term = getSelectedText();
		int start = editPanel.getSelectionStart()-PanelTextEditor.error;
		int end = editPanel.getSelectionEnd()-PanelTextEditor.error;

		AnnotationPosition posC = new AnnotationPosition(start,end);
		if(term==null)
		{
			Workbench.getInstance().warn("Please select the text you want to correct! Ignoring...");
		}
		else if(!aiDocument.getDocument().getAnnotNERAnnotations().containsKey(posC))
		{
			Workbench.getInstance().warn("Please select an annotation of text! Ignoring...");
		}
		else
		{	
			correctSelectionForm.getNewTermTextArea().setText(term);
			correctSelectionForm.getTagList().clearSelection();
			String currentAnnotarion = aiDocument.getDocument().getAnnotations().getTermClass().get(term);
			if(currentAnnotarion!=null)
			{
				correctSelectionForm.getCurrentTagTextArea().setText(currentAnnotarion);
			}
			else
			{
				correctSelectionForm.getCurrentTagTextArea().setText("");
			}
			correctSelectionForm.setEnabled(true);
			Utilities.centerOnOwner(correctSelectionForm);
			correctSelectionForm.setVisible(true);
		}
	}
	
	
	
	
	
	private void goToTextPos(Object comp_tag){
		int caret;
		String c_tag = comp_tag.toString();
		String token = aiDocument.getDocument().getDocumentStructure().getPieces().get(c_tag);
		
		if(token==null) // comp_tag is a Section
		{	
			token = c_tag;
		}
		if(token.length()>50)
		{
			token = token.substring(0,50);
		}	
		caret = aiDocument.getDocument().getDocumentText().getClearText().indexOf(token);
		if(caret!=-1)
			getEditPanel().setCaretPosition(caret);

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

	public void changed(){
		ANoteDocument doc = this.aiDocument.getDocument();
		ANoteDocumentPreviousData previousData = new ANoteDocumentPreviousData(doc.getDocumentText(),doc.getAnnotations(),doc.getAnnotNERAnnotations());
		doc.setPreviousData(previousData);
		this.undoButton.setEnabled(true);		
	}
	
	private void openLookuptableGUI() {
		final ChangeResourcesGUI chResouGUI = new ChangeResourcesGUI(nerDoc, getDic()); 		
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
					setLookupTable(chResouGUI.getResource());
				}
			}
			
		});
	}
	
	public JButton gtUnButton(){return this.undoButton;}
	public AiANoteDocument getAiDocument(){return this.aiDocument;}
	synchronized public JEditorPane getEditPanel() {return editPanel;}
	public synchronized int getCaretPosition() {return caretPosition;}
	public synchronized void setCaretPosition(int caretPosition) {this.caretPosition = caretPosition;}
	public synchronized String getSelectedText() {return selectedText;}
	
	public synchronized void setSelectedText(String selectedtext) {
		this.selectedText = ParsingUtils.removeBoundarieSpaces(selectedtext);
	}
	
	public void update(Observable o, Object arg) {
		refreshStatistics();
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
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Curator_View_Annotated_Document");
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}
}
