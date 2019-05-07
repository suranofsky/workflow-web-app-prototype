package edu.lehigh.lib.wayfinder.data;

import java.util.HashMap;
import java.util.List;

import edu.lehigh.lib.wayfinder.models.Wip;








public interface WipMapper {
	public List<Wip> getWorkInProgress(String accountId,String processId,String currentTaskId);
	public Wip getWip(String wipId,String accountId);
	public void updateWipStatus(String newStatus,String wipId);
	public int insertWip(Wip wip);
	public List<HashMap> getTaskSummary();
}
