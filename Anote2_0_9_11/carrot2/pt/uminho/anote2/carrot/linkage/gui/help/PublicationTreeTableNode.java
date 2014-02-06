package pt.uminho.anote2.carrot.linkage.gui.help;

import pt.uminho.anote2.core.document.IPublication;


public class PublicationTreeTableNode extends InformationTreeTableNode {
	
	private IPublication pub;
	
	public PublicationTreeTableNode(IPublication pub,int columnNumber) {
		super(pub, false,columnNumber);
		this.pub = pub;
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode#getUserObject()
	 */
	@Override
	public IPublication getUserObject() {
		return pub;
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode#getValueAt(int)
	 */
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return pub.getTitle();
		case 1:
			return "ID : "+ pub.getID();
		default:
			return "";
		}
	}
	
}
