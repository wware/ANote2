package pt.uminho.anote2.aibench.corpus.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpora;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateCorpusWizard4 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPanel;
	private JTextPane jTextPane;
	private JPanel jPanelBarPanel;
	private JPanel jPanelPresentationPAnel;
	
	protected ParamsReceiver paramsRec=null;

	public CreateCorpusWizard4(int sizeH, int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		initGUI();
		this.setTitle("Create a Corpus Step 4/4");
		Utilities.centerOnOwner(this);
		this.setModal(true);
		this.setVisible(true);
	}

	private void initGUI() {
		setEnableNextButton(false);
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
					jPanelPresentationPAnel.add(jTextPane, new GridBagConstraints(1, 1, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
	
	private void createPaneText() {
		String text = "You create a Corpus.. Please clic on \"Ok\" button to finished the process";
		jTextPane.setText(text);	
	}
	
	
	@SuppressWarnings("unchecked")
	public void done() {
		
		String description = getParam().get(0).toString();
		Set<Integer> ids = (Set<Integer>) getParam().get(2);
		Properties prop = (Properties) getParam().get(3);
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(Corpora.class);
		Corpora project = (Corpora) items.get(0).getUserData();
		
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("project", Corpora.class,project, null),
				new ParamSpec("name", String.class,description, null),
				new ParamSpec("prop", Properties.class,prop, null),
				new ParamSpec("ruleClass", Set.class,ids, null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.initcreatecorpuspublicationmanager")){			
				Workbench.getInstance().executeOperation(def, paramsSpec);
				this.setVisible(false);
				this.dispose();
				this.setModal(false);
				return;
			}
		}
			
	}

	public void goBack() {
		this.setVisible(false);
		List<Object> param = getParam();
		new CreateCorpusWizard3(800,600,param);
		this.setVisible(false);
		this.dispose();
		this.setModal(false);
	}

	public void goNext() {
		
	}

	public static void main(String[] args) throws IOException{
		new CreateCorpusWizard4(600,400,new ArrayList<Object>());
	}
}
