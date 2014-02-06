package pt.uminho.anote2.aibench.curator.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;




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
public class CorrectSelectionDialog extends JDialog {

	private static final long serialVersionUID = 3961815075939544826L;
	private JTextArea newTermTextArea;
	private JButton aplyButton;
	private JList tagList;
	private String[] tags;
	private JScrollPane jScrollPane1;
	private JLabel labelCurrentTag;
	private JTextArea currentTagTextArea;
	private JButton cancelButton;
	private JLabel labelNewTag;
	private JLabel labelNewText;

	public CorrectSelectionDialog(JFrame parent,String[] tags){
		super(parent);
		this.tags = tags;
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.0, 0.1, 0.1, 0.0, 0.0, 0.0};
			thisLayout.rowHeights = new int[] {11, 27, 11, 136, 5, 20, 7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1, 0.01};
			thisLayout.columnWidths = new int[] {78, 202, 19, 7, 7};
			getContentPane().setLayout(thisLayout);
			this.setTitle("Correct Selected Text / Tag");
			{
				newTermTextArea = new JTextArea();
				getContentPane().add(newTermTextArea, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				newTermTextArea.setPreferredSize(new java.awt.Dimension(137, 44));
				newTermTextArea.setBackground(Color.lightGray);
				newTermTextArea.setEditable(false);
			}
			{
				aplyButton = new JButton();
				getContentPane().add(aplyButton, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				aplyButton.setText(" Apply");
				aplyButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/apply.png")));
			}
			{
				labelNewText = new JLabel();
				getContentPane().add(labelNewText, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 3), 0, 0));
				labelNewText.setText("Term:");
			}
			{
				labelNewTag = new JLabel();
				getContentPane().add(labelNewTag, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 3), 0, 0));
				labelNewTag.setText("New Class:");
			}
			{
				cancelButton = new JButton();
				getContentPane().add(cancelButton, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				cancelButton.setText("Cancel");
				cancelButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/cancel.png")));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						cancel();
					}
				});	
			}
			{
				currentTagTextArea = new JTextArea();
				getContentPane().add(currentTagTextArea, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				currentTagTextArea.setBackground(Color.LIGHT_GRAY);
				currentTagTextArea.setEditable(false);
			}
			{
				labelCurrentTag = new JLabel();
				getContentPane().add(labelCurrentTag, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				labelCurrentTag.setText("Current Class:");
			}
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1, new GridBagConstraints(1, 3, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					ListModel tagListModel = new DefaultComboBoxModel(tags);
					tagList = new JList();
					jScrollPane1.setViewportView(tagList);
					tagList.setBackground(Color.LIGHT_GRAY);
					tagList.setModel(tagListModel);
					tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
			}
			{
				this.setSize(417, 253);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancel(){
		this.setVisible(false);
	}
	
	public String getSelectedClass(){
		int newClassIndex = tagList.getSelectedIndex();
		if(newClassIndex<0)
			return null;
		return tags[newClassIndex];
	}
	
	public JTextArea getNewTermTextArea() {return newTermTextArea;}
	public JList getTagList() {return tagList;}
	public JButton getAplyButton() {return aplyButton;}
	public JButton getCancelButton() {return cancelButton;}
	public JTextArea getCurrentTagTextArea(){return currentTagTextArea;}
}
