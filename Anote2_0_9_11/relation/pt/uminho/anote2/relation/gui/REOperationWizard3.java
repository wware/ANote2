package pt.uminho.anote2.relation.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.relation.datastructures.RelationsModelEnem;
import pt.uminho.anote2.relation.settings.RERelationDefaultSettings;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REOperationWizard3 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JLabel jLabelRelationModelImage;
	private JPanel jPanelRelationModelImage;
	private JComboBox jComboBoxRelationModels;
	private JPanel jPanelChangeRelationModel;

	public REOperationWizard3(List<Object> param) {
		super(param);
		initGUI();
		if(param.size() == 3)
		{
			fillWithPreviousSettings(param.get(2));
			param.remove(2);
		}
		else
		{
			defaultSettings();
		}
		fillImage();
		this.setTitle("Relation Extraction - Relation Model Selection");
		Utilities.centerOnOwner(this);
		this.setModal(true);	
		this.setVisible(true);	
	}

	private void fillWithPreviousSettings(Object object) {
		RelationsModelEnem reModel = (RelationsModelEnem) object;
		jComboBoxRelationModels.setSelectedItem(reModel);		
	}

	private void defaultSettings() {
		jComboBoxRelationModels.setSelectedItem(RelationsModelEnem.convertStringToRelationsModelEnem(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.MODEL).toString()));
	}

	private void initGUI() {
		setEnableDoneButton(false);
	}


	private void fillImage() {
		RelationsModelEnem relationModel = (RelationsModelEnem) jComboBoxRelationModels.getSelectedItem();
		jLabelRelationModelImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource(relationModel.getImagePath())));
	}

	public void done() {}

	public void goBack() {
		List<Object> param = getParam();
		closeView();
		new REOperationWizard2(param);
	}

	public void goNext() {
		List<Object> param = getParam();
		param.add(jComboBoxRelationModels.getSelectedItem());
		closeView();
		new REOperationWizard4(param);
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
				ComboBoxModel jComboBoxRelationModelsModel = new DefaultComboBoxModel(RelationsModelEnem.values());
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
		return jPanelUpperPanel;
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Relation_Extraction#Relation_Extraction_Model_Selection";
	}
	
}
