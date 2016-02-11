package com.mlink.iwm.framework;


import com.mlink.iwm.entity.AbstractEntity;


/**
 * Abstract super interface for entity related services.
 * 
 * @param <T>  abstract entity type
 */
public interface IAbstractEntityService<T extends AbstractEntity> {
	/**
	 * Find entity by id.
	 * 
	 * @param id
	 * @return entity of typr T, null if not found
	 */
	T find(Long id);

	/**
	 * Delete the entity from datastore.
	 * 
	 * @param entity entity of typr T 
	 *           
	 */
	String delete(T entity);

	/**
	 * * Store or update the entity in the datastore.
	 * 
	 * @param entity entity of typeS T 
	 * @return
	 */
	String saveOrUpdate(T entity);

	
	/**
	 *   refresh entity of type T
	 * @param entity
	 */
	//void refresh(T entity);
	
	
}
