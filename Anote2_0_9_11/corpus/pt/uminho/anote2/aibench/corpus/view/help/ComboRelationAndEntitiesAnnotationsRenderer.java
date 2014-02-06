package pt.uminho.anote2.aibench.corpus.view.help;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pt.uminho.anote2.aibench.corpus.stats.ClassNumberOfOccurrences;
import pt.uminho.anote2.aibench.corpus.stats.DocumentEntityStatistics;
import pt.uminho.anote2.aibench.corpus.stats.DocumentRelationsAndEntitiesStats;


public class  ComboRelationAndEntitiesAnnotationsRenderer extends AbstractCellEditor  implements TableCellEditor,TableCellRenderer{
	
	private static final long serialVersionUID = 1L;
	
	private Map<Integer, DocumentRelationsAndEntitiesStats> docStatistics;
	private Map<Integer,JComboBox> docIDComboBox;
	private int editable = 0;
	
	public ComboRelationAndEntitiesAnnotationsRenderer(Map<Integer, DocumentRelationsAndEntitiesStats> docStatistics)
	{
		this.docStatistics = docStatistics;
		this.docIDComboBox = new HashMap<Integer, JComboBox>();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		int documentID = (Integer) table.getValueAt(row,3);
		if(!docIDComboBox.containsKey(documentID))
		{
			docIDComboBox.put(documentID, getComboBox(documentID, docStatistics));

		}
		return docIDComboBox.get(documentID);
	}

	public Object getCellEditorValue() {
		return editable;
	}

	public Component getTableCellEditorComponent(JTable table, Object arg1,boolean isSelected, int row, int column) {
		int documentID = (Integer) table.getValueAt(row,3);
		if(!docIDComboBox.containsKey(documentID))
		{
			docIDComboBox.put(documentID, getComboBox(row, docStatistics));
		}
		editable = docStatistics.get(documentID).getRelationsNumber();
		return docIDComboBox.get(documentID);
	}

	public JComboBox getComboBox(int documentID,Map<Integer, DocumentRelationsAndEntitiesStats> docStatistics2) {
		DefaultComboBoxModel amodel = new DefaultComboBoxModel();
		amodel.addElement("Relations :"+docStatistics2.get(documentID).getRelationsNumber());
		if(docStatistics2.containsKey(documentID))
		{
			DocumentEntityStatistics docStats = docStatistics2.get(documentID);
			for(int classID : docStats.getClassIDStats().keySet())
			{
				amodel.addElement(new ClassNumberOfOccurrences(classID, docStats.getClassIDStats().get(classID)));
			}
		}
		return new JComboBox(amodel);
	}

}
