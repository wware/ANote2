package pt.uminho.generic.components.table.tableremover;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * DynamicModelSimulator
 * Created By
 * User: ptiago
 * Date: Mar 2, 2009
 * Time: 5:55:02 PM
 */
public class TableRemoverModelHandler implements TableModelListener{
    protected TableModelColumnRemover tableRemover;

    public TableRemoverModelHandler(TableModelColumnRemover tableRemover) {
        this.tableRemover = tableRemover;
    }

    @Override
    public void tableChanged(TableModelEvent tableModelEvent) {
        if(!tableRemover.isEnableRemove())
            tableRemover.clearRemove();
    }
}
