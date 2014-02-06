package pt.uminho.anote2.aibench.utils.gui;

import pt.uminho.anote2.aibench.utils.operations.TimerJDialog;



public class ProcessRunThread extends Thread{

	public ProcessRunThread(){
		this.start();
	}
	
	public void run() {
		
		TimerJDialog time = new TimerJDialog();
		
		time.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		time.setVisible(false);
	}
	
	

}
