package pt.uminho.anote2.aibench.utils.operations;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class TimerJDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1;
	private JLabel jLabel1;

	public TimerJDialog()
	{
		super(Workbench.getInstance().getMainFrame());
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				jPanel1 = new JPanel();
				jPanel1.setBackground(Color.PINK);
				GridBagLayout jPanel1Layout = new GridBagLayout();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanel1Layout.rowHeights = new int[] {7, 7, 7, 7};
				jPanel1Layout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanel1Layout.columnWidths = new int[] {7, 7, 7, 7};
				jPanel1.setLayout(jPanel1Layout);
				{
					
					jLabel1 = new JLabel();
					ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("icons/longtask.gif")); 
					jLabel1.setIcon(image);
					jPanel1.add(jLabel1, new GridBagConstraints(1, 1, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				
				}

			}
			{
				this.setVisible(true);
				this.setTitle("Please Wait...");
				this.setSize(50,10);
				Utilities.centerOnOwner(this);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		this.setVisible(false);
		this.dispose();
		
	}

}
