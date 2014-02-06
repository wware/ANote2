package pt.uminho.anote2.aibench.curator.gui.help;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColors;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;

public class SentenceAnnotationPane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JEditorPane jEditorPaneSelectClue;
	private JScrollPane jScrollPaneEditorPaneClueSelection;
	private JButton jButtonAddMoreText;
	protected int zoom = 12;
	private JButton jButtonAddLessRange;
	protected CuratorDocument curatorDocument;
	protected AnnotationPosition sentencesRange;
	protected AnnotationPosition selectedRange;
	protected AnnotationPositions entities;
	protected AnnotationPositions clues;
	
	public SentenceAnnotationPane(CuratorDocument curatorDocument,AnnotationPosition sentencesRange,AnnotationPosition selectedRange) throws SQLException, DatabaseLoadDriverException
	{
		this.curatorDocument = curatorDocument;
		this.sentencesRange=sentencesRange;
		this.selectedRange=selectedRange;
		entities = calculateEntitiesInRange(sentencesRange);
		clues = calculateCluesInRange(sentencesRange);
		initGUI();
	}

	private AnnotationPositions calculateCluesInRange(AnnotationPosition sentencesRange) throws SQLException, DatabaseLoadDriverException {
		return ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEventAnnotationPositions().getAnnotationPositionsSubSet(sentencesRange.getStart(), sentencesRange.getEnd());
	}

	private AnnotationPositions calculateEntitiesInRange(AnnotationPosition sentencesRange) throws SQLException, DatabaseLoadDriverException {
		return ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityAnnotationPositions().getAnnotationPositionsSubSet(sentencesRange.getStart(), sentencesRange.getEnd());
	}
	private void initGUI() throws SQLException, DatabaseLoadDriverException {
		GridBagLayout jPanelSelectRelationClueLayout = new GridBagLayout();
		jPanelSelectRelationClueLayout.rowWeights = new double[] {0.0, 0.0, 0.1};
		jPanelSelectRelationClueLayout.rowHeights = new int[] {7, 7, 7};
		jPanelSelectRelationClueLayout.columnWeights = new double[] {0.1, 0.0};
		jPanelSelectRelationClueLayout.columnWidths = new int[] {7, 7};
		this.setLayout(jPanelSelectRelationClueLayout);
		this.add(getJScrollPaneEditorPaneClueSelection(), new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getJButtonAddMoreText(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getJButtonAddLessRange(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
	}
	
	private JScrollPane getJScrollPaneEditorPaneClueSelection() throws SQLException, DatabaseLoadDriverException {
		if(jScrollPaneEditorPaneClueSelection == null) {
			jScrollPaneEditorPaneClueSelection = new JScrollPane();
			jScrollPaneEditorPaneClueSelection.setViewportView(getJEditorPaneSelectClue());
		}
		return jScrollPaneEditorPaneClueSelection;
	}
	
	private JButton getJButtonAddMoreText() {
		if(jButtonAddMoreText == null) {
			jButtonAddMoreText = new JButton();
			jButtonAddMoreText.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag+.png")));
			jButtonAddMoreText.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag+Pressed.png")));
			jButtonAddMoreText.setToolTipText("Zoom In");
			jButtonAddMoreText.setBorderPainted(false);
			jButtonAddMoreText.setContentAreaFilled(false);
			jButtonAddMoreText.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						zoomIn();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
		}
		return jButtonAddMoreText;
	}
	
	private void zoomIn() throws SQLException, DatabaseLoadDriverException {
		zoom ++;
		jEditorPaneSelectClue.setText(getHtmlStream());
	}

	private JButton getJButtonAddLessRange() {
		if(jButtonAddLessRange == null) {
			jButtonAddLessRange = new JButton();
			jButtonAddLessRange.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag-.png")));
			jButtonAddLessRange.setPressedIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/viewmag-Pressed.png")));
			jButtonAddLessRange.setToolTipText("Zoom Out");
			jButtonAddLessRange.setBorderPainted(false);
			jButtonAddLessRange.setContentAreaFilled(false);
			jButtonAddLessRange.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent evt) {
					try {
						zoomOut();
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
		    });
		}
		return jButtonAddLessRange;
	}
	
	private void zoomOut() throws SQLException, DatabaseLoadDriverException {
		zoom--;
		jEditorPaneSelectClue.setText(getHtmlStream());		
	}
	
	private JEditorPane getJEditorPaneSelectClue() throws SQLException, DatabaseLoadDriverException {
		if(jEditorPaneSelectClue == null) {
			jEditorPaneSelectClue = new JEditorPane();
			jEditorPaneSelectClue.setBackground(Color.WHITE);
			jEditorPaneSelectClue.setContentType("text/html");
			jEditorPaneSelectClue.setText(getHtmlStream());
			jEditorPaneSelectClue.setEditable(false);
		}
		return jEditorPaneSelectClue;
	}

	protected String getHtmlStream() throws SQLException, DatabaseLoadDriverException {
		String header = "<DIV STYLE =\"margin-left: 10px; margin-right: 5px; font-size:" + zoom + "pt\" align=\"justify\">";
		String text = curatorDocument.getAnnotatedDocument().getDocumentAnnotationTextHTML().substring(this.sentencesRange.getStart(), this.sentencesRange.getEnd());
		StringBuffer htmlText = new StringBuffer();
		int pointer=0;
		AnnotationPositions annotationsPositions = new AnnotationPositions();
		for(AnnotationPosition pos:this.clues.getAnnotations().keySet())
		{
			annotationsPositions.addAnnotationWhitConflicts(pos, this.clues.getAnnotations().get(pos));
		}
		for(AnnotationPosition pos:this.entities.getAnnotations().keySet())
		{
			annotationsPositions.addAnnotationWhitConflicts(pos, entities.getAnnotations().get(pos));
		}
		for(AnnotationPosition position : annotationsPositions.getAnnotations().keySet())
		{
			Integer difI = (int) position.getStart() - this.sentencesRange.getStart();
			Integer difS = (int) position.getEnd() - this.sentencesRange.getStart();
			IAnnotation annotation = annotationsPositions.getAnnotations().get(position);
			if(annotation instanceof IEventAnnotation)
			{
				
				if(pointer<difI)
					htmlText.append(text.substring((int) pointer , difI));		
				String clue;
				if(this.selectedRange!=null && position.equals(this.selectedRange))
				{
					clue = "<FONT COLOR=\""+OtherConfigurations.getVerbColor()+"\"><b bgcolor=\""+OtherConfigurations.getVerbColorBackGround()+"\">";
					htmlText.append(clue);
					htmlText.append(text.substring(difI, difS));
					htmlText.append("</b></FONT>");
				}
				else
				{
					clue = "<FONT COLOR=\""+OtherConfigurations.getVerbColor()+"\"><b><u>";
					htmlText.append(clue);
					htmlText.append(text.substring(difI, difS));
					htmlText.append("</u></b></FONT>");
				}
				pointer = difS;
			}
			else
			{
				IEntityAnnotation annot = (IEntityAnnotation) annotation;
				if(pointer<difI)
					htmlText.append(text.substring((int) pointer, difI));	
				String entity = new String();
				if(annot.getClassAnnotationID()>=0)
				{
					entity = "<FONT COLOR=" + CorporaProperties.getCorporaClassColor(annot.getClassAnnotationID()).getColor() + "><b>";
				}
				else
				{
					entity = AnnotationColors.getDefaultColor();
				}
				htmlText.append(entity);
				htmlText.append(text.substring(difI, difS));
				htmlText.append("</b></FONT>");
				pointer = difS;
			}
		}
		if(pointer<this.sentencesRange.getEnd()-this.sentencesRange.getStart())
			htmlText.append(text.substring((int) pointer,this.sentencesRange.getEnd()-this.sentencesRange.getStart()));
		htmlText.append("\n\t</body>\n</html>");
		return header + htmlText.toString();
	}
	
}
