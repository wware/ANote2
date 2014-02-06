package pt.uminho.anote2.aibench.utils.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;

import pt.uminho.anote2.aibench.utils.gui.panes.ExitMainPane;
import pt.uminho.anote2.aibench.utils.gui.panes.ExitSavePane;
import pt.uminho.anote2.core.configuration.ISaveModule;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


public class ExitGUI extends JDialog implements InputGUI{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExitMainPane mainPane;
	private ExitSavePane savePane;
	private ParamsReceiver paramsRec = null;

	
	public ExitGUI()
	{
		super(Workbench.getInstance().getMainFrame());
		initGUI();
		this.setModal(true);
		
	}
	
	
	
	private void initGUI() {
		savePane = new ExitSavePane();
		mainPane = new ExitMainPane();
		mainPane.getjButtonCancel().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finish();
			};
		});
		mainPane.getjButtonYes().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				savePane.setVisible(true);
				mainPane.setVisible(false);
			}
		});
		mainPane.getjButtonNo().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				exitWithoutSaving();
			}


		});

		savePane.getJButtonCancel().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finish();				
			}
		});
		savePane.getJButtonButtonSaveOk().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 exitSaving();				
			}
		});
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1};
		thisLayout.rowHeights = new int[] {7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		{
			getContentPane().add(savePane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			savePane.setVisible(false);
		}
		{
			getContentPane().add(mainPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}


	}
	
	private void exitWithoutSaving() {
		ArrayList<ISaveModule> projects2save = null;
		File file = null;
		this.paramsRec.paramsIntroduced(new ParamSpec[]{
				new ParamSpec("savemodule",ArrayList.class,projects2save,null),
				new ParamSpec("file",File.class,file,null),			
		});
	}
	
	private void exitSaving() {
		if(savePane.validateFields())
		{
			List<ISaveModule> projects2save = savePane.getProjectToSave();
			File file = savePane.getFileToSave();
			this.paramsRec.paramsIntroduced(new ParamSpec[]{
					new ParamSpec("savemodule",List.class,projects2save,null),
					new ParamSpec("file",File.class,file,null),			
			});
		}
	}



	@Override
	public void finish() {
		this.setModal(false);
		this.setVisible(false);
		this.dispose();		
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.paramsRec = arg0;
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setTitle("Exit/save Panel");

	}

	@Override
	public void onValidationError(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

}
