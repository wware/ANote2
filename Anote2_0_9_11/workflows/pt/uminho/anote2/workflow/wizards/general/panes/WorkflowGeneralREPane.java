package pt.uminho.anote2.workflow.wizards.general.panes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.workflow.gui.wizard.panes.AREPaneWizard;
import pt.uminho.anote2.workflow.settings.general.WorkflowGeneralDefaulSettings;

public class WorkflowGeneralREPane extends AREPaneWizard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowGeneralREPane() throws SQLException, DatabaseLoadDriverException {
		super(getDefaultSettingskey());
	}

	private static List<String> getDefaultSettingskey() {
		List<String> list = new ArrayList<String>();
		list.add(WorkflowGeneralDefaulSettings.POSTAGGER);
		list.add(WorkflowGeneralDefaulSettings.MODEL);
		list.add(WorkflowGeneralDefaulSettings.VERB_FILTER);
		list.add(WorkflowGeneralDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID);
		list.add(WorkflowGeneralDefaulSettings.VERB_ADDITION);
		list.add(WorkflowGeneralDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID);
		list.add(WorkflowGeneralDefaulSettings.BIOMEDICAL_VERB_MODEL);
		list.add(WorkflowGeneralDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES);
		list.add(WorkflowGeneralDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE);
		list.add(WorkflowGeneralDefaulSettings.ADVANCED_RELATIONS_TYPE);
		list.add(WorkflowGeneralDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB);
		return list;
	}

	@Override
	protected void defaultSetting() {
//		jComboBoxREProcesses.setSelectedItem(PropertiesManager.getPManager().getProperty(WorkflowGeneralDefaulSettings.RE));
//		changeMainPanel();
	}
	
	

}
