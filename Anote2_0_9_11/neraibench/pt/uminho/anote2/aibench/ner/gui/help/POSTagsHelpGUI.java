package pt.uminho.anote2.aibench.ner.gui.help;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import pt.uminho.anote2.datastructures.nlptools.PartOfSpeechLabels;
import pt.uminho.anote2.datastructures.utils.conf.GlobalGUIOptions;

public class POSTagsHelpGUI {
	
	
	private static TableModel getPOSTaggingTableModel(List<PartOfSpeechLabels> postags){
		
		String[] columns = new String[] {"Short Name (Long Name)",""};
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(columns);	
		Object[][] data;
		int i = 0;
		data = new Object[postags.size()][2];		
		for(PartOfSpeechLabels label: postags)
		{
			data[i][0] = label;
			data[i][1] = label.getEnableDefaultValue();
			tableModel.addRow(data[i]);
			i++;
		}
		return tableModel;		
	}
	
	private static void constructTable(JTable jtablePOSTags) {
		jtablePOSTags.getColumnModel().getColumn(1).setMinWidth(GlobalGUIOptions.tableSelectionSize);
		jtablePOSTags.getColumnModel().getColumn(1).setMaxWidth(GlobalGUIOptions.tableSelectionSize);
	}
	
	public static JTable getJtablePOSTags()
	{
		List<PartOfSpeechLabels> postags = getPOStags();
		TableModel tableModel = getPOSTaggingTableModel(postags);
		JTable jtablePOSTags = createTable();
		jtablePOSTags.setModel(tableModel);
		constructTable(jtablePOSTags);
		return jtablePOSTags;
	}
	
	private static JTable createTable() {
		JTable jtablePOSTags = new JTable(){

			private static final long serialVersionUID = -4090662450740771673L;

			@Override
			public boolean isCellEditable(int x,int y){
				if(y==1)
					return true;
				return false;
			}

			public Class<?> getColumnClass(int column){
				if(column == 1)
					return Boolean.class;
				return String.class;
			}
		};
		return jtablePOSTags;
	}

	private static List<PartOfSpeechLabels> getPOStags() {
		List<PartOfSpeechLabels> postags = new ArrayList<PartOfSpeechLabels>();
		for(PartOfSpeechLabels label : PartOfSpeechLabels.values())
		{
			postags.add(label);
		}
		return postags;
	}

}
