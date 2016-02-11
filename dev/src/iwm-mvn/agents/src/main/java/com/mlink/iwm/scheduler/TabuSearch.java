package com.mlink.iwm.scheduler;

class TabuSearch implements TabuList {
	int tbl;		// nominal length of tabu list
	int tblmin;		// minimal length of tabu list
	int tblmax;		// maximal length of tabu list
	int [][] tabuAddList;
	int [][] tabuSwapList;
	int [][] tabuRemoveList;
	int numTrials;  // number of iterations
	
	TabuSearch(int numArs, int numAssets, int tblLen) {
		numTrials = 0;
		tbl = tblLen;
		tblmin = tbl-4;
		tblmax = tbl+5;
		tabuAddList = new int[numArs][numAssets];
		tabuSwapList = new int[numArs][numArs];
		tabuRemoveList = new int[numArs][numAssets];	
		
		for(int i = 0; i < numArs; i++) {
			for(int j = 0; j < numAssets; j++) {
				tabuAddList[i][j] = 0;
				tabuRemoveList[i][j] = 0;
			}
		}
		
		for(int i = 0; i < numArs; i++) {
			for(int j = 0; j < numArs; j++) {
				tabuSwapList[i][j] = 0;
			}
		}
	}

	//@Override
	public			
    void makeAddTabu(int addJarIndex, int addAIndex, boolean newBest) {
        tabuAddList[addJarIndex][addAIndex] = numTrials + tbl;
        if (newBest && (tbl > tblmin)) tbl--;
        if (!newBest && (tbl < tblmax)) tbl++;
    }
    
	public			
    void makeSwapTabu(int addJarIndex1, int addJarIndex2, boolean newBest) {
        tabuSwapList[addJarIndex1][addJarIndex2] = numTrials + tbl;
        if (newBest && (tbl > tblmin)) tbl--;
        if (!newBest && (tbl < tblmax)) tbl++;
    }
    
	public			
    void makeRemoveTabu(int addJarIndex, int addAIndex, boolean newBest) {
        tabuRemoveList[addJarIndex][addAIndex] = numTrials + tbl;
        if (newBest && (tbl > tblmin)) tbl--;
        if (!newBest && (tbl < tblmax)) tbl++;
    }

	public			
    boolean isAddTabu(int addJarIndex, int addAIndex) {
        return tabuAddList[addJarIndex][addAIndex] >= numTrials;
    }
	public			
    boolean isSwapTabu(int addJarIndex1, int addJarIndex2) {
       return tabuSwapList[addJarIndex1][addJarIndex2] >= numTrials;
    }
	public			
    boolean isRemoveTabu(int addJarIndex, int addAIndex) {
        return tabuRemoveList[addJarIndex][addAIndex] >= numTrials;
    }

	public			
    void nextIteration() {
        numTrials++;
    }
}
