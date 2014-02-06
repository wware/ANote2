package pt.uminho.anote2.aibench.publicationmanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.anote2.aibench.publicationmanager.datatypes.QueryInformationRetrievalExtension;
import pt.uminho.anote2.core.document.IPublication;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.workbench.Workbench;
import es.uvigo.ei.aibench.workbench.utilities.Utilities;

public class AddFileView extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private JTextField jTextFieldHost;
	private FileChooserExtensionPdf jFileChooserAddFile;
	private JPanel jPanelFileChoser;
	private JLabel jLabel1;

	private IPublication pub;
	private QueryInformationRetrievalExtension query;

	public AddFileView(IPublication pub,QueryInformationRetrievalExtension query) {
		super(Workbench.getInstance().getMainFrame());
		this.pub=pub;
		this.query=query;
		initGUI();
		Utilities.centerOnOwner(this);
		this.setSize(GlobalOptions.smallWidth, GlobalOptions.smallHeight);
		this.setModal(true);
		this.setVisible(true);
	}


	private void initGUI() {
		
		{
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.25, 0.0, 0.75};
			thisLayout.rowHeights = new int[] {7, 7, 7};
			thisLayout.columnWeights = new double[] {0.25, 0.75};
			thisLayout.columnWidths = new int[] {147, 149};
			getContentPane().setLayout(thisLayout);
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setText("File Identification :");
			}
			{
				jTextFieldHost = new JTextField();
				getContentPane().add(jTextFieldHost, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				jTextFieldHost.setText(this.pub.getOtherID());
				jTextFieldHost.setEditable(false);
			}
			{
				jPanelFileChoser = new JPanel();
				getContentPane().add(jPanelFileChoser, new GridBagConstraints(0, 1, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				jPanelFileChoser.setBorder(BorderFactory.createTitledBorder("Select File"));
				{
					jFileChooserAddFile = new FileChooserExtensionPdf();
					jPanelFileChoser.add(jFileChooserAddFile, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
		}
	}

	protected void okButtonAction() {
		File file = jFileChooserAddFile.getSelectedFile();
		if(jFileChooserAddFile.accept(file))
		{
			ParamSpec[] paramsSpec = new ParamSpec[]{ 
					new ParamSpec("Publication",IPublication.class,this.pub, null),
					new ParamSpec("Query",QueryInformationRetrievalExtension.class,this.query, null),
					new ParamSpec("File",File.class,file, null)
			};
			
			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.addfile")){			
					Workbench.getInstance().executeOperation(def, paramsSpec);
					this.setVisible(false);
					this.dispose();
					this.setModal(false);
					return;
				}
			}

		}
		else
		{
			Workbench.getInstance().warn("File extension incorrect...\n Please Select a PDF File");
		}
		

	}
	
	

}
