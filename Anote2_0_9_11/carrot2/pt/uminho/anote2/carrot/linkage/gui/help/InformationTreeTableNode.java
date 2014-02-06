package pt.uminho.anote2.carrot.linkage.gui.help;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class InformationTreeTableNode extends DefaultMutableTreeTableNode {
		/**
		 * 
		 */
		private int columNumber;
	
	
	
		public InformationTreeTableNode(int columnNumber) {
			super();
			this.columNumber=columnNumber;
		}

		/**
		 * @param userObject
		 * @param allowsChildren
		 */
		public InformationTreeTableNode(Object userObject,boolean allowsChildren,int columnNumber) {
			super(userObject, allowsChildren);
			this.columNumber = columnNumber;
		}

		/**
		 * @param userObject
		 */
		public InformationTreeTableNode(Object userObject,int columnNumber) {
			super(userObject);
			this.columNumber = columnNumber;
		}
		
		/* (non-Javadoc)
		 * @see org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode#isEditable(int)
		 */
		public boolean isEditable(int column) { 
			return false;
		}
		
		/* (non-Javadoc)
		 * @see org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode#getColumnCount()
		 */
		@Override
		public int getColumnCount() {
			return columNumber;
		}
		
		/* (non-Javadoc)
		 * @see org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode#getValueAt(int)
		 */
		@Override
		public Object getValueAt(int column) {
			return (column==0)?this.getUserObject():"";
		}
	}
	