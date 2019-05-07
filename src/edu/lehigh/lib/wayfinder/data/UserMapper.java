package edu.lehigh.lib.wayfinder.data;

import edu.lehigh.lib.wayfinder.models.User;





public interface UserMapper {
	public void insertUser(User user);
	public User getUser(String userid);

}
