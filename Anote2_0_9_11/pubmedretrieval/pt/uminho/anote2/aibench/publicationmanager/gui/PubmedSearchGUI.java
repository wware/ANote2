package pt.uminho.anote2.aibench.publicationmanager.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Properties;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.PublicationManager;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.APubmedSeach;
import pt.uminho.anote2.aibench.publicationmanager.gui.help.PubmedSearchOptionsPane;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

/**
 * 
 * AibenchGUI for PubmedSearchOperation
 * 
 */
public class PubmedSearchGUI extends DialogGenericViewInputGUI {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private APubmedSeach panel;

	
	private boolean finish = false;
	
	public PubmedSearchGUI() {
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		this.setModal(true);
		initGUI();	
	}

	protected void okButtonAction() {
		if(panel.validateOptions())
		{
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(PublicationManager.class);
			IIRSearchConfiguration conf = panel.getConfiguration();
			this.paramsRec.paramsIntroduced( new ParamSpec[]{ 
				new ParamSpec("catalogue",PublicationManager.class,(PublicationManager) cl.get(0).getUserData(),null),
				new ParamSpec("keywords",String.class,conf.getKeywords(),null),
				new ParamSpec("organism",String.class,conf.getOrganism(),null),
				new ParamSpec("properties",Properties.class,conf.getProperties(),null),
			});
		}
	}
	
	private void initGUI(){
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7,7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				panel = new PubmedSearchOptionsPane();
				getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
				getContentPane().add(this.getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
			}
		}
	}	
	
	@Override
	public void init(ParamsReceiver arg0, @SuppressWarnings("rawtypes") OperationDefinition arg1) {
		
		if(finish)
			finish();
		else
		{
			this.paramsRec = arg0;
			this.setTitle("Pubmed Search");
			Utilities.centerOnOwner(this);
			this.setVisible(true);
		}
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);		
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Pubmed_Search";
	}

}
