package pt.uminho.anote2.process.IE.network;

public enum XGMMLPolygnos {
	ROUND_RECTANGLE,
	DIAMOND,
	OCTAGON,
	ELLIPSE,
	RECTANGLE,
	PARALLELOGRAM,
	HEXAGON,
	TRIANGLE,
	V;
	
	public static XGMMLPolygnos geDefault()
	{
		return XGMMLPolygnos.ROUND_RECTANGLE;
	}
}

