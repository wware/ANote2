package pt.uminho.anote2.aibench.resources.gui.loader.lookuptable;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.anote2.aibench.resources.gui.loader.FileLoadAndDelimiterSelectionPane;
import pt.uminho.anote2.datastructures.exceptions.LoaderFileException;
import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.csv.ColumnNames;
import pt.uminho.anote2.datastructures.utils.generic.CSVFileConfigurations;
import pt.uminho.anote2.datastructures.utils.generic.ColumnDelemiterDefaultValue;
import pt.uminho.anote2.datastructures.utils.generic.ColumnParameters;
import pt.uminho.anote2.process.IE.io.export.DefaultDelimiterValue;
import pt.uminho.anote2.process.IE.io.export.Delimiter;
import pt.uminho.anote2.process.IE.io.export.TextDelimiter;
import pt.uminho.generic.csvloaders.tabTable.GenericSCSVColumnsNAmes;

public class LookupTableImportPane extends FileLoadAndDelimiterSelectionPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LookupTableImportPane() throws Exception {
		super(false,GenericSCSVColumnsNAmes.lookuptable);
	}

	@Override
	public CSVFileConfigurations getconfigurations() {
		Map<String, ColumnParameters> columnNameColumnParameters = new HashMap<String, ColumnParameters>();
		Map<String, Integer> headers = delemiterSelection.getGenericHeaders();	
		DefaultDelimiterValue defaultValueDelimiter = delemiterSelection.getDefaultValue();
		ColumnParameters value = new ColumnParameters(headers.get(ColumnNames.term),Delimiter.USER, defaultValueDelimiter);
		columnNameColumnParameters.put(ColumnNames.term, value);;
		ColumnParameters classe = new ColumnParameters(headers.get(ColumnNames.classe),Delimiter.USER, defaultValueDelimiter);
		columnNameColumnParameters.put(ColumnNames.classe, classe);;
		ColumnDelemiterDefaultValue columsDelemiterDefaultValue = new ColumnDelemiterDefaultValue(columnNameColumnParameters);
		CSVFileConfigurations csv = new CSVFileConfigurations(delemiterSelection.getGeneralDelimiter(),
				delemiterSelection.getTextDelimiter(),
				delemiterSelection.getDefaultValue(),
				columsDelemiterDefaultValue,
				delemiterSelection.getHasHeaders());
		return csv;
	}

	@Override
	public void validateSettings() throws LoaderFileException {
		if(getFile().isEmpty())
		{
			throw new LoaderFileException("Please select file...");
		}
		Map<String, Integer> headers = delemiterSelection.getGenericHeaders();		
		if(!headers.containsKey(ColumnNames.term))
		{
			throw new LoaderFileException("Please select Term Column...");
		}
		if(!headers.containsKey(ColumnNames.classe))
		{
			throw new LoaderFileException("Please select Class Column...");
		}
		Delimiter generalDelimiter = delemiterSelection.getGeneralDelimiter();
		if(generalDelimiter.equals(Delimiter.USER) && generalDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define General Delimiter...");
		}
		TextDelimiter textDelimiter = delemiterSelection.getTextDelimiter();	
		if(generalDelimiter.getValue().equals(textDelimiter.getValue()))
		{
			throw new LoaderFileException("General Delimiter and Text Delimiter can not be the same...");
		}
		if(textDelimiter.equals(TextDelimiter.USER) && textDelimiter.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define Text Delimiter...");
		}
		DefaultDelimiterValue defaultValue = delemiterSelection.getDefaultValue();
		if(defaultValue.equals(DefaultDelimiterValue.USER) && defaultValue.getValue().isEmpty())
		{
			throw new LoaderFileException("Please define Default Delimiter...");
		}
	}

}
