package pt.uminho.anote2.aibench.utils.wizart;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import pt.uminho.anote2.aibench.utils.conf.GlobalOptions;
import pt.uminho.anote2.aibench.utils.gui.Help;
import es.uvigo.ei.aibench.workbench.InputGUI;
import es.uvigo.ei.aibench.workbench.Workbench;

public abstract class SimpleDialogView extends JDialog implements InputGUI{

	public List<Object> getParam() {
		return param;
	}

	public void setParam(List<Object> param) {
		this.param = param;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Object> param;

	private JButton jButtonCancel;
	private JButton jButtonHelp;
	private JButton jButtonDone;
	private JButton jButtonBack;
	private JButton jButtonNext;

	private JPanel jPaneButtons;
	
	public SimpleDialogView(List<Object> param){
		super(Workbench.getInstance().getMainFrame());
		this.param=param;	
		addDialogListener();
	}
	
	private void addDialogListener(){
		this.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				finish();
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
			
		});
		
	}

	public JButton getJButtonBack() {
		return jButtonBack;
	}

	/** Abstract Methods */
	public abstract void goNext();
	public abstract void goBack();
	public abstract void done();
	
	public JPanel getJPaneButtons() {
		if(jPaneButtons==null)
		{
			jPaneButtons = new JPanel();
			GridBagLayout jPaneButtonsLayout = new GridBagLayout();
			jPaneButtonsLayout.rowWeights = new double[] {0.1};
			jPaneButtonsLayout.rowHeights = new int[] {7};
			jPaneButtonsLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.0, 0.1, 0.1, 0.1, 0.1};
			jPaneButtonsLayout.columnWidths = new int[] {7, 7, 20, 7, 180, 7, 20, 7, 7};
			jPaneButtons.setLayout(jPaneButtonsLayout);
			{

				jPaneButtons.add(getJButtonNext(), new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
				jPaneButtons.add(getJButtonPrevious(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
				jPaneButtons.add(getJButtonDone(), new GridBagConstraints(7, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPaneButtons.add(getJButtonCancel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPaneButtons.add(getJButtonHelp(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return jPaneButtons;
	}
	
	private JButton getJButtonNext() {
		if(jButtonNext==null)
		{
			jButtonNext = new JButton();
			jButtonNext.setText("Next");
			jButtonNext.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Green_01.png")));
			jButtonNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					goNext();
				}
			});
		}
		return jButtonNext;
	}
	
	public void setEnableNextButton(boolean enable)
	{
		if(jButtonNext!=null)
		{
			jButtonNext.setEnabled(enable);
		}
	}
	
	private JButton getJButtonPrevious() {
		if(jButtonBack==null)
		{
			jButtonBack = new JButton();
			jButtonBack.setText("Back ");
			jButtonBack.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Green_04.png")));
			jButtonBack.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
			jButtonBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					goBack();
				}
			});
		}
		return jButtonBack;
	}
	
	public void setEnableBackButton(boolean enable)
	{
		if(jButtonBack!=null)
		{
			jButtonBack.setEnabled(enable);
		}
	}
	
	private JButton getJButtonDone() {
		if(jButtonDone==null)
		{
			jButtonDone = new JButton();
			jButtonDone.setText("Ok");
			jButtonDone.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/apply.png")));
			jButtonDone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					done();
				}
			});
		}
		return jButtonDone;
	}

	public void setEnableDoneButton(boolean enable)
	{
		if(jButtonDone!=null)
		{
			jButtonDone.setEnabled(enable);
		}
	}
	
	private JButton getJButtonCancel() {
		if(jButtonCancel==null)
		{
			jButtonCancel = new JButton();
			jButtonCancel.setText("Cancel");
			jButtonCancel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
			jButtonCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					finish();
				}
			});
		}
		return jButtonCancel;
	}
	
	public void finish() {
		this.setVisible(false);
		this.dispose();
		Workbench.getInstance().warn("@Note close");;
		System.exit(1);
	}
	
	private JButton getJButtonHelp() {
		if(jButtonHelp == null) {
			jButtonHelp = new JButton();
			jButtonHelp.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/messagebox_info.png")));
			jButtonHelp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Help.internetAcess(GlobalOptions.wikiGeneralLink+"Getting_Starting");
				}
			});
			jButtonHelp.setEnabled(true);
			jButtonHelp.setVisible(true);
		}
		return jButtonHelp;
	}
	
	public void setJButtonHelpEnable(boolean enable)
	{
		if(jButtonHelp!=null)
		{
			jButtonHelp.setEnabled(enable);
		}
	}
}
