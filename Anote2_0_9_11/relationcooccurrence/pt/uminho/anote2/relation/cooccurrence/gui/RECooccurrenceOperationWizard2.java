package pt.uminho.anote2.relation.cooccurrence.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IIEProcess;
import pt.uminho.anote2.relation.cooccurrence.configuration.IRECooccurrenceConfiguration;
import pt.uminho.anote2.relation.cooccurrence.configuration.RECooccurrenceConfiguration;
import pt.uminho.anote2.relation.cooccurrence.core.RECooccurrenceModelEnum;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class RECooccurrenceOperationWizard2 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JLabel jLabelRelationModelImage;
	private JPanel jPanelRelationModelImage;
	private JComboBox jComboBoxRelationModels;
	private JPanel jPanelChangeRelationModel;

	public RECooccurrenceOperationWizard2(List<Object> param) {
		super(param);
		initGUI();
		fillImage();
		this.setTitle("Relation Cooccurrence Extraction - Model Selection");	
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);	
	}

	private void initGUI() {
		setEnableDoneButton(true);
		setEnableNextButton(false);
	}


	private void fillImage() {
		RECooccurrenceModelEnum relationModel = (RECooccurrenceModelEnum) jComboBoxRelationModels.getSelectedItem();
		jLabelRelationModelImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource(relationModel.getImagePath())));
	}

	public void done() {
		Object obj = HelpAibench.getSelectedItem(Corpus.class);
		Corpus corpus = (Corpus) obj;		
		IIEProcess entityProcess = (IIEProcess) getParam().get(0);
		IRECooccurrenceConfiguration configuration = new RECooccurrenceConfiguration(corpus, entityProcess,  (RECooccurrenceModelEnum) jComboBoxRelationModels.getSelectedItem());
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("Corpus", Corpus.class,corpus, null),
				new ParamSpec("Configuration", IRECooccurrenceConfiguration.class,configuration, null)
		};

		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.recooccurrence")){	
				Workbench.getInstance().executeOperation(def, paramsSpec);
				closeView();
				return;
			}
		}
	}

	public void goBack() {
		closeView();
		try {
			new RECooccurrenceOperationWizard1(getParam());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	public void goNext() {

	}
	
	public JPanel getJPanelChangeRelationModel() {
		return jPanelChangeRelationModel;
	}
	
	public JPanel getJPanelRelationModelImage() {
		return jPanelRelationModelImage;
	}
	
	public JLabel getJLabelRelationModelImage() {
		return jLabelRelationModelImage;
	}

	public JComponent getMainComponent() {
		if(jPanelUpperPanel == null)
		{
			jPanelUpperPanel = new JPanel();
			jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Model Selection", TitledBorder.LEADING, TitledBorder.TOP));
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			getContentPane().add(jPanelUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanelLayout.rowWeights = new double[] {0.05, 0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7};
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jPanelChangeRelationModel = new JPanel();
				GridBagLayout jPanelChangeRelationModelLayout = new GridBagLayout();
				jPanelUpperPanel.add(getJPanelChangeRelationModel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelChangeRelationModelLayout.rowWeights = new double[] {0.05};
				jPanelChangeRelationModelLayout.rowHeights = new int[] {7};
				jPanelChangeRelationModelLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelChangeRelationModelLayout.columnWidths = new int[] {7, 7, 7};
				jPanelChangeRelationModel.setLayout(jPanelChangeRelationModelLayout);
				{
					ComboBoxModel jComboBoxRelationModelsModel = new DefaultComboBoxModel(RECooccurrenceModelEnum.values());
					jComboBoxRelationModels = new JComboBox();
					jPanelChangeRelationModel.add(jComboBoxRelationModels, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxRelationModels.setModel(jComboBoxRelationModelsModel);
					jComboBoxRelationModels.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							fillImage();					
						}
					});
				}
			}
			{
				jPanelRelationModelImage = new JPanel();
				jPanelRelationModelImage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Example", TitledBorder.LEADING, TitledBorder.TOP));

				GridBagLayout jPanelRelationModelImageLayout = new GridBagLayout();
				jPanelUpperPanel.add(getJPanelRelationModelImage(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelRelationModelImageLayout.rowWeights = new double[] {0.1};
				jPanelRelationModelImageLayout.rowHeights = new int[] {7};
				jPanelRelationModelImageLayout.columnWeights = new double[] {0.0, 0.1, 0.0};
				jPanelRelationModelImageLayout.columnWidths = new int[] {7, 7, 7};
				jPanelRelationModelImage.setLayout(jPanelRelationModelImageLayout);
				{
					jLabelRelationModelImage = new JLabel();
					jPanelRelationModelImage.add(getJLabelRelationModelImage(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
		return jPanelUpperPanel;
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Relation_Co-occurrence_Extraction#Relation_Co-occurrence_Extraction_Model_Selection";
	}
}
