package pt.uminho.anote2.aibench.loadsave.gui;

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

import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class LoadSessionGUI extends DialogGenericViewInputGUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jUpperPanel;
	private JFileChooser filePath;
	private JTextField jTextFieldTextChooser;
	private JButton jButtonFileChooser;
	
	
	public LoadSessionGUI()
	{
		initGUI();
		this.setTitle("Load Session");
		this.setModal(true);
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
						if(!save_path.isDirectory())
						{
							jTextFieldTextChooser.setText(save_path.getAbsolutePath());
						}
						else
						{
							Workbench.getInstance().warn("Please select a Load Session File");
							jTextFieldTextChooser.setText("");
						}
					}
				}

	
			});
		}
		return jButtonFileChooser;
	}

	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		this.paramsRec = arg0;
		this.setSize(GlobalOptions.smallWidth,GlobalOptions.smallHeight);
		Utilities.centerOnOwner(this);
		this.setVisible(true);
	}

	
	@Override
	protected void okButtonAction() {
		if(jTextFieldTextChooser.getText().equals(""))
		{
			Workbench.getInstance().warn("Please select a Load Session File");
		}
		else
		{
			this.paramsRec.paramsIntroduced(new ParamSpec[]{
					new ParamSpec("Load File", File.class,new File(jTextFieldTextChooser.getText()), null)
			});
		}
	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Load_Session";
	}

}
