package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.NERProcess;
import pt.uminho.anote2.aibench.corpus.datatypes.REProcess;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.process.IProcess;
import pt.uminho.anote2.process.IE.IIEProcess;


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
public class CorpusProcessesView extends JPanel implements Observer{

	private static final long serialVersionUID = 1L;
	private JTable jTableProcesses = new JTable();
	private JScrollPane jScrollPaneCorpusProcesses;
	private JPanel jPanelCorpusProcessesContent;
	private Corpus corpus;
	private List<IProcess> processes;
	private JButton jButton1;
	private JButton jButtonHelp;

	public CorpusProcessesView(Corpus corpus)
	{
		super();
		this.corpus=corpus;
		this.corpus.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelCorpusProcessesContent = new JPanel();
				jPanelCorpusProcessesContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Corpus Processes", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelCorpusProcessesContentLayout = new GridBagLayout();
				this.add(jPanelCorpusProcessesContent, new GridBagConstraints(0, 0, 5, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelCorpusProcessesContentLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.0};
				jPanelCorpusProcessesContentLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelCorpusProcessesContentLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelCorpusProcessesContentLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelCorpusProcessesContent.setLayout(jPanelCorpusProcessesContentLayout);
				{
					jScrollPaneCorpusProcesses = new JScrollPane();
					jPanelCorpusProcessesContent.add(jScrollPaneCorpusProcesses, new GridBagConstraints(0, 0, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelCorpusProcessesContent.add(getJButton1(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					{					
						jScrollPaneCorpusProcesses.setViewportView(getJTableProcesses());			
					}
				}
			}
		}
	}
	
	/** create a schema from processes aplied to Corpus */
	private TableModel getTableModelProcesses()
	{
		Set<IProcess> proce = this.corpus.getIProcesses("");
		processes = new ArrayList<IProcess>(proce);
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"ID", "Type", "Description", "Properties"};
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;
		data = new Object[this.processes.size()][4];
		Properties prop;
		String properties;
		String value;
		for(IProcess process: processes)
		{
			IIEProcess processIE = (IIEProcess) process;
			data[i][0] = processIE.getID()+"\n";
			data[i][1] = processIE.getType()+"\n";
			data[i][2] = processIE.getDescription()+"\n";
			prop = processIE.getProperties();
			properties = new String();
			for(String key:prop.stringPropertyNames())
			{
				value = prop.getProperty(key);
				properties = properties+key+": "+value+"\n";
			}
			data[i][3] = properties;
			tableModel.addRow(data[i]);
			i++;
		}	
		return tableModel;
	}
	
	
	private JTable getJTableProcesses()
	{
		if(jTableProcesses==null)
		{
			jTableProcesses = new JTable();
		}
		jTableProcesses.setModel(getTableModelProcesses());
		completeTable();
		return jTableProcesses;
		
	}

	private void completeTable() {
		
		jTableProcesses.getColumnModel().getColumn(0).setMaxWidth(30);
		jTableProcesses.getColumnModel().getColumn(0).setMinWidth(30);
		jTableProcesses.getColumnModel().getColumn(0).setPreferredWidth(30);		
		jTableProcesses.getColumnModel().getColumn(1).setMaxWidth(120);
		jTableProcesses.getColumnModel().getColumn(1).setMinWidth(120);
		jTableProcesses.getColumnModel().getColumn(1).setPreferredWidth(120);
		jTableProcesses.getColumnModel().getColumn(2).setMaxWidth(200);
		jTableProcesses.getColumnModel().getColumn(2).setMinWidth(200);
		jTableProcesses.getColumnModel().getColumn(2).setPreferredWidth(200);
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();
		TableColumn viewColumn = new TableColumn();
		viewColumn.setHeaderValue("Load");
		viewColumn.setMinWidth(40);
		viewColumn.setMaxWidth(40);
		viewColumn.setPreferredWidth(40);
		viewColumn.setCellRenderer(new TableCellRenderer(){
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadProcess();
					}
				});
				return viewButton;
			}
				
		});	
		viewColumn.setCellEditor(new TableCellEditor(){

			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")));
				viewButton.setBackground(Color.WHITE);
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadProcess();
					}
				});
				return viewButton;
			}

			public void addCellEditorListener(CellEditorListener arg0) {}

			public void cancelCellEditing() {}

			public Object getCellEditorValue() { return null;}

			public boolean isCellEditable(EventObject arg0) { return true;}

			public void removeCellEditorListener(CellEditorListener arg0) {}

			public boolean shouldSelectCell(EventObject arg0) { return true;}

			public boolean stopCellEditing() {return true;}
				
		});		
		jTableProcesses.addColumn(viewColumn);
		for (int j = 0; j < 4; j++) {
			jTableProcesses.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jTableProcesses.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}
	
	
	
	private void loadProcess() {
		IProcess process = processes.get(jTableProcesses.getSelectedRow());
		if(process.getType().equals("NER")||process.getType().equals("ner")||process.getType().equals("Manual Curation"))
		{
			IIEProcess processIE = (IIEProcess) process;
			NERProcess nerProcess = new NERProcess(process.getID(),corpus, processIE.getDescription(),"NER" , processIE.getProperties(), corpus.getCorpora().getDb());
			corpus.addProcess(nerProcess);
		}
		else if(process.getType().equals("RE")||process.getType().equals("re")||process.getType().equals("Manual Curation"))
		{
			IIEProcess processIE = (IIEProcess) process;
			REProcess reProcess = new REProcess(process.getID(),corpus, processIE.getDescription(),"RE" , processIE.getProperties(), corpus.getCorpora().getDb());
			corpus.addProcess(reProcess);
		}
	}

	public void update(Observable arg0, Object arg1) {
		jTableProcesses.setModel(getTableModelProcesses());
		completeTable();
		jTableProcesses.updateUI();
	}
	
	private JButton getJButton1() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Corpus_Load_Process");
				}
			});
		}
		return jButtonHelp;
	}

}
