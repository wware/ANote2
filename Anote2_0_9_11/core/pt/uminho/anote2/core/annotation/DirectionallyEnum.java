package pt.uminho.anote2.core.annotation;

public enum DirectionallyEnum {
	LeftToRight
	{
		
		public int databaseValue()
		{
			return 0;
		}
		
		public String toString()
		{
			return "LeftToRight";
		}
		
	},
	RightToLeft
	{
		
		public int databaseValue()
		{
			return 1;
		}
		
		public String toString()
		{
			return "RightToLeft";
		}
	},
	Both
	{
		public int databaseValue()
		{
			return 2;
		}
		
		public String toString()
		{
			return "Both";
		}
	},
	Unknown{
		
		public int databaseValue()
		{
			return 3;
		}
		
		public String toString()
		{
			return "Unknown";
		}
	};
	
	public String toString()
	{
		return this.toString();
	}
	
	public int databaseValue()
	{
		return this.databaseValue();
	}
	
	public static DirectionallyEnum covertIntToDirectionallyEnum(int number)
	{
		switch (number) {
		case 0:
			return DirectionallyEnum.LeftToRight;
		case 1:
			return DirectionallyEnum.RightToLeft;
		case 3:
			return DirectionallyEnum.Both;
		default:
			return  DirectionallyEnum.Unknown;
		}
	}
	
}
