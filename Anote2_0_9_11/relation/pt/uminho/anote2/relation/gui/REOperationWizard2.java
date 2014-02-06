package pt.uminho.anote2.relation.gui;

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
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.relation.datastructures.PosTaggerEnem;
import pt.uminho.anote2.relation.settings.RERelationDefaultSettings;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REOperationWizard2 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JLabel jLabelSourceImage;
	private JComboBox jComboBoxPosTaggingAvailable;
	private JTextPane jTextPaneDescription;
	private JPanel jPanelImage;
	private JPanel jPanelDescripion;
	private JPanel jPanelPosTaggerSelection;

	public REOperationWizard2(List<Object> param) {
		super(param);
		initGUI();
		if(param.size()==2)
		{
			fillWithPreviousSettings(param.get(1));
			param.remove(1);
		}
		else
		{
			defaultSettings();
		}
		changeInformation();
		this.setTitle("RElation Extraction - Pos Tagger Selection");
		Utilities.centerOnOwner(this);
		this.setModal(true);	
		this.setVisible(true);	
	}


	private void fillWithPreviousSettings(Object object) {
		PosTaggerEnem tagger = (PosTaggerEnem) object;
		jComboBoxPosTaggingAvailable.setSelectedItem(tagger);
	}


	private void defaultSettings() {
		jComboBoxPosTaggingAvailable.setSelectedItem(PosTaggerEnem.convertStringInPosTaggerEnem(PropertiesManager.getPManager().getProperty(RERelationDefaultSettings.POSTAGGER).toString()));
	}


	private void initGUI() {
		setEnableDoneButton(false);
	}


	protected void changeInformation() {
		PosTaggerEnem pos = (PosTaggerEnem) jComboBoxPosTaggingAvailable.getSelectedItem();
		jTextPaneDescription.setText(pos.getDescrition());
		jLabelSourceImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource(pos.getImagePath())));
	}


	public void done() {}

	public void goBack() {
		closeView();
		try {
			new REOperationWizard1(getParam());
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	public void goNext() {
		getParam().add(jComboBoxPosTaggingAvailable.getSelectedItem());
		closeView();
		new REOperationWizard3(getParam());	
	}
	
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Corpus_Relation_Extraction#POS-Tagger_selection";
	}

	public JComponent getMainComponent() {
		if(jPanelUpperPanel == null)
		{
			jPanelUpperPanel = new JPanel();
			GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
			getContentPane().add(jPanelUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			jPanelUpperPanelLayout.rowWeights = new double[] {0.1, 0.1};
			jPanelUpperPanelLayout.rowHeights = new int[] {7, 7};
			jPanelUpperPanelLayout.columnWeights = new double[] {0.1, 0.1};
			jPanelUpperPanelLayout.columnWidths = new int[] {7, 7};
			jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "POS-Tagger Selection", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
			{
				jPanelPosTaggerSelection = new JPanel();
				GridBagLayout jPanelPosTaggerSelectionLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelPosTaggerSelection, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPosTaggerSelectionLayout.rowWeights = new double[] {0.1};
				jPanelPosTaggerSelectionLayout.rowHeights = new int[] {7};
				jPanelPosTaggerSelectionLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelPosTaggerSelectionLayout.columnWidths = new int[] {7, 7, 7};
				jPanelPosTaggerSelection.setLayout(jPanelPosTaggerSelectionLayout);
				{
					ComboBoxModel jComboBoxPosTaggingAvailableModel = new DefaultComboBoxModel(PosTaggerEnem.values());
					jComboBoxPosTaggingAvailable = new JComboBox();
					jComboBoxPosTaggingAvailable.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							changeInformation();
						}
					});
					jComboBoxPosTaggingAvailable.setModel(jComboBoxPosTaggingAvailableModel);
					jPanelPosTaggerSelection.add(jComboBoxPosTaggingAvailable, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
			{
				jPanelDescripion = new JPanel();
				GridBagLayout jPanelDescripionLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelDescripion, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelDescripionLayout.rowWeights = new double[] {0.025, 0.1, 0.025};
				jPanelDescripionLayout.rowHeights = new int[] {7, 7, 7};
				jPanelDescripionLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelDescripionLayout.columnWidths = new int[] {7, 7, 7};
				jPanelDescripion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "POS-Tagger Description", TitledBorder.LEADING, TitledBorder.TOP));
				jPanelDescripion.setLayout(jPanelDescripionLayout);
				{
					jTextPaneDescription = new JTextPane();
					jPanelDescripion.add(jTextPaneDescription, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
			{
				jPanelImage = new JPanel();
				GridBagLayout jPanelImageLayout = new GridBagLayout();
				jPanelUpperPanel.add(jPanelImage, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelImageLayout.rowWeights = new double[] {0.025, 0.1, 0.025};
				jPanelImageLayout.rowHeights = new int[] {7, 7, 7};
				jPanelImageLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelImageLayout.columnWidths = new int[] {7, 7, 7};
				jPanelImage.setLayout(jPanelImageLayout);
				jPanelImage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Source", TitledBorder.LEADING, TitledBorder.TOP));
				{
					jLabelSourceImage = new JLabel();
					jPanelImage.add(jLabelSourceImage, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
		return jPanelUpperPanel;
	}
}
