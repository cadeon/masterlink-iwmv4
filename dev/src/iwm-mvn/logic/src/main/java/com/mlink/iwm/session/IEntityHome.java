package com.mlink.iwm.session;

import com.mlink.iwm.entity.AbstractEntity;

public interface IEntityHome<T extends AbstractEntity> {
	
	/**
	 * Find entity by id.
	 * 
	 * @param id
	 * @return entity of typr T, null if not found
	 */
	T find(Long id);

	/**
	 * Delete the entity from datastore.
	 * Note: for root entities, the cascade type is CascadeType.ALL
	 * as defined in jpa 2.0
	 * 
	 * @param entity entity of typr T
	 * @return  "deleted" when successfuly deletd
	 * 			 null otherwise 
	 *           
	 */
	String delete(T entity);

	/**
	 * First time store or update the entity in the datastore.
	 * Note: for root entities, the cascade type is CascadeType.ALL
	 * as defined in jpa 2.0
	 * 
	 * 
	 * @param entity entity of typeS T 
	 * @return  "updated" when successfuly updated
	 * 		    "persisted" when stored for the first time
	 * 			 null otherwise
	 */
	String saveOrUpdate(T entity);

	
	
	
	/**
	 * Refresh (clears dirty) entity of type T
	 * @param entity
	 */
	//void refresh(T entity);

}
