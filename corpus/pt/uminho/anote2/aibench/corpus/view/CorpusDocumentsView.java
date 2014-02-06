package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.gui.PublicationResumeGUI;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;


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
public class CorpusDocumentsView extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable jTableArticle;
	private JPanel seachPanel;
	private JScrollPane jScrollPaneArticleTable;
	private JPanel jPanelArticleTable;	
	private ICorpus corpus;
	private JTextField searchTextField;
	private JCheckBox jCheckBoxKeySensetive;
	private JComboBox searchComboBox;
	private List<IDocument> docs;
	private JButton jButtonHelp;

	public CorpusDocumentsView(Corpus corpus)
	{
		this.corpus=corpus;
		this.docs = new ArrayList<IDocument>(corpus.getArticlesCorpus().getAllDocuments().values());
		((Corpus) this.corpus).addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		this.setSize(500,300);
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		{
			jPanelArticleTable = new JPanel();
			jPanelArticleTable.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Corpus Documents", TitledBorder.LEADING, TitledBorder.TOP));

			GridBagLayout jPanelArticleTableLayout = new GridBagLayout();
			this.add(jPanelArticleTable, new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelArticleTableLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelArticleTableLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelArticleTableLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelArticleTableLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelArticleTable.setLayout(jPanelArticleTableLayout);
			{
				jScrollPaneArticleTable = new JScrollPane();
				jPanelArticleTable.add(jScrollPaneArticleTable, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jScrollPaneArticleTable.setViewportView(getJTableArtiTable());
				}
			}
		}
		{
			this.add(getSearchPAnel(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJButtonHelp(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
	
	private JPanel getSearchPAnel()
	{
		if(seachPanel==null)
		{
			seachPanel = new JPanel();
			jCheckBoxKeySensetive = new JCheckBox();
			jCheckBoxKeySensetive.setSelected(true);
			GridBagLayout seachPanelLayout = new GridBagLayout();
			seachPanelLayout.rowWeights = new double[] {0.0};
			seachPanelLayout.rowHeights = new int[] {22};
			seachPanelLayout.columnWeights = new double[] {0.1, 0.0};
			seachPanelLayout.columnWidths = new int[] {100, 7};
			seachPanel.setLayout(seachPanelLayout);					
			seachPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Search", TitledBorder.LEADING, TitledBorder.TOP));
			{
				searchTextField = new JTextField();
				seachPanel.add(searchTextField, new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				searchTextField
				.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory
						.createEtchedBorder(BevelBorder.LOWERED),
						null));
				searchTextField.setPreferredSize(new java.awt.Dimension(54, 17));
				searchTextField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent evt) {
						if(jCheckBoxKeySensetive.isSelected())
						{
							searchInTable(evt);
						}
						else
						{
							searchInTableKeySensetive(evt);
						}
					}
				});

				ComboBoxModel searchComboBoxModel = new DefaultComboBoxModel(
						new String[] { "Title","Abstract", "Authors", "Date","All" });
				searchComboBox = new JComboBox();
				seachPanel.add(searchComboBox, new GridBagConstraints(1, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				seachPanel.add(jCheckBoxKeySensetive, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				searchComboBox.setModel(searchComboBoxModel);
			}
		}
		return seachPanel;
	}
	
	public void searchInTable(KeyEvent evt){

		String text;
		List<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();

		if(searchTextField.getText().compareTo("")!=0 && evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
			text = searchTextField.getText();
		else
			text = searchTextField.getText()+evt.getKeyChar();

		switch(this.searchComboBox.getSelectedIndex())
		{
		case 0:
		{
			for(int i=0;i<this.docs.size();i++)
			{
				IPublication pub = (IPublication) this.docs.get(i);
				if(pub.getTitle().contains(text))
					rows.add(new Integer(i));
			}
			break;
		}
		case 1:
		{
			for(int i=0;i<this.docs.size();i++)
			{
				IPublication pub = (IPublication) this.docs.get(i);
				if(pub.getAbstractSection()!=null)
				{
					if(pub.getAbstractSection().contains(text))
						rows.add(new Integer(i));
				}
			}
			break;
		}
		case 2:
		{
			for(int i=0;i<this.docs.size();i++)
			{
				IPublication pub = (IPublication) this.docs.get(i);
				if(pub.getAuthors().contains(text))
					rows.add(new Integer(i));
			}
			break;
		}
		case 3:
		{
			for(int i=0;i<this.docs.size();i++)
			{
				IPublication pub = (IPublication) this.docs.get(i);
				if(String.valueOf(pub.getDate()).contains(text))
					rows.add(new Integer(i));
			}
			break;
		}
		default:
		{
			for(int i=0;i<this.docs.size();i++)
			{
				IPublication pub = (IPublication) this.docs.get(i);
				if(pub.getTitle().contains(text) || pub.getAuthors().contains(text) || String.valueOf(pub.getDate()).contains(text)|| pub.getAbstractSection().contains(text))
					rows.add(new Integer(i));
			}
			break;
		}		
		}	
		setTableChangesSearch(rows, selectionModel);	
	}

	protected void searchInTableKeySensetive(KeyEvent evt) {
		String text;
		ArrayList<Integer> rows = new ArrayList<Integer>();
		DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
		
		if(searchTextField.getText().compareTo("")!=0 && evt.getKeyChar() == KeyEvent.VK_BACK_SPACE)
			text = searchTextField.getText();
		else
			text = searchTextField.getText()+evt.getKeyChar();
		
		
		switch(this.searchComboBox.getSelectedIndex())
		{
			case 0:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(pub.getTitle().toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));

				}
				break;
			}
			case 1:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(pub.getAbstractSection()!=null)
					{
						if(pub.getAbstractSection().toLowerCase().contains(text.toLowerCase()))
							rows.add(new Integer(i));
					}
				}
				break;
			}
			case 2:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(pub.getAuthors().toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
				}
				break;
			}
			case 3:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(String.valueOf(pub.getDate()).toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
				}
				break;
			}
			default:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(pub.getTitle().toLowerCase().contains(text.toLowerCase()) || 
							pub.getAuthors().toLowerCase().contains(text.toLowerCase()) || 
							String.valueOf(pub.getDate()).toLowerCase().contains(text.toLowerCase()) ||
							pub.getAbstractSection().toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
				}
				break;
			}	
		}	
		setTableChangesSearch(rows, selectionModel);		
	}

	@SuppressWarnings("static-access")
	private void setTableChangesSearch(List<Integer> rows,
			DefaultListSelectionModel selectionModel) {
		int row = 0;
		for(Integer r: rows)
		{
			row = r.intValue();
			selectionModel.addSelectionInterval(row, row);
		}
		
		jTableArticle.setSelectionMode(selectionModel.MULTIPLE_INTERVAL_SELECTION);
		jTableArticle.setSelectionModel(selectionModel);
		if(selectionModel.isSelectionEmpty()&& (this.searchTextField.getText().compareTo("")!=0))
		{
			this.searchTextField.setForeground(new java.awt.Color(255,0,0));
			searchTextField.setBackground(new java.awt.Color(174,174,174));
		}
		else
		{
			this.searchTextField.setForeground(Color.BLACK);
			this.searchTextField.setBackground(Color.WHITE);
		}
		if(rows.size()>0)
		{
			jTableArticle.scrollRectToVisible(jTableArticle.getCellRect(rows.get(0), 0, true));
		}
	}
	
	/** create a schema from processes aplied to Corpus */
	private TableModel getTableModelArticles()
	{
		
		corpus.getArticlesCorpus();
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"Title", "Authors", "Date"};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;	
		data = new Object[this.docs.size()][3];
		for(int i=0;i<this.docs.size();i++)
		{
			IPublication pub = (IPublication) this.docs.get(i);
			data[i][0] = pub.getTitle();
			data[i][1] = pub.getAuthors();
			data[i][2] = pub.getDate();
			
			tableModel.addRow(data[i]);
		}	
		return tableModel;
	}
	
	private JTable getJTableArtiTable()
	{
		if(jTableArticle==null)
		{
			jTableArticle = new JTable();
		}
		jTableArticle.setModel(getTableModelArticles());
		completeTable();
		return jTableArticle;
		
	}
	
	private void completeTable() {
		
		jTableArticle.getColumnModel().getColumn(1).setMaxWidth(120);
		jTableArticle.getColumnModel().getColumn(1).setMinWidth(120);
		jTableArticle.getColumnModel().getColumn(1).setPreferredWidth(120);		
		jTableArticle.getColumnModel().getColumn(2).setMaxWidth(80);
		jTableArticle.getColumnModel().getColumn(2).setMinWidth(80);
		jTableArticle.getColumnModel().getColumn(2).setPreferredWidth(80);	
		jTableArticle.setRowHeight(20);
		jTableArticle.setSelectionForeground(Color.WHITE);	
		TableColumn viewColumn2 = new TableColumn();
		viewColumn2.setHeaderValue("Pdf");
		viewColumn2.setMinWidth(30);
		viewColumn2.setMaxWidth(30);
		viewColumn2.setPreferredWidth(30);
		viewColumn2.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				IPublication pub = ((IPublication)docs.get(row));
				String id_path = Corpora.saveDocs+"id"+pub.getID()+".pdf";
				String id_path_otherID = Corpora.saveDocs+"id"+pub.getID()+"-"+pub.getOtherID()+".pdf";
				String pdf_path = Corpora.saveDocs+pub.getOtherID()+".pdf";
				if(new File(pdf_path).exists()||new File(id_path).exists()||new File(id_path_otherID).exists())
				{
					viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
				}
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
					}
				});
				return viewButton;
			}
				
		});
		
		viewColumn2.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return false;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return true;
			}

			public boolean stopCellEditing() {
				return false;
			}
				
		});
		
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("");
		viewColumn.setMinWidth(25);
		viewColumn.setMaxWidth(25);
		viewColumn.setPreferredWidth(25);
		viewColumn.setCellRenderer(new TableCellRenderer(){

			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/find.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						initViewGUI();
					}

				});
				return viewButton;
			}
				
		});
		
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/find.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						initViewGUI();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {
			}

			public void cancelCellEditing() {
			}

			public Object getCellEditorValue() {
				return null;
			}

			public boolean isCellEditable(EventObject arg0) {
				return true;
			}

			public void removeCellEditorListener(CellEditorListener arg0) {
				
			}

			public boolean shouldSelectCell(EventObject arg0) {
				return true;
			}

			public boolean stopCellEditing() {
				return true;
			}
				
		});
		jTableArticle.addColumn(viewColumn2);
		jTableArticle.addColumn(viewColumn);	
	
	}
	

	private void initViewGUI() {
		IPublication pub = (IPublication) docs.get(jTableArticle.getSelectedRow());		
		new PublicationResumeGUI(pub,((Corpus) corpus).getCorpora().getDb());
	}

	public void update(Observable arg0, Object arg1) {
		jTableArticle.setModel(getTableModelArticles());
		completeTable();
		jTableArticle.updateUI();
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Process_Load_Annotated_Document");
				}
			});
		}
		return jButtonHelp;
	}

}
