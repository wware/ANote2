package pt.uminho.generic.genericpanelold.tablesearcher;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class FilterRowPanel extends JPanel  {
	private static final long serialVersionUID = 1L;
	public static final String REMOVE_ZERO_ROWS_CHECKBOX_ACTION_COMMAND = "removeZeroRowsActionCommand";
	protected JCheckBox removeZeroRowsCheckBox;
	
	public FilterRowPanel(){
		initGUI();
	}
	
	protected void initGUI() {
		setLayout(new FlowLayout());
		removeZeroRowsCheckBox = new JCheckBox("Remove zero rows");
		add(removeZeroRowsCheckBox);
		removeZeroRowsCheckBox.setActionCommand(REMOVE_ZERO_ROWS_CHECKBOX_ACTION_COMMAND);
	}

	public void addRemoveZeroRowsCheckBoxActionListener(ActionListener actionListener){
		removeZeroRowsCheckBox.addActionListener(actionListener);
	}

	public boolean isRemoveZeroValueSelected() {
		return removeZeroRowsCheckBox.isSelected();
	}

	
}
