package pt.uminho.anote2.aibench.corpus.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.corpus.datatypes.RESchema;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.process.IE.IRESchema;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class REAdvanceExportCSVGUI extends DialogGenericView{
	
	
	private static final long serialVersionUID = 1L;
	private JPanel jUpperPanel;
	private JFileChooser filePath;
	private JTextField jTextFieldTextChooser;
	private JButton jButtonFileChooser;
	private IRESchema process;
	
	public REAdvanceExportCSVGUI(RESchema process)
	{
		this.process = process;
		initGUI();
		this.setTitle("Export RE Schema to CSV File");
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		GridBagLayout panelLayout = new GridBagLayout();
		panelLayout.rowWeights = new double[] {0.1,0.0};
		panelLayout.rowHeights = new int[] {7, 7};
		panelLayout.columnWeights = new double[] {0.1};
		panelLayout.columnWidths = new int[] {7};
		this.setLayout(panelLayout);
		jUpperPanel = new JPanel();
		filePath = new JFileChooser(); 	
		filePath.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jUpperPanel.setBorder(BorderFactory.createTitledBorder("Select File"));
		GridBagLayout jUpperPanelLayout = new GridBagLayout();
		jUpperPanelLayout.rowWeights = new double[] {0.1, 0.05, 0.1};
		jUpperPanelLayout.rowHeights = new int[] {7, 7, 7};
		jUpperPanelLayout.columnWeights = new double[] {0.0, 0.1, 0.0, 0.0};
		jUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
		jUpperPanel.setLayout(jUpperPanelLayout);
		{
			jTextFieldTextChooser = new JTextField();
			jTextFieldTextChooser.setEditable(false);
			jUpperPanel.add(jTextFieldTextChooser, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		}
		{
			jUpperPanel.add(getJButtonFileChooser(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
		}
		this.add(jUpperPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getButtonsPanel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));		
	}
	
	private JButton getJButtonFileChooser() {
		if(jButtonFileChooser == null) {
			jButtonFileChooser = new JButton();
			jButtonFileChooser.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Folder26.png")));
			jButtonFileChooser.addActionListener(new ActionListener(){

				@SuppressWarnings("static-access")
				public void actionPerformed(ActionEvent arg0) {

					int rel = filePath.showOpenDialog(new JFrame());
					if(rel == filePath.CANCEL_OPTION)
					{

					}
					else
					{
						File save_path = filePath.getSelectedFile();
						if(save_path.isDirectory())
						{
							jTextFieldTextChooser.setText(save_path.getAbsolutePath()+"/"+"re_"+process.getID()+".tsv");
						}
						else
						{
							jTextFieldTextChooser.setText(save_path.getAbsolutePath());
						}
					}
				}
			});
		}
		return jButtonFileChooser;
	}


	@Override
	protected void okButtonAction() {
		if(jTextFieldTextChooser.getText().equals(""))
		{
			Workbench.getInstance().warn("You must select a file or directory");
		}
		else 
		{
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("process", IRESchema.class,process, null),
					new ParamSpec("file", File.class,new File(jTextFieldTextChooser.getText()), null)
			};
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.exportretocsv")){			
					Workbench.getInstance().executeOperation(def, paramsSpec);
					finish();
					return;
				}
			}
		}		
	}

	@Override
	protected String getHelpLink() {
		return null;
	}

}
