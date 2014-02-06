package pt.uminho.anote2.aibench.corpus.settings.corpora;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;


public class CorporaChangeSettingsGUI extends javax.swing.JPanel implements IPropertiesPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> initial_props;
	private JLabel jLabelCorpusSize;
	private JSpinner jSpinnerCorpusSizeMax;
	private JCheckBox jCheckBoxRestrictCorpusSize;
	private JPanel jPanelCorporaLimits;
	private Map<String, Object> defaultProps;
	
	public CorporaChangeSettingsGUI(Map<String, Object> initial_props, Map<String, Object> defaultProps)
	{
		super();

		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		this.initial_props = initial_props;
		this.defaultProps = defaultProps;
		initGUI();
		fillSettings();
	}
	
	public CorporaChangeSettingsGUI()
	{
		
	}
	
	
	protected void fillSettings()
	{
		int value = Integer.valueOf(initial_props.get(CorporaDefaultSettings.CORPUS_SIZE_LIMIT).toString());
		if(value == -1)
		{
			jCheckBoxRestrictCorpusSize.setSelected(false);
			jLabelCorpusSize.setEnabled(false);
			jSpinnerCorpusSizeMax.setEnabled(false);
			SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 10000, 1);
			jSpinnerCorpusSizeMax.setModel(model);	
		}
		else
		{
			jCheckBoxRestrictCorpusSize.setSelected(true);
			SpinnerNumberModel model = new SpinnerNumberModel(value, 1, 10000, 1);
			jSpinnerCorpusSizeMax.setModel(model);	
		}
	}
	
	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(551, 476));
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelCorporaLimits = new JPanel();
				GridBagLayout jPanelCorporaLimitsLayout = new GridBagLayout();
				this.add(jPanelCorporaLimits, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelCorporaLimits.setBorder(BorderFactory.createTitledBorder("Corpus Size"));
				jPanelCorporaLimitsLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				jPanelCorporaLimitsLayout.rowHeights = new int[] {7, 7, 7};
				jPanelCorporaLimitsLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
				jPanelCorporaLimitsLayout.columnWidths = new int[] {7, 20, 7};
				jPanelCorporaLimits.setLayout(jPanelCorporaLimitsLayout);
				{
					jCheckBoxRestrictCorpusSize = new JCheckBox();
					jPanelCorporaLimits.add(jCheckBoxRestrictCorpusSize, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jCheckBoxRestrictCorpusSize.setText("Restrict Corpus Size");
					jCheckBoxRestrictCorpusSize.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							if(jCheckBoxRestrictCorpusSize.isSelected())
							{
								jLabelCorpusSize.setEnabled(true);
								jSpinnerCorpusSizeMax.setEnabled(true);
							}
							else
							{
								jLabelCorpusSize.setEnabled(false);
								jSpinnerCorpusSizeMax.setEnabled(false);
							}
						}
					});
				}
				{
					jSpinnerCorpusSizeMax = new JSpinner();
					jSpinnerCorpusSizeMax.setName("Corpus Size ");
					jPanelCorporaLimits.add(jSpinnerCorpusSizeMax, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
				}
				{
					jLabelCorpusSize = new JLabel();
					jPanelCorporaLimits.add(jLabelCorpusSize, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelCorpusSize.setText("Corpus Max Size :");
				}
			}
		}
	}


	@Override
	public Map<String, Object> getProperties() {
		HashMap<String, Object> settings = new HashMap<String, Object>();
		int value = -1;
		if(jCheckBoxRestrictCorpusSize.isSelected())
		{
			value = (Integer) jSpinnerCorpusSizeMax.getValue();
		}
		settings.put(CorporaDefaultSettings.CORPUS_SIZE_LIMIT, value);
		return settings;
	}

	@Override
	public boolean haveChanged() {
		int value = -1;
		if(jCheckBoxRestrictCorpusSize.isSelected())
		{
			value = (Integer) jSpinnerCorpusSizeMax.getValue();
		}
		if(!initial_props.get(CorporaDefaultSettings.CORPUS_SIZE_LIMIT).equals(value))
		{
			return true;
		}
		return false;
	}

}
