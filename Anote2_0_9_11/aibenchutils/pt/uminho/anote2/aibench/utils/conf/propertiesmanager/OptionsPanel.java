package pt.uminho.anote2.aibench.utils.conf.propertiesmanager;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


public class OptionsPanel extends javax.swing.JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree tree;
	private JButton importButton;
	private JButton exportButton;
	private JPanel buttonsPanel;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Set<String> yo = new HashSet<String>();
		yo.add("YO.YE.WAWA");
		yo.add("YO.YE");
		yo.add("YA.WQWQWWQ.SUP");
		OptionsPanel panel = new OptionsPanel(yo);
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		panel.getSelected();
	}

	private Set<String> options;
	private boolean usesImportExport = true;

	public OptionsPanel(Set<String> opt) {
		super();
		this.options = opt;
		initGUI();
	}
	
	public OptionsPanel(Set<String> opt, boolean exportImport) {
		super();
		this.options = opt;
		this.usesImportExport  = exportImport;
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.0};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.0};
			thisLayout.columnWidths = new int[] {7};
			this.setLayout(thisLayout);

			DefaultMutableTreeNode top =
			        new DefaultMutableTreeNode("Options");
			
			tree = createNodes(top);
			
//			tree.setRootVisible(false);
			ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/anote_icon_16.png"));
			DefaultTreeCellRenderer renderer = new IconNodeRenderer(icon);
//		    renderer.setLeafIcon(icon);
//		    renderer.setIcon(icon);
			tree.setCellRenderer(renderer);
			
			JScrollPane treeView = new JScrollPane(tree);
			
			this.add(treeView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			{
				if(usesImportExport){
					buttonsPanel = new JPanel();
					GridBagLayout buttonsPanelLayout = new GridBagLayout();
					this.add(buttonsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					buttonsPanelLayout.rowWeights = new double[] {0.1,0.1};
					buttonsPanelLayout.rowHeights = new int[] {7,7};
					buttonsPanelLayout.columnWeights = new double[] {0.1};
					buttonsPanelLayout.columnWidths = new int[] {7};
					buttonsPanel.setLayout(buttonsPanelLayout);
					{
						importButton = new JButton();
						buttonsPanel.add(importButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
						importButton.setText("Import");
						importButton.setActionCommand("import");
					}
					{
						exportButton = new JButton();
						buttonsPanel.add(exportButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));
						exportButton.setText("Export");
						exportButton.setActionCommand("export");
					}
				}
			}
//			for (int i = 0; i < tree.getRowCount(); i++) {
//		         tree.expandRow(i);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addButtonsActionListener(ActionListener al){
		if(usesImportExport){
			importButton.addActionListener(al);
			exportButton.addActionListener(al);
		}
	}

	public void addNodeTreeSelectionListener(TreeSelectionListener tl){
		tree.addTreeSelectionListener(tl);
	}
	
	private JTree createNodes(DefaultMutableTreeNode top) {
		// TODO Auto-generated method stub
		
		for(String s : options){
			
			createTreeBranch(top, s);

		}
		return new JTree(top);
	}

	private void createTreeBranch(DefaultMutableTreeNode root, String path){
		
		String[] allnodes = path.split("\\.");
		
		DefaultMutableTreeNode aux = root;
		
		for(int j = 0; j < allnodes.length; j++){
						
			boolean existsNode = false;
			int indexNode = -1;
			
			for(int i = 0; i < aux.getChildCount(); i++){
				if(aux.getChildAt(i).toString().equals(allnodes[j])){
					existsNode = true;
					indexNode = i;
				}
			}

			if(!existsNode){
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(allnodes[j]);
				aux.add(newnode);
				aux = newnode;
			}
			else{
					aux = (DefaultMutableTreeNode) aux.getChildAt(indexNode);
			}
		}
	}
	
	public String getSelected(){
			return tree.getSelectionModel().toString();
	}
	
	public Object getSelectedPath(){
		return tree.getSelectionPath().getPath();
	}
	
	private class IconNodeRenderer extends DefaultTreeCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ImageIcon icon;
		
		public IconNodeRenderer(ImageIcon icon) {
			this.icon = icon;
		}
		  public Component getTreeCellRendererComponent(JTree tree, Object value,
		      boolean sel, boolean expanded, boolean leaf, int row,
		      boolean hasFocus) {

		    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
		        row, hasFocus);

		    if(row!=0)
		    	setIcon(this.icon);
		    else{
//		    	setBounds(0, 0, 0, 0);
//		    	setAlignmentY(-5);
		    	setIcon(null);
		    	setVisible(false);
		    	
		    }

		    return this;
		  }
		}
}
