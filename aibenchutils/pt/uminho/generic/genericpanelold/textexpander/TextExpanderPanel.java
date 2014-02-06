package pt.uminho.generic.genericpanelold.textexpander;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;


public class TextExpanderPanel extends JPanel{

	
	private static final long serialVersionUID = 1L;
	private JScrollPane textScrollPane;
	private JTextArea textArea;
	private JToggleButton expanderButton;
	private JLabel textLabel;

	public TextExpanderPanel(){
		super();
		initGUI();
	}
	
	public TextExpanderPanel(String label, String button, String textArea){
		super();
		initGUI();
		this.textLabel.setText(label);
		this.expanderButton.setText(button);
		this.textArea.setText(textArea);
		this.textArea.setVisible(false);
		this.textArea.setEnabled(false);
		this.expanderButton.setSelected(false);
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7};
			this.setLayout(thisLayout);
			{
				textLabel = new JLabel();
				this.add(textLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				expanderButton = new JToggleButton();
				this.add(expanderButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				expanderButton.setText("show");
				expanderButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/bottom.png")));
				expanderButton.setHorizontalTextPosition(SwingConstants.LEADING);
				expanderButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						expanderButtonActionPerformed(evt);
					}
				});
			}
			{
				textScrollPane = new JScrollPane();
				this.add(textScrollPane, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					textArea = new JTextArea();
					textScrollPane.setViewportView(textArea);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void expanderButtonActionPerformed(ActionEvent evt) {
		if(expanderButton.isSelected()){
			expanderButton.setText("hide");
			textArea.setVisible(true);
			textArea.setEnabled(true);
		}else{
			expanderButton.setText("show");
			textArea.setVisible(false);
			textArea.setEnabled(false);
		}				
	}
	
	public static void main(String... args){
		JFrame frame = new JFrame("test");
		frame.setPreferredSize(new Dimension(500,300));
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		TextExpanderPanel texp = new TextExpanderPanel("Sou uma label","show","empty");
		frame.add(texp,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
