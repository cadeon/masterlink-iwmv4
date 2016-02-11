package com.mlink.iwm.framework;

import java.util.List;


import com.mlink.iwm.entity.AbstractEntity;



public interface IAbstractEntityListService<T extends AbstractEntity> {

	/**
	 * 
	 * @return result list for type T
	 */
	//List<T> getResultList();
	
	List<T> getAll();

	//void setSelectedEntity(T entity);
	
	//T getSelectedEntity();
	
	void refresh();

	
}
