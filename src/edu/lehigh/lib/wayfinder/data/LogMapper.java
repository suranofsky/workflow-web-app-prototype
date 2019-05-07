package edu.lehigh.lib.wayfinder.data;

import java.util.List;

import edu.lehigh.lib.wayfinder.models.Log;

public interface LogMapper {
	public void insertLogRow(Log log);
	public List<Log> getLogsForWip(int wipid);
	public List<Log> getLogsForDisplayByWipId(int wipid);

}
