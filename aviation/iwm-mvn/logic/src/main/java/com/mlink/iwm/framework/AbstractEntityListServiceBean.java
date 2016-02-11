package com.mlink.iwm.framework;

import java.util.List;


import com.mlink.iwm.entity.AbstractEntity;

public abstract class AbstractEntityListServiceBean<T extends AbstractEntity>
		implements IAbstractEntityListService<T> {

	private int maxResults = 20;

	public abstract List<T> getResultList();

	public abstract void setSelectedEntity(T entity);
	
	public abstract T getSelectedEntity();

	public abstract void refresh();

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	
}
