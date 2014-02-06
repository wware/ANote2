package pt.uminho.anote2.aibench.utils.gui;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;

/* This class is a generic JDialog that contains a panel button's with cancel and OK actions. Each class that extends
 * this one, have to add a action listener to the button OK, the cancel button have already an associated action.
 * This class have also the three methods of the ImputGUI interface already implemented and the ParamsReceiver variable. */
public abstract class DialogGenericViewInputGUI extends DialogGenericView implements InputGUI{

	private static final long serialVersionUID = -2L;
	
	protected JButton cancelButton;
	protected JButton okButton;
	protected JPanel buttonsPanel;
	
	protected ParamsReceiver paramsRec = null;
	
	public DialogGenericViewInputGUI(String title){
		super(title);
	}
	
	public DialogGenericViewInputGUI(){
		super();
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);		
	}
	
	
	/* ----------------------------------- Abstract Methods ----------------------------------- */
	
	protected abstract void okButtonAction();
	
	protected abstract String getHelpLink();
	
}
