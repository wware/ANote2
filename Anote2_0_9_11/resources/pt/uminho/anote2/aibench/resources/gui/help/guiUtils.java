package pt.uminho.anote2.aibench.resources.gui.help;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class guiUtils {

	/**This method changes the default renderer of a given table
	 * @param table - the table where the renderer will be applied 
	 * @param alignment - the alignment for the cells*/
	public static void setTableRenderer(JTable table, Integer alignment){
		
		final Integer alnt = alignment;
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {  
	
			private static final long serialVersionUID = -8183629931912150659L;
	
			public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {  
		         super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
		         if (row % 2 != 0) {  
		        	 setBackground(Color.decode("#F0F0F0"));  
		         } else {  
		        	 setBackground(null);  
		         }  
				
		         if(alnt!=null)
					this.setHorizontalAlignment(alnt);
				 return this;
		    }  
		}); 
	}
	
	/**This method changes the default renderer of a given table
	 * @param table - the table where the renderer will be applied 
	 * @param indexs - type of renderer for each index
	 * @param colors - color of renderer each type in the indexs vector
	 * @param alignment - the alignment for the cells*/
	public static void setTableRenderer(JTable table, int[] indexs, Color[] colors, Integer alignment){
		
		final Integer alnt = alignment;
		final Color[] clrs = colors;
		final int[] idxs = indexs;
		
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {  
	
			private static final long serialVersionUID = -8183629931912150659L;
	
			public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {  
		         super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
		      
		         if(idxs[row]>=clrs.length)
		         {
		        	 if (row % 2 != 0) 
			        	 setBackground(Color.decode("#F0F0F0"));  
			         else
			        	 setBackground(null);  
			     }
		         else
		        	 setBackground(clrs[idxs[row]]);  
		         
		         if(alnt!=null)
					this.setHorizontalAlignment(alnt);
				 return this;
		    }  
		}); 
	}
	
	/**This method returns a table with the given model.
	 * The table will be not editable and only row selection will be allowed. */
	public static JTable constructTable(TableModel model){
		
		JTable table = new JTable(){
	
			private static final long serialVersionUID = -5079355133615100914L;

			@Override
			public boolean isCellEditable(int x,int y){
				return false;
			}
		};
		
		table.setModel(model);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setSelectionForeground(Color.BLUE);
		return table;
	}
	
	/**This class extends a ComboBox model
	 * This extension adds to the model, elements of ANoteProperty type, and each element will have the
	 * color defined for the own anote property*/
//	public static class TaskComboCellRenderer extends JLabel implements ListCellRenderer
//	{
//		private static final long serialVersionUID = 3574629326149754076L;
//
//		public TaskComboCellRenderer()
//	    {
//	    	setOpaque(true);
//	    }
//
//		public Component getListCellRendererComponent(JList list,Object value, int index, boolean isSelected,boolean cellHasFocus) 
//		{
//			if( value == null ) 
//				return this;
//		    
//		    ANoteProperty p = (ANoteProperty) value;
//		    setText(p.getName());
//
//		    if(!isSelected)
//		    	setForeground(Color.decode(p.getColor()));
//		    else
//		        setForeground( list.getSelectionForeground() );
//		    
//		    return this;
//		}
//	}
	
	/**Creates a GridBag with the given number of rows and columns*/
	public static GridBagLayout createGrid(int rows, int columns){
		GridBagLayout layout = new GridBagLayout();
		
		double[] rowWeights = new double[rows], columnWeights = new double[columns];
		int[] rowHeights = new int[rows], columnHeights = new int[columns];
		
		for(int i=0; i<rows; i++)
		{
			rowWeights[i] = 0.1;
			rowHeights[i] = 7;
		}
		
		for(int i=0; i<columns; i++)
		{
			columnWeights[i] = 0.1;
			columnHeights[i] = 7;
		}
		
		layout.rowWeights = rowWeights;
		layout.rowHeights = rowHeights;
		layout.columnWeights = columnWeights;
		layout.columnWidths = columnHeights;
		
		return layout;
	}
	
	/**Returns a model for ComboBox. The model will contain the projects on the Clipboard
	 * @throws ANoteMissingDatatypesException */
//	public static ComboBoxModel getProjectComboModel() throws ANoteMissingDatatypesException{
//		DefaultComboBoxModel model = new DefaultComboBoxModel();
//		
//		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(ANoteProject.class);
//
//		if (cl == null || cl.size()==0) {
//			throw new ANoteMissingDatatypesException("There is no project to proceed!");
//		}
//		
//		for (ClipboardItem item : cl) {
//			model.addElement((ANoteProject)item.getUserData());
//		}
//		return model;
//	}
	
	/**Returns a model for ComboBox. The model will contain the tasks of the given project
	 * The other param, is true when is desired to add a "other" value to the model*/
//	public static ComboBoxModel getTaskComboModel(ANoteProject project, boolean other){
//		DefaultComboBoxModel model = new DefaultComboBoxModel();
//		
//		if(other)
//			model.addElement(new ANoteProperty("other", "#000000", null));
//		
//		ANoteTask task = project.getProjectPreferences().getTask();
//		for(ANoteProperty p: task.getProperties().values())
//		{
//			if(!p.getColor().equals("none"))
//				model.addElement(p);
//		}
//		
//		return model;
//	}
	
	/**Returns a model for ComboBox. The model will contain the objects on the Clipboard of the given class
	 * @throws ANoteMissingDatatypesException */
//	public static ComboBoxModel getDatatypeComboModel(Class type) throws ANoteMissingDatatypesException{
//		DefaultComboBoxModel model = new DefaultComboBoxModel();
//		
//		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(type);
//
//		if (cl == null || cl.size()==0)
//			throw new ANoteMissingDatatypesException("There is no " + type.getName() + "'s intances on the clipboard to proceed!");
//		
//		
//		for (ClipboardItem item : cl)
//			model.addElement(item.getUserData());
//	
//		return model;
//	}
}
