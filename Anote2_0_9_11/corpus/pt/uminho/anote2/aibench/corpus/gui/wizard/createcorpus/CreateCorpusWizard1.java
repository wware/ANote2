package pt.uminho.anote2.aibench.corpus.gui.wizard.createcorpus;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import pt.uminho.anote2.aibench.utils.wizard.WizardStandard;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextFont;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfo;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateCorpusWizard1 extends WizardStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelPanelName;
	private JLabel jLabelName;
	private JTextField jTextFieldName;
	private JPanel jPaneltextInfo;
	private JTextPane jTextPaneInfo;
	private JPanel jupperPanel;
	
	private static List<Object> param = new ArrayList<Object>();

	public CreateCorpusWizard1() 
	{
		super(param);
		initGUI();
		this.setTitle("Create Corpus - Select Name");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}
	
	// Deriving from a step ahead
	public CreateCorpusWizard1(List<Object> options)
	{
		super(param);
		initGUI();
		this.setTitle("Create Corpus - Select Name");
		Utilities.centerOnOwner(this);
		fillGUI(options.get(0));
		this.setModal(true);
		this.setVisible(true);
	}

	private void fillGUI(Object object) {
		jTextFieldName.setText(object.toString());
	}

	private void initGUI() {
		setEnableBackButton(false);
		setEnableDoneButton(false);	
	}	
	
	public void done() {}

	public void goBack() {}

	public void goNext() {
		if(jTextFieldName.getText().equals(""))
		{
			Workbench.getInstance().warn("Please insert a Corpus Name");
		}
		else
		{
			String corpusName = jTextFieldName.getText();
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(corpusName);
			closeView();
			new CreateCorpusWizard2(list);
		}
	}


	public JComponent getMainComponent() {
		if(jupperPanel==null)
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.025, 0.1};
			thisLayout.rowHeights = new int[] {7, 7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};
			jupperPanel = new JPanel();
			jupperPanel.setLayout(thisLayout);
			{
				GridBagLayout jPanelPanelNameLayout = new GridBagLayout();
				jPanelPanelName = new JPanel();
				jPanelPanelName.setBorder(BorderFactory.createTitledBorder("Select Corpus Name"));
				jupperPanel.add(jPanelPanelName, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
				jupperPanel.add(jPaneltextInfo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
		return jupperPanel;
	}

	public String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Create_Corpus_By_Publication_Manager#Select_Corpus_Name";
	}

}
