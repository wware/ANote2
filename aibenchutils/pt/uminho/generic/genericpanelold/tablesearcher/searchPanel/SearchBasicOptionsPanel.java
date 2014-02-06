package pt.uminho.generic.genericpanelold.tablesearcher.searchPanel;

import java.awt.GridLayout;

import javax.swing.JCheckBox;


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
public class SearchBasicOptionsPanel extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	protected JCheckBox caseSensitiveCheckBox;
	protected JCheckBox matchWholeWordCheckBox;
	
	public SearchBasicOptionsPanel() {
		initGUI();
	}
	
	protected void initGUI() {
		caseSensitiveCheckBox = new JCheckBox("Case sensitive");
		matchWholeWordCheckBox = new JCheckBox("Whole word");
		try {
			GridLayout thisLayout = new GridLayout(0, 1);
			thisLayout.setColumns(1);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			this.setLayout(thisLayout);
			add(caseSensitiveCheckBox);
			add(matchWholeWordCheckBox);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isCaseSensitiveSelected(){
		return caseSensitiveCheckBox.isSelected();
	}
	
	public boolean isMatchWholeWordSelected(){
		return matchWholeWordCheckBox.isSelected();
	}

}
