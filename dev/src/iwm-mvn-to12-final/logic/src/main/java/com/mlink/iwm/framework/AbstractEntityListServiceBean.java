package com.mlink.iwm.framework;

import java.io.Serializable;
import java.util.List;


import com.mlink.iwm.entity.AbstractEntity;

public abstract class AbstractEntityListServiceBean<T extends AbstractEntity>
		implements IAbstractEntityListService<T>, Serializable {


	private int maxResults = 20;

	/**
	 * {@inheritDoc}
	 */
	public abstract List<T> getAll();



	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	
}
