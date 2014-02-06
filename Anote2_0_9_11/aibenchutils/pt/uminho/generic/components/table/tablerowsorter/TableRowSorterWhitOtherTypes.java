package pt.uminho.generic.components.table.tablerowsorter;

import java.util.Comparator;
import java.util.Map;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * 
 * 
 * @author Hugo Costa
 *
 */
public class TableRowSorterWhitOtherTypes extends TableRowSorter<TableModel>{
	
	
	private Map<Integer,Comparator<?>> columnComparators;
	
	public TableRowSorterWhitOtherTypes(Map<Integer,Comparator<?>> columnComparators)
	{
		this.columnComparators = columnComparators;
	}	
	
	public  Comparator<?> getComparator(int column)
	{
		
		if(columnComparators!=null && columnComparators.containsKey(column) && columnComparators.get(column)!=null)
		{
			return columnComparators.get(column);
		}
		return super.getComparator(column);
	}
	
	@Override
	protected boolean useToString(int column) {
		
		if(columnComparators!=null && columnComparators.containsKey(column) && columnComparators.get(column)!=null)
		{
			return false;
		}
		return super.useToString(column);
	}
	
}
