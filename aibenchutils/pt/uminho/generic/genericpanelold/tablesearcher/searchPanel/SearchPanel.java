package pt.uminho.generic.genericpanelold.tablesearcher.searchPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class SearchPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected JTextField searchTextField;
	private SearchBasicOptionsPanel searchBasicOptionsPanel;

	public SearchPanel(){
		initGUI();
	}
	
	protected void initGUI() {
		{
			GridBagLayout searchPanelLayout = new GridBagLayout();
			searchPanelLayout.rowWeights = new double[] {0.1};
			searchPanelLayout.rowHeights = new int[] {7};
			searchPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.0, 0.1};
			searchPanelLayout.columnWidths = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 27, 7};
			this.setLayout(searchPanelLayout);
			{
				JLabel searchLabel = new JLabel();
				add(searchLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				searchLabel.setText("search :");
				searchLabel.setFont(new java.awt.Font("Arial",3,12));
				searchLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
				searchLabel.setHorizontalAlignment(SwingConstants.CENTER);
				searchLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/find.png")));
			}
			{
				searchTextField = new JTextField();
				this.add(searchTextField, new GridBagConstraints(1, 0, 7, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				searchBasicOptionsPanel = new SearchBasicOptionsPanel();
				this.add(searchBasicOptionsPanel, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		
	}
	
	public void addSearchTextFieldKeyListener(KeyListener keyListener){
		searchTextField.addKeyListener(keyListener);
	}
	
	public String getSearchText(){
		return searchTextField.getText();
	}
	
	public boolean isSearchCaseSensitive(){
		return searchBasicOptionsPanel.isCaseSensitiveSelected();
	}
	
	public boolean isSearchWholeWord(){
		return searchBasicOptionsPanel.isMatchWholeWordSelected();
	}
}
