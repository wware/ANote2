package pt.uminho.generic.csvloaders.tabTable.table.cellrender;

import javax.swing.SpinnerModel;
import javax.swing.event.ChangeListener;

public class SpinnerNullModel implements SpinnerModel{

	@Override
	public void addChangeListener(ChangeListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getNextValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPreviousValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		return "---";
	}

	@Override
	public void removeChangeListener(ChangeListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Object value) {
		// TODO Auto-generated method stub
		
	}

}
