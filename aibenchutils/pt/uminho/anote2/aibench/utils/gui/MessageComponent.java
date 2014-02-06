package pt.uminho.anote2.aibench.utils.gui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MessageComponent extends JPanel{

	private static final long serialVersionUID = -671920653856720139L;
	
	private JTextArea messageTextArea;
	private JPanel msgPanel;
	private JLabel titleLabel;
	private JLabel iconLabel;

	public MessageComponent(String message){
		super();
		initGUI();
		this.messageTextArea.setText(message);
	}

	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				this.setPreferredSize(new java.awt.Dimension(348, 133));
				thisLayout.rowWeights = new double[] {0.0, 0.1};
				thisLayout.rowHeights = new int[] {7, 7};
				thisLayout.columnWeights = new double[] {0.0, 0.1};
				thisLayout.columnWidths = new int[] {7, 7};
				this.setLayout(thisLayout);
				{
					iconLabel = new JLabel();
					this.add(iconLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 15, 0, 5), 0, 0));
					iconLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/at24.png")));
				}
				{
					titleLabel = new JLabel();
					this.add(titleLabel, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					titleLabel.setText("Output");
					titleLabel.setFont(new java.awt.Font("Tahoma",1,14));
					titleLabel.setForeground(new java.awt.Color(255,0,128));
				}
				{
					msgPanel = new JPanel();
					GridBagLayout msgPanelLayout = new GridBagLayout();
					this.add(msgPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
					msgPanelLayout.rowWeights = new double[] {0.1};
					msgPanelLayout.rowHeights = new int[] {7};
					msgPanelLayout.columnWeights = new double[] {0.1};
					msgPanelLayout.columnWidths = new int[] {7};
					msgPanel.setLayout(msgPanelLayout);
					msgPanel.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
					{
						messageTextArea = new JTextArea();
						msgPanel.add(messageTextArea, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						messageTextArea.setBorder(null);
						messageTextArea.setForeground(new java.awt.Color(0,0,0));
						messageTextArea.setFont(new java.awt.Font("Tahoma",1,12));
						messageTextArea.setEditable(false);
						messageTextArea.setBackground(new java.awt.Color(239,239,239));
					}
				}
				{
					this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new java.awt.Color(0,0,0), new java.awt.Color(0,0,255), new java.awt.Color(0,0,255), new java.awt.Color(0,0,255)));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
