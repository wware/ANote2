package pt.uminho.anote2.core.annotation;


public enum PolarityEnum{
	Negative
	{
		public int databaseValue()
		{
			return 0;
		}
		
		public String toString()
		{
			return "Negative (-)";
		}
	}
	,
	Conditional
	{
		public int databaseValue()
		{
			return 1;
		}
		
		public String toString()
		{
			return "Conditional (?)";
		}
	},
	Positive
	{
		public int databaseValue()
		{
			return 2;
		}
		
		public String toString()
		{
			return "Positive (+)";
		}
	},
	Unknown
	{
		public int databaseValue()
		{
			return 3;
		}
		
		public String toString()
		{
			return "Unknown";
		}
	};
	
	public static PolarityEnum covertIntToPolarityEnum(int number) {
		switch (number) {
		case 2:
			return PolarityEnum.Positive;
		case 0:
			return PolarityEnum.Negative;
		case 1:
			return PolarityEnum.Conditional;
		default:
			return  PolarityEnum.Unknown;
		}
	}

	public int databaseValue() {
		return this.databaseValue();
	}
}
