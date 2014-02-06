package pt.uminho.generic.genericpanel.buttons;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;

public abstract class ButtonTableCellEditor extends DefaultCellEditor{

	private static final long serialVersionUID = 1L;

	protected JButton button;

	  private String label;

	  private boolean isPushed;

	  public ButtonTableCellEditor() {
	    super(new JCheckBox());
	    button = new JButton();
	    button.setOpaque(true);
	    button.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        fireEditingStopped();
	      }
	    });
	  }

	  public Component getTableCellEditorComponent(JTable table, Object value,
	      boolean isSelected, int row, int column) {
	    if (isSelected) {
	      button.setForeground(table.getSelectionForeground());
	      button.setBackground(table.getSelectionBackground());
	    } else {
	      button.setForeground(table.getForeground());
	      button.setBackground(table.getBackground());
	    }
	    label = (value == null) ? "" : value.toString();
	    button.setText(label);
	    isPushed = true;
	    return button;
	  }

	  public Object getCellEditorValue() {
	    if (isPushed) {
	    	try {
				whenClick();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
	    }
	    isPushed = false;
	    return new String(label);
	  }



	  public boolean stopCellEditing() {
		  isPushed = false;
		  return super.stopCellEditing();
	  }
	
	  /**
	   * vERIFICAR DEPOIS
	   */
	protected void fireEditingStopped() {
		try {
			super.fireEditingStopped();
		} catch (Exception e) {
		}
	}

	  
	  public abstract void whenClick() throws SQLException, DatabaseLoadDriverException;
}
