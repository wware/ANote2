	package pt.uminho.anote2.core.document;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.core.annotation.IEventAnnotation;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.process.IProcess;

/**
 * This interface implements a interface for representing as Annotated Document for {@link IEProcess}
 * 
 * @author Hugo Costa
 * 
 *
 */
public interface IAnnotatedDocument extends IPublication{
	
	/**
	 * Method that return all {@link IEntityAnnotation} for document
	 * 
	 * @return List<IEntityAnnotation>
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public List<IEntityAnnotation> getEntitiesAnnotations() throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * Method that return all {@link IEventAnnotation} annotation for document
	 * 
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public List<IEventAnnotation> getEventAnnotations() throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * return {@link IProcess} associated
	 * @return
	 */
	public IProcess getProcess();

	
	/**
	 * Return {@link ICorpus} associated to {@link IAnnotatedDocument}
	 * @return
	 */
	public ICorpus getCorpus();
	
	/**
	 * Return Document list of {@link ISentence}
	 * 
	 * @return
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public List<ISentence> getSentencesText() throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * Return document annotated text ( could be diferent of original)
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public String getDocumetAnnotationText() throws SQLException, DatabaseLoadDriverException;
	
	/**
	 * Return document annotated text with html tags in {@link IEntityAnnotation} and {@link IEventAnnotation}
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	public String getDocumentAnnotationTextHTML() throws SQLException, DatabaseLoadDriverException;
	
}
