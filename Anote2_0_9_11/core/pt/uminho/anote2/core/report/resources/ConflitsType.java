package pt.uminho.anote2.core.report.resources;

public enum ConflitsType {
	AlreadyHaveTerm{
		
		public String getDescription() {
			return "Term is already Present in Destiny Resource";
		};
	},
	TermInDiferentClasses
	{
		public String getDescription() {
			return "Term is already Present in Destiny Resource whit a diferent entity class";
		};
	},
	AlreadyHaveSynonyms
	{
		public String getDescription() {
			return "Synonym is already Present in Destiny Resource";
		};
	},
	AlteradyHaveExternalID{
		
		public String getDescription() {
			return "External ID is already Present in Destiny Resource";
		};
		
	};
	
	public String getDescription() {
		return this.getDescription();
	}
}


