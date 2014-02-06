package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateCorpusWizard1 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JTextPane jTextPane;
	private JPanel jPanelBarPanel;
	private JPanel jPanelPresentationPAnel;
	private JLabel jLabeName;
	private JTextPane jTextPaneName;
	
	private static int sizeH = 600;
	private static int sizeV = 400;
	private static List<Object> param = new ArrayList<Object>();

	public CreateCorpusWizard1() 
	{
		super(sizeH,sizeV,param);
		initGUI();
		this.setTitle("Create a Corpus Step 1/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		setEnableBackButton(false);
		setEnableDoneButton(false);
		{
			{
				jPanelUpperPanel = new JPanel();
				jPanelUpperPanel.setBorder(BorderFactory.createTitledBorder("Information"));

				GridBagLayout jPanelUpperPanelLayout = new GridBagLayout();
				
				jPanelUpperPanelLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
				jPanelUpperPanelLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelUpperPanelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelUpperPanelLayout.columnWidths = new int[] {7, 7, 7, 7};
				jPanelUpperPanel.setLayout(jPanelUpperPanelLayout);
				{
					jPanelPresentationPAnel = new JPanel();
					GridBagLayout jPanelPresentationPAnelLayout = new GridBagLayout();
					jPanelUpperPanel.add(jPanelPresentationPAnel, new GridBagConstraints(0, 1, 4, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelPresentationPAnelLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelPresentationPAnelLayout.rowHeights = new int[] {7, 7, 7, 7};
					jPanelPresentationPAnelLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
					jPanelPresentationPAnelLayout.columnWidths = new int[] {7, 7, 7, 7};
					jPanelPresentationPAnel.setLayout(jPanelPresentationPAnelLayout);
					{
						jTextPane = new JTextPane();
						createPaneText();
						jPanelPresentationPAnel.add(jTextPane, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jLabeName = new JLabel();
						jPanelPresentationPAnel.add(jLabeName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jLabeName.setText("Name :");
					}
					{
						jTextPaneName = new JTextPane();
						jPanelPresentationPAnel.add(jTextPaneName, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				{
					jPanelBarPanel = new JPanel();
					GridBagLayout jPanelBarPanelLayout = new GridBagLayout();
					jPanelUpperPanel.add(jPanelBarPanel, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jPanelBarPanelLayout.rowWeights = new double[] {0.1};
					jPanelBarPanelLayout.rowHeights = new int[] {7};
					jPanelBarPanelLayout.columnWeights = new double[] {0.1, 0.1};
					jPanelBarPanelLayout.columnWidths = new int[] {20, 7};
				}
				getJScrollPaneUpPanel().setViewportView(jPanelUpperPanel);
			}
		}		
	}
	
	private void createPaneText() {
		String text = "Welcome do Wizard for create a Corpus";
		jTextPane.setText(text);	
	}
	
	
	public void done() {}

	public void goBack() {}

	public void goNext() {
		if(jTextPaneName.getText().equals(""))
		{
			Workbench.getInstance().warn("Please insert a Corpus Name");
		}
		else
		{
			this.setVisible(false);
			String corpusName = jTextPaneName.getText();
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(corpusName);
			new CreateCorpusWizard2(600,400,list);
		}
	}

	public static void main(String[] args) throws IOException{
		new CreateCorpusWizard1();
	}

}
