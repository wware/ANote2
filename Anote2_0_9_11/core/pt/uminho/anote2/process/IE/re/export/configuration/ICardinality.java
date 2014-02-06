package pt.uminho.anote2.process.IE.re.export.configuration;


public interface ICardinality extends Comparable<ICardinality>{
	public int getLeftEntities();
	public int getRightEntities();
}
