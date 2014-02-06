package pt.uminho.anote2.aibench.corpus.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.corpus.datatypes.REProcess;
import pt.uminho.anote2.aibench.corpus.gui.RelationDetailsGUI;
import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import pt.uminho.anote2.aibench.utils.gui.TextAreaEditor;
import pt.uminho.anote2.aibench.utils.gui.TextAreaRenderer;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.generic.genericpanel.tablesearcher.TableSearchPanel;



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
public class RERelationsViewer extends JPanel{

	private static final long serialVersionUID = 1L;
	private TableSearchPanel environmentalConditionTable;
	private JButton jButtonHelp;
	private JPanel jPanelHeader;
	private REProcess reprocess;
	Map<Integer, String> classIDclass;
	
	public RERelationsViewer(REProcess reprocess)
	{
		this.reprocess = reprocess;
		try {
			classIDclass = ResourcesHelp.getClassIDClassOnDatabase(((Corpus)reprocess.getCorpus()).getCorpora().getDb());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		initGUI();
		fillView();
	}


	private void initGUI() 
	{
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(524, 289));
			thisLayout.rowWeights = new double[] {0.05, 0.95};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jPanelHeader = new JPanel();
				GridBagLayout jPanelHeaderLayout = new GridBagLayout();
				this.add(jPanelHeader, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelHeaderLayout.rowWeights = new double[] {0.1};
				jPanelHeaderLayout.rowHeights = new int[] {7};
				jPanelHeaderLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelHeaderLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelHeader.setLayout(jPanelHeaderLayout);
			}
			{
				environmentalConditionTable = new TableSearchPanel(true);
				this.add(environmentalConditionTable, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jButtonHelp = getJButtonHelp();
					environmentalConditionTable.add(jButtonHelp, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
					jButtonHelp.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							Help.internetAcess(GlobalOptions.wikiGeneralLink+"Process_Relations_view");
						}
					});
				}
			}
		}

	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Process_Relations_view");
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}
	
	private void fillView() {		
		TableModel tableModel = fillModelPubTable();
		environmentalConditionTable.setModel(tableModel);
		complementTable();
	}
	
	private TableModel fillModelPubTable(){
		String[] columns = new String[] {"Clue(Verb)", "Lemma(Clue)", "Entities At Left", "Entities At Right","Polarity","Directionally","RelationID"};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;		
		int relatiosNumber = reprocess.getEventsAnnotations().size();
		data = new Object[relatiosNumber][7];
		for(int relationID:reprocess.getEventsAnnotations().keySet())
		{
			String verb = new String();
			String lemma = new String();
			String polarity = "unknown";
			String directionally = "unknown";
			IEventAnnotation annot = reprocess.getEventsAnnotations().get(relationID);
			if(annot.getEventClue()!=null)
				verb = annot.getEventClue();
			if(annot.getEventProperties().getLemma()!=null)
				lemma = annot.getEventProperties().getLemma();
			data[i][0] = verb;
			data[i][1] = lemma;			
			data[i][2] = convertEntitiesToString(annot.getEntitiesAtLeft());
			data[i][3] = convertEntitiesToString(annot.getEntitiesAtRight());
			if(annot.getEventProperties().getPolarity()!=null)
			{
				int pol = annot.getEventProperties().getPolarity();
				if(pol == 0)
				{
					polarity = "Conditional (?)";
				}
				else if(pol == -1)
				{
					polarity = "Negative (-)";
				}
				else
				{
					polarity = "Positive (+)";
				}
			}
			data[i][4] = polarity;
			if(annot.getEventProperties().isDirectionally()!=null)
			{
				boolean dir = annot.getEventProperties().isDirectionally();
				if(dir)
					directionally = "R -> L";
				else
					directionally = "L -> R";
			}
			data[i][5] = directionally;
			data[i][6] = relationID;
			tableModel.addRow(data[i]);
			i++;
		}		
		return tableModel;		
	}
	
	private String convertEntitiesToString(List<IEntityAnnotation> entities) {
		String entityToString = new String();
		for(IEntityAnnotation ent:entities)
		{
			entityToString = entityToString + ent.getAnnotationValue() + " (" + classIDclass.get(ent.getClassAnnotationID()) + ") \n";
		}
		return entityToString + "\n";
	}

	private void initViewGUI() 
	{	
		int relationID = (Integer) environmentalConditionTable.getMainTable().getValueAt(environmentalConditionTable.getMainTable().getSelectedRow(), 6);
		IEventAnnotation event = reprocess.getEventsAnnotations().get(relationID);
		new RelationDetailsGUI(event,classIDclass,reprocess);	
	}
	
	private void complementTable(){		
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(0).setMaxWidth(150);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(0).setMinWidth(150);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(0).setPreferredWidth(150);		
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(1).setMaxWidth(150);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(1).setMinWidth(150);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(1).setPreferredWidth(150);	
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(4).setMaxWidth(100);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(4).setMinWidth(100);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(4).setPreferredWidth(100);	
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(5).setMaxWidth(50);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(5).setMinWidth(50);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(5).setPreferredWidth(50);	
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(6).setMaxWidth(70);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(6).setMinWidth(70);
		environmentalConditionTable.getMainTable().getColumnModel().getColumn(6).setPreferredWidth(70);			
		environmentalConditionTable.getMainTable().setRowHeight(20);																
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
			public void addCellEditorListener(CellEditorListener arg0) {}
			public void cancelCellEditing() {}
			public Object getCellEditorValue() {return null;}
			public boolean isCellEditable(EventObject arg0) {return true;}
			public void removeCellEditorListener(CellEditorListener arg0) {}
			public boolean shouldSelectCell(EventObject arg0) {return true;}
			public boolean stopCellEditing() {return true;}		
		});
		environmentalConditionTable.getMainTable().addColumn(viewColumn);
		TextAreaRenderer renderer = new TextAreaRenderer();
		TextAreaEditor editor = new TextAreaEditor();
		for (int j = 0; j < 7; j++) {
			environmentalConditionTable.getMainTable().getColumnModel().getColumn(j).setCellRenderer(renderer);
			environmentalConditionTable.getMainTable().getColumnModel().getColumn(j).setCellEditor(editor);
		}		
		for (int k = 0; k<environmentalConditionTable.getMainTable().getRowCount(); k++){
			for (int j = 0; j<environmentalConditionTable.getMainTable().getColumnCount(); j++){
				environmentalConditionTable.getMainTable().getCellRenderer(k, j).getTableCellRendererComponent(environmentalConditionTable.getMainTable(), environmentalConditionTable.getMainTable().getValueAt(k, j), false, false, k,j);
			}
		}	
	}
}
