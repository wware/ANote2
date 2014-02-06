package pt.uminho.generic.genericpanel.view;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;



public class JPanelWait extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2056087932023389222L;
	
	private String info = GlobalTextInfo.pleasewait;
	
	public JPanelWait()
	{
		initGUI();
		this.setVisible(false);
		this.setEnabled(false);
	}
	
	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		this.setSize(800, 600);
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7};
		this.setLayout(thisLayout);
		{
			JLabel working = new JLabel(info);
			this.add(working, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			GridLayout workingLayout = new GridLayout(1, 1);
			workingLayout.setHgap(5);
			workingLayout.setVgap(5);
			workingLayout.setColumns(1);
			working.setLayout(workingLayout);
			working.setOpaque(true);
			working.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/longtask.gif")));
		}
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

}
