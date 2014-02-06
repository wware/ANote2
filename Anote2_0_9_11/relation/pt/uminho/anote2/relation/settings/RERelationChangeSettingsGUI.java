package pt.uminho.anote2.relation.settings;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Map;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.IPropertiesPanel;
import pt.uminho.anote2.relation.settings.pane.RERelationChangeSettingsPanel;


public class RERelationChangeSettingsGUI extends javax.swing.JPanel implements IPropertiesPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> initial_props;
	private Map<String, Object> defaultProps;

	private RERelationChangeSettingsPanel jPanelSettings;
	
	public RERelationChangeSettingsGUI(Map<String, Object> initial_props, Map<String, Object> defaultProps) throws DatabaseLoadDriverException, SQLException
	{
		super();

		if(initial_props.isEmpty()) 
			initial_props = defaultProps;
		this.initial_props = initial_props;
		this.defaultProps = defaultProps;
		if(Configuration.getDatabase()!=null)
		{
			initGUI();
		}
	}
	
	public RERelationChangeSettingsGUI()
	{

	}
	
	
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(708, 491));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);
			{
				jPanelSettings = new RERelationChangeSettingsPanel(initial_props, defaultProps);
				this.add(jPanelSettings, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}

	}


	@Override
	public Map<String, Object> getProperties() {
		return jPanelSettings.getProperties();
	}


	@Override
	public boolean haveChanged() {
		return jPanelSettings.haveChanged();
	}



}
