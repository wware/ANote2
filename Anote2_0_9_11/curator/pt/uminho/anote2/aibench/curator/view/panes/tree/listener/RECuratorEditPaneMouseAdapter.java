package pt.uminho.anote2.aibench.curator.view.panes.tree.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import pt.uminho.anote2.aibench.corpus.datatypes.REDocumentAnnotation;
import pt.uminho.anote2.aibench.curator.datastructures.document.CuratorDocument;
import pt.uminho.anote2.aibench.curator.gui.MultipleRelationsCuratorView;
import pt.uminho.anote2.aibench.curator.gui.RelationAddGUI;
import pt.uminho.anote2.aibench.curator.view.panes.PanelTextEditor;
import pt.uminho.anote2.aibench.curator.view.panes.RECuratorPane;
import pt.uminho.anote2.aibench.utils.exceptions.treat.TreatExceptionForAIbench;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.annotation.AnnotationPosition;
import pt.uminho.anote2.datastructures.exceptions.process.manualcuration.ManualCurationException;
import pt.uminho.anote2.datastructures.exceptions.process.reprocess.RelationDelimiterExeption;

public class RECuratorEditPaneMouseAdapter extends MouseAdapter{
	
	private RECuratorPane recuratorPane;
	private CuratorDocument curatorDocument;
	
	public RECuratorEditPaneMouseAdapter(RECuratorPane recuratorPane,CuratorDocument curatorDocument)
	{
		this.recuratorPane = recuratorPane;
		this.curatorDocument = curatorDocument;
	}
	
	public void mouseClicked(MouseEvent evt) {
		// Double Click
		if (evt.getClickCount() == 2) {
			try {
				showAssociatedRelations();
			} catch (BadLocationException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (ManualCurationException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (RelationDelimiterExeption e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
		// Right button Click
		else if(SwingUtilities.isRightMouseButton(evt))
		{
			try {
				showMenuOptions();
			} catch (SQLException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (DatabaseLoadDriverException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			} catch (BadLocationException e) {
				TreatExceptionForAIbench.treatExcepion(e);
			}
		}
		else
		{
			recuratorPane.getEditPanel().getHighlighter().removeAllHighlights();
		}
		
	}

	private void showMenuOptions() throws SQLException,
			DatabaseLoadDriverException, BadLocationException {
		recuratorPane.getEditPanel().getHighlighter().removeAllHighlights();
		int start = recuratorPane.getEditPanel().getSelectionStart()-PanelTextEditor.error;
		int end = recuratorPane.getEditPanel().getSelectionEnd()-PanelTextEditor.error;
		recuratorPane.getEditPanel().getHighlighter().addHighlight(start+1, end+1,DefaultHighlighter.DefaultPainter);
		final AnnotationPosition position = new AnnotationPosition(start,end);
		// If position is an clue
		if(((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEventAnnotationPositions().containsKey(position))
		{
			JPopupMenu menu = new JPopupMenu ();
			if(((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getVerbRelations().get(position).size()>0)
			{
				JMenuItem viewRelations = new JMenuItem("Add Relation with Clue");
				viewRelations.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						new RelationAddGUI(recuratorPane.getCuratorDocument(), position, true);
					}
				});
				menu.add(viewRelations);
				menu.addSeparator();
				viewRelations = new JMenuItem("View Clue Relations");
				viewRelations.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						try {
							showClueRelations(position);
						} catch (ManualCurationException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (RelationDelimiterExeption e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
				menu.add(viewRelations);
				menu.addSeparator();
				JMenuItem removeClue = new JMenuItem("Remove Clue (All Relations)");
				removeClue.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						try {
							curatorDocument.removeAllEventFromClue(position);
						} catch (ManualCurationException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
				menu.add(removeClue);		
			}
            menu.show(recuratorPane.getEditPanel(),recuratorPane.getEditPanel().getMousePosition().x,recuratorPane.getEditPanel().getMousePosition().y);
		}
		// If position is an entity
		else if(((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityRelationSet().containsKey(position))
		{
			JPopupMenu menu = new JPopupMenu ();
			if(((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityRelationSet().get(position).size()>0)
			{
				menu.addSeparator();
				JMenuItem viewRelations = new JMenuItem("View Entity Relations");
				viewRelations.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						try {
							showEntitiesRelations(position);
						} catch (ManualCurationException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (RelationDelimiterExeption e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
				menu.add(viewRelations);
				menu.addSeparator();
				JMenuItem removeClue = new JMenuItem("Remove All Relations associated to Entity");
				removeClue.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent evt) {
						try {
							curatorDocument.removeAllEntityEventFromClue(position);
						} catch (ManualCurationException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (SQLException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						} catch (DatabaseLoadDriverException e) {
							TreatExceptionForAIbench.treatExcepion(e);
						}
					}
				});
				menu.add(removeClue);		
			}
            menu.show(recuratorPane.getEditPanel(),recuratorPane.getEditPanel().getMousePosition().x,recuratorPane.getEditPanel().getMousePosition().y);
		}
		// free position
		else
		{
			JPopupMenu menu = new JPopupMenu ();
			JMenuItem addRelation = new JMenuItem("Add Relation (Selected Section)");
			addRelation.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					new RelationAddGUI(recuratorPane.getCuratorDocument(), position, false);
				}
			});
			menu.add(addRelation);
			JMenuItem viewRelations = new JMenuItem("View Relations (Selected Section)");
			viewRelations.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						showPositionRangeRelations(position);
					} catch (ManualCurationException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (RelationDelimiterExeption e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			menu.add(viewRelations);
			menu.addSeparator();
			JMenuItem removeClue = new JMenuItem("Remove All Relations (Selected Section)");
			removeClue.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
					try {
						curatorDocument.removeAllEventFromRange(position);
					} catch (ManualCurationException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (SQLException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					} catch (DatabaseLoadDriverException e) {
						TreatExceptionForAIbench.treatExcepion(e);
					}
				}
			});
			menu.add(removeClue);	
            menu.show(recuratorPane.getEditPanel(),recuratorPane.getEditPanel().getMousePosition().x,recuratorPane.getEditPanel().getMousePosition().y);

		}
	}



	protected void showPositionRangeRelations(AnnotationPosition position) throws ManualCurationException, SQLException, DatabaseLoadDriverException, RelationDelimiterExeption {		
		Set<IEventAnnotation> relations = curatorDocument.getEventFromSubSet(position);
		new MultipleRelationsCuratorView(curatorDocument,position,relations );	
	}

	/**
	 * 
	 * @throws BadLocationException
	 * @throws SQLException
	 * @throws DatabaseLoadDriverException
	 * @throws ManualCurationException 
	 * @throws RelationDelimiterExeption 
	 */
	private void showAssociatedRelations() throws  SQLException, DatabaseLoadDriverException, BadLocationException, ManualCurationException, RelationDelimiterExeption {
		int startPointer = recuratorPane.getEditPanel().getSelectionStart();
		// Get From Clue List
		AnnotationPosition positionClue = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEventAnnotationPositions().getAnnotationPositionByInsideIndex(startPointer);
		// Get From Entity List
		AnnotationPosition positionEntity = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityAnnotationPositions().getAnnotationPositionByInsideIndex(startPointer);
		if(positionClue!=null)
		{
			recuratorPane.getEditPanel().getHighlighter().addHighlight(positionClue.getStart()+1, positionClue.getEnd()+1,DefaultHighlighter.DefaultPainter);
			showClueRelations(positionClue);
		}
		else if(positionEntity!=null)
		{
			recuratorPane.getEditPanel().getHighlighter().addHighlight(positionEntity.getStart()+1, positionEntity.getEnd()+1,DefaultHighlighter.DefaultPainter);
			showEntitiesRelations(positionEntity);
		}
	}

	private void showEntitiesRelations(AnnotationPosition positionEntity)
			throws ManualCurationException, RelationDelimiterExeption, SQLException, DatabaseLoadDriverException {
		Set<IEventAnnotation> relations;
		if(((((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityRelationSet().containsKey(positionEntity))))
		{
			relations = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getEntityRelationSet().get(positionEntity);
			if(relations.size()>0)
			{
				// Show all relations
				new MultipleRelationsCuratorView(curatorDocument,positionEntity, relations);	
			}
		}
		else
			throw new ManualCurationException("Note: Entity Annotation does not has Event/Event Associated ");
	}

	private void showClueRelations(AnnotationPosition positionClue)
			throws ManualCurationException, RelationDelimiterExeption, SQLException, DatabaseLoadDriverException {
		Set<IEventAnnotation> relations;
		if(((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getVerbRelations().containsKey(positionClue))
		{
			relations = ((REDocumentAnnotation)curatorDocument.getAnnotatedDocument()).getVerbRelations().get(positionClue);
			if(relations.size()>0)
			{
				// Show all relations
				new MultipleRelationsCuratorView(curatorDocument,positionClue, relations);	
			}
		}
		else
			throw new ManualCurationException("Note: Annotation does not has Event/Event Associated ");
	}

}
