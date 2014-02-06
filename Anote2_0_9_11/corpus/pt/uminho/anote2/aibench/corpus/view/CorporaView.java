package pt.uminho.anote2.aibench.corpus.view;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.gui.RemoveCorpusGUI;
import pt.uminho.anote2.aibench.corpus.management.database.CorporaDatabaseManagement;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.aibench.utils.gui.options.PreferencesSizeComponents;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;



public class CorporaView extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton jButtonHelp;
	private JPanel jPanelCorpusProcessesContent;
	private Corpora corpora;
	private TableSearchPanel jTableSearch;

	public CorporaView(Corpora corpora)
	{
		super();
		this.corpora=corpora;
		this.corpora.addObserver(this);
		try {
			initGUI();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}

	private void initGUI() throws DatabaseLoadDriverException, SQLException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelCorpusProcessesContent = new JPanel();
				jPanelCorpusProcessesContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Corpora Available", TitledBorder.LEADING, TitledBorder.TOP));

				GridBagLayout jPanelCorpusProcessesContentLayout = new GridBagLayout();
				this.add(jPanelCorpusProcessesContent, new GridBagConstraints(0, 0, 5, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelCorpusProcessesContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
				jPanelCorpusProcessesContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelCorpusProcessesContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelCorpusProcessesContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelCorpusProcessesContent.setLayout(jPanelCorpusProcessesContentLayout);
				{
					jPanelCorpusProcessesContent.add(getSearchPanel(), new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelCorpusProcessesContent.add(getJButtonHelp(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
	}
	
	private JPanel getSearchPanel() throws DatabaseLoadDriverException, SQLException {
		if(jTableSearch == null) {
			jTableSearch = new TableSearchPanel();
			jTableSearch.getMainTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableSearch.setModel(getTableModelProcesses());
			completeTable(jTableSearch.getMainTable());
		}
		return jTableSearch;
	}

	/** create a schema from processes aplied to Corpus 
	 * @throws SQLException 
	 * @throws DatabaseLoadDriverException */
	private TableModel getTableModelProcesses() throws DatabaseLoadDriverException, SQLException
	{
		DefaultTableModel tableModel = new DefaultTableModel()		{
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int columnIndex){
				return (columnIndex == 0 || columnIndex == 3)?(Integer.class):String.class;
			}
			
		};
		String[] columns = new String[] {"ID", "Name","Properties","Documents","DB","Load"};
		tableModel.setColumnIdentifiers(columns);		
		Object[] data;
		data = new Object[6];	
		for(ICorpus corpus: corpora.getAllCorpus())
		{
			String prop = new String();
			data[0] = corpus.getID()+"\n";
			data[1] = corpus.getDescription()+"\n";		
			for(String props:((Corpus)corpus).getProperties().stringPropertyNames())
			{
				prop = prop +props+" :"+((Corpus)corpus).getProperties().getProperty(props) + "\n";
			}	
			data[2] = prop;
			data[3] = CorporaDatabaseManagement.getCorpusSize(corpus.getID());
			data[4] = "";
			data[5] = "";
			tableModel.addRow(data);
		}	
		return tableModel;
	}
	

	
	private void completeTable(JTable jtable)
	{
		jtable.setRowHeight(PreferencesSizeComponents.generalRowHeight);
		jtable.getColumnModel().getColumn(0).setMaxWidth(PreferencesSizeComponents.idfieldmaxWith);
		jtable.getColumnModel().getColumn(0).setMinWidth(PreferencesSizeComponents.idfieldminWidth);
		jtable.getColumnModel().getColumn(0).setPreferredWidth(PreferencesSizeComponents.idfieldpreferredWidth);
		jtable.getColumnModel().getColumn(1).setMaxWidth(750);
		jtable.getColumnModel().getColumn(1).setMinWidth(150);
		jtable.getColumnModel().getColumn(1).setPreferredWidth(400);
		jtable.getColumnModel().getColumn(2).setMaxWidth(750);
		jtable.getColumnModel().getColumn(2).setMinWidth(100);
		jtable.getColumnModel().getColumn(2).setPreferredWidth(300);
		jtable.getColumnModel().getColumn(3).setMaxWidth(200);
		jtable.getColumnModel().getColumn(3).setMinWidth(75);
		jtable.getColumnModel().getColumn(3).setPreferredWidth(100);
		jtable.getColumnModel().getColumn(4).setMaxWidth(PreferencesSizeComponents.removefieldmaxWith);
		jtable.getColumnModel().getColumn(4).setMinWidth(PreferencesSizeComponents.removefieldminWith);
		jtable.getColumnModel().getColumn(4).setPreferredWidth(PreferencesSizeComponents.removefieldpreferredWith);
		jtable.getColumnModel().getColumn(5).setMaxWidth(PreferencesSizeComponents.loadfieldmaxWith);
		jtable.getColumnModel().getColumn(5).setMinWidth(PreferencesSizeComponents.loadfieldminWith);
		jtable.getColumnModel().getColumn(5).setPreferredWidth(PreferencesSizeComponents.loadfieldpreferredWith);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/Remove_from_database.png"));
		jtable.getColumn("DB").setCellRenderer(new ButtonTableDBRenderer(icon));
		jtable.getColumn("DB").setCellEditor(new ButtonTableDBCellEditor());
		ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("icons/bottom16.png"));
		jtable.getColumn("Load").setCellRenderer(new ButtonLoadResourceRenderer(icon2));
		jtable.getColumn("Load").setCellEditor(new ButtonLoadResourceCellEditor());
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();	
		for(int i=0;i<4;i++)
		{
			jtable.getColumnModel().getColumn(i).setCellRenderer(renderer);
			jtable.getColumnModel().getColumn(i).setCellEditor(editor);
		}

	}
	
	
	
	private void removeFromDB() throws SQLException, DatabaseLoadDriverException {
		if(jTableSearch.getSelectedRowsInOriginalModel()[0]==-1)
		{
			
		}
		else
		{
			ICorpus corpus = corpora.getAllCorpus().get(jTableSearch.getSelectedRowsInOriginalModel()[0]);
			new RemoveCorpusGUI(corpus);
		}
	}


	private void loadCorpus() throws DatabaseLoadDriverException, SQLException {
		corpora.addCorpus(corpora.getAllCorpus().get(jTableSearch.getSelectedRowsInOriginalModel()[0]));		
	}

	public void update(Observable arg0, Object arg1) {
		try {
			corpora.updateCorpus();
			jTableSearch.setModel(getTableModelProcesses());
			completeTable(jTableSearch.getMainTable());
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						Help.internetAccess(GlobalOptions.wikiGeneralLink+"Corpora_Load_Corpus");
					} catch (IOException e1) {
						TreatExceptionForAIbench.treatExcepion(e1);
					}
				}
			});
		}
		return jButtonHelp;
	}
	
	class ButtonTableDBRenderer extends ButtonTableRenderer{

		private static final long serialVersionUID = 6770443805813243771L;

		public ButtonTableDBRenderer(ImageIcon icon) {
			super(icon);
		}

		@Override
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			removeFromDB();
		}

	}
	
	class ButtonTableDBCellEditor extends ButtonTableCellEditor
	{
		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			removeFromDB();	
		}
		
	}
	
	class ButtonLoadResourceRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonLoadResourceRenderer(ImageIcon icon)
		{
			super(icon);
		}
		
		public void whenClick() throws DatabaseLoadDriverException, SQLException {
			loadCorpus();
		}

	}

	class ButtonLoadResourceCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() throws DatabaseLoadDriverException, SQLException {
			loadCorpus();
		}

	}

}
