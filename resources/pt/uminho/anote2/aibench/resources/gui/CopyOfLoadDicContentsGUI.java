package pt.uminho.anote2.aibench.resources.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import pt.uminho.anote2.aibench.resources.datastructures.BrendaExtension;
import pt.uminho.anote2.aibench.resources.datastructures.ChebiExtension;
import pt.uminho.anote2.aibench.resources.datastructures.EntrezGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.NcbiTaxonomyExtension;
import pt.uminho.anote2.aibench.resources.datastructures.UniprotExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycCoumpoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycEnzymeExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycGeneExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycPathwaysExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycProteinExtension;
import pt.uminho.anote2.aibench.resources.datastructures.biocyc.BioCycReactionExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggCompoundExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggEnzymesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggGenesExtension;
import pt.uminho.anote2.aibench.resources.datastructures.kegg.KeggReactionsExtension;
import pt.uminho.anote2.aibench.resources.datatypes.DictionaryAibench;
import pt.uminho.anote2.aibench.utils.exceptions.MissingDatatypesException;
import pt.uminho.anote2.aibench.utils.exceptions.NonExistingConnection;
import pt.uminho.anote2.aibench.utils.gui.DialogGenericView;
import pt.uminho.anote2.aibench.utils.operations.HelpAibench;
import pt.uminho.anote2.aibench.utils.operations.TimeLeftProgress;
import pt.uminho.anote2.resource.dictionary.IDicionaryFlatFilesLoader;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

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
public class CopyOfLoadDicContentsGUI extends DialogGenericView{

	private static final long serialVersionUID = -2836164760917009160L;
	private JComboBox dicsComboBox;
	private JLabel jLabel3;
	private JTextField jTextFieldFilePath;
	private JComboBox jComboBoxEntity;
	private JTextPane jTextPaneOrganism;
	private JLabel jLabelOrganism;
	private JComboBox jComboBoxSource;
	private JLabel jLabelCategory;
	private JLabel jLabelType;
	private JButton jButtonFileChooser;
	private JLabel jLabelFile;
	private JPanel jPanelDictionary;

	private JPanel jPanelOrganism;
	private JPanel jPanelLoaderSelection;
	private JPanel jPanelFile;
		
	private DictionaryAibench dictionary;
	private JFileChooser filePath;
	protected boolean cancel_choose;

	
	public CopyOfLoadDicContentsGUI() throws NonExistingConnection, SQLException,MissingDatatypesException{
		super("Udpate Dictionary Information");
		confirmDic();
		initGUI();

	}
	
	private void confirmDic() {
		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class);
		if(items==null||items.size()==0)
		{
			Workbench.getInstance().warn("No Dicnioneries on clipboard");
			return;
		}
	}

	private void initGUI() throws NonExistingConnection, SQLException, MissingDatatypesException {
	
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {55, 70, 60, 7, 7, 7};
				thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1, 0.1, 0.0};
				thisLayout.columnWidths = new int[] {7, 81, 76, 20, 20, 7};
				getContentPane().setLayout(thisLayout);		
				{

					{
						jPanelDictionary = new JPanel();
						jPanelDictionary.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder(""), "Dictionary", TitledBorder.LEADING, TitledBorder.TOP));
						GridBagLayout jPanelDictionaryLayout = new GridBagLayout();
						getContentPane().add(jPanelDictionary, new GridBagConstraints(1, 0, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelDictionaryLayout.rowWeights = new double[] {0.1};
						jPanelDictionaryLayout.rowHeights = new int[] {7};
						jPanelDictionaryLayout.columnWeights = new double[] {0.05, 0.95};
						jPanelDictionaryLayout.columnWidths = new int[] {78, 7};
						jPanelDictionary.setLayout(jPanelDictionaryLayout);
						jPanelDictionary.add(getDicsComboBox(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
						jPanelDictionary.add(getJLabel3(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					}
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
						getContentPane().add(jPanelFile, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelFileLayout.rowWeights = new double[] {0.1};
						jPanelFileLayout.rowHeights = new int[] {7};
						jPanelFileLayout.columnWeights = new double[] {0.0, 0.0, 0.05};
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
						getContentPane().add(jPanelOrganism, new GridBagConstraints(1, 4, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						jPanelOrganismLayout.rowWeights = new double[] {0.1};
						jPanelOrganismLayout.rowHeights = new int[] {7};
						jPanelOrganismLayout.columnWeights = new double[] {0.0, 0.0, 0.1};
						jPanelOrganismLayout.columnWidths = new int[] {179, 339, 7};
						jPanelOrganism.setLayout(jPanelOrganismLayout);
						jPanelOrganism.add(getJLabelOrganism(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
						jPanelOrganism.add(getJTextPaneOrganism(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
					{
						jPanelLoaderSelection = new JPanel();
						getContentPane().add(jPanelLoaderSelection, new GridBagConstraints(1, 1, 5, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
						getContentPane().add(getButtonsPanel(), new GridBagConstraints(1, 5, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
					
				}
				this.setSize(688, 432);
				this.setVisible(true);
				Utilities.centerOnOwner(this);
				this.setTitle("Load Dictionary Contents");
				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
		
	}

	public void finish() {
		this.setVisible(false);
		this.dispose();		
	}

	public void onValidationError(Throwable arg0) {
		Workbench.getInstance().error(arg0);
	}

	
	private JLabel getJLabel3() {
		if(jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setText("Dictionary:");
		}
		return jLabel3;
	}

	private JComboBox getDicsComboBox() throws NonExistingConnection, SQLException, MissingDatatypesException {
		if(dicsComboBox == null) {
			ComboBoxModel dicsComboBoxModel = 
				new DefaultComboBoxModel();
			dicsComboBox = new JComboBox();
			dicsComboBox.setModel(dicsComboBoxModel);

			
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(DictionaryAibench.class);
			for (ClipboardItem item : cl) {
				dicsComboBox.addItem((DictionaryAibench)item.getUserData());
			}
			if (dicsComboBox.getModel().getSize() > 0)
			{
				Object obj = HelpAibench.getSelectedItem(DictionaryAibench.class);
				DictionaryAibench dicAibnech = (DictionaryAibench) obj;
				dicsComboBox.setSelectedItem(dicAibnech);
				dictionary = dicAibnech;
			}
			else
			{
				dictionary = (DictionaryAibench) dicsComboBox.getModel().getElementAt(0);
			}
			dicsComboBox.addActionListener(new ActionListener(){		
				public void actionPerformed(ActionEvent arg0) {
					dictionary = (DictionaryAibench) ((ClipboardItem)dicsComboBox.getSelectedItem()).getUserData();
				}
			});
		
		}
		return dicsComboBox;
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
			jButtonFileChooser.setText("...");
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
					if(testFile(save_path))
					{			
						if(!cancel_choose && save_path!=null)
							jTextFieldFilePath.setText(save_path.getAbsolutePath());
					}
					else if(!save_path.isDirectory())
					{
						Workbench.getInstance().warn("File is not compatible!!!");
						jTextFieldFilePath.setText("");
					}
					else
					{
						Workbench.getInstance().warn("Directory is not compatible!!!");
						jTextFieldFilePath.setText("");
					}
					cancel_choose=false;
				}

			}
			
		});
		}
		return jButtonFileChooser;
	}
	
	protected boolean testFile(File file) {
		
		IDicionaryFlatFilesLoader dicLoader;
		DictionaryAibench dic =  this.dictionary;
		boolean cancel=true;
		TimeLeftProgress progress = new TimeLeftProgress();
		
		String source = (String) jComboBoxSource.getSelectedItem();
		String entity = (String) jComboBoxEntity.getSelectedItem();
			
		if(source.equals("Uniprot"))
		{
			dicLoader = new UniprotExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("EntrezGene"))
		{
			dicLoader = new EntrezGeneExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("NCBI Taxonomy"))
		{
			dicLoader = new NcbiTaxonomyExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("Chebi"))
		{
			dicLoader = new ChebiExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("Brenda"))
		{
			dicLoader = new BrendaExtension(dic,cancel,progress);
			return dicLoader.checkFile(file);
		}
		else if(source.equals("Kegg"))
		{
			if(entity.equals("enzyme"))
			{
				dicLoader = new KeggEnzymesExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("compound"))
			{
				dicLoader = new KeggCompoundExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("reaction"))
			{
				dicLoader = new KeggReactionsExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("gene"))
			{
				dicLoader = new KeggGenesExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else //all
			{
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
				dicLoader = new BioCycCoumpoundExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("enzyme"))
			{
				dicLoader = new BioCycEnzymeExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("gene"))
			{
				dicLoader = new BioCycGeneExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("pathways"))
			{
				dicLoader = new BioCycPathwaysExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("protein"))
			{
				dicLoader = new BioCycProteinExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else if(entity.equals("enzyme (reactions file)"))
			{
				dicLoader = new BioCycReactionExtension(dic,cancel,progress);
				return dicLoader.checkFile(file);
			}
			else //all
			{
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
		ComboBoxModel jComboBoxEntityModel;
		jTextPaneOrganism.setEnabled(true);
		jLabelOrganism.setText("Organism (Name)");
		if(loader.equals("Uniprot"))
		{
			jComboBoxEntityModel = getUniprotSource();
		}
		else if(loader.equals("EntrezGene"))
		{
			jComboBoxEntityModel = getEntrezGeneSource();
			jLabelOrganism.setText("Organism NCBI TAxonomyID :");
		}
		else if(loader.equals("NCBI Taxonomy"))
		{
			jComboBoxEntityModel = getNCBITaxonomySource();
			jTextPaneOrganism.setEnabled(false);
		}
		else if(loader.equals("Chebi"))
		{
			jComboBoxEntityModel = getChebiSource();
			jTextPaneOrganism.setEnabled(false);
		}
		else if(loader.equals("Brenda"))
		{
			jComboBoxEntityModel = getBrendaSource();
		}
		else if(loader.equals("Kegg"))
		{
			jComboBoxEntityModel = getKeggSource();
			jTextPaneOrganism.setEnabled(false);
		}
		else
		{
			jComboBoxEntityModel = getBioCycSource();
			jTextPaneOrganism.setEnabled(false);
		}
		jComboBoxEntity.setModel(jComboBoxEntityModel);
		jTextFieldFilePath.setText("");
		
		
	}

	private ComboBoxModel getSource() {
		ComboBoxModel jComboBoxEntityModel = 
			new DefaultComboBoxModel(
					new String[] { "Uniprot", "EntrezGene","NCBI Taxonomy","Chebi","Brenda","Kegg","BioCyc" });
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
					new String[] {"protein"});
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
	
	private JTextPane getJTextPaneOrganism() {
		if(jTextPaneOrganism == null) {
			jTextPaneOrganism = new JTextPane();
		}
		return jTextPaneOrganism;
	}

	@Override
	protected void okButtonAction() {

		if(jTextFieldFilePath.getText().equals(""))
		{
			Workbench.getInstance().warn("Please Select a File");
		}
		else
		{
			Properties prop = new Properties();
			if(jTextPaneOrganism.isEnabled()&&!jTextPaneOrganism.getText().equals(""))
			{
				prop.put("organism", jTextPaneOrganism.getText());
			}
			
			paramsRec.paramsIntroduced( new ParamSpec[]{
						new ParamSpec("dictionary",DictionaryAibench.class,this.dictionary, null),
						new ParamSpec("source",String.class,jComboBoxSource.getSelectedItem(), null),
						new ParamSpec("entity",String.class,jComboBoxEntity.getSelectedItem(),null),
						new ParamSpec("properties", Properties.class,prop, null),
						new ParamSpec("filepath", String.class, this.jTextFieldFilePath.getText(), null),
				});
		}		
		
	}
}
