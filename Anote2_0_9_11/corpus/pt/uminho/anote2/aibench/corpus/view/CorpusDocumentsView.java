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
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.gui.PublicationResumeGUI;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.core.document.IDocument;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import es.uvigo.ei.aibench.workbench.Workbench;



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
		try {
			this.docs = new ArrayList<IDocument>(corpus.getArticlesCorpus().getAllDocuments().values());
			((Corpus) this.corpus).addObserver(this);
			initGUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout thisLayout = new GridBagLayout();
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
		}
	}
	
	private JPanel getSearchPAnel()
	{
		if(seachPanel==null)
		{
			seachPanel = new JPanel();
			jCheckBoxKeySensetive = new JCheckBox();
			jCheckBoxKeySensetive.setText("Case sensitive");
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
						new String[] { "Title","Abstract", "Authors", "Date","PMID/OtherID","All" });
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
		else if(evt.isControlDown())
			text = searchTextField.getText();
		else	
			text = searchTextField.getText()+evt.getKeyChar();
		if(!text.equals(""))
		{
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
					if(String.valueOf(pub.getYearDate()).contains(text))
						rows.add(new Integer(i));
				}
				break;
			}
			case 4:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(String.valueOf(pub.getOtherID()).contains(text))
					{
						rows.add(new Integer(i));
					}
				}
				break;
			}
			default:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(pub.getTitle().contains(text) || 
							pub.getAuthors().contains(text) ||
							String.valueOf(pub.getYearDate()).contains(text)|| 
							pub.getAbstractSection().contains(text) || 
							String.valueOf(pub.getOtherID()).contains(text))
						rows.add(new Integer(i));
				}
				break;
			}		
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
					if(String.valueOf(pub.getYearDate()).toLowerCase().contains(text.toLowerCase()))
						rows.add(new Integer(i));
				}
				break;
			}
			case 4:
			{
				for(int i=0;i<this.docs.size();i++)
				{
					IPublication pub = (IPublication) this.docs.get(i);
					if(String.valueOf(pub.getOtherID()).toLowerCase().contains(text.toLowerCase()))
					{
						rows.add(new Integer(i));
					}
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
							String.valueOf(pub.getYearDate()).toLowerCase().contains(text.toLowerCase()) ||
							pub.getAbstractSection().toLowerCase().contains(text.toLowerCase()) ||
							String.valueOf(pub.getOtherID()).contains(text))
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
	
	/** create a schema from processes aplied to Corpus 
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException */
	private TableModel getTableModelArticles() throws SQLException, DatabaseLoadDriverException
	{
		
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"Title", "Authors", "Date","PMID/OtherID","PDF","Details"};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;	
		data = new Object[this.docs.size()][6];
		for(int i=0;i<this.docs.size();i++)
		{
			IPublication pub = (IPublication) this.docs.get(i);
			data[i][0] = pub.getTitle() +"\n";
//			data[i][1] = pub.getAuthors().replace(",","\n").replace("AND", "\n") + "\n";
			data[i][1] = pub.getAuthors();
			data[i][2] = pub.getYearDate();
			data[i][3] = pub.getOtherID();
			data[i][4] = "";
			data[i][5] = "";
			tableModel.addRow(data[i]);
		}	
		return tableModel;
	}
	
	private JTable getJTableArtiTable() throws SQLException, DatabaseLoadDriverException
	{
		if(jTableArticle==null)
		{
			jTableArticle = new JTable();
		}
		jTableArticle.setModel(getTableModelArticles());
		completeTable(jTableArticle);
		return jTableArticle;
		
	}
	
	private void completeTable(JTable jtable) {
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(750);
		jtable.getColumnModel().getColumn(0).setMinWidth(150);
		jtable.getColumnModel().getColumn(1).setMaxWidth(300);
		jtable.getColumnModel().getColumn(1).setMinWidth(100);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(100);
		jtable.getColumnModel().getColumn(2).setMaxWidth(PreferencesSizeComponents.datefieldmaxWith);
		jtable.getColumnModel().getColumn(2).setMinWidth(PreferencesSizeComponents.datefieldminWith);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(PreferencesSizeComponents.datefieldpreferredWith);
		jtable.getColumnModel().getColumn(3).setMaxWidth(PreferencesSizeComponents.pmidotherIDfieldmaxWith);
		jtable.getColumnModel().getColumn(3).setMinWidth(PreferencesSizeComponents.pmidotherIDfieldminWith);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(PreferencesSizeComponents.pmidotherIDfieldpreferredWith);
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.pdfavailablefieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.pdfavailablefieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.pdfavailablefieldpreferredWith);
		jtable.getColumnModel().getColumn(5).setMaxWidth(PreferencesSizeComponents.moreinfofieldmaxWith);
		jtable.getColumnModel().getColumn(5).setMinWidth(PreferencesSizeComponents.moreinfofieldminWith);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(PreferencesSizeComponents.moreinfofieldpreferredWith);
		jtable.getColumn("PMID/OtherID").setCellRenderer(new ButtonTablePMIDRender());
		jtable.getColumn("PMID/OtherID").setCellEditor(new ButtonTablePMIDCellEditor());
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/find.png"));
		jtable.getColumn("Details").setCellRenderer(new ButtonTableFindRenderer(icon));
		jtable.getColumn("Details").setCellEditor(new ButtonTableFindCellEditor());
		jtable.getColumn("PDF").setCellRenderer(new ButtonTablePDFRenderer());
		jtable.getColumn("PDF").setCellEditor(new ButtonTablePDFCellEditor());
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for (int j = 0; j < 3; j++) {
			jtable.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	
	}

	private void initViewGUI() {
		IPublication pub = (IPublication) docs.get(jTableArticle.getSelectedRow());		
		new PublicationResumeGUI(pub);
	}

	public void update(Observable arg0, Object arg1) {
		try {
			jTableArticle.setModel(getTableModelArticles());
			completeTable(jTableArticle);
			jTableArticle.updateUI();
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}

	}
	
	class ButtonTablePDFCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
		}
		
	}
	
	class ButtonTablePDFRenderer extends ButtonTableRenderer
	{

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JButton viewButton = new JButton();
			if(((IPublication) docs.get(row)).isPDFAvailable())
			{
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
			}
			return viewButton;
		}
		
	}
	
	class ButtonTableFindRenderer extends ButtonTableRenderer
	{
		private static final long serialVersionUID = 1L;
		
		public ButtonTableFindRenderer(ImageIcon icon)
		{
			super(icon);
		}

		public void whenClick() {
			initViewGUI();
		}
		
	}
	
	class ButtonTableFindCellEditor extends ButtonTableCellEditor
	{

		private static final long serialVersionUID = 1L;

		public void whenClick() {
			initViewGUI();
		}
		
	}
	


	class ButtonTablePMIDRender  implements TableCellRenderer{
		
		public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
			JButton jbutton = new JButton();
			if(value.equals("") || value.equals(0))
			{
				jbutton.setText("Without");
			}
			else
			{
				jbutton.setText(value.toString());
			}
			jbutton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						lanchPMID();
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
			return jbutton;
		}
	}
	
	class ButtonTablePMIDCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() {
			try {
				lanchPMID();
			} catch (IOException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}	
		}
		
	}
	
	public void lanchPMID() throws IOException {
		String externalLink = ((IPublication) docs.get(jTableArticle.getSelectedRows()[0])).getExternalLink();
		if(externalLink==null || externalLink.length() < 1)
		{
			Workbench.getInstance().warn("The document does not contains external link... Please update External Link in Publication Resume GUI");
		}
		else
		{
			Help.internetAccess(externalLink);
		}
	}

}
