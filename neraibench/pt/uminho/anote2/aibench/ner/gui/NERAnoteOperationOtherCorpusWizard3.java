package pt.uminho.anote2.aibench.ner.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;

import pt.uminho.anote2.aibench.corpus.datatypes.Corpus;
import pt.uminho.anote2.aibench.utils.wizart.WizartStandard;
import pt.uminho.anote2.process.IE.IIEProcess;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class NERAnoteOperationOtherCorpusWizard3 extends WizartStandard{
	
	private static final long serialVersionUID = 1L;
	private JPanel jPanelUpperPAnel;
	private JLabel jLabelId;
	private JList jListProperties;
	private JPanel jPanelProperties;
	private JTextField jTextField1;
	private JLabel jLabelNAme;
	private JTextField jTextFieldType;
	private JLabel jLabelType;
	private JTextField jTextFieldID;
	
	private IIEProcess process;
	
	

	public NERAnoteOperationOtherCorpusWizard3(int sizeH,int sizeV,List<Object> param) {
		super(sizeH,sizeV,param);
		process = (IIEProcess) getParam().get(2);
		initGUI();
		this.setTitle("NER @Note - Lexical Resources");
		Utilities.centerOnOwner(this);
		this.setVisible(true);
		this.setModal(true);	
	}

	private void initGUI() {
		setEnableNextButton(false);
		{
			jPanelUpperPAnel = new JPanel();
			GridBagLayout jPanelUpperPAnelLayout = new GridBagLayout();
			jPanelUpperPAnelLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1};
			jPanelUpperPAnelLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelUpperPAnelLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.025};
			jPanelUpperPAnelLayout.columnWidths = new int[] {7, 7, 7, 20, 7};
			jPanelUpperPAnel.setLayout(jPanelUpperPAnelLayout);
			{
				jLabelId = new JLabel();
				jPanelUpperPAnel.add(jLabelId, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelId.setText("ID :");
				
			}
			{
				jTextFieldID = new JTextField();
				jTextFieldID.setEditable(false);
				jTextFieldID.setText(String.valueOf(process.getID()));
				jPanelUpperPAnel.add(jTextFieldID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jLabelType = new JLabel();
				jPanelUpperPAnel.add(jLabelType, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelType.setText("Type :");
			}
			{
				jTextFieldType = new JTextField();
				jTextFieldType.setEditable(false);
				jTextFieldType.setText(process.getType());
				jPanelUpperPAnel.add(jTextFieldType, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{
				jLabelNAme = new JLabel();
				jPanelUpperPAnel.add(jLabelNAme, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelNAme.setText("Description :");
			}
			{
				jTextField1 = new JTextField();
				jTextField1.setEditable(false);
				jTextField1.setText(process.getDescription());
				jPanelUpperPAnel.add(jTextField1, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			}
			{

				jPanelProperties = new JPanel();
				GridBagLayout jPanelPropertiesLayout = new GridBagLayout();
				jPanelUpperPAnel.add(jPanelProperties, new GridBagConstraints(0, 2, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelPropertiesLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanelPropertiesLayout.rowHeights = new int[] {7, 7, 7, 7};
				jPanelPropertiesLayout.columnWeights = new double[] {0.025, 0.1, 0.025};
				jPanelPropertiesLayout.columnWidths = new int[] {7, 7, 7};
				jPanelProperties.setLayout(jPanelPropertiesLayout);
				{
					ListModel jListPropertiesModel = getProcessesModel();
					jListProperties = new JList();
					jPanelProperties.add(jListProperties, new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					jListProperties.setModel(jListPropertiesModel);
				}
			}
			getJScrollPaneUpPanel().setViewportView(jPanelUpperPAnel);
		}	
	}
	
	private DefaultComboBoxModel getProcessesModel()
	{
		DefaultComboBoxModel model = new DefaultComboBoxModel();

		Properties prop = process.getProperties();
		for(String key:prop.stringPropertyNames())
		{
			String line = key + " " + prop.getProperty(key);
			model.addElement(line);
		}
		return model;
	}

	public void done()
	{
		Corpus corpus = (Corpus) getParam().get(1);
		IIEProcess process = (IIEProcess) getParam().get(2);
		
		ParamSpec[] paramsSpec = new ParamSpec[]{
				new ParamSpec("Corpus", Corpus.class,corpus, null),
				new ParamSpec("Process", IIEProcess.class,process, null)
		};
		
		for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
			if (def.getID().equals("operations.neranotecorpus")){			
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
		param.remove(2);
		new NERAnoteOperationOtherCorpusWizard2(600,400,param);
	}

	public void goNext() {
		
	}
	
	public void finish() {
		this.setVisible(false);
		this.dispose();
		Workbench.getInstance().warn("Operation Ner Anote Cancel");
		return;
	}

}
