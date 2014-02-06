package pt.uminho.gate;

import gate.Gate;
import gate.util.GateException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColors;

public class GateInit {
	
	private boolean init;
	private Set<String> plugnsloaded;
	
	private static GateInit _instance;
	
	protected GateInit()
	{
		synchronized (AnnotationColors.class){
		if (_instance == null) {
			plugnsloaded = new HashSet<String>();
			init = false;
			_instance = this;
		} else {
			throw new RuntimeException("This is a singleton.");
		}
		}
	}
	
	
	/**
	 * Creates the singleton instance.
	 */
	private static synchronized void createInstance() {

		if (_instance == null) {
			_instance = new GateInit();
		}	
	}

	/**
	 * Gives access to the Workbench instance
	 * @return
	 */
	public static GateInit getInstance() {
		if (_instance == null) {
			GateInit.createInstance();
		}
		return _instance;
	}
	
	public void init() throws GateException
	{
		if(!getInstance().init)
		{
			Gate.init();
			getInstance().init = true;
		}
	}
	
	public void creoleRegister(String creole) throws MalformedURLException, GateException
	{
		if(!getInstance().plugnsloaded.contains(creole))
		{
			Gate.getCreoleRegister().registerDirectories(new URL(Gate.getGateHome().toURI().toURL(),creole));
			getInstance().plugnsloaded.add(creole);
		}
	}
	
}
