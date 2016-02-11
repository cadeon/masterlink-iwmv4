package com.mlink.iwm.framework;

import java.util.List;


import com.mlink.iwm.entity.AbstractEntity;



public interface IAbstractEntityListService<T extends AbstractEntity> {


	//List<T> getResultList();
	
	/**
	 * retrieves from data store all entities of type T
	 * @return result list for type T
	 */
	List<T> getAll();

	//void setSelectedEntity(T entity);
	
	//T getSelectedEntity();
	

	//void refresh();

	
}
