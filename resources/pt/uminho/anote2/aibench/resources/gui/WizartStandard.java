package pt.uminho.anote2.aibench.resources.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public abstract class WizartStandard extends SimpleDialogView{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int sizeH;
	private int sizeV;
	private JPanel jPanelUppanel;

	private JScrollPane jScrollPaneUpPanel;

	public WizartStandard(int sizeH,int sizeV,List<Object> param)
	{
		super(param);
		this.sizeH=sizeH;
		this.sizeV=sizeV;
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
				jScrollPaneUpPanel = new JScrollPane();
				getContentPane().add(jScrollPaneUpPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jPanelUppanel = new JPanel();
					
				}
				{
					jScrollPaneUpPanel.setViewportView(jPanelUppanel);
				}
				{
					getContentPane().add(getJPaneButtons(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}			
				this.setSize(sizeH, sizeV);		
			}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	public JScrollPane getJScrollPaneUpPanel() {
		return jScrollPaneUpPanel;
	}

	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		
	}

	public void onValidationError(Throwable arg0) {
		
	}
}
