package pt.uminho.generic.components.table.tablesearcher;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 2, 2009
 * Time: 3:23:51 PM
 */
public class TableSearcherModelHandler implements TableModelListener {
    protected TableModelSearcher tableModelSearcher;

    public TableSearcherModelHandler(TableModelSearcher tableModelSearcher) {
        this.tableModelSearcher = tableModelSearcher;
    }

    @Override
    public void tableChanged(TableModelEvent tableModelEvent) {
        if(tableModelSearcher.isSearching())
            tableModelSearcher.clearSearch();
    }
}
