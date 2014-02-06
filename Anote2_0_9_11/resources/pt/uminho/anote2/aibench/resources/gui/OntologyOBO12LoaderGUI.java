package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datastructures.ontologies.OBOOntologyExtensionv1p2;
import pt.uminho.anote2.aibench.resources.datatypes.OntologyAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.session.SessionSettings;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTextInfoSmall;
import pt.uminho.generic.components.jchooser.FileChooserOBOExtention;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;


public class OntologyOBO12LoaderGUI extends DialogGenericViewInputGUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FileChooserOBOExtention chooser;
	private JPanel filePanel;
	private JPanel comboboxPanel;
	private JTextField textFile;
	private JButton selectFile;
	private JComboBox jComboBoxOntologies;
	public static final String CHOOSE_FILE = "choose file";



	public OntologyOBO12LoaderGUI() {
		super("Load OBO 1.2 Ontology");
		initGUI();
		this.setModal(true);
	}


	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.0};
		thisLayout.rowHeights = new int[] {7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1};
		thisLayout.columnWidths = new int[] {7};
		getContentPane().setLayout(thisLayout);
		getContentPane().add(getOntologiesComboBoxPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(getChoseFile(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		chooser = new FileChooserOBOExtention();

	}
	
	private JPanel getOntologiesComboBoxPanel() {
		if(comboboxPanel==null)
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1,0.1,0.1};
			thisLayout.rowHeights = new int[] {7,7,7};
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {7};	
			comboboxPanel = new JPanel();
			comboboxPanel.setBorder(BorderFactory.createTitledBorder(null, "Select Ontology", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			comboboxPanel.setLayout(thisLayout);
			comboboxPanel.add(getJComboBoxlookupTable(), new GridBagConstraints(0, 1, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		return comboboxPanel;
	}

	
	private JComboBox getJComboBoxlookupTable()  {
		if(jComboBoxOntologies == null) {
			ComboBoxModel jComboBoxlookupTableModel = 
				new DefaultComboBoxModel();
			jComboBoxOntologies = new JComboBox();
			jComboBoxOntologies.setModel(jComboBoxlookupTableModel);
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(OntologyAibench.class);

			for (ClipboardItem item : cl) {
				jComboBoxOntologies.addItem((OntologyAibench)item.getUserData());
			}
			if (jComboBoxOntologies.getModel().getSize() > 0)
			{
				Object obj = HelpAibench.getSelectedItem(OntologyAibench.class);
				OntologyAibench dicAibnech = (OntologyAibench) obj;
				jComboBoxOntologies.setSelectedItem(dicAibnech);
			}
		}
		return jComboBoxOntologies;
	}

	private JPanel getChoseFile() {
		if(filePanel==null)
		{
			filePanel = new JPanel();
			GridBagLayout filePanelLayout = new GridBagLayout();
			filePanelLayout.rowWeights = new double[] {0.1};
			filePanelLayout.rowHeights = new int[] {7};
			filePanelLayout.columnWeights = new double[] {0.1, 0.0};
			filePanelLayout.columnWidths = new int[] {7, 7};
			filePanel.setLayout(filePanelLayout);
			filePanel.setBorder(BorderFactory.createTitledBorder(null, "Select File", TitledBorder.LEADING, TitledBorder.BELOW_TOP));
			{
				textFile = new JTextField();
				textFile.setEditable(false);
				filePanel.add(textFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				textFile.setPreferredSize(new java.awt.Dimension(6, 30));
			}
			{
				selectFile = new JButton();
				selectFile.setText("File...");
				selectFile.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						changeDir();			
					}
				});
				selectFile.setActionCommand(CHOOSE_FILE);
				selectFile.setPreferredSize(new java.awt.Dimension(100, 30));
				filePanel.add(selectFile, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			}
		}
		return filePanel;
	}
	
	protected void changeDir() {
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			textFile.setText(chooser.getSelectedFile().getAbsolutePath());
		}
	}


	@Override
	protected void okButtonAction() {
		if(textFile.getText().equals(""))
		{
			Workbench.getInstance().warn("Please select File");
		}
		else
		{

			TimeLeftProgress progress = new TimeLeftProgress("Load OBO File");
			OntologyAibench onto = (OntologyAibench) jComboBoxOntologies.getSelectedItem();
			OBOOntologyExtensionv1p2 ontoLoder = new OBOOntologyExtensionv1p2(onto, onto.getName(), progress );
			File file = new File(textFile.getText());
			try {
				if(!ontoLoder.validateFile(file))
				{
					Workbench.getInstance().warn(GlobalTextInfoSmall.fileNotcompatible);
				}
				else
				{
					SessionSettings.getSessionSettings().setSearchDirectory(file.getParent());
					if(onto.isOntologyFill())
					{
						Workbench.getInstance().warn(GlobalTextInfoSmall.ontologyLoaderProblem);
					}
					else
					{
						paramsRec.paramsIntroduced(new ParamSpec[]{
								new ParamSpec("ontology",OntologyAibench.class,(OntologyAibench) jComboBoxOntologies.getSelectedItem() ,null),
								new ParamSpec("file",File.class,new File(textFile.getText()),null)
						});
					}
				}
			} catch (IllegalArgumentException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (IOException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
				dispose();
			}
		}	
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Ontology_Update";
	}
	
	@Override
	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		Object obj = HelpAibench.getSelectedItem(OntologyAibench.class);
		if(obj==null)
		{
			Workbench.getInstance().warn("No Ontology selected on clipboard");
			dispose();
		}
		else
		{
			this.paramsRec = arg0;
			this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
			Utilities.centerOnOwner(this);
			this.setVisible(true);	
		}
	}
}
