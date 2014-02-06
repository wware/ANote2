package pt.uminho.anote2.datastructures.annotation.log;

/**
 * Type of Log Annotations (Also associated with Database Table Annotation_log column type)
 * 
 * @author Hugo Costa
 *
 */
public enum AnnotationLogTypeEnum {
	ENTITYADD,
	ENTITYREMOVE,
	ENTITYUPDATE,
	RELATIONADD,
	RELATIONREMOVE,
	RELATIONUPDATE
}
