package pt.uminho.anote2.aibench.resources.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import pt.uminho.anote2.aibench.resources.datatypes.LexicalWordsAibench;
import pt.uminho.anote2.aibench.resources.gui.loader.lexicalwords.LexicalWordsImportPane;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.session.SessionPropertykeys;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.datastructures.exceptions.LoaderFileException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class ImportLexicalWordTermsFromCSVFileGUI extends DialogGenericViewInputGUI implements InputGUI{
	
	private static final long serialVersionUID = 1L;
	private LexicalWordsImportPane selectDelimiterPane;

	public ImportLexicalWordTermsFromCSVFileGUI()
	{
		super("Import Terms From CVS File");
		initGUI();
		this.setModal(true);
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);

	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		{
			getContentPane().add(getFileLoadAndDelimiterSelectionPane(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}



	private Component getFileLoadAndDelimiterSelectionPane() {
		if(selectDelimiterPane == null)
		{
			try {
				selectDelimiterPane = new LexicalWordsImportPane();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return selectDelimiterPane;
	}

	@Override
	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}


	protected void okButtonAction() {
		try {
			if(selectDelimiterPane.getFile()!=null && !selectDelimiterPane.getFile().isEmpty())
			{
				File file = new File(selectDelimiterPane.getFile());
				selectDelimiterPane.validateSettings();
				Object obj = HelpAibench.getSelectedItem(LexicalWordsAibench.class);
				LexicalWordsAibench lw = (LexicalWordsAibench) obj;
				CSVFileConfigurations conf = selectDelimiterPane.getconfigurations();
				paramsRec.paramsIntroduced(new ParamSpec[]{
						new ParamSpec("lexicalwords",LexicalWordsAibench.class,lw ,null),
						new ParamSpec("parameters",CSVFileConfigurations.class,conf ,null),
						new ParamSpec("file",File.class,file,null)
				});
				finish();	
				SessionSettings.getSessionSettings().setSessionProperty(SessionPropertykeys.generalDelimiter, conf.getGeneralDelimiter().name());
				SessionSettings.getSessionSettings().setSessionProperty(SessionPropertykeys.generaltextDelimiter, conf.getTextDelimiter().name());
				SessionSettings.getSessionSettings().setSessionProperty(SessionPropertykeys.generalDefaultValueDelimiter, conf.getDefaultValue().name());
			}
			else
			{
				throw new LoaderFileException("Please select file...");
			}
		} catch (LoaderFileException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
	}



	

	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"LexicalWords_Import_Element_From_CSV_FIle";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(LexicalWordsAibench.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Lexical Words selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.generalWidth,GlobalOptions.generalHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);	
		}
	}
	

}
