package pt.uminho.anote2.aibench.curator.gui;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;

import pt.uminho.anote2.aibench.corpus.utils.AnnotationPosition;
import pt.uminho.anote2.aibench.curator.datastructures.ANoteDocument;
import pt.uminho.anote2.aibench.curator.view.ANoteDocumentView;
import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.annotation.EntityAnnotation;
import pt.uminho.anote2.datastructures.textprocessing.NormalizationForm;
import pt.uminho.anote2.resource.IResource;
import pt.uminho.anote2.resource.IResourceElement;
import es.uvigo.ei.aibench.workbench.Workbench;

public class PanelTextEditor {

	private ANoteDocumentView documentView;
	public static int error=1;
	
	public PanelTextEditor(ANoteDocumentView documentView){
		this.documentView = documentView;
	}
	
	
	public void addTagButtonActionPerformed(ActionEvent evt,int classID,boolean allselected,IResource<IResourceElement> dic){
		
		if(allselected)
		{
			addAllTerm(classID,dic);
		}
		else
		{
			addOneTerm(classID,dic);
		}
	}


	private void addAllTerm(int classID,IResource<IResourceElement> dic) {
		JEditorPane editPanel = documentView.getEditPanel();
		ANoteDocument doc = documentView.getAiDocument().getDocument();		
		synchronized(documentView.getEditPanel()){documentView.setSelectedText(editPanel.getSelectedText());}
		String selectedText = documentView.getSelectedText();
		if(selectedText==null)
			Workbench.getInstance().warn("Please select the text you want to tag! Ignoring...");
		else
		{
			IEntityAnnotation elem = new EntityAnnotation(-1,0,0,classID,0,selectedText,NormalizationForm.getNormalizationForm(selectedText));
			if(doc.addAllAnnotation(elem,dic))
			{
				doc.rebuildHtml(documentView.getZoom());
				synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}	
				documentView.changed();
				documentView.refreshStatistics();
				synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlText());}
				documentView.goToCaret(selectedText);
			}
			else
				Workbench.getInstance().warn("This term is already annotated! Ignoring...");
		}
	}
	
	private void addOneTerm(int classID,IResource<IResourceElement> dic) {
		JEditorPane editPanel = documentView.getEditPanel();
		ANoteDocument doc = documentView.getAiDocument().getDocument();		
		synchronized(documentView.getEditPanel()){documentView.setSelectedText(editPanel.getSelectedText());}
		int start = editPanel.getSelectionStart()-error;
		int end = editPanel.getSelectionEnd()-error;
		AnnotationPosition posC = new AnnotationPosition(start,end);
		String selectedText = documentView.getSelectedText();
		if(selectedText==null)
			Workbench.getInstance().warn("Please select the text you want to tag! Ignoring...");
		else
		{
			IEntityAnnotation elem = new EntityAnnotation(-1,0,0,classID,0,selectedText,NormalizationForm.getNormalizationForm(selectedText));
			if(doc.addOneAnnotation(posC, elem,dic))
			{
				doc.rebuildHtml(documentView.getZoom());
				synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}	
				documentView.changed();
				doc.getAnnotations().addAnnotation(elem,documentView.getAiDocument().getDocument().getProperties().getClassIDclass().get(classID));
				documentView.refreshStatistics();
				synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlText());}
				documentView.goToCaret(selectedText);
			}
			else
				Workbench.getInstance().warn("This term is already annotated! Ignoring...");
		}
	}
	
	public void removeTagButtonActionPerformed(ActionEvent evt,boolean allselected) {
		if(allselected)
		{
			removeAllOption(documentView.getZoom());
		}
		else
		{
			removeOneOption(documentView.getZoom());
		}
	}


	private void removeAllOption(int zoom) {
		JEditorPane editPanel = documentView.getEditPanel();
		synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
		ANoteDocument doc = documentView.getAiDocument().getDocument();
		String term = documentView.getSelectedText();
		int start = editPanel.getSelectionStart()-error;
		int end = editPanel.getSelectionEnd()-error;
		AnnotationPosition position = new AnnotationPosition(start,end);
		if(term==null&&doc.getAnnotNERAnnotations().getAnnotations().containsKey(position))
		{
			Workbench.getInstance().warn("Please select the text you want to remove! Ignoring...");
		}
		documentView.changed();	
		{
			if(doc.removeByTerm(term))
			{
				doc.rebuildHtml(zoom);
				doc.getAnnotations().removeAllAnnotation(term);
				synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
				documentView.refreshStatistics();
				synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlText());}
				documentView.goToCaret(term);
			}
			else
			{
				Workbench.getInstance().warn("Term not fund in annotations! Ignoring...");
			}
		}
	}
	
	private void removeOneOption(int zoom) {
		JEditorPane editPanel = documentView.getEditPanel();
		synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
		int start = editPanel.getSelectionStart()-error;
		int end = editPanel.getSelectionEnd()-error;
		ANoteDocument doc = documentView.getAiDocument().getDocument();
		String term = documentView.getSelectedText();
		if(term==null)
		{
			Workbench.getInstance().warn("Please select the text you want to annotate! Ignoring...");
		}
		documentView.changed();	
		AnnotationPosition position = new AnnotationPosition(start,end);
		{
			if(doc.removeByPosition(position))
			{
				doc.rebuildHtml(zoom);
				doc.getAnnotations().removeOneAnnotation(term);
				synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
				documentView.refreshStatistics();
				synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlText());}
				documentView.goToCaret(term);
			}
			else
			{
				Workbench.getInstance().warn("Nothing to remove! Ignoring...");
			}
		}
	}
	
	public synchronized void applyCorrectSelectionButtonActionPerfomed(ActionEvent evt, CorrectSelectionDialog correctSelectionForm,boolean allselected,IResource<IResourceElement> dic){

		String newClass = correctSelectionForm.getSelectedClass();
		JEditorPane editPanel = documentView.getEditPanel();
		if(newClass==null)
			Workbench.getInstance().warn("Please select a new class for the term!");
		else
		{
			int start = editPanel.getSelectionStart()-error;
			int end = editPanel.getSelectionEnd()-error;
			AnnotationPosition position = new AnnotationPosition(start,end);
			ANoteDocument doc = documentView.getAiDocument().getDocument();
			synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
			String term = documentView.getSelectedText();
			synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
			documentView.changed();
			correctSelectionForm.setEnabled(false);
			correctSelectionForm.setVisible(false);
			int classID = documentView.getAiDocument().getDocument().getProperties().getClassClassID().get(newClass);
			if(doc.getAnnotNERAnnotations().containsKey(position))
			{
				int numberTiems;
				if(allselected)
				{
					numberTiems = documentView.getAiDocument().getDocument().updateAllTerm(term,classID);
				}
				else
				{
					numberTiems = documentView.getAiDocument().getDocument().updateOneTerm(position, classID);
				}
				if(numberTiems>0)
					doc.getAnnotations().correctAnnotation(term, newClass,0, numberTiems, doc);
				doc.rebuildHtml(documentView.getZoom());
				synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
				documentView.refreshStatistics();
				synchronized(editPanel){editPanel.setText(doc.getDocumentText().getHtmlText());}
				documentView.goToCaret(term);
				
			}
			else
			{
				Workbench.getInstance().warn("Please select an annotation in text...Ignoring!!!");
			}
		}
	}
	
	public void addRelationButtonActionPerfomed(ActionEvent evt){
		
//		JEditorPane editPanel = documentView.getEditPanel();
//		synchronized(editPanel){documentView.setSelectedText(editPanel.getSelectedText());}
//		String text = documentView.getSelectedText();
//		
//		if(text==null)
//			Workbench.getInstance().warn("Please select the text you want to tag! Ignoring...");
//		else
//		{
//			synchronized(editPanel){documentView.setCaretPosition(editPanel.getCaretPosition());}
//			ANoteDocument doc = documentView.getAiDocument().getDocument();		
//			documentView.changed();					
//			//String selectedHtml = doc.getDocumentText().captureAnnotations(text, doc.getAnnotations(),documentView.getAiDocument().getDocument().getProperties().getTasks());	
//			String htmlText = doc.getDocumentText().getHtmlText();			
//			//htmlText = htmlText.replaceAll("[ ]{0,1}" + ParsingUtils.textToRegExp(selectedHtml) + "[ ]{0,1}", "<FONT style=\"BACKGROUND-COLOR: yellow\"> " + selectedHtml + " </FONT>");	
//			doc.getDocumentText().setHtmlText(htmlText);
//			synchronized(editPanel){editPanel.setText(htmlText);}	
//			doc.getAnnotations().addRelation(text, true);
//			documentView.goToCaret(text);
//		}
	}
	
	

	
}
