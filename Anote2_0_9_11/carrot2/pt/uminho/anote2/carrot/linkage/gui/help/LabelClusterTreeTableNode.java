package pt.uminho.anote2.carrot.linkage.gui.help;

import pt.uminho.anote2.carrot.linkage.datastructures.ILabelCluster;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;

 public class LabelClusterTreeTableNode extends InformationTreeTableNode {
	

	private static final int ColumnEditable = 3 ;
	private final ILabelCluster label;
	private boolean selected;

	public LabelClusterTreeTableNode(ILabelCluster label,int columnNumber) {
		super(label, true,columnNumber);
		this.label = label;
		this.selected = false;
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode#getUserObject()
	 */
	public ILabelCluster getUserObject() {
		return this.label;
	}
	
	/* (non-Javadoc)
	 * @see es.uvigo.ei.sing.aibench.pluginmanager.PluginInformationComponent.InformationTreeTableNode#isEditable(int)
	 */
	public boolean isEditable(int column) {
		if(column == ColumnEditable)
		{
			boolean selectedaux = new Boolean(selected);
			selected = !selectedaux;
		}
		return (column == ColumnEditable);
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode#setValueAt(java.lang.Object, int)
	 */
	public void setValueAt(Object value, int column) {
		if (column == ColumnEditable && value instanceof Boolean) {
//			selected = (Boolean) value;
		}
		else
		{
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode#getValueAt(int)
	 */
	public Object getValueAt(int column) {
		switch(column) {
		case 0:
			return label.getLabelName();
		case 1:
			return label.getLabelDocumentsID().size();
		case 2:
			return GlobalOptions.decimalformat.format(label.getScore());
		default :
			return selected;
		}
	}
	
	public ILabelCluster getLabel() {
		return label;
	}

	public boolean isSelected() {
		return selected;
	}

}

