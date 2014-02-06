package pt.uminho.anote2.aibench.corpus.utils;

import pt.uminho.anote2.core.annotation.IAnnotation;


public class AnnotationPositions implements Cloneable{

	private OrderedMap<AnnotationPosition,IAnnotation> annotations;
	
	public AnnotationPositions(){
		annotations = new OrderedMap<AnnotationPosition,IAnnotation>(new PositionComparator<AnnotationPosition>());
	}
	
	public boolean addAnnotationWhitConflicts(AnnotationPosition position, IAnnotation entityAnnotaion){
		for(AnnotationPosition p : annotations.keySet())
			if(p.contain(position))
				return false;
		annotations.put(position, entityAnnotaion);
		return true;
	}
	
	public boolean addAnnotationWhitoutConflicts(AnnotationPosition position, IAnnotation entityAnnotaion){
		for(AnnotationPosition p : annotations.keySet())
			if(p.contain(position))
				return false;
		annotations.put(position, entityAnnotaion);
		return true;
	}
	
	
	public boolean removeAnnotation(AnnotationPosition position)
	{
		if(annotations.containsKey(position))
		{
			annotations.remove(position);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public AnnotationPositions clone()
	{
		AnnotationPositions position = new AnnotationPositions();
		for(AnnotationPosition p : annotations.keySet())
		{
			position.addAnnotationWhitConflicts(p.clone(),annotations.get(p).clone());
		}
		return position;
	}
	
	public boolean containsKey(Object key)
	{
		if(key instanceof AnnotationPosition)
		{
			AnnotationPosition posCom = (AnnotationPosition) key;
			for(AnnotationPosition pos:annotations.keySet())
			{
				if(pos.equals(posCom))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public IAnnotation get(Object key)
	{
		if(key instanceof AnnotationPosition)
		{
			AnnotationPosition posCom = (AnnotationPosition) key;
			for(AnnotationPosition pos:annotations.keySet())
			{
				if(pos.equals(posCom))
				{
					IAnnotation elem = annotations.get(pos);
					return elem;
				}
			}
		}
		return null;
	}

	public OrderedMap<AnnotationPosition, IAnnotation> getAnnotations() {return annotations;}
	public void setAnnotations(OrderedMap<AnnotationPosition, IAnnotation> annotations) {this.annotations = annotations;}
}
