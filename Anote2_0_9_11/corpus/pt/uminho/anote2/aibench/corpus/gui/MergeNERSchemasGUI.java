package pt.uminho.anote2.aibench.corpus.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.gui.help.ViewIEProcessDetails;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.document.ICorpus;
import pt.uminho.anote2.datastructures.process.IEProcess;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.process.IE.manualcuration.ManualCurationEnum;
import pt.uminho.generic.genericpanel.buttons.ButtonTableCellEditor;
import pt.uminho.generic.genericpanel.buttons.ButtonTableRenderer;
import pt.uminho.generic.genericpanel.corpus.CorpusPanel;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class MergeNERSchemasGUI  extends CorpusPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelCorpusSelection;
	private JTabbedPane jTabbedPaneNERSchemasSelection;
	private JTable jTableNormalizedNERProcesses;
	private JTable jTableNormalNERProcesses;
	private JPanel jPanelProcessesNormalized;
	private JPanel jPanelNormalProcessOption;

	public MergeNERSchemasGUI() throws SQLException, DatabaseLoadDriverException
	{
		super("Merge NER Schemas");
		initGUI();
		updateTables();
		this.setModal(true);
	}

	private void updateTables() throws SQLException, DatabaseLoadDriverException {
		ICorpus corpus = getSelectedCorpus();
		if(corpus!=null)
		{
			List<String> processTypes = new ArrayList<String>();
			processTypes.add(GlobalNames.ner);
			processTypes.add(ManualCurationEnum.NER.getProcessName());
			List<IIEProcess> processes = corpus.getIProcessesFilterByTypes(processTypes);
			String[] columNames = new String[] {"NER Process","Info","Selected"};			
			jTableNormalNERProcesses.setModel(new DefaultTableModel());
			((DefaultTableModel) jTableNormalNERProcesses.getModel()).setColumnIdentifiers(columNames);
			jTableNormalizedNERProcesses.setModel(new DefaultTableModel());
			((DefaultTableModel) jTableNormalizedNERProcesses.getModel()).setColumnIdentifiers(columNames);
			for(IIEProcess process:processes)
			{
				Object[] objets = new Object[3];
				objets[0] = process;
				objets[1] = "";
				objets[2] = Boolean.FALSE;
				if(process.getProperties().containsKey(GlobalNames.normalization) && Boolean.valueOf((String) process.getProperties().get(GlobalNames.normalization)))
				{
					((DefaultTableModel) jTableNormalizedNERProcesses.getModel()).addRow(objets);
				}
				else
				{
					((DefaultTableModel) jTableNormalNERProcesses.getModel()).addRow(objets);
				}
				completeTables();
			}
		}
		else
		{
			Workbench.getInstance().warn("Any Corpus has selected");
		}
	}

	private void completeTables() {
		completeNormalTable(jTableNormalNERProcesses);
		completeNormalTable(jTableNormalizedNERProcesses);
	}

	private void completeNormalTable(JTable table) {
		table.setRowHeight(20);
		table.getColumnModel().getColumn(1).setMaxWidth(45);
		table.getColumnModel().getColumn(1).setMinWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(45);
		table.getColumnModel().getColumn(2).setMaxWidth(45);
		table.getColumnModel().getColumn(2).setMinWidth(45);
		table.getColumnModel().getColumn(2).setPreferredWidth(45);
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/search.png"));
		table.getColumn("Info").setCellRenderer(new ButtonInfoRenderer(icon));
		table.getColumn("Info").setCellEditor(new ButtonInfoeCellEditor());
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.15, 0.75, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelCorpusSelection = new JPanel();
				getContentPane().add(getCorpusSelectionPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jTabbedPaneNERSchemasSelection = new JTabbedPane();
				jTabbedPaneNERSchemasSelection.setBorder(BorderFactory.createTitledBorder(null, "NER Processes", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				getContentPane().add(getJTabbedPaneNERSchemasSelection(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelNormalProcessOption = new JPanel();
					GridLayout jPanelNormalProcessOptionLayout = new GridLayout(1, 1);
					jPanelNormalProcessOptionLayout.setVgap(5);
					jPanelNormalProcessOptionLayout.setHgap(5);
					jPanelNormalProcessOptionLayout.setColumns(1);
					jPanelNormalProcessOption.setLayout(jPanelNormalProcessOptionLayout);
					jTabbedPaneNERSchemasSelection.addTab("Regular", null, jPanelNormalProcessOption, null);
					{
						jTableNormalNERProcesses = new JTable()
						{

							private static final long serialVersionUID = -2L;

							public boolean isCellEditable(int x,int y){
								if(y==1 || y==2)
									return true;
								return false;
							}
							
							public Class<?> getColumnClass(int column){
								if(column == 2)
									return Boolean.class;
								return String.class;
							}
						};		
						jPanelNormalProcessOption.add(jTableNormalNERProcesses);
					}
					jTableNormalNERProcesses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

				}
				{
					jPanelProcessesNormalized = new JPanel();
					GridLayout jPanelProcessesNormalizedLayout = new GridLayout(1, 1);
					jPanelProcessesNormalizedLayout.setVgap(5);
					jPanelProcessesNormalizedLayout.setHgap(5);
					jPanelProcessesNormalizedLayout.setColumns(1);
					jPanelProcessesNormalized.setLayout(jPanelProcessesNormalizedLayout);
					jTabbedPaneNERSchemasSelection.addTab("Normalized", null, jPanelProcessesNormalized, null);
					{
						jTableNormalizedNERProcesses = new JTable()
						{

							private static final long serialVersionUID = -2L;

							public boolean isCellEditable(int x,int y){
								if(y==1 || y==2)
									return true;
								return false;
							}
							
							public Class<?> getColumnClass(int column){
								if(column == 2)
									return Boolean.class;
								return String.class;
							}
						};
						jTableNormalizedNERProcesses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						jPanelProcessesNormalized.add(jTableNormalizedNERProcesses);
					}
				}
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}

	class ButtonInfoRenderer extends ButtonTableRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonInfoRenderer(ImageIcon icon)
		{
			super(icon);
		    setOpaque(false);
		}
		
		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			IEProcess ieprocessSelected = getSelectedProcess();
			new ViewIEProcessDetails(ieprocessSelected);
		}

	}

	class ButtonInfoeCellEditor extends ButtonTableCellEditor {

		private static final long serialVersionUID = 1L;

		public void whenClick() throws SQLException, DatabaseLoadDriverException {
			IEProcess ieprocessSelected = getSelectedProcess();
			new ViewIEProcessDetails(ieprocessSelected);
		}
	}

	public IEProcess getSelectedProcess() {
		int select = jTabbedPaneNERSchemasSelection.getSelectedIndex();
		if(select == 0)
		{
			return (IEProcess) jTableNormalNERProcesses.getValueAt(jTableNormalNERProcesses.getSelectedRow(), 0);
		}
		else
		{
			return (IEProcess) jTableNormalizedNERProcesses.getValueAt(jTableNormalizedNERProcesses.getSelectedRow(), 0);
		}
	}


	protected void changeComboBoxCorpus() throws SQLException, DatabaseLoadDriverException {
		updateTables();
	}



	protected void okButtonAction() {
		int select = jTabbedPaneNERSchemasSelection.getSelectedIndex();
		JTable aux;
		if(select == 0)
		{
			aux = jTableNormalNERProcesses;
		}
		else
		{
			aux = jTableNormalizedNERProcesses;
		}
		List<IEProcess> listSelected = getSelected(aux);
		if(listSelected.size()<2)
		{
			Workbench.getInstance().warn("Selecte at least two NER process");
		}
		else
		{
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
					new ParamSpec("Corpus",Corpus.class,(Corpus) getSelectedCorpus(),null),
					new ParamSpec("PortIEProcess",List.class,listSelected,null)
				});
			finish();

		}
	}
	
	private List<IEProcess> getSelected(JTable aux) {
		List<IEProcess> processes = new ArrayList<IEProcess>();
		for(int i=0;i<aux.getRowCount();i++)
		{
			boolean availbale = (Boolean) aux.getModel().getValueAt(i, 2);
			if(availbale)
			{
				IEProcess e = (IEProcess) aux.getModel().getValueAt(i, 0);
				processes.add(e);
			}
		}
		return processes;
	}

	public JPanel getJPanelCorpusSelection() {
		return jPanelCorpusSelection;
	}
	
	public JTabbedPane getJTabbedPaneNERSchemasSelection() {
		return jTabbedPaneNERSchemasSelection;
	}

	public DefaultComboBoxModel getAvailableCorpusOnClipBoard()
	{
		List<ClipboardItem> corpusOnCLipboad = Core.getInstance().getClipboard().getItemsByClass(Corpus.class);
		DefaultComboBoxModel combobox = new DefaultComboBoxModel();
		for(ClipboardItem item:corpusOnCLipboad)
		{
			combobox.addElement((Corpus) item.getUserData());
		}
		Corpus corpusSelected = (Corpus) HelpAibench.getSelectedItem(Corpus.class);
		if(corpusSelected!=null)
		{
			combobox.setSelectedItem(corpusSelected);
		}
		return combobox;		
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Merging_NER_Schemas";
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Corpus selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}

	}

}
