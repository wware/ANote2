package pt.uminho.anote2.datastructures.process.io.export;

import pt.uminho.anote2.process.IE.ner.export.INERCSVColumns;

public class NERCSVColumn implements INERCSVColumns{

	private int annotationIDColumn;
	private int publicationIDColumn;
	private int elementColumn;
	private int classColumn;
	private int resorcesIDColumn;
	private int startOffset;
	private int endOffset;
	private int resourceInformation;
	private int resourceExternalIds;
	
	
	public NERCSVColumn(int annotationIDColumn, int publicationIDColumn,int elementColumn, int classColumn, int resorcesIDColumn,
			int startOffset, int endOffset,int resourceInformation,int resourceExternalIds) {
		super();
		this.annotationIDColumn = annotationIDColumn;
		this.publicationIDColumn = publicationIDColumn;
		this.elementColumn = elementColumn;
		this.classColumn = classColumn;
		this.resorcesIDColumn = resorcesIDColumn;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.resourceInformation = resourceInformation;
		this.resourceExternalIds = resourceExternalIds;
	}

	@Override
	public int getAnnotationIDColumn() {
		return annotationIDColumn;
	}

	public int getResourceInformation() {
		return resourceInformation;
	}

	public int getResourceExternalIDs() {
		return resourceExternalIds;
	}

	@Override
	public int getPublicationIDColumn() {
		return publicationIDColumn;
	}

	@Override
	public int getElementColumn() {
		return elementColumn;
	}

	@Override
	public int getClassColumn() {
		return classColumn;
	}

	@Override
	public int getResourceIDColumn() {
		return resorcesIDColumn;
	}

	@Override
	public int getStartOffset() {
		return startOffset;
	}

	@Override
	public int getEndOffset() {
		return endOffset;
	}

}
