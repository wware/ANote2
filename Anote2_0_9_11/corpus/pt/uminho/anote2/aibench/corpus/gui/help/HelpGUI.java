package pt.uminho.anote2.aibench.corpus.gui.help;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextFont;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;


public class HelpGUI extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jPanelPanelName;
	private JTextPane jTextPaneInfo;
	private JTextField jTextFieldName;
	private JLabel jLabelName;
	private JPanel jPaneltextInfo;

	public HelpGUI()
	{
		initGUI();
		this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.025, 0.1};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			getContentPane().setLayout(thisLayout);
			{
				jPanelPanelName = new JPanel();
				GridBagLayout jPanelPanelNameLayout = new GridBagLayout();
				jPanelPanelName.setBorder(BorderFactory.createTitledBorder("Select Corpus Name"));

				getContentPane().add(jPanelPanelName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPanelNameLayout.rowWeights = new double[] {0.1};
				jPanelPanelNameLayout.rowHeights = new int[] {7};
				jPanelPanelNameLayout.columnWeights = new double[] {0.01, 0.1, 0.01};
				jPanelPanelNameLayout.columnWidths = new int[] {7, 7, 7};
				jPanelPanelName.setLayout(jPanelPanelNameLayout);
				{
					jLabelName = new JLabel();
					jPanelPanelName.add(jLabelName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelName.setText("Select Name : ");
				}
				{
					jTextFieldName = new JTextField();
					jPanelPanelName.add(jTextFieldName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
			{
				jPaneltextInfo = new JPanel();
				BoxLayout jPaneltextInfoLayout = new BoxLayout(jPaneltextInfo, javax.swing.BoxLayout.Y_AXIS);
				jPaneltextInfo.setLayout(jPaneltextInfoLayout);
				jPaneltextInfo.setBorder(BorderFactory.createTitledBorder(""));
				getContentPane().add(jPaneltextInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				{
					jTextPaneInfo = new JTextPane();
					jPaneltextInfo.add(jTextPaneInfo);
					jTextPaneInfo.setOpaque(false);
					jTextPaneInfo.setEditable(false);
					Font font = GlobalTextFont.largeFontItalic;
					jTextPaneInfo.setFont(font);
					jTextPaneInfo.setText(GlobalTextInfo.createCorpus);
				}
			}
		}		
	}

}
