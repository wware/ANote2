package pt.uminho.generic.csvloaders.tabTable;

import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.datastructures.resources.Dictionary.Loaders.csv.ColumnNames;

public class GenericSCSVColumnsNAmes {

	public static final Set<String> All = getColumnNames();
	
	public static final Set<String> lexicalWords = getLexicalWordsColumns();

	public static final Set<String> lookuptable = getLookupTbale();

	private static Set<String> getColumnNames() {
		Set<String> clolumnNames = new HashSet<String>();
		clolumnNames.add(ColumnNames.term);
		clolumnNames.add(ColumnNames.classe);
		clolumnNames.add(ColumnNames.synonyms);
		clolumnNames.add(ColumnNames.externalID);
		clolumnNames.add(ColumnNames.without);
		return clolumnNames;
	}

	private static Set<String> getLookupTbale() {
		Set<String> clolumnNames = new HashSet<String>();
		clolumnNames.add(ColumnNames.term);
		clolumnNames.add(ColumnNames.classe);
		clolumnNames.add(ColumnNames.without);
		return clolumnNames;
	}

	private static Set<String> getLexicalWordsColumns() {
		Set<String> clolumnNames = new HashSet<String>();
		clolumnNames.add(ColumnNames.term);
		clolumnNames.add(ColumnNames.without);
		return clolumnNames;
	}
	
	

}
