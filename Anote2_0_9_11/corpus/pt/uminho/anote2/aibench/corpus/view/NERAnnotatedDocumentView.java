package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.NERDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.corpus.structures.CorporaPropertiesGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ISentence;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;
import pt.uminho.generic.genericpanel.view.JPanelWait;



public class NERAnnotatedDocumentView extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1229351096914888366L;
	private NERDocumentAnnotation nerAnnotDoc;
	private JTable jTableSentences;
	private JScrollPane jScrollPaneSentences;
	private JPanel jPanelSentenceAnnotations;
	private JTabbedPane jTabbedPaneAnnotations;
	private JButton jButtonHelp;
	private List<ISentence> sentences;
	private TableSearchPanel jTableSearch;
	private JPanelWait jWaitPanelWait;

	public NERAnnotatedDocumentView(NERDocumentAnnotation nerAnnotDoc)
	{
		this.nerAnnotDoc=nerAnnotDoc;
		try {
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		this.nerAnnotDoc.addObserver(this);
	}

	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};			
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.add(getJButtonHelp(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJTabbedPaneAnnotations(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJWaitPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		}

	}
	
	private JPanel getJWaitPanel() {
		if(jWaitPanelWait==null)
		{
			jWaitPanelWait = new JPanelWait();
		}
		return jWaitPanelWait;
	}
	
	private void stopWaitPAnel() {
		jWaitPanelWait.setVisible(false);	
		jWaitPanelWait.setEnabled(false);
		jTabbedPaneAnnotations.setEnabled(true);
		jTabbedPaneAnnotations.setVisible(true);	
	}

	private void startWaitPAnel()
	{
		jTabbedPaneAnnotations.setVisible(false);
		jTabbedPaneAnnotations.setEnabled(false);
		jWaitPanelWait.setEnabled(true);
		jWaitPanelWait.setVisible(true);	
	}

	
	public JPanel getJpanelAnnotations() throws SQLException, DatabaseLoadDriverException
	{
		if(jTableSearch == null) {
			jTableSearch = new TableSearchPanel();
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableSearch.setModel(getModelAnnots() );
		}
		return jTableSearch;
	}




	private TableModel getModelAnnots() throws SQLException, DatabaseLoadDriverException {
		DefaultTableModel tableModel = new DefaultTableModel(){
			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 3 || columnIndex == 2)?(Integer.class):String.class;
			}
			
		};
		String[] columns = new String[] {"Entity", "Class", "Start","End"};
		tableModel.setColumnIdentifiers(columns);
		AnnotationPositions annotPos = nerAnnotDoc.getEntityAnnotationPositions();
		Object[][] data;
		data = new Object[annotPos.getAnnotations().size()][4];
		annotPos.getAnnotations().keySet();
		Set<AnnotationPosition> annot = annotPos.getAnnotations().keySet();
		int i=0;
		for(AnnotationPosition pos :annot)
		{
			IAnnotation annotation = annotPos.getAnnotations().get(pos);
			if(annotation instanceof IEntityAnnotation)
			{
				IEntityAnnotation elem = (IEntityAnnotation) annotPos.getAnnotations().get(pos);
				data[i][0] = elem.getAnnotationValue();
				data[i][1] = ClassProperties.getClassIDClass().get(elem.getClassAnnotationID());
				data[i][2] = pos.getStart();	
				data[i][3] = pos.getEnd();	
				tableModel.addRow(data[i]);
				i++;
			}
		}	
		return tableModel;
	}

	public void update(Observable arg0, Object arg1) {
		startWaitPAnel();
		try {
			jTableSearch.setModel(getModelAnnots());
			createTableData();
			completetable();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		stopWaitPAnel();
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Annotated_Document_Default_View");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	private JTabbedPane getJTabbedPaneAnnotations() throws SQLException, DatabaseLoadDriverException {
		if(jTabbedPaneAnnotations == null) {
			jTabbedPaneAnnotations = new JTabbedPane();
			jTabbedPaneAnnotations.addTab("Annotations", null, getJpanelAnnotations(), null);
			jTabbedPaneAnnotations.addTab("Sentences", null, getJPanelSentenceAnnotations(), null);
		}
		return jTabbedPaneAnnotations;
	}
	
	private JPanel getJPanelSentenceAnnotations() throws SQLException, DatabaseLoadDriverException {
		if(jPanelSentenceAnnotations == null) {
			jPanelSentenceAnnotations = new JPanel();
			jPanelSentenceAnnotations.setBorder(BorderFactory.createTitledBorder("Entities (Organized By Sentence)"));
			jPanelSentenceAnnotations = new JPanel();
			GridBagLayout jPanelSentenceAnnotationsLayout = new GridBagLayout();
			jPanelSentenceAnnotationsLayout.rowWeights = new double[] {0.1, 0.01};
			jPanelSentenceAnnotationsLayout.rowHeights = new int[] {7, 20};
			jPanelSentenceAnnotationsLayout.columnWeights = new double[] {0.1};
			jPanelSentenceAnnotationsLayout.columnWidths = new int[] {7};
			jPanelSentenceAnnotations.setLayout(jPanelSentenceAnnotationsLayout);
			jPanelSentenceAnnotations.setPreferredSize(new java.awt.Dimension(983, 765));
			jPanelSentenceAnnotations.add(getJScrollPaneSentences(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSentenceAnnotations.add(CorporaPropertiesGUI.getjScrollPaneClassColor(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelSentenceAnnotations;
	}
	
	private JScrollPane getJScrollPaneSentences() throws SQLException, DatabaseLoadDriverException {
		if(jScrollPaneSentences == null) {
			jScrollPaneSentences = new JScrollPane();
			jScrollPaneSentences.setViewportView(getJTableSentences());
		}
		return jScrollPaneSentences;
	}
	
	private JTable getJTableSentences() throws SQLException, DatabaseLoadDriverException {
		if(jTableSentences == null) {
			createTableData();
			completetable();
		}

		return jTableSentences;
	}

	private void createTableData() throws SQLException, DatabaseLoadDriverException {
		sentences = nerAnnotDoc.getSentencesText();
		DefaultTableModel jTableSentencesModel = new DefaultTableModel();
		String[] columns = new String[] {"Line"};
		jTableSentencesModel.setColumnIdentifiers(columns);
		Object[] data = new Object[1];
		for(int i=1;i<=sentences.size();i++)
		{
			data[0] = i;
			jTableSentencesModel.addRow(data);
		}
		jTableSentences = new JTable();
		jTableSentences.setModel(jTableSentencesModel);
	}

	private void completetable() {
		jTableSentences.getColumnModel().getColumn(0).setMaxWidth(80);
		jTableSentences.getColumnModel().getColumn(0).setMinWidth(80);
		jTableSentences.getColumnModel().getColumn(0).setPreferredWidth(80);
		jTableSentences.setRowHeight(60);
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Sentence with annotations");
		viewColumn.setCellRenderer(new TableCellRenderer(){		
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JEditorPane editPanel = new JEditorPane();
				editPanel.setContentType("text/html");
				ISentence sentence = sentences.get(row);
				String html = new String();
				try {
					html = gerateHtmlForAnnotations(sentence);
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}				if(row%2!=0)
				{
					editPanel.setText("<body bgcolor=\"#E8E8E8 \">" + html + "</body>"); 
				}
				else
				{
					editPanel.setText(html);
				}
				if(isSelected)
				{
					editPanel.setText("<body bgcolor=\"#66FFCC\">" + html + "</body>"); 
				}
				return editPanel;
			}				
		});	
		viewColumn.setCellEditor(new TableCellEditor(){
			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JEditorPane editPanel = new JEditorPane();
				editPanel.setContentType("text/html");
				ISentence sentence = sentences.get(row);
				String html = new String();
				try {
					html = gerateHtmlForAnnotations(sentence);
				} catch (SQLException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				} catch (DatabaseLoadDriverException e) {
					TreatExceptionForAIbench.treatExcepion(e);
				}
				editPanel.setText(html);
				if(row%2!=0)
				{
					editPanel.setText("<body bgcolor=\"#E8E8E8 \">" + html + "</body>"); 
				}
				else
				{
					editPanel.setText(html);
				}
				return editPanel;
			}
			public void addCellEditorListener(CellEditorListener arg0) {}
			public void cancelCellEditing() {}
			public Object getCellEditorValue() {return null;}
			public boolean isCellEditable(EventObject arg0) {return false;}
			public void removeCellEditorListener(CellEditorListener arg0) {}
			public boolean shouldSelectCell(EventObject arg0) {return true;}
			public boolean stopCellEditing() {return true;}		
		});
		jTableSentences.addColumn(viewColumn);	
	}
	
	private String textAsHtml(String text, AnnotationPositions annotationsPositions, long offsetStep) throws SQLException, DatabaseLoadDriverException{
		StringBuffer htmlText = new StringBuffer();
		int pointer=0;
		
		for(AnnotationPosition pos : annotationsPositions.getAnnotations().keySet())
		{
			IAnnotation annotation = annotationsPositions.getAnnotations().get(pos);
			if(annotation instanceof IEntityAnnotation)
			{
				IEntityAnnotation annot = (IEntityAnnotation) annotationsPositions.getAnnotations().get(pos);
				int start = pos.getStart()-Integer.valueOf(String.valueOf(offsetStep));
				int end = pos.getEnd()-Integer.valueOf(String.valueOf(offsetStep));
				if(pointer<start)
					htmlText.append(text.substring(pointer, start));			
				String entity = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(annot.getClassAnnotationID()).getColor() + "><a title=\""+ClassProperties.getClassIDClass().get(annot.getClassAnnotationID())+"\">";
				htmlText.append(entity);
				htmlText.append(text.substring(start, end));
				htmlText.append("</a></FONT>");
				pointer = end;
			}
		}
		if(pointer<text.length())
			htmlText.append(text.substring(pointer, text.length()));
		return htmlText.toString();
	}
	
	public String gerateHtmlForAnnotations(ISentence sentence) throws SQLException, DatabaseLoadDriverException {		
		String htmlText = textAsHtml(sentence.getText(),nerAnnotDoc.getEntityAnnotationPositions().getAnnotationPositionsSubSet(sentence.getStartOffset(), sentence.getEndOffset()),sentence.getStartOffset());
		htmlText = htmlText.replaceAll("\\r",".");
		htmlText = htmlText.replaceAll("\\n",".");
		htmlText = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:12pt\" align=\"justify\">" + htmlText + "</DIV>";
		return htmlText;
	}
}
