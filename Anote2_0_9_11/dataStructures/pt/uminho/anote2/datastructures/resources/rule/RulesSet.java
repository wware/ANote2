package pt.uminho.anote2.datastructures.resources.rule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.Set;

import org.apache.log4j.Logger;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.core.report.resources.ConflitsType;
import pt.uminho.anote2.core.report.resources.IInsertConflits;
import pt.uminho.anote2.core.report.resources.IResourceMergeReport;
import pt.uminho.anote2.datastructures.database.HelpDatabase;
import pt.uminho.anote2.datastructures.database.queries.resources.rules.QueriesRules;
import pt.uminho.anote2.datastructures.general.ClassProperties;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeConflits;
import pt.uminho.anote2.datastructures.report.resources.ResourceMergeReport;
import pt.uminho.anote2.datastructures.resources.ResourceElement;
import pt.uminho.anote2.datastructures.resources.ResourceElementSet;
import pt.uminho.anote2.datastructures.resources.ResourcesHelp;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.datastructures.utils.conf.GlobalNames;
import pt.uminho.anote2.datastructures.utils.conf.GlobalOptions;
import pt.uminho.anote2.datastructures.utils.conf.GlobalTablesName;
import pt.uminho.anote2.resource.IResourceElement;
import pt.uminho.anote2.resource.IResourceElementSet;
import pt.uminho.anote2.resource.rules.IRule;

public class RulesSet extends ResourcesHelp implements IRule{
	
	/**
	 * Logger
	 */
	static Logger logger = Logger.getLogger(RulesSet.class.getName());
	
	public RulesSet(int id, String name, String info) {
		super( id, name, info);
	}
	
	public RulesSet( int id, String name, String info,boolean active) {
		super( id, name, info,active);
	}

	public void removeElement(IResourceElement ruleElement) throws SQLException, DatabaseLoadDriverException {
		super.removeElement(ruleElement);
	}

	public void updateElement(IResourceElement ruleElement) throws SQLException, DatabaseLoadDriverException {
		super.updateElement(ruleElement);
	}
	
	public int compareTo(IRule dic)
	{
		if(this.getID()==dic.getID())
		{
			return 0;
		}
		else if(this.getID()<=dic.getID())
		{
			return -1;
		}
		return 1;
	}
	
	public boolean equals(Object ruleset)
	{
		if(!(ruleset instanceof IRule))
			return false;
		if(this.getID() == ((IRule) ruleset).getID())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String toString()
	{
		if(getID() < 1)
		{
			return "None";
		}
		String info = new String();
		info ="Rules Set : " + getName() + " (ID :"+ getID() + " ) ";
		if(!getInfo().equals(""))
		{
			info = info + "Notes: "+getInfo();
		}
		if(!isActive())
		{
			info = info + " (Inactive) ";
		}
		return info;
	}

	@Override
	public String getType() {
		return GlobalOptions.resourcesRuleSetName;
	}
	
	public boolean addElement(IResourceElement elem)
	{	
		if(super.addSimpleElement(elem))
		{
			int elementID;
			try {
				elementID = HelpDatabase.getNextInsertTableID(GlobalTablesName.resource_elements);
				int findPriorety = findMAxPriorety();
				insertPrioretyInElement(elementID-1,findPriorety+1);
			} catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			};
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	protected void insertPrioretyInElement(int elementID,int priorety) throws SQLException, DatabaseLoadDriverException
	{
		PreparedStatement findMAxPrioretyPS = GlobalOptions.database.getConnection().prepareStatement(QueriesRules.insertPrioretyOnRule);
		findMAxPrioretyPS.setInt(1,elementID);
		findMAxPrioretyPS.setInt(2,getID());
		findMAxPrioretyPS.setInt(3,priorety);
		findMAxPrioretyPS.execute();
		findMAxPrioretyPS.close();
	}
	
	protected int findMAxPriorety() throws SQLException, DatabaseLoadDriverException
	{
		int priorety = 0;	

		PreparedStatement findMAxPrioretyPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesRules.findMAxPriorety);
		findMAxPrioretyPS.setInt(1,getID());
		ResultSet rs = findMAxPrioretyPS.executeQuery();
		if(rs.next())
		{
			if(rs.getString(1)!=null)
			{
				priorety =  rs.getInt(1);
			}
		}
		rs.close();
		findMAxPrioretyPS.close();	
		return priorety;
	}
	
	public IResourceElementSet<IResourceElement> getResourceElements() throws SQLException, DatabaseLoadDriverException {
		IResourceElementSet<IResourceElement> resourceElems = new ResourceElementSet<IResourceElement>();
		IResourceElement elem = null;

		String term=null;
		int idResourceElement,classID;	
		int priorety;
		PreparedStatement getElems = Configuration.getDatabase().getConnection().prepareStatement(QueriesRules.getAllElementsWhitPriorety);
		getElems.setInt(1,getID());	
		ResultSet rs = getElems.executeQuery();
		while(rs.next())
		{
			idResourceElement = rs.getInt(1);
			classID = rs.getInt(3);
			term = rs.getString(4);
			priorety = rs.getInt(7);
			elem = new ResourceElement(idResourceElement,term,classID,ClassProperties.getClassIDClass().get(classID),priorety);
			resourceElems.addElementResource(elem);
		}
		rs.close();	
		getElems.close();
		return resourceElems;
	}

	public IResourceMergeReport merge(IRule ruleSnd) throws DatabaseLoadDriverException, SQLException 
	{
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeRulesReportTitle, ruleSnd, this);
		loadAllTermsFromDatabaseToMemory();	
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		Set<Integer> classesSnd = ruleSnd.getClassContent();
		Set<Integer> classes1st = getClassContent();
		updateContent(report, classesSnd, classes1st);
		IResourceElementSet<IResourceElement> terms = ruleSnd.getResourceElements();;	
		updateTerms(terms, report);
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

	private void updateContent(IResourceMergeReport report,
			Set<Integer> classesSnd, Set<Integer> classes1st) throws SQLException, DatabaseLoadDriverException {
		for(Integer classID:classesSnd)
		{
			if(!classes1st.contains(classID))
			{
				this.addResourceContent(classID);
				report.addClassesAdding(1);
			}
		}
	}

	private void updateTerms(IResourceElementSet<IResourceElement> terms, IResourceMergeReport report) throws DatabaseLoadDriverException, SQLException {
		int step = 0;
		int total = terms.getElements().size();
		for(IResourceElement elem:terms.getElements())
		{
			if(this.addElement(elem))
			{
				report.addTermAdding(1);
			}
			else
			{
				IInsertConflits conflit;
				IResourceElement elemValue = getFirstTermByName(elem.getTerm());
				if(elem.getTermClassID()==elemValue.getTermClassID())
				{
					conflit = new ResourceMergeConflits(ConflitsType.AlreadyHaveTerm, elemValue, elem);
				}
				else
				{
					conflit = new ResourceMergeConflits(ConflitsType.TermInDiferentClasses, elemValue, elem) ;
				}
				report.addConflit(conflit);
			}
			if(step%10==0)
			{
				memoryAndProgress(step, total);
			}
			step++;
		}

	}
	
	public IResourceMergeReport merge(IRule ruleSnd,Set<Integer> classIDSource) throws DatabaseLoadDriverException, SQLException 
	{
		IResourceMergeReport report = new ResourceMergeReport(GlobalNames.mergeRulesReportTitle, ruleSnd, this);
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		loadAllTermsFromDatabaseToMemory();	
		Set<Integer> classes1st = getClassContent();
		IResourceElementSet<IResourceElement> terms = new ResourceElementSet<IResourceElement>();
		for(Integer classID:classIDSource)
		{
			if(!classes1st.contains(classID))
			{
				this.addResourceContent(classID);
				report.addClassesAdding(1);
			}
			terms.addAllElementResource(ruleSnd.getTermByClass(classID).getElements());	
		}
		updateTerms(terms, report);
		deleteTermsInMemory();
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startTime);
		return report;
	}

}
