package com.bpn.diplom.dao;

import java.util.List;

public interface DAOLBP {

	public List<EntityLBPUser> getAllUsers();
	
	public EntityLBPUser getUser(int id);
	
	public List<EntityLBPUser> getUsers(String name);
	
	public int removeUser(int id);
	
	public void addUser(EntityLBPUser user);
}
