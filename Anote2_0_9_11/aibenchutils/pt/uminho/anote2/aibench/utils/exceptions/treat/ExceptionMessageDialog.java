package pt.uminho.anote2.aibench.utils.exceptions.treat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.text.html.HTMLEditorKit;



public class ExceptionMessageDialog extends JDialog{
	
	
	private static final long serialVersionUID = 1L;

	private Throwable error;

	private int WIDTH=500;
	
	
	// components
	private JPanel buttonsPanel;
	private JPanel infoPanel;
	
	public ExceptionMessageDialog(JFrame parent, Throwable error){
		super(parent,true);
		this.error = error;
		initialize("");
		setLocationRelativeTo(parent.getContentPane());

	}
	
	private void initialize(String title){
		BorderLayout layout = new BorderLayout();
		layout.setHgap(5);
		layout.setVgap(5);
		this.getContentPane().setLayout(layout);
		this.add(getButtonsPanel(), BorderLayout.SOUTH);
		this.setTitle(title);
		
		//JTextArea errorArea = new JTextArea("<b>There were an error during process:</b><br>"+this.error.getMessage());
		JTextPane errorArea = new JTextPane();
	
		errorArea.setEditorKit(new HTMLEditorKit());
		
		
		errorArea.setText("<font face='Dialog' size='7px'><b>"+this.error.getMessage()+"</b>:</font>");
		
		this.add(new JScrollPane(errorArea), BorderLayout.NORTH);
		
		errorArea.setEditable(false);
		errorArea.setMargin(new Insets(5, 5, 5, 5));
		errorArea.setPreferredSize(new Dimension(WIDTH, 50));
		
		this.pack();
	}
	
	private JPanel getButtonsPanel(){
		if(buttonsPanel == null){
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new FlowLayout());
			buttonsPanel.setPreferredSize(new Dimension(WIDTH, 40));
			final JToggleButton moreInfoButton = new JToggleButton("Show Details...");
			
			moreInfoButton.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e)  {
					if (moreInfoButton.isSelected()){
						moreInfoButton.setText("Hide Details...");
						showInfo();
					}
					else{
						moreInfoButton.setText("Show Details...");
						hideInfo();
					}
					
				}});
			
			buttonsPanel.add(moreInfoButton);
			
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					ExceptionMessageDialog.this.setVisible(false);
					
			}});
			
			buttonsPanel.add(okButton);
	
		}
		return buttonsPanel;
	}
	private JPanel getInfoPanel(){
		if (this.infoPanel==null){
			this.infoPanel = new JPanel();
			JTextArea area = new JTextArea();
			
			area.setText(getDetails());
			area.setCaretPosition(0);
			JScrollPane scroll = new JScrollPane(area);
			scroll.setPreferredSize(new Dimension(WIDTH, 200));
			BorderLayout layout = new BorderLayout();
			layout.setHgap(5);
			this.infoPanel.setLayout(layout);
			this.infoPanel.add(new JLabel(), BorderLayout.EAST);
			this.infoPanel.add(new JLabel(), BorderLayout.WEST);
			this.infoPanel.add(scroll, BorderLayout.CENTER);
		}
		return this.infoPanel;
	}
	private String getDetails(){
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		error.printStackTrace(pw);
		String message = sw.toString();
		pw.close();
		try {
			sw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	private void showInfo(){
		this.add(getInfoPanel(), BorderLayout.CENTER);
		this.setLocation(this.getLocation());
		this.pack();
	}
	
	private void hideInfo(){
		this.remove(getInfoPanel());
		this.setLocation(this.getLocation());
		this.pack();
	}
	
	public static void showMessageDialog(JFrame parent, Throwable error){
		ExceptionMessageDialog md = new ExceptionMessageDialog(parent, error);
		md.setVisible(true);
	}
}
