package pt.uminho.anote2.aibench.publicationmanager.views.help;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import pt.uminho.anote2.aibench.publicationmanager.gui.help.RelevanceInfoPane;
import pt.uminho.anote2.core.document.RelevanceType;

public class ButtonTableRelevanceRender extends AbstractCellEditor  implements TableCellEditor,TableCellRenderer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RelevanceInfoPane panel;
	
	public ButtonTableRelevanceRender()
	{
		panel = new RelevanceInfoPane(RelevanceType.none);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
		RelevanceType rt = (RelevanceType) value;
		panel.setrelevance(rt);
		return panel;
	}

	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,boolean isSelected, int row, int column) {
		RelevanceType rt = (RelevanceType) value;
		panel.setrelevance(rt);
		return panel;
	}
	
	

}
