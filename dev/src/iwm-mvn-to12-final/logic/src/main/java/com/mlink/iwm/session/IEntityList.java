package com.mlink.iwm.session;

import java.util.List;

import com.mlink.iwm.entity.AbstractEntity;


public interface IEntityList<T extends AbstractEntity> {
	
	/**
	 * retrieves from data store all entities of type T
	 * @return result list for type T
	 */
	public List<T> getAll();
	
	/**
	 * clears resultList associated with a given component of type <T>List
	 */
	public void clear();

}
