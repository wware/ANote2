package pt.uminho.anote2.aibench.utils.wizard;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;

import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;


public abstract class WizardStandard extends SimpleDialogView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WizardStandard(List<Object> param)
	{
		super(param);
		initGUI();
	}
	

	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.1, 0.0};
				thisLayout.rowHeights = new int[] {7, 7};
				thisLayout.columnWeights = new double[] {0.1};
				thisLayout.columnWidths = new int[] {7};
				getContentPane().setLayout(thisLayout);
			}
			{
				getContentPane().add(getMainComponent(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					getContentPane().add(getJPaneButtons(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}			
				this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);		
			}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	public abstract JComponent getMainComponent();

	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {	
	}

	public void onValidationError(Throwable arg0) {	
	}
}
