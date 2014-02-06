package pt.uminho.anote2.relation.datastructures;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.datastructures.utils.GenericPair;
import pt.uminho.anote2.datastructures.utils.GenericTriple;

public class POSTaggerHelp {
	
	public static List<GenericPair<Long, Long>> getGateDocumentSentencelimits(Document doc, Long long1, Long long2) {
		List<GenericPair<Long,Long>> sentenceLimits = new ArrayList<GenericPair<Long,Long>>();
		AnnotationSet annotSetSentences = doc.getAnnotations();
		AnnotationSet annotSetSentences2 = annotSetSentences.get(long1, long2).get("Sentence");
		for(Annotation annot:annotSetSentences2)
		{
			sentenceLimits.add(new GenericPair<Long, Long>(annot.getStartNode().getOffset(),annot.getEndNode().getOffset()));
		}
		return sentenceLimits;
	}
	
	public static List<GenericTriple<Long, Long,Integer>> getGateDocumentlimits(Document doc) {
		List<GenericTriple<Long,Long,Integer>> sentenceLimits = new ArrayList<GenericTriple<Long,Long,Integer>>();
		AnnotationSet annotSetSentences = doc.getAnnotations("Original markups");
		AnnotationSet annotSetSentences2 = annotSetSentences.get("Doc");
		for(Annotation annot:annotSetSentences2)
		{
			String idS = (String) annot.getFeatures().get("id");
			int id = Integer.valueOf(idS);
			sentenceLimits.add(new GenericTriple<Long, Long,Integer>(annot.getStartNode().getOffset(),annot.getEndNode().getOffset(),id));
		}
		return sentenceLimits;
	}

}
