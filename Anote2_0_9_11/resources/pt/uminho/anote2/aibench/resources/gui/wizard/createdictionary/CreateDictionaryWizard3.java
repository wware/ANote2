package pt.uminho.anote2.aibench.resources.gui.wizard.createdictionary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datastructures.ChebiExtension;
import pt.uminho.anote2.aibench.resources.datastructures.NcbiTaxonomyExtension;
import pt.uminho.anote2.aibench.resources.datastructures.UniprotExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycCoumpoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycEnzymeExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycPathwaysExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycProteinExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycReactionExtension;
import pt.uminho.anote2.aibench.resources.datastructures.brenda.BrendaExtension;
import pt.uminho.anote2.aibench.resources.datastructures.entrezgene.EntrezGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggCompoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggEnzymesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggGenesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggReactionsExtension;
import pt.uminho.anote2.aibench.resources.datatypes.Dictionaries;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericViewInputGUI;
import pt.uminho.anote2.aibench.utils.timeleft.TimeLeftProgress;
import pt.uminho.anote2.core.database.IDatabase;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.mysql.MySQLDatabase;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.ParamsReceiver;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class CreateDictionaryWizard3 extends DialogGenericViewInputGUI{

	private static final long serialVersionUID = -2836164760917009160L;
	private JTextField jTextFieldFilePath;
	private JComboBox jComboBoxEntity;
	private JPanel jPanel2;
	private JPanel jPanel1;
	private JPanel jPanelSpecialFilters;
	private JButton jButtonHelp;
	private JTextField jTextFieldOrganism;
	private JLabel jLabelOrganism;
	private JComboBox jComboBoxSource;
	private JLabel jLabelCategory;
	private JLabel jLabelType;
	private JButton jButtonFileChooser;
	private JLabel jLabelFile;

	private JPanel jPanelOrganism;
	private JPanel jPanelLoaderSelection;
	private JPanel jPanelFile;
		
	private DictionaryAibench dic;
	private Dictionaries dics;
	private JFileChooser filePath;
	protected boolean cancel_choose;
	private boolean isbiowarehouse;
	
	private JPanel jPanelBioWareHouse;
	private JLabel jLabelPort;
	private JCheckBox jCheckBoxOrganism;
	private JCheckBox jCheckBoxPathways;
	private JCheckBox jCheckBoxprotein;
	private JCheckBox jCheckBoxGene;
	private JCheckBox jCheckBoxEnzymes;
	private JCheckBox jCheckBoxMetabolicGenes;
	private JPanel jPanelClassOption;
	private JPasswordField jTextFieldPwd;
	private JTextField jTextFieldUser;
	private JTextField jTextFieldSchema;
	private JTextField jTextFieldPort;
	private JTextField jTextFieldHost;
	private JLabel jLabelUser;
	private JCheckBox jCheckBoxSyno;
	private JPanel jPanelSyn;
	private JCheckBox jCheckBoxReaction;
	private JCheckBox jCheckBoxCompound;
	private JLabel jLabelPwd;
	private JLabel jLabelSchema;
	private JLabel jLabelHost;
	private JPanel jPanelBiowarehouseCredentials;
	private JPanel jPanelSpecialEntrezGeneFilters;
	private JCheckBox jCheckEntrezGeneFilterHP;

	
	public CreateDictionaryWizard3(Dictionaries dics,String name,String info){
		super("Create Dictionary");
		int after;
		try {
			after = HelpDatabase.getNextInsertTableID(GlobalTablesName.resources);
			dic =  new DictionaryAibench(after,name,info);
			this.dics=dics;
			initGUI();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
		}
		this.setModal(true);
		this.setVisible(true);

	}

	private void initGUI() throws SQLException {
	
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.1, 0.1, 0.1, 0.0};
				thisLayout.rowHeights = new int[] {70, 7, 7, 20, 7};
				thisLayout.columnWeights = new double[] {0.1};
				thisLayout.columnWidths = new int[] {20};
				getContentPane().setLayout(thisLayout);		
				{

					{
						filePath = new JFileChooser(); 
						filePath.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if(e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION))
									cancel_choose = true;
							}
						});
						
						filePath.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					}
					{
						jPanelFile = new JPanel();
						jPanelFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "File", TitledBorder.LEADING, TitledBorder.TOP));
						GridBagLayout jPanelFileLayout = new GridBagLayout();
						getContentPane().add(jPanelFile, new GridBagConstraints(-1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelFileLayout.rowWeights = new double[] {0.1};
						jPanelFileLayout.rowHeights = new int[] {7};
						jPanelFileLayout.columnWeights = new double[] {0.0, 0.0, 0.03};
						jPanelFileLayout.columnWidths = new int[] {106, 500, 7};
						jPanelFile.setLayout(jPanelFileLayout);
						jPanelFile.add(getJLabelFile(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelFile.add(getJTextFieldFilePath(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jPanelFile.add(getJButtonFileChooser(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jPanelOrganism = new JPanel();
						GridBagLayout jPanelOrganismLayout = new GridBagLayout();
						jPanelOrganism.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Organism", TitledBorder.LEADING, TitledBorder.TOP));
						getContentPane().add(jPanelOrganism, new GridBagConstraints(-1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelOrganismLayout.rowWeights = new double[] {0.1};
						jPanelOrganismLayout.rowHeights = new int[] {7};
						jPanelOrganismLayout.columnWeights = new double[] {0.0, 0.0, 0.1};
						jPanelOrganismLayout.columnWidths = new int[] {179, 339, 7};
						jPanelOrganism.setLayout(jPanelOrganismLayout);
						jPanelOrganism.add(getJLabelOrganism(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelOrganism.add(getJTextFieldOrganism(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));}
					{
						jPanelLoaderSelection = new JPanel();
						getContentPane().add(jPanelLoaderSelection, new GridBagConstraints(-1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						GridBagLayout jPanelLoaderSelectionLayout = new GridBagLayout();
						jPanelLoaderSelection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Source", TitledBorder.LEADING, TitledBorder.TOP));
						jPanelLoaderSelectionLayout.rowWeights = new double[] {0.1, 0.1};
						jPanelLoaderSelectionLayout.rowHeights = new int[] {7, 7};
						jPanelLoaderSelectionLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
						jPanelLoaderSelectionLayout.columnWidths = new int[] {103, 7, 7, 7};
						jPanelLoaderSelection.setLayout(jPanelLoaderSelectionLayout);
						jPanelLoaderSelection.add(getJLabelType(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelLoaderSelection.add(getJLabelCategory(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelLoaderSelection.add(getJComboBoxSource(), new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jPanelLoaderSelection.add(getJComboBoxEntity(), new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						/**
						 * PAnel da Biowarehouse
						 */
						jPanelBioWareHouse = new JPanel();
						getContentPane().add(jPanelBioWareHouse, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						GridBagLayout jPanelUppanelLayout = new GridBagLayout();
						jPanelUppanelLayout.rowWeights = new double[] {0.025, 0.1, 0.1, 0.1, 0.1};
						jPanelUppanelLayout.rowHeights = new int[] {7, 7, 7, 20, 7};
						jPanelUppanelLayout.columnWeights = new double[] {0.0, 0.1, 0.1};
						jPanelUppanelLayout.columnWidths = new int[] {7, 7, 7};
						jPanelBioWareHouse.setLayout(jPanelUppanelLayout);
						{
							jPanelBiowarehouseCredentials = new JPanel();
							jPanelBiowarehouseCredentials.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"BiowareHouse Database Credentials", TitledBorder.LEADING,TitledBorder.TOP));

							GridBagLayout jPanelBiowarehouseCredentialsLayout = new GridBagLayout();
							jPanelBioWareHouse.add(jPanelBiowarehouseCredentials, new GridBagConstraints(0, 0, 2, 5, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelBiowarehouseCredentialsLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
							jPanelBiowarehouseCredentialsLayout.rowHeights = new int[] {7, 20, 7, 7, 7};
							jPanelBiowarehouseCredentialsLayout.columnWeights = new double[] {0.0, 0.1, 0.1, 0.1};
							jPanelBiowarehouseCredentialsLayout.columnWidths = new int[] {7, 7, 7, 7};
							jPanelBiowarehouseCredentials.setLayout(jPanelBiowarehouseCredentialsLayout);
							{
								jLabelHost = new JLabel();
								jPanelBiowarehouseCredentials.add(jLabelHost, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelHost.setText("Host :");
							}
							{
								jLabelPort = new JLabel();
								jPanelBiowarehouseCredentials.add(jLabelPort, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelPort.setText("Port :");
							}
							{
								jLabelSchema = new JLabel();
								jPanelBiowarehouseCredentials.add(jLabelSchema, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelSchema.setText("Schema :");
							}
							{
								jLabelUser = new JLabel();
								jPanelBiowarehouseCredentials.add(jLabelUser, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelUser.setText("User :");
							}
							{
								jLabelPwd = new JLabel();
								jPanelBiowarehouseCredentials.add(jLabelPwd, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelPwd.setText("Password :");
							}
							{
								jTextFieldHost = new JTextField();
								jTextFieldHost.setText("localhost");
								jPanelBiowarehouseCredentials.add(jTextFieldHost, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							}
							{
								jTextFieldPort = new JTextField();
								jPanelBiowarehouseCredentials.add(jTextFieldPort, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldPort.setText("3306");
							}
							{
								jTextFieldSchema = new JTextField();
								jPanelBiowarehouseCredentials.add(jTextFieldSchema, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							}
							{
								jTextFieldUser = new JTextField();
								jPanelBiowarehouseCredentials.add(jTextFieldUser, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
								jTextFieldUser.setText("root");
							}
							{
								jTextFieldPwd = new JPasswordField();
								jPanelBiowarehouseCredentials.add(jTextFieldPwd, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							}
						}
						{
							jPanelClassOption = new JPanel();
							jPanelClassOption.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Class Loader", TitledBorder.LEADING,TitledBorder.TOP));

							GridBagLayout jPanelClassOptionLayout = new GridBagLayout();
							jPanelBioWareHouse.add(jPanelClassOption, new GridBagConstraints(2, 0, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelClassOptionLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
							jPanelClassOptionLayout.rowHeights = new int[] {7, 20, 7, 7};
							jPanelClassOptionLayout.columnWeights = new double[] {0.1, 0.1};
							jPanelClassOptionLayout.columnWidths = new int[] {20, 7};
							jPanelClassOption.setLayout(jPanelClassOptionLayout);
							{
								jCheckBoxMetabolicGenes = new JCheckBox();
								jPanelClassOption.add(jCheckBoxMetabolicGenes, new GridBagConstraints(-1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxMetabolicGenes.setText("Metabolic Genes");
							}
							{
								jCheckBoxEnzymes = new JCheckBox();
								jPanelClassOption.add(jCheckBoxEnzymes, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxEnzymes.setText("Enzymes");
							}
							{
								jCheckBoxGene = new JCheckBox();
								jPanelClassOption.add(jCheckBoxGene, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxGene.setText("Gene");
							}
							{
								jCheckBoxprotein = new JCheckBox();
								jPanelClassOption.add(jCheckBoxprotein, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxprotein.setText("Protein");
							}
							{
								jCheckBoxPathways = new JCheckBox();
								jPanelClassOption.add(jCheckBoxPathways, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxPathways.setText("Pathways");
							}
							{
								jCheckBoxOrganism = new JCheckBox();
								jPanelClassOption.add(jCheckBoxOrganism, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxOrganism.setText("Organism");
							}
							{
								jCheckBoxCompound = new JCheckBox();
								jPanelClassOption.add(jCheckBoxCompound, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxCompound.setText("Compound");
							}
							{
								jCheckBoxReaction = new JCheckBox();
								jPanelClassOption.add(jCheckBoxReaction, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxReaction.setText("Reaction");
							}
						}
						{
							jPanelSyn = new JPanel();
							jPanelSyn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""),"Synonyms", TitledBorder.LEADING,TitledBorder.TOP));

							GridBagLayout jPanelSynLayout = new GridBagLayout();
							jPanelBioWareHouse.add(jPanelSyn, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							jPanelSynLayout.rowWeights = new double[] {0.1};
							jPanelSynLayout.rowHeights = new int[] {7};
							jPanelSynLayout.columnWeights = new double[] {0.1};
							jPanelSynLayout.columnWidths = new int[] {7};
							jPanelSyn.setLayout(jPanelSynLayout);
							{
								jCheckBoxSyno = new JCheckBox();
								jPanelSyn.add(jCheckBoxSyno, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jCheckBoxSyno.setText("Synonyms");
							}
						}
						getContentPane().add(jPanelBioWareHouse, new GridBagConstraints(-1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						getContentPane().add(getButtonsPanel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						getContentPane().add(getJPanelSpecialFilter(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));		
						getContentPane().add(getJPanelSpecialFiltersKegg(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

						jPanelBioWareHouse.setVisible(false);
					}
					
				}
				this.setSize(GlobalOptions.generalWidth, GlobalOptions.generalHeight);
				Utilities.centerOnOwner(this);
				this.setTitle("Load Dictionary Contents");
				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
		
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}

	private JLabel getJLabelFile() {
		if(jLabelFile == null) {
			jLabelFile = new JLabel();
			jLabelFile.setText("File :");
		}
		return jLabelFile;
	}
	
	private JTextField getJTextFieldFilePath() {
		if(jTextFieldFilePath == null) {
			jTextFieldFilePath = new JTextField();
			jTextFieldFilePath.setEditable(false);
		}
		return jTextFieldFilePath;
	}
	
	private JButton getJButtonFileChooser() {
		if(jButtonFileChooser == null) {
			jButtonFileChooser = new JButton();
			jButtonFileChooser.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Folder26.png")));
			jButtonFileChooser.addActionListener(new ActionListener(){
			
			@SuppressWarnings("static-access")
			public void actionPerformed(ActionEvent arg0) {
				
				int rel = filePath.showOpenDialog(new JFrame());
				if(rel == filePath.CANCEL_OPTION)
				{
					
				}
				else
				{
					File save_path = filePath.getSelectedFile();
					try {
						if(testFile(save_path))
						{			
							if(!cancel_choose && save_path!=null)
								jTextFieldFilePath.setText(save_path.getAbsolutePath());
						}
						else if(!save_path.isDirectory())
						{
							Workbench.getInstance().warn("File is not compatible.");
							jTextFieldFilePath.setText("");
						}
						else
						{
							Workbench.getInstance().warn("Directory is not compatible.");
							jTextFieldFilePath.setText("");
						}
					} catch (IOException e) {
						e.printStackTrace();
						Workbench.getInstance().error(e.getMessage());
					}
					cancel_choose=false;
				}
			}		
		});
		}
		return jButtonFileChooser;
	}
	
	protected boolean testFile(File file) throws IOException {
		
		IDicionaryFlatFilesLoader dicLoader;

		boolean cancel=true;
		TimeLeftProgress progress;
	
		String source = (String) jComboBoxSource.getSelectedItem();
		String entity = (String) jComboBoxEntity.getSelectedItem();
			
		if(source.equals("Uniprot"))
		{
			progress = new TimeLeftProgress("Loading Uniprot file");
			dicLoader = new UniprotExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("EntrezGene"))
		{
			progress = new TimeLeftProgress("Loading EntrezGene file");
			dicLoader = new EntrezGeneExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("NCBI Taxonomy"))
		{
			progress = new TimeLeftProgress("Loading NCBI Taxonomy file");
			dicLoader = new NcbiTaxonomyExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("Chebi"))
		{
			progress = new TimeLeftProgress("Loading Chebi file");
			dicLoader = new ChebiExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("Brenda"))
		{
			progress = new TimeLeftProgress("Loading Brenda file");
			dicLoader = new BrendaExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("Kegg"))
		{
			if(entity.equals("enzyme"))
			{
				progress = new TimeLeftProgress("Loading Kegg enzyme file");
				dicLoader = new KeggEnzymesExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("compound"))
			{
				progress = new TimeLeftProgress("Loading Kegg compound file");
				dicLoader = new KeggCompoundExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("reaction"))
			{
				progress = new TimeLeftProgress("Loading Kegg reaction file");
				dicLoader = new KeggReactionsExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("gene"))
			{
				progress = new TimeLeftProgress("Loading Kegg gene file");
				dicLoader = new KeggGenesExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else //all
			{
				progress = new TimeLeftProgress("Loading Kegg files");
				dicLoader = new KeggEnzymesExtension(dic,cancel,progress);
				for(File file_folder:file.listFiles())
				{
					if(dicLoader.checkFile(file_folder))
					{
						return true;
					}
				}
				return false;
			}
		}
		else
		{
			if(entity.equals("compound"))
			{
				progress = new TimeLeftProgress("Loading MetaCyc compound file");
				dicLoader = new BioCycCoumpoundExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("enzyme"))
			{
				progress = new TimeLeftProgress("Loading MetaCyc compound file");
				dicLoader = new BioCycEnzymeExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("gene"))
			{
				progress = new TimeLeftProgress("Loading MetaCyc gene file");
				dicLoader = new BioCycGeneExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("pathways"))
			{
				progress = new TimeLeftProgress("Loading MetaCyc pathways file");
				dicLoader = new BioCycPathwaysExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("protein"))
			{
				progress = new TimeLeftProgress("Loading MetaCyc protein file");
				dicLoader = new BioCycProteinExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("enzyme (reactions file)"))
			{
				progress = new TimeLeftProgress("Loading MetaCyc enzyme (reactions file) file");
				dicLoader = new BioCycReactionExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else //all
			{
				progress = new TimeLeftProgress("Loading MetaCyc files");
				dicLoader = new BioCycReactionExtension(dic,cancel,progress);
				for(File file_folder:file.listFiles())
				{
					if(dicLoader.checkFile(file_folder))
					{
						return true;
					}
				}
				return false;
			}
		}
	}

	private JLabel getJLabelType() {
		if(jLabelType == null) {
			jLabelType = new JLabel();
			jLabelType.setText("Source :");
		}
		return jLabelType;
	}
	
	private JLabel getJLabelCategory() {
		if(jLabelCategory == null) {
			jLabelCategory = new JLabel();
			jLabelCategory.setText("Entity :");
		}
		return jLabelCategory;
	}
	
	private JComboBox getJComboBoxSource() {
		if(jComboBoxSource == null) {
			ComboBoxModel jComboBoxSourceModel = getSource();
			jComboBoxSource = new JComboBox();
			jComboBoxSource.setModel(jComboBoxSourceModel);
			jComboBoxSource.addActionListener(new ActionListener(){		
				public void actionPerformed(ActionEvent arg0) {
					change();
				}
			});
			
		}
		return jComboBoxSource;
	}
	
	private JComboBox getJComboBoxEntity() {
		if(jComboBoxEntity == null) {
			ComboBoxModel jComboBoxEntityModel =
				new DefaultComboBoxModel(
						new String[] { "protein" });
			jComboBoxEntity = new JComboBox();
			jComboBoxEntity.setModel(jComboBoxEntityModel);
			jComboBoxEntity.addActionListener(new ActionListener(){		
				public void actionPerformed(ActionEvent arg0) {
					jTextFieldFilePath.setText("");
				}
			});
		}
		return jComboBoxEntity;
	}
	

	
	protected void change() {
		String loader = (String) jComboBoxSource.getSelectedItem();
		ComboBoxModel jComboBoxEntityModel = null;
		jTextFieldOrganism.setEnabled(true);
		jLabelOrganism.setText("Organism (Name)");
		if(loader.equals("Uniprot"))
		{
			jComboBoxEntityModel = getUniprotSource();
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
			
		}
		else if(loader.equals("EntrezGene"))
		{
			jComboBoxEntityModel = getEntrezGeneSource();
			jLabelOrganism.setText("Organism NCBI TAxonomyID :");
			jButtonHelp.setVisible(true);
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(false);
			jPanelSpecialEntrezGeneFilters.setVisible(true);
		}
		else if(loader.equals("NCBI Taxonomy"))
		{
			jComboBoxEntityModel = getNCBITaxonomySource();
			jTextFieldOrganism.setEnabled(false);
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
		}
		else if(loader.equals("Chebi"))
		{
			jComboBoxEntityModel = getChebiSource();
			jTextFieldOrganism.setEnabled(false);
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
		}
		else if(loader.equals("Brenda"))
		{
			jComboBoxEntityModel = getBrendaSource();
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
		}
		else if(loader.equals("Kegg"))
		{
			jComboBoxEntityModel = getKeggSource();
			jTextFieldOrganism.setEnabled(false);
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
		}
		else if(loader.equals("Biocyc / MetaCyc"))
		{
			jComboBoxEntityModel = getBioCycSource();
			jTextFieldOrganism.setEnabled(false);		
			isbiowarehouse=false;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
		}
		else if(loader.equals("BiowareHouse"))
		{
			jComboBoxEntityModel = new DefaultComboBoxModel();
			isbiowarehouse=true;
			jPanelSpecialFilters.setVisible(true);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
			
		}
		if(isbiowarehouse)
		{
			jPanelOrganism.setVisible(false);
			jPanelFile.setVisible(false);
			jPanelBioWareHouse.setVisible(true);
			jComboBoxEntity.setVisible(false);
			jLabelCategory.setVisible(false);
			jButtonHelp.setVisible(false);

		}
		else
		{
			jPanelOrganism.setVisible(true);
			jLabelCategory.setVisible(true);
			jPanelFile.setVisible(true);
			jPanelBioWareHouse.setVisible(false);
			jComboBoxEntity.setVisible(true);
			jComboBoxEntity.setModel(jComboBoxEntityModel);
			jTextFieldFilePath.setText("");
		}
	}

	private ComboBoxModel getSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] { "Uniprot", "EntrezGene","NCBI Taxonomy","Chebi","Brenda","Kegg","Biocyc / MetaCyc","BiowareHouse"});
		return jComboBoxEntityModel;
	}
	
	private ComboBoxModel getUniprotSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"protein"});
		return jComboBoxEntityModel;
	}
	
	private ComboBoxModel getBrendaSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"enzyme"});
		return jComboBoxEntityModel;
	}
	
	private ComboBoxModel getEntrezGeneSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"gene"});
		return jComboBoxEntityModel;
	}
	private ComboBoxModel getNCBITaxonomySource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"organism"});
		return jComboBoxEntityModel;
	}
	
	private ComboBoxModel getChebiSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"compound"});
		return jComboBoxEntityModel;
	}
	
	private ComboBoxModel getKeggSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"enzyme","compound","reaction","gene","all"});
		return jComboBoxEntityModel;
	}
	
	private ComboBoxModel getBioCycSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] {"compound","enzyme","gene","pathways","protein","enzyme (reactions file)","all"});
		return jComboBoxEntityModel;
	}


	
	private JLabel getJLabelOrganism() {
		if(jLabelOrganism == null) {
			jLabelOrganism = new JLabel();
			jLabelOrganism.setText("Organism (Name):");
		}
		return jLabelOrganism;
	}
	
	private JTextField getJTextFieldOrganism() {
		if(jTextFieldOrganism == null) {
			jTextFieldOrganism = new JTextField();
		}
		return jTextFieldOrganism;
	}

	@Override
	protected void okButtonAction() {
		try {

		if(isbiowarehouse)
		{
			if(testconection())
			{
				IDatabase db = getDatabaseBioFromPanels();
				Set<String> classLoader = getClassesLoader();
				if(classLoader.size()==0)
				{
					Workbench.getInstance().warn("Please select at least one class .");
				}
				else
				{
						dics.getResources().newResource(dic.getName(),dic.getInfo(),GlobalOptions.resourcesDictionaryName);
						Boolean selectSyn = jCheckBoxSyno.isSelected();
						
						ParamSpec[] paramsIntroduced1 = new ParamSpec[]{
								new ParamSpec("biowarehousedb", IDatabase.class,db, null),
								new ParamSpec("dictionary", DictionaryAibench.class,dic, null),
								new ParamSpec("classloader", Set.class,classLoader, null),
								new ParamSpec("synonyms", Boolean.class,selectSyn, null),
						};
						
						for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
							if (def.getID().equals("operations.updatebioware")){			
								Workbench.getInstance().executeOperation(def,paramsIntroduced1);
								dics.notifyViewObservers();
							}
						}


				}
				finish();
			}
			else
			{
				Workbench.getInstance().warn("BiowareHouse Access denied\n Please verify your Credentials");
			}		
		}
		else
		{
			if(jTextFieldFilePath.getText().equals(""))
			{
				Workbench.getInstance().warn("Please Select a File");
			}
			else
			{
				dics.getResources().newResource(dic.getName(),dic.getInfo(),GlobalOptions.resourcesDictionaryName);
				Properties prop = new Properties();
				if(jTextFieldOrganism.isEnabled()&&!jTextFieldOrganism.getText().equals(""))
				{
					prop.put("organism", jTextFieldOrganism.getText());
				}
				if(jPanelSpecialEntrezGeneFilters.isVisible() && jCheckEntrezGeneFilterHP.isSelected())
				{
					prop.put(GlobalNames.entrezGeneHyphoteticalProtein,true);
				}
				ParamSpec[] paramsIntroduced1 = new ParamSpec[]{
						new ParamSpec("dictionary",DictionaryAibench.class,this.dic, null),
						new ParamSpec("source",String.class,jComboBoxSource.getSelectedItem(), null),
						new ParamSpec("entity",String.class,jComboBoxEntity.getSelectedItem(),null),
						new ParamSpec("properties", Properties.class,prop, null),
						new ParamSpec("filepath", String.class, this.jTextFieldFilePath.getText(), null),
				};
				for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
					if (def.getID().equals("operations.updateDic")){			
						Workbench.getInstance().executeOperation(def,paramsIntroduced1);
						dics.notifyViewObservers();
					}
				}
				finish();
			}	
		}
		} catch (SQLException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		} catch (DatabaseLoadDriverException e) {
			TreatExceptionForAIbench.treatExcepion(e);
			dispose();
		}
	}

	private Set<String> getClassesLoader() {
		Set<String> classLoader = new HashSet<String>();
		if(jCheckBoxCompound.isSelected())
		{
			classLoader.add(GlobalNames.compounds);
		}
		if(jCheckBoxEnzymes.isSelected())
		{
			classLoader.add(GlobalNames.enzymes);
		}
		if(jCheckBoxGene.isSelected())
		{
			classLoader.add(GlobalNames.gene);
		}
		if(jCheckBoxMetabolicGenes.isSelected())
		{
			classLoader.add(GlobalNames.metabolicGenes);
		}
		if(jCheckBoxOrganism.isSelected())
		{
			classLoader.add(GlobalNames.organism);
		}
		if(jCheckBoxPathways.isSelected())
		{
			classLoader.add(GlobalNames.pathways);
		}
		if(jCheckBoxprotein.isSelected())
		{
			classLoader.add(GlobalNames.protein);
		}
		if(jCheckBoxReaction.isSelected())
		{
			classLoader.add(GlobalNames.reactions);
		}
		return classLoader;
	}

	private boolean testconection() throws DatabaseLoadDriverException, SQLException {
		IDatabase dbbiowarehouse = getDatabaseBioFromPanels();
		Connection con = dbbiowarehouse.getConnection();
		if(con==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	private IDatabase getDatabaseBioFromPanels() {
		String host = jTextFieldHost.getText();
		String port = jTextFieldPort.getText();
		String schema = jTextFieldSchema.getText();
		String user = jTextFieldUser.getText();
		String pwd = String.valueOf(jTextFieldPwd.getPassword());
		IDatabase db = new MySQLDatabase(host,port,schema,user,pwd);		
		return db;
	}
	
	private JPanel getJPanelSpecialFilter() {
		if(jPanelSpecialFilters == null) {
			jPanelSpecialFilters = new JPanel();
			GridBagLayout jPanelSpecialFiltersLayout = new GridBagLayout();
			jPanelSpecialFilters.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Special Filters", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelSpecialFiltersLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSpecialFiltersLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSpecialFiltersLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSpecialFiltersLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSpecialFilters.setLayout(jPanelSpecialFiltersLayout);
		}
		return jPanelSpecialFilters;
	}
	
	private JPanel getJPanelSpecialFiltersKegg() {
		if(jPanelSpecialEntrezGeneFilters == null) {
			jPanelSpecialEntrezGeneFilters = new JPanel();
			GridBagLayout jPanelSpecialFiltersLayout = new GridBagLayout();
			jPanelSpecialEntrezGeneFilters.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Special Filters Entrez Gene", TitledBorder.LEADING, TitledBorder.TOP));
			jPanelSpecialFiltersLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSpecialFiltersLayout.rowHeights = new int[] {7, 7, 7, 7};
			jPanelSpecialFiltersLayout.columnWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			jPanelSpecialFiltersLayout.columnWidths = new int[] {7, 7, 7, 7};
			jPanelSpecialEntrezGeneFilters.add(getJCheckEntrezGene(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jPanelSpecialEntrezGeneFilters.setLayout(jPanelSpecialFiltersLayout);
			jPanelSpecialEntrezGeneFilters.setVisible(false);
		}
		return jPanelSpecialEntrezGeneFilters;
	}
	
	private JCheckBox getJCheckEntrezGene() {
		if(jCheckEntrezGeneFilterHP == null) {
			jCheckEntrezGeneFilterHP = new JCheckBox();
			jCheckEntrezGeneFilterHP.setText("Hyphotetical Protein");
		}
		return jCheckEntrezGeneFilterHP;
	}
	
	protected String getHelpLink() {
		return GlobalOptions.wikiGeneralLink+"Dictionary_Create#Adding_Terms";
	}

	public void init(ParamsReceiver arg0, OperationDefinition<?> arg1) {
		
	}
	
}
