package pt.uminho.anote2.aibench.corpus.view;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;

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

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.ShowMessagePopup;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.core.document.ICorpus;


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
public class CorporaView extends JPanel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable jTableProcesses;
	private JScrollPane jScrollPaneCorpusProcesses;
	private JButton jButtonHelp;
	private JPanel jPanelCorpusProcessesContent;
	private Corpora corpora;

	public CorporaView(Corpora corpora)
	{
		super();
		this.corpora=corpora;
		this.corpora.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		{
			this.setSize(500,300);
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelCorpusProcessesContent = new JPanel();
				jPanelCorpusProcessesContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Corpus Available", TitledBorder.LEADING, TitledBorder.TOP));

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
					jPanelCorpusProcessesContent.add(getJButtonHelp(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] columns = new String[] {"ID", "Name","Properties"};
		tableModel.setColumnIdentifiers(columns);		
		Object[][] data;
		int i = 0;	
		data = new Object[corpora.getAllCorpus().size()][3];	
		for(ICorpus corpus: corpora.getAllCorpus())
		{
			String prop = new String();
			data[i][0] = corpus.getID()+"\n";
			data[i][1] = corpus.getDescription()+"\n";		
			for(String props:((Corpus)corpus).getProperties().stringPropertyNames())
			{
				prop = "Key : "+props+" Value :"+((Corpus)corpus).getProperties().getProperty(props);
			}	
			data[i][2] = prop;
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
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadCorpus();
					}
				});
				return viewButton;
			}		
		});	
		viewColumn.setCellEditor(new TableCellEditor(){
			public Component getTableCellEditorComponent(JTable table, Object value, boolean flag, int row, int column) {
				JButton viewButton = new JButton();
				viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Add.png")));
				viewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						loadCorpus();
					}
				});
				return viewButton;
			}
			public void addCellEditorListener(CellEditorListener arg0) {}
			public void cancelCellEditing() {}
			public Object getCellEditorValue() {return null;}
			public boolean isCellEditable(EventObject arg0) { return true;}
			public void removeCellEditorListener(CellEditorListener arg0) {}
			public boolean shouldSelectCell(EventObject arg0) {return true;}
			public boolean stopCellEditing() { return true;}	
		});		
		jTableProcesses.addColumn(viewColumn);
		for (int j = 0; j < 3; j++) {
			jTableProcesses.getColumnModel().getColumn(j).setCellRenderer(renderer);
			jTableProcesses.getColumnModel().getColumn(j).setCellEditor(editor);
		}
	}
	
	
	
	private void loadCorpus() {
		corpora.addCorpus(corpora.getAllCorpus().get(jTableProcesses.getSelectedRow()));
		new ShowMessagePopup("Corpus Added !!!");
		
	}

	public void update(Observable arg0, Object arg1) {
		corpora.updateCorpus();
		jTableProcesses.setModel(getTableModelProcesses());
		completeTable();
		jTableProcesses.updateUI();
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Corpora_Load_Corpus");
				}
			});
		}
		return jButtonHelp;
	}

}
