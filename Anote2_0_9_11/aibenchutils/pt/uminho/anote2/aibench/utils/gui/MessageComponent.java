package pt.uminho.anote2.aibench.utils.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;


public class MessageComponent extends JPanel{

	private static final long serialVersionUID = -671920653856720139L;
	
	private JTextArea messageTextArea;
	private JPanel msgPanel;
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
				this.setPreferredSize(new java.awt.Dimension(300, 50));
				thisLayout.rowWeights = new double[] {0.05};
				thisLayout.rowHeights = new int[] {7};
				thisLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
				thisLayout.columnWidths = new int[] {7, 7, 7};
				this.setLayout(thisLayout);
				{
					iconLabel = new JLabel();
					this.add(iconLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 15, 0, 5), 0, 0));
					iconLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/icon_anote_26.png")));
				}
				{
					msgPanel = new JPanel();
					msgPanel.setBackground(Color.WHITE);
					GridBagLayout msgPanelLayout = new GridBagLayout();
					this.add(msgPanel, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
					msgPanelLayout.rowWeights = new double[] {0.095};
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
						messageTextArea.setBackground(Color.WHITE);
					}
				}
				{
					this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, new java.awt.Color(0,0,0), new java.awt.Color(0,255,0), new java.awt.Color(0,255,0), new java.awt.Color(0,255,0)));
					this.setOpaque(false);
					this.setBackground(Color.white);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
