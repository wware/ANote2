package pt.uminho.anote2.datastructures.database.queries.process;

public class QueriesIEProcess {

	public static final String selectAnnotationFilterByOffset = "SELECT idannotations,start,end,element,resource_elements_id,normalization_form,classes_idclasses " +
			"FROM annotations "+ 
			"WHERE annotations.processes_idprocesses=? AND annotations.corpus_idcorpus=? AND annotations.publications_id=? AND type='ner' AND start >=? AND start<=? ";


}
