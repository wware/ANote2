package pt.uminho.anote2.workflow.settings.query.pane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.workflow.settings.query.WorkflowQueryDefaulSettings;

public class WorkflowQueryCorpusSettings extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> initial_props;
	private JPanel jPanelCorpusName;
	private JPanel jPanelCorpusType;
	private JCheckBox jCheckBoxRetievalPDF;
	private JPanel jPanelRetrievalPDF;
	private JComboBox jComboBoxCorpusType;
	private JTextField jTextFieldCorpusName;

	public WorkflowQueryCorpusSettings(Map<String, Object> initial_props)
	{
		this.initial_props = initial_props;
		initGUI();
		fillSettings();
	}

	private void fillSettings() {
		jTextFieldCorpusName.setText(initial_props.get(WorkflowQueryDefaulSettings.CORPUS_NAME).toString());
		jComboBoxCorpusType.setSelectedItem(initial_props.get(WorkflowQueryDefaulSettings.CORPUS_TYPE));
		jCheckBoxRetievalPDF.setSelected(Boolean.parseBoolean(initial_props.get(WorkflowQueryDefaulSettings.CORPUS_RETRIEVAL_PDF).toString()));
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(613, 489));
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			this.add(getJPanelCorpusName(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJPanelCorpusType(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			this.add(getJPanelRetrievalPDF(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}

	}
	
	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put(WorkflowQueryDefaulSettings.CORPUS_NAME, jTextFieldCorpusName.getText());
		settings.put(WorkflowQueryDefaulSettings.CORPUS_TYPE, jComboBoxCorpusType.getSelectedItem());
		settings.put(WorkflowQueryDefaulSettings.CORPUS_RETRIEVAL_PDF, jCheckBoxRetievalPDF.isSelected());
		return settings;
	}


	public boolean haveChanged() {
		if(!initial_props.get(WorkflowQueryDefaulSettings.CORPUS_NAME).equals(jTextFieldCorpusName.getText()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowQueryDefaulSettings.CORPUS_TYPE).equals(jComboBoxCorpusType.getSelectedItem()))
		{
			return true;
		}
		else if(!initial_props.get(WorkflowQueryDefaulSettings.CORPUS_RETRIEVAL_PDF).equals(jCheckBoxRetievalPDF.isSelected()))
		{
			return true;
		}
		return false;
	}
	
	private JPanel getJPanelCorpusName() {
		if(jPanelCorpusName == null) {
			jPanelCorpusName = new JPanel();
			GridBagLayout jPanelCorpusNameLayout = new GridBagLayout();
			jPanelCorpusName.setBorder(BorderFactory.createTitledBorder("Corpus Name"));
			jPanelCorpusNameLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelCorpusNameLayout.rowHeights = new int[] {7, 7, 7};
			jPanelCorpusNameLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelCorpusNameLayout.columnWidths = new int[] {7, 7, 7};
			jPanelCorpusName.setLayout(jPanelCorpusNameLayout);
			jPanelCorpusName.add(getJTextFieldCorpusName(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelCorpusName;
	}
	
	private JTextField getJTextFieldCorpusName() {
		if(jTextFieldCorpusName == null) {
			jTextFieldCorpusName = new JTextField();
			jTextFieldCorpusName.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return jTextFieldCorpusName;
	}
	
	private JPanel getJPanelCorpusType() {
		if(jPanelCorpusType == null) {
			jPanelCorpusType = new JPanel();
			GridBagLayout jPanelCorpusTypeLayout = new GridBagLayout();
			jPanelCorpusType.setBorder(BorderFactory.createTitledBorder("Corpus Type"));
			jPanelCorpusTypeLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelCorpusTypeLayout.rowHeights = new int[] {7, 7, 7};
			jPanelCorpusTypeLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
			jPanelCorpusTypeLayout.columnWidths = new int[] {7, 7, 7};
			jPanelCorpusType.setLayout(jPanelCorpusTypeLayout);
			jPanelCorpusType.add(getJComboBoxCorpusType(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelCorpusType;
	}
	
	private JComboBox getJComboBoxCorpusType() {
		if(jComboBoxCorpusType == null) {
			ComboBoxModel jComboBoxCorpusTypeModel = 
					new DefaultComboBoxModel(CorpusTextType.values());
			jComboBoxCorpusType = new JComboBox();
			jComboBoxCorpusType.setModel(jComboBoxCorpusTypeModel);
		}
		return jComboBoxCorpusType;
	}
	
	private JPanel getJPanelRetrievalPDF() {
		if(jPanelRetrievalPDF == null) {
			jPanelRetrievalPDF = new JPanel();
			GridBagLayout jPanelRetrievalPDFLayout = new GridBagLayout();
			jPanelRetrievalPDF.setBorder(BorderFactory.createTitledBorder("Retrieve PDF"));
			jPanelRetrievalPDFLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
			jPanelRetrievalPDFLayout.rowHeights = new int[] {7, 7, 7};
			jPanelRetrievalPDFLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			jPanelRetrievalPDFLayout.columnWidths = new int[] {7, 7, 7};
			jPanelRetrievalPDF.setLayout(jPanelRetrievalPDFLayout);
			jPanelRetrievalPDF.add(getJCheckBoxRetievalPDF(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		}
		return jPanelRetrievalPDF;
	}
	
	private JCheckBox getJCheckBoxRetievalPDF() {
		if(jCheckBoxRetievalPDF == null) {
			jCheckBoxRetievalPDF = new JCheckBox();
			jCheckBoxRetievalPDF.setText("Retrieve PDF");
		}
		return jCheckBoxRetievalPDF;
	}

}
