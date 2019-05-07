package edu.lehigh.lib.wayfinder.data;

import java.sql.Date;
import java.util.List;
import edu.lehigh.lib.wayfinder.models.Process;


public interface ProcessMapper {
	public List<Process> getProcessListForAccount(String accountid);

	public void createProcessForAccount(edu.lehigh.lib.wayfinder.models.Process proc);
	
	public void updateProcess(edu.lehigh.lib.wayfinder.models.Process proc);
	
	public void deleteProcess(Integer id);
	
	public Process getProcessById(String processId);
}
