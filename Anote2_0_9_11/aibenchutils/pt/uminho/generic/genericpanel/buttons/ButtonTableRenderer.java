package pt.uminho.generic.genericpanel.buttons;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

public abstract class ButtonTableRenderer extends JButton implements TableCellRenderer{

	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;

	public ButtonTableRenderer() {
	    setOpaque(true);
	 }
	
	public ButtonTableRenderer(ImageIcon imageIcon) {
	    setOpaque(true);
	    this.imageIcon = imageIcon;
	  }

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if(imageIcon!=null)
			this.setIcon(imageIcon);
		this.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					whenClick();
				} catch (SQLException e1) {
					TreatExceptionForAIbench.treatExcepion(e1);
				} catch (DatabaseLoadDriverException e1) {
					TreatExceptionForAIbench.treatExcepion(e1);
				}
			}
		});
		return this;
	  }

	public abstract void whenClick() throws SQLException, DatabaseLoadDriverException;
	
	
	public String toString()
	{
		return getText();
	}
	
}
