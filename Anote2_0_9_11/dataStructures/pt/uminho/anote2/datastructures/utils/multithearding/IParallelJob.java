package pt.uminho.anote2.datastructures.utils.multithearding;

public interface IParallelJob<T> extends Runnable{
	
	public T getResultJob();
	public void kill();

}
