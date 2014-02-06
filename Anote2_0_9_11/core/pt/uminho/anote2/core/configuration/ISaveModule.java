package pt.uminho.anote2.core.configuration;

import java.io.File;

public interface ISaveModule {
	public boolean saveFile(File file) throws Exception;
	public void loadFile(File file) throws Exception;
	public String getName();
}
