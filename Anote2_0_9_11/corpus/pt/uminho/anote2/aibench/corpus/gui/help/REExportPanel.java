package pt.uminho.anote2.aibench.corpus.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.uminho.anote2.core.report.processes.IRESchemaExportReport;
import pt.uminho.anote2.datastructures.utils.Utils;

public class REExportPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private IRESchemaExportReport report;
	private JLabel jLabelDuration;
	private JTextField jTextFieldEntityAnnotations;
	private JTextField jTextFieldDuration;
	private JLabel jLabelEntitiesAnnotated;

	public REExportPanel(IRESchemaExportReport report)
	{
		this.report = report;
		initGUI();
		completeGUI();
	}

	private void completeGUI() {
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
		jTextFieldEntityAnnotations.setText(String.valueOf(report.relationsExported()));
	}

	private void initGUI() {
		{
			GridBagLayout thisLayout = new GridBagLayout();
			this.setPreferredSize(new java.awt.Dimension(352, 232));
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.05, 0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				jLabelDuration = new JLabel();
				this.add(jLabelDuration, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelDuration.setText("Duration :");
			}
			{
				jLabelEntitiesAnnotated = new JLabel();
				this.add(jLabelEntitiesAnnotated, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabelEntitiesAnnotated.setText("Annotations Exported :");
			}
			{
				jTextFieldDuration = new JTextField();
				jTextFieldDuration.setEditable(false);
				this.add(jTextFieldDuration, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			}
			{
				jTextFieldEntityAnnotations = new JTextField();
				jTextFieldEntityAnnotations.setEditable(false);
				this.add(jTextFieldEntityAnnotations, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			}
		}		
	}
}
