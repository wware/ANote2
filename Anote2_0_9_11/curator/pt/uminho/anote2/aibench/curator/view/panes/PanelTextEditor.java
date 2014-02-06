package pt.uminho.anote2.aibench.curator.view.panes;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JEditorPane;

import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.gui.CorrectSelectionDialog;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.annotation.ner.EntityAnnotation;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.schema.DatabaseTablesName;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.Workbench;

public class PanelTextEditor {

	private NERCuratorPane documentView;
	public static int error=1;
	
	public PanelTextEditor(NERCuratorPane nerCuratorPane){
		this.documentView = nerCuratorPane;
	}
	
	
	public void addTagButtonActionPerformed(ActionEvent evt,int classID,boolean allselected,boolean caseSensitive,IResource<IResourceElement> dic) throws SQLException, DatabaseLoadDriverException, ManualCurationException{
		
		if(allselected)
		{
			addAllTerm(classID,dic,caseSensitive);
		}
		else
		{
			addOneTerm(classID,dic);
		}
	}


	private void addAllTerm(int classID,IResource<IResourceElement> dic, boolean caseSensitive) throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		JEditorPane editPanel = documentView.getEditPanel();
		CuratorDocument doc = documentView.getCuratorDocument();	
		synchronized(documentView.getEditPanel()){documentView.setSelectedText(editPanel.getSelectedText());}
		String selectedText = documentView.getSelectedText();
		if(selectedText==null)
			Workbench.getInstance().warn("Please select the text you want to tag! Ignoring...");
		else
		{
			IEntityAnnotation elem = new EntityAnnotation(-1,0,0,classID,0,selectedText,NormalizationForm.getNormalizationForm(selectedText));
			doc.addAllEntityAnnotations(elem,dic,caseSensitive);
			doc.rebuildHtml(documentView.getCuratorDocument().getCuratorView().getZoom());
			synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}	
			documentView.changed();
			documentView.refreshStatistics();
			synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlTextForEntities());}
			documentView.goToCaret(selectedText);
		}
	}
	
	private void addOneTerm(int classID,IResource<IResourceElement> dic) throws DatabaseLoadDriverException, SQLException, ManualCurationException {
		JEditorPane editPanel = documentView.getEditPanel();
		CuratorDocument doc = documentView.getCuratorDocument();		
		synchronized(documentView.getEditPanel()){documentView.setSelectedText(editPanel.getSelectedText());}
		int start = editPanel.getSelectionStart()-error;
		int end = editPanel.getSelectionEnd()-error;
		AnnotationPosition posC = new AnnotationPosition(start,end);
		String selectedText = documentView.getSelectedText();
		if(selectedText==null)
			Workbench.getInstance().warn("Please select the text you want to tag! Ignoring...");
		else
		{
			int nextAnnotationID = HelpDatabase.getNextInsertTableID(DatabaseTablesName.annotation);
			IEntityAnnotation elem = new EntityAnnotation(nextAnnotationID,posC.getStart(),posC.getEnd(),classID,0,selectedText,NormalizationForm.getNormalizationForm(selectedText));
			doc.addOneEntityAnnotation(posC, elem,dic);
			doc.rebuildHtml(documentView.getCuratorDocument().getCuratorView().getZoom());
			synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}	
			documentView.changed();
			doc.getAnnotations().addAnnotation(elem,ClassProperties.getClassIDClass().get(classID));
			documentView.refreshStatistics();
			synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlTextForEntities());}
			documentView.goToCaret(selectedText);
		}
	}
	
	public void removeTagButtonActionPerformed(ActionEvent evt,boolean allselected,boolean casesensitive, IResource<IResourceElement> look) throws DatabaseLoadDriverException, SQLException, ManualCurationException {
		if(allselected)
		{
			removeAllOption(casesensitive,look);
		}
		else
		{
			removeOneOption();
		}
	}

	private void removeOneOption() throws SQLException, DatabaseLoadDriverException, ManualCurationException {
		JEditorPane editPanel = documentView.getEditPanel();
		synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
		int start = editPanel.getSelectionStart()-error;
		int end = editPanel.getSelectionEnd()-error;
		CuratorDocument doc = documentView.getCuratorDocument();
		String term = documentView.getSelectedText();
		if(term==null)
		{
			Workbench.getInstance().warn("Please select the annotation you want to remove! Ignoring...");
		}
		documentView.changed();	
		AnnotationPosition position = new AnnotationPosition(start,end);
		doc.removeEntityAnnotation(position);
		doc.rebuildHtml(documentView.getCuratorDocument().getCuratorView().getZoom());
		doc.getAnnotations().removeOneAnnotation(term);
		synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
		documentView.refreshStatistics();
		synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlTextForEntities());}
		documentView.goToCaret(term);
	}

	private void removeAllOption(boolean casesensitive, IResource<IResourceElement> look) throws DatabaseLoadDriverException, SQLException, ManualCurationException {
		JEditorPane editPanel = documentView.getEditPanel();
		synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
		CuratorDocument doc = documentView.getCuratorDocument();
		String term = documentView.getSelectedText();
		int start = editPanel.getSelectionStart()-error;
		int end = editPanel.getSelectionEnd()-error;
		AnnotationPosition position = new AnnotationPosition(start,end);
		if(term==null&&doc.getNERAnnotations().getAnnotations().containsKey(position))
		{
			Workbench.getInstance().warn("Please select the text you want to remove! Ignoring...");
		}
		documentView.changed();	
		List<AnnotationPosition> list = doc.removeAllEntityAnnotations(term,look,casesensitive);
		if(casesensitive)
		{
			doc.getAnnotations().removeAllAnnotation(term);
		}
		else
		{
			Set<String> terms = new HashSet<String>();
			for(AnnotationPosition pos: list)
			{
				terms.add(pos.getOriginalText());
			}
			for(String terPut :terms)
			{
				doc.getAnnotations().removeAllAnnotation(terPut);
			}
		}
		doc.rebuildHtml(documentView.getCuratorDocument().getCuratorView().getZoom());
		synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
		documentView.refreshStatistics();
		synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlTextForEntities());}
		documentView.goToCaret(term);
	}

	public synchronized void applyCorrectSelectionButtonActionPerfomed(ActionEvent evt, CorrectSelectionDialog correctSelectionForm,boolean allselected,boolean casesensitive,IResource<IResourceElement> look) throws DatabaseLoadDriverException, SQLException, ManualCurationException{

		String newClass = correctSelectionForm.getSelectedClassText();
		JEditorPane editPanel = documentView.getEditPanel();
		if(newClass==null)
			Workbench.getInstance().warn("Please select a new class for the term!");
		else
		{
			int start = editPanel.getSelectionStart()-error;
			int end = editPanel.getSelectionEnd()-error;
			AnnotationPosition position = new AnnotationPosition(start,end);
			CuratorDocument doc = documentView.getCuratorDocument();
			synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
			String term = documentView.getSelectedText();
			synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
			documentView.changed();
			correctSelectionForm.setEnabled(false);
			correctSelectionForm.setVisible(false);
			int classID = ClassProperties.getClassClassID().get(newClass);
			if(doc.getNERAnnotations().containsKey(position))
			{
				int numberTiems = 0;
				if(allselected)
				{
					Map<String,Integer> termcOUNT = new  HashMap<String, Integer>();
					List<AnnotationPosition> list = documentView.getCuratorDocument().updateAllEntiyAnnotations(term,classID,casesensitive,look);
					if(!casesensitive)
					{
						for(AnnotationPosition pos: list)
						{
							if(!termcOUNT.containsKey(pos.getOriginalText()))
							{
								termcOUNT.put(pos.getOriginalText(), 0);
							}
							int number = termcOUNT.get(pos.getOriginalText());
							termcOUNT.put(pos.getOriginalText(), ++number);
						}
						for(String terPut :termcOUNT.keySet())
						{
							doc.getAnnotations().correctAnnotation(terPut, newClass,0, termcOUNT.get(terPut));
						}
					}
					else
					{
						numberTiems = list.size();
					}

				}
				else
				{
					numberTiems = 1;
					documentView.getCuratorDocument().updateOneEntityAnnotation(position, classID);
				}
				doc.getAnnotations().correctAnnotation(term, newClass,0, numberTiems);
				doc.rebuildHtml(documentView.getCuratorDocument().getCuratorView().getZoom());
				synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
				documentView.refreshStatistics();
				synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlTextForEntities());}
				documentView.goToCaret(term);
			}
			else
			{
				Workbench.getInstance().warn("Please select an annotation in text...Ignoring.");
			}
		}
	}
	
	

	
}
