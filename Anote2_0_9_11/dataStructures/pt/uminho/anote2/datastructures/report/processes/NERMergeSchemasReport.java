package pt.uminho.anote2.datastructures.report.processes;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.anote2.core.report.processes.INERMergeProcess;
import pt.uminho.anote2.datastructures.report.Report;
import pt.uminho.anote2.process.IE.IIEProcess;

public class NERMergeSchemasReport extends Report implements INERMergeProcess{

	private List<IIEProcess> nertomerge;
	private IIEProcess newProcess;
	
	
	public NERMergeSchemasReport(String title, IIEProcess newProcess2,IIEProcess primary,List<IIEProcess> nertomerge) {
		super(title);
		this.nertomerge = new ArrayList<IIEProcess>();
		this.nertomerge.add(primary);
		this.nertomerge.addAll(nertomerge);
		this.newProcess = newProcess2;
	}
	
	@Override
	public List<IIEProcess> nerProcessesMerged() {
		return nertomerge;
	}

	@Override
	public IIEProcess getNERSchema() {
		return newProcess;
	}
	

}
