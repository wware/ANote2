package pt.uminho.anote2.aibench.curator.gui.help;

import java.sql.SQLException;

import javax.swing.JPopupMenu;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.corpus.structures.CorporaProperties;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.core.annotation.IAnnotation;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.AnnotationPositions;
import pt.uminho.anote2.datastructures.annotation.properties.AnnotationColors;
import pt.uminho.anote2.datastructures.annotation.re.EventAnnotation;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.utils.conf.OtherConfigurations;

public class SentenceAnnotationPaneExtention extends SentenceAnnotationPane{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int error = 1;
	private boolean manualAdded = false;
	private boolean selectedClue;
	
	public SentenceAnnotationPaneExtention(CuratorDocument curatorDocument,
			AnnotationPosition sentencesRange, AnnotationPosition selectedRange)
			throws SQLException, DatabaseLoadDriverException {
		super(curatorDocument, sentencesRange, selectedRange);
		manualAdded = false;
		selectedClue = false;
	}

	public void addMenuToJeditor(JPopupMenu menu)
	{
		jEditorPaneSelectClue.setComponentPopupMenu(menu);
	}
	
	public void changeHtml() throws SQLException, DatabaseLoadDriverException
	{
		jEditorPaneSelectClue.setText(getHtmlStream());
	}

	public void validateSelection() throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		int start = jEditorPaneSelectClue.getSelectionStart()-error ;
		int end = jEditorPaneSelectClue.getSelectionEnd()-error;
		if(start>=0 && end>0)
		{
			start = start+sentencesRange.getStart();
			end = end + sentencesRange.getStart();
			AnnotationPosition newClue = new AnnotationPosition(start, end);
			REDocumentAnnotation reDoc = (REDocumentAnnotation) this.curatorDocument.getAnnotatedDocument();
			if(selectedRange==null || selectedRange!=null  && !selectedRange.equals(newClue))
			{
				if(reDoc.getVerbRelations().containsKey(newClue))
				{
					selectedRange = newClue;
					selectedClue = true;
				}
				else if(reDoc.getEventAnnotationPositions().containsConflits(newClue))
				{
					throw new ManualCurationException("notes: Select Clue have conflis with another clues");
				}
				else if(reDoc.getEntityAnnotationPositions().containsConflits(newClue))
				{
					throw new ManualCurationException("notes: Selected Clue have conflis with Entity Annotation");
				}
				else
				{
					manualAdded = true;
					selectedRange = newClue;
					selectedClue = true;
				}
			}
		}
		else
		{
			throw new ManualCurationException("notes: Selected new Clue in text");
		}
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
		if(manualAdded)
		{
			annotationsPositions.addAnnotationWhitConflicts(selectedRange,new EventAnnotation(-1, selectedRange.getStart(), selectedRange.getEnd(), "re") );

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

	public void removeMenuFromEditorPane()
	{
		jEditorPaneSelectClue.setComponentPopupMenu(null);
	}
	
	public AnnotationPosition getSelectClueAnnotation() {
		return selectedRange;
	}

	public boolean isselectedClue() {	// TODO Auto-generated method stub
		return selectedClue;
	}

}
