package pt.uminho.anote2.aibench.corpus.gui.help;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import pt.uminho.anote2.core.report.processes.ie.io.exporter.INetworkExportReport;
import pt.uminho.anote2.datastructures.utils.Utils;


public class ExportToNetworkPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private INetworkExportReport report;
	private JLabel jLabelNodes;
	private JTextPane jTextPaneNodes;
	private JLabel jLabelEdges;
	private JTextPane jTextPaneEdges;
	private JTextField jTextFieldDuration;
	private JLabel jLabelDuration;

	public ExportToNetworkPanel(INetworkExportReport report)
	{
		this.report = report;
		initGUI();
		completeGUI();
	}


	private void completeGUI() {
		jTextPaneNodes.setText(String.valueOf(report.getNumberOFNodes()));
		jTextPaneEdges.setText(String.valueOf(report.getNumberofEdges()));
		jTextFieldDuration.setText(Utils.convertTimeToString(report.getTime()));
	}


	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
		thisLayout.rowHeights = new int[] {7, 7, 7, 7};
		thisLayout.columnWeights = new double[] {0.1, 0.0, 0.1, 0.1};
		thisLayout.columnWidths = new int[] {7, 7, 7, 7};
		this.setLayout(thisLayout);
		this.setPreferredSize(new java.awt.Dimension(322, 227));
		this.setBorder(BorderFactory.createTitledBorder("RE to Network File Report"));
		{
			jLabelNodes = new JLabel();
			this.add(jLabelNodes, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
			jLabelNodes.setText("Nodes :");
		}
		{
			jTextPaneNodes = new JTextPane();
			jTextPaneNodes.setEditable(false);
			this.add(jTextPaneNodes, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		}
		{
			jLabelEdges = new JLabel();
			this.add(jLabelEdges, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
			jLabelEdges.setText("Edges :");
		}
		{
			jTextPaneEdges = new JTextPane();
			jTextPaneEdges.setEditable(false);
			this.add(jTextPaneEdges, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		}
		{
			jLabelDuration = new JLabel();
			this.add(jLabelDuration, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			jLabelDuration.setText("Duration :");
		}
		{
			jTextFieldDuration = new JTextField();
			jTextFieldDuration.setEditable(false);
			this.add(jTextFieldDuration, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		}
	}

}
