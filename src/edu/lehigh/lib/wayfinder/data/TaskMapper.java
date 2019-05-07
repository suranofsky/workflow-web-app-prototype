package edu.lehigh.lib.wayfinder.data;

import java.util.List;

import edu.lehigh.lib.wayfinder.models.Task;


public interface TaskMapper {
	public List<Task> getTaskListForProcess(String processId,String accountId);

	public void createTask(Task t);
	
	public void updateTask(edu.lehigh.lib.wayfinder.models.Task t);
	
	public void deleteTask(Integer id);
	
	public Task getTaskById(String taskId);
}
