package pt.uminho.anote2.carrot.linkage.gui.help;

import java.util.List;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;


public class ClusterSelectionTreeTableModel extends DefaultTreeTableModel {
	
	private int columnEnable = 3;

	public ClusterSelectionTreeTableModel(TreeTableNode root,int columnEnable,List<String> COLUMN_NAMES) {
		super(root, COLUMN_NAMES);
		this.columnEnable = columnEnable;
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.treetable.AbstractTreeTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		if (column == columnEnable) {
			return Boolean.class;
		} else {
			return super.getColumnClass(column);
		}
	}
}