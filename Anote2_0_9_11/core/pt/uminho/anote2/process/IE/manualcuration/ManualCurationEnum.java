package pt.uminho.anote2.process.IE.manualcuration;

public enum ManualCurationEnum {
	NER
	{
		public String getProcessName()
		{
			return "Manual Curation";
		}
	},
	RE{
		public String getProcessName()
		{
			return "Manual Curation RE";
		}
	};
	
	
	public String getProcessName()
	{
		return this.getProcessName();
	}

}
