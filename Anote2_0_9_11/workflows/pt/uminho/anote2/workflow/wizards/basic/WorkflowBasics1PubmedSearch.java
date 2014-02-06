package pt.uminho.anote2.workflow.wizards.basic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.publicationmanager.gui.panes.PubmedSearchSimpleAndAdvanced;
import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.core.corpora.ICorpusCreateConfiguration;
import pt.uminho.anote2.core.document.CorpusTextType;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.corpora.CorpusCreateConfiguration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextFont;
import pt.uminho.anote2.datastructures.utils.conf.propertiesmanager.PropertiesManager;
import pt.uminho.anote2.process.IR.IIRSearchConfiguration;
import pt.uminho.anote2.workflow.settings.basic.WorkflowBasicsDefaulSettings;
import pt.uminho.anote2.workflow.text.WorkflowBasicsTextFont;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class WorkflowBasics1PubmedSearch extends WizardStandard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PubmedSearchSimpleAndAdvanced panel;
	private JTextField jTextFieldInfo;
	private JPanel mainPanel;

	public WorkflowBasics1PubmedSearch() {
		super(new ArrayList<Object>());
		initGUI();
		this.setTitle("Basic Workflow - Step 1");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public WorkflowBasics1PubmedSearch(List<Object> objects) {
		super(new ArrayList<Object>());
		initGUI();
		fillSettings(objects.get(0));
		this.setTitle("Basic Workflow - Step 1");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillSettings(Object object) {
		IIRSearchConfiguration pubmedSearchConfigutration = (IIRSearchConfiguration) object;
		panel.updateInfo(pubmedSearchConfigutration);
	}

	public void initGUI()
	{
		setEnableDoneButton(false);
		setEnableBackButton(false);
		setEnableNextButton(true);
	}
	
	@Override
	public JComponent getMainComponent() {
		if(mainPanel ==null)
			mainPanel = new JPanel();
		GridBagLayout mainPanelLayout = new GridBagLayout();
			mainPanelLayout.rowWeights = new double[] {0.0, 0.1};
			mainPanelLayout.rowHeights = new int[] {7, 7};
			mainPanelLayout.columnWeights = new double[] {0.1};
			mainPanelLayout.columnWidths = new int[] {7};
			mainPanel.setLayout(mainPanelLayout);
			panel = new PubmedSearchSimpleAndAdvanced();
			mainPanel.add(panel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));	
			mainPanel.add(getJTextFieldInfo(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		return mainPanel;
	}

	@Override
	public void goNext() {
		if(panel.validateOptions())
		{
			IIRSearchConfiguration pubmedSearchConfigutration = panel.getConfiguration();
			String corpusName = PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.CORPUS_NAME).toString();
			if(corpusName == null || corpusName.length()==0)
			{
				if(!pubmedSearchConfigutration.getKeywords().equals(""))
					corpusName = "KEYWORDS :\""+pubmedSearchConfigutration.getKeywords() + "\" ";
				if(!pubmedSearchConfigutration.getOrganism().equals(""))
					corpusName = corpusName + "ORGANISM :\""+pubmedSearchConfigutration.getOrganism() + "\"";
				else
					corpusName = GregorianCalendar.getInstance().getTime().toString();
			}
			CorpusTextType corpustext =  (CorpusTextType) PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.CORPUS_TYPE);
			if(corpustext.equals(CorpusTextType.Hybrid))
				corpustext = CorpusTextType.Abstract;
			boolean retrievalDocs = Boolean.parseBoolean(PropertiesManager.getPManager().getProperty(WorkflowBasicsDefaulSettings.CORPUS_RETRIEVAL_PDF).toString());
			ICorpusCreateConfiguration corpusCreation = new CorpusCreateConfiguration(corpusName , new HashSet<IPublication>(), corpustext, retrievalDocs);
			List<Object> list = new ArrayList<Object>();
			list.add(pubmedSearchConfigutration);
			list.add(corpusCreation);
			closeView();
			new WorkflowBasics2DictionarySelection(list);
		}
	}

	@Override
	public void goBack() {}

	@Override
	public void done() {}

	@Override
	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Workflow_:_Information_Retrieval_and_Extraction_Basic#Step_1:_Pubmed_Search";
	}
	
	private JTextField getJTextFieldInfo() {
		if(jTextFieldInfo == null) {
			jTextFieldInfo = new JTextField();
			jTextFieldInfo.setText(WorkflowBasicsTextFont.WorkflowBasicStep1);
			jTextFieldInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "", TitledBorder.LEADING, TitledBorder.TOP));
			jTextFieldInfo.setEditable(false);
			jTextFieldInfo.setFont(GlobalTextFont.largeFontItalic);
		}
		return jTextFieldInfo;
	}

}
