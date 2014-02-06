package pt.uminho.anote2.aibench.curator.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CorrectSelectionDialog extends DialogGenericView {

	private static final long serialVersionUID = 3961815075939544826L;
	private JTextField newTermTextArea;
	private JList tagList;
	private JScrollPane jScrollPane1;
	private JLabel labelCurrentTag;
	private JTextField currentTagTextArea;
	private JLabel labelNewTag;
	private JLabel labelNewText;

	public CorrectSelectionDialog(){
		super();
		initGUI();
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		this.setTitle("Correct Selected Text / Tag");
		Utilities.centerOnOwner(this);
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.0, 0.1, 0.0};
			thisLayout.rowHeights = new int[] {11, 27, 11, 136, 5, 20, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1, 0.01};
			thisLayout.columnWidths = new int[] {78, 202, 19, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				newTermTextArea = new JTextField();
				getContentPane().add(newTermTextArea, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				newTermTextArea.setPreferredSize(new java.awt.Dimension(137, 44));
				newTermTextArea.setBackground(Color.lightGray);
				newTermTextArea.setEditable(false);
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
				currentTagTextArea = new JTextField();
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
					ListModel tagListModel = new DefaultComboBoxModel();
					tagList = new JList();
					jScrollPane1.setViewportView(tagList);
					tagList.setBackground(Color.LIGHT_GRAY);
					tagList.setModel(tagListModel);
					tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
			}
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 6, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				changeOkButtonName("Apply");
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateClassModel()
	{
		ListModel tagListModel = new DefaultComboBoxModel(ClassProperties.getClassClassID().keySet().toArray());
		tagList.setModel(tagListModel);		
	}
	
	public String getSelectedClassText(){
		int newClassIndex = tagList.getSelectedIndex();
		if(newClassIndex<0)
			return null;
		return tagList.getSelectedValue().toString();
	}
	
	public JTextField getNewTermTextArea() {return newTermTextArea;}
	public JList getTagList() {return tagList;}
	public JTextField getCurrentTagTextArea(){return currentTagTextArea;}

	@Override
	protected void okButtonAction() {

	}

	@Override
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Curator_View#Edit_Entity";
	}
}
