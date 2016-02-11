package org.mlink.agents.scheduler;

interface TabuList {
	void makeAddTabu(int addJarIndex, int addAIndex, boolean newBest);
	void makeSwapTabu(int addJarIndex1, int addJarIndex2, boolean newBest);
	void makeRemoveTabu(int addJarIndex, int addAIndex, boolean newBest);
	boolean isAddTabu(int addJarIndex, int addAIndex);
	boolean isSwapTabu(int addJarIndex1, int addJarIndex2);
	boolean isRemoveTabu(int addJarIndex, int addAIndex);
	void nextIteration();
}
