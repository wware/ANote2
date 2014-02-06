package pt.uminho.anote2.aibench.curator.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.manualcuration.ManualCurationEnum;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class CreateManualCurationProcessGUI extends DialogGenericViewInputGUI{

	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPAnel;
	private JTextField jTextFieldNAme;
	private Corpus corpus;
	private JCheckBox jCheckBoxEntities;
	private JCheckBox jCheckBoxEventRelations;
	private JPanel jPanelSelectEntitiesAndOrRelations;

	public CreateManualCurationProcessGUI()
	{
		super("Create Manual Curation Process");

	}
	
	
	private boolean confirmSelectedDatatype() {
		Corpus corpus = (Corpus) HelpAibench.getSelectedItem(Corpus.class);
		if(corpus == null)
		{
			Workbench.getInstance().warn("You need to select a Corpus");
			return false;
		}
		else
		{
			this.corpus = corpus;
			return true;
		}
	}


	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelUpperPAnel = new JPanel();
				GridBagLayout jPanelUpperPAnelLayout = new GridBagLayout();
				getContentPane().add(jPanelUpperPAnel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelUpperPAnel.setBorder(BorderFactory.createTitledBorder("Select Manual Curation Process Name"));
				jPanelUpperPAnelLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelUpperPAnelLayout.rowHeights = new int[] {7, 7, 7};
				jPanelUpperPAnelLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelUpperPAnelLayout.columnWidths = new int[] {7, 7, 7};
				jPanelUpperPAnel.setLayout(jPanelUpperPAnelLayout);
				{
					jTextFieldNAme = new JTextField();
					jPanelUpperPAnel.add(jTextFieldNAme, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
				}
				{
					jPanelSelectEntitiesAndOrRelations = new JPanel();
					GridBagLayout jPanelSelectEntitiesAndOrRelationsLayout = new GridBagLayout();
					jPanelUpperPAnel.add(jPanelSelectEntitiesAndOrRelations, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelSelectEntitiesAndOrRelationsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
					jPanelSelectEntitiesAndOrRelationsLayout.rowHeights = new int[] {7, 7, 7};
					jPanelSelectEntitiesAndOrRelationsLayout.columnWeights = new double[] {0.025, 0.1, 0.1, 0.025};
					jPanelSelectEntitiesAndOrRelationsLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelSelectEntitiesAndOrRelations.setLayout(jPanelSelectEntitiesAndOrRelationsLayout);
					{
						jCheckBoxEntities = new JCheckBox();
						jCheckBoxEntities.setSelected(true);
						jPanelSelectEntitiesAndOrRelations.add(jCheckBoxEntities, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jCheckBoxEntities.setText("Entities");
						jCheckBoxEntities.setEnabled(false);
					}
					{
						jCheckBoxEventRelations = new JCheckBox();
						jPanelSelectEntitiesAndOrRelations.add(jCheckBoxEventRelations, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jCheckBoxEventRelations.setText("Event / Relations");
					}
				}
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}


	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.paramsRec = arg0;
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		if(confirmSelectedDatatype())
		{
			initGUI();
			this.setModal(true);
			this.setVisible(true);
		}
		else
		{
			finish();
		}
	}
	
	
	@Override
	protected void okButtonAction() {
		if(jTextFieldNAme.getText().length()<1)
		{
			Workbench.getInstance().warn("Please Select Manual Curation Process Name");
		}
		else
		{
			ManualCurationEnum curatortype = ManualCurationEnum.NER;
			if(jCheckBoxEventRelations.isSelected())
			{
				curatortype = ManualCurationEnum.RE;
			}
			this.paramsRec.paramsIntroduced(new ParamSpec[]{
					new ParamSpec("Corpus", Corpus.class,corpus, null),
					new ParamSpec("CuratorType", ManualCurationEnum.class,curatortype , null),
					new ParamSpec("Process Name", String.class,jTextFieldNAme.getText(), null)
					}
			);
		}
	}
	
	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Manual_Curation";
	}

}
