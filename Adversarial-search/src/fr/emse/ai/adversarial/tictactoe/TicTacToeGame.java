/* Mbaye Diongue March 2022, EMSE, 'Defi' Artificial Intelligence*/

package fr.emse.ai.adversarial.tictactoe;
import java.util.ArrayList;
import java.util.List;

import fr.emse.ai.adversarial.Game;

public class TicTacToeGame implements Game<List<Integer>, Integer, Integer> {

    public final static Integer[] players = {1, -1};
    
    // We represent the state by a a list of 3*3+1=10 integers describing the grid of tic-toc-toe:
    // 		* the first integers state[0]  indicates the current player (state[0]=-1 or state[0]= 1)
    //      * the 9 others integers describe the flattened 3*3 grid 
    //				- state[k]==0 : the cell (i,j) is empty  ( k=3*i+j) 
    //				- state[k]==1 :  the cell (i,j) is occupied by a cross (X)
    //				- state[k]==-1: the cell (i,j) is occupied by an Os (O)
   
	public final static List<Integer> initialState = new ArrayList<Integer>();

		
    public TicTacToeGame() {
    	initialState.add(1); // the player 1 (the one who draws crosses) is first to play
    	for( int i=0; i<9; i++)
    		initialState.add(0);
    }
    

    @Override
    public List<Integer> getInitialState() {
        return initialState;
    }

    @Override
    public Integer[] getPlayers() {
        return players;
    }

    @Override
    public Integer getPlayer(List<Integer> state) {
        return state.get(0);
    }

    @Override
    public List<Integer> getActions(List<Integer> state) {
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for (int i = 1; i <= 9; i++) {
        	if ( state.get(i)==0 )
        		actions.add( i*state.get(0) );
        }
            
        return actions;
    }

    @Override
    public List<Integer> getResult(List<Integer> state, Integer action) {
        List<Integer> newState = new ArrayList<Integer>();
        
        // copy elements of old state
        for( int e: state)
        	newState.add(e);
        
        newState.set(0, -state.get(0));// change the player
        
        int indexAction=Math.abs(action);
        if ( action >0)
        	action= 1;
        else
        	action=-1;
        newState.set( indexAction, action);
        
        return newState;
    }

    @Override
    public boolean isTerminal(List<Integer> state) {
    	
        int sum_=0;
        for( int k=1; k<=9; k++)
        	sum_+=Math.abs(state.get(k));
        if (sum_==9) // no more space in the grid !
        	return true;
   
    	for( int i=0; i<3; i++) {
    		// We sum over each row of the grid to verify if there is 3 aligned cells
    		int row=3*i+1;
    		int sum_row=state.get(row)+state.get(row+1)+state.get(row+2);
	    	if ( Math.abs(sum_row)==3) 
	    		return true;
    	}
    	
    	for( int j=0; j<3; j++) {
    		// We sum over each column of the grid to verify if there is 3 aligned cells
    		int col=j+1;
    		int sum_col=state.get(col)+state.get(col+3)+state.get(col+6);
	    	if ( Math.abs(sum_col)==3 ) 
	    		return true;
    	}
    	
    	// We sum over each 2 diagonal of the grid to verify if there is 3 aligned cells
    	if(Math.abs(state.get(1)+state.get(5)+state.get(9))==3)
    		return true;
    	if(Math.abs(state.get(3)+state.get(5)+state.get(7))==3)
    		return true;
    	
    	return false;
    }

    @Override
    public double getUtility(List<Integer> state, Integer player) {
    	
    	int nbPlayerPawns=0;
    	int nbOppPawns=0; // number of opponent pawns (in a fixed row, in column or diagonal)
    	int nbPlayerPawnsTot=0;
    	int nbOppPawnsTot=0;
    	
    	for( int i=0; i<3; i++) {
    		// We sum over each row of the grid 
    		int row=3*i+1;
    		for (int k=0; k<3; k++){
    			if (state.get(row+k)== player)
    				nbPlayerPawns++;
    			else if (state.get(row+k)==-player)
    				nbOppPawns++;
    		}
    		if ( nbPlayerPawns==3) 
    			return Double.POSITIVE_INFINITY;
    		else if (nbOppPawns==3) 
    			return Double.NEGATIVE_INFINITY;  
    		
        	nbPlayerPawnsTot+=nbPlayerPawns;
        	nbOppPawnsTot+=nbOppPawns;
        	
        	nbPlayerPawns=0;
        	nbOppPawns=0;
    	}
        

    	
    	for( int j=0; j<3; j++) {
    		// We sum over each column of the grid 
    		int col=j+1;
    		for (int k=0; k<3; k++){
    			if (state.get(col+3*k)== player)
    				nbPlayerPawns++;
    			else if (state.get(col+3*k)==-player)
    				nbOppPawns++;
    		}
    		if ( nbPlayerPawns==3) 
    			return Double.POSITIVE_INFINITY;
    		else if (nbOppPawns==3) 
    			return Double.NEGATIVE_INFINITY; 
    		
        	nbPlayerPawnsTot+=nbPlayerPawns;
        	nbOppPawnsTot+=nbOppPawns;
        	
        	nbPlayerPawns=0;
        	nbOppPawns=0;
    	}
        
    	// We sum over each 2 diagonal of the grid 
    	int[] diag1= {1, 5, 9}; // first diagonal of the grid (\)
    	for( int k: diag1) {
    		if (state.get(k)== player)
    			nbPlayerPawns++;
    		else if (state.get(k)==-player)
    			nbOppPawns++;
    	}
    	if ( nbPlayerPawns==3) 
    		return Double.POSITIVE_INFINITY;
    	else if (nbOppPawns==3) 
    		return Double.NEGATIVE_INFINITY;  	
    	
    	nbPlayerPawnsTot+=nbPlayerPawns;
    	nbOppPawnsTot+=nbOppPawns;
    	
    	nbPlayerPawns=0;
    	nbOppPawns=0;
    	
    	int[] diag2= {3, 5, 7}; 
    	for( int k: diag2) {
    		if (state.get(k)== player)
    			nbPlayerPawns++;
    		else if (state.get(k)==-player)
    			nbOppPawns++;
    	}
    	if ( nbPlayerPawns==3) 
    		return Double.POSITIVE_INFINITY;
    	else if (nbOppPawns==3) 
    		return Double.NEGATIVE_INFINITY;  
    	
    	nbPlayerPawnsTot+=nbPlayerPawns;
    	nbOppPawnsTot+=nbOppPawns;
    	
    	return nbPlayerPawnsTot-nbOppPawnsTot;
    }
}
