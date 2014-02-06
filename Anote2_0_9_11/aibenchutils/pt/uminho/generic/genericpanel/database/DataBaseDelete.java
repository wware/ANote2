package pt.uminho.generic.genericpanel.database;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextFont;



public abstract class DataBaseDelete extends DialogGenericView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelInformation;
	private JTextPane jTextPaneInfo;

	public DataBaseDelete(String info)
	{
		super();
		initGUI();
		setInformation(info);
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.025, 0.075, 0.0};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			getContentPane().setLayout(thisLayout);
			{
				getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jPanelInformation = new JPanel();
				jPanelInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Warning", TitledBorder.LEADING, TitledBorder.TOP));
				GridBagLayout jPanelInformationLayout = new GridBagLayout();
				getContentPane().add(jPanelInformation, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelInformationLayout.rowWeights = new double[] {0.1};
				jPanelInformationLayout.rowHeights = new int[] {7};
				jPanelInformationLayout.columnWeights = new double[] {0.1};
				jPanelInformationLayout.columnWidths = new int[] {7};
				jPanelInformation.setLayout(jPanelInformationLayout);
				{
					jTextPaneInfo = new JTextPane();
					jTextPaneInfo.setOpaque(false);
					jTextPaneInfo.setEditable(false);
					Font font = GlobalTextFont.largeFontItalic;
					jTextPaneInfo.setFont(font);
					jPanelInformation.add(jTextPaneInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));		
				}
			}
			{
				getContentPane().add(getDetailPanel(), new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
	}

	public void setInformation(String text)
	{
		this.jTextPaneInfo.setText(text);
	}
	
	public abstract JPanel getDetailPanel();
	

}
