package pt.uminho.anote2.aibench.publicationmanager.views.help;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ButtonTablePDFRenderer extends AbstractCellEditor  implements TableCellEditor,TableCellRenderer{
	
	private static final long serialVersionUID = 1L;
	private JButton viewButton;

	public ButtonTablePDFRenderer()
	{
		viewButton = new JButton();
	}
	
	public void whenClick() {
		
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if((Boolean)value)
		{
			viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
		}
		else
		{
			viewButton.setIcon(null);
		}
		return viewButton;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object value,boolean arg2, int arg3, int arg4) {
		if((Boolean) value)
		{
			viewButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/pdf.png")));
		}
		else
		{
			viewButton.setIcon(null);
		}
		return viewButton;
	}

}
