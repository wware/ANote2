package pt.uminho.anote2.carrot.linkage.gui.help;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

final class BooleanCellEditor extends DefaultCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JCheckBox checkBox;
	public BooleanCellEditor() {
		this(new JCheckBox());
	}
	
	public BooleanCellEditor(JCheckBox checkBox) {
		super(checkBox);
		this.checkBox = checkBox;
		this.checkBox.setHorizontalAlignment(JCheckBox.CENTER);
//		this.checkBox.setOpaque(true);
	}
}
