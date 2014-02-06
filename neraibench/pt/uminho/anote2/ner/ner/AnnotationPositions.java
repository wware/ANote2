package pt.uminho.anote2.ner.ner;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.annotation.IEntityAnnotation;
import pt.uminho.anote2.datastructures.utils.OrderedMap;
import pt.uminho.anote2.ner.annotations.AnnotationPosition;

public class AnnotationPositions {

	private OrderedMap<AnnotationPosition, IEntityAnnotation> annotations;
	
	public AnnotationPositions(){
		annotations = new OrderedMap<AnnotationPosition, IEntityAnnotation>(new PositionComparator<AnnotationPosition>());
	}
	
	public boolean addAnnotation(AnnotationPosition position, IEntityAnnotation info){
		for(AnnotationPosition p : annotations.keySet())
			if(p.contain(position))
				return false;
		annotations.put(position, info);
		return true;
	}
	
	public boolean addAnnotationNER2(AnnotationPosition position, IEntityAnnotation info){
//		for(AnnotationPosition p : annotations.keySet())
//			if(p.contain(position))
//				return false;
//		annotations.put(position, info);
//		return true;
		boolean insert = false;
		boolean insertNotContais=true;
		List<AnnotationPosition> toRemove = new ArrayList<AnnotationPosition>();
		for(AnnotationPosition p : annotations.keySet())
		{
			if(p.contain(position))
			{
				insertNotContais=false;
				int lenghtP = p.lenght();
				int lenghtPosition = position.lenght();
				if(lenghtPosition>lenghtP)
				{
					toRemove.add(p);
					if(insert)
					{
						annotations.put(position, info);
						insert=true;
					}
				}
			}
		}
		for(AnnotationPosition pos:toRemove)
		{
			annotations.remove(pos);
		}
		if(insertNotContais)
		{
			annotations.put(position, info);
		}
		return insert;
	}

	public OrderedMap<AnnotationPosition, IEntityAnnotation> getAnnotations() {return annotations;}
	public void setAnnotations(OrderedMap<AnnotationPosition, IEntityAnnotation> annotations) {this.annotations = annotations;}
}
