package pt.uminho.anote2.workflow.wizards.query.pane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.workflow.gui.wizard.panes.AREPaneWizard;
import pt.uminho.anote2.workflow.settings.query.WorkflowQueryDefaulSettings;

public class WorkflowQueryREPane extends AREPaneWizard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WorkflowQueryREPane() throws SQLException, DatabaseLoadDriverException {
		super(getDefaultSettingskey());
	}

	private static List<String> getDefaultSettingskey() {
		List<String> list = new ArrayList<String>();
		list.add(WorkflowQueryDefaulSettings.POSTAGGER);
		list.add(WorkflowQueryDefaulSettings.MODEL);
		list.add(WorkflowQueryDefaulSettings.VERB_FILTER);
		list.add(WorkflowQueryDefaulSettings.VERB_FILTER_LEXICAL_WORDS_ID);
		list.add(WorkflowQueryDefaulSettings.VERB_ADDITION);
		list.add(WorkflowQueryDefaulSettings.VERB_ADDITION_LEXICAL_WORDS_ID);
		list.add(WorkflowQueryDefaulSettings.BIOMEDICAL_VERB_MODEL);
		list.add(WorkflowQueryDefaulSettings.ADVANCED_ONLY_NEAREST_VERB_ENTITIES);
		list.add(WorkflowQueryDefaulSettings.ADVANCED_VERB_ENTITES_MAX_DISTANCE);
		list.add(WorkflowQueryDefaulSettings.ADVANCED_RELATIONS_TYPE);
		list.add(WorkflowQueryDefaulSettings.ADVANCED_ONLY_USE_ENTITY_TO_NEAREST_VERB);
		return list;
	}

	@Override
	protected void defaultSetting() {
//		jComboBoxREProcesses.setSelectedItem(PropertiesManager.getPManager().getProperty(WorkflowQueryDefaulSettings.RE));
//		changeMainPanel();
	}
	
	

}
