/* Mbaye Diongue March 2022, EMSE, 'Defi' Artificial Intelligence*/

package fr.emse.ai.adversarial.connectFour;
import java.util.ArrayList;
import java.util.List;

import fr.emse.ai.adversarial.Game;

public class ConnectFourGame implements Game<List<Integer>, Integer, Integer> {

    public final static Integer[] players = {1, -1};
    
    // We represent the state by a list of 6*7+1=43 integers describing the 6*7 grid of tic-toc-toe:
    // 		* the first integers state[0]  indicates the current player (state[0]=-1 or state[0]= 1)
    //      * the 9 others integers describe the flattened 3*3 grid 
    //				- state[k]==0 : the cell (i,j) is empty ( i=k/6 and j=k%6) 
    //				- state[k]==1 :  the cell (i,j) is occupied by a cross (X)
    //				- state[k]==-1: the cell (i,j) is occupied by an Os (O)
   
	public final static List<Integer> initialState = new ArrayList<Integer>();

		
    public ConnectFourGame() {
    	initialState.add(1); // the player 1 (the one who draws crosses) is first to play
    	
    	for( int i=0; i<6*7; i++)
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
        for (int j = 1; j <=7; j++) {
        	if ( state.get( j )==0 )
        		actions.add( j );
        }
            
        return actions;
    }

    @Override
    public List<Integer> getResult(List<Integer> state, Integer action) {
        List<Integer> newState = new ArrayList<Integer>();
        
        // copy elements of the old state
        for( int e: state)
        	newState.add(e);
        
        newState.set(0, -state.get(0));// change the player
        
        boolean modified=false;
        int high=1;
        while( !modified && high<6){
        	if ( state.get(7*high+action)!=0) {
        		newState.set(7*(high-1)+action , state.get(0));
        		modified=true;
        	}
        	high++;
        }
        if (!modified)
        	newState.set(7*5+action , state.get(0));
        
        return newState;
    }

    @Override
    public boolean isTerminal(List<Integer> state) {
    	
        int sum_=0;
        for( int k=1; k<=42; k++)
        	sum_+=Math.abs(state.get(k));
        if (sum_==42) // no more space in the grid !
        	return true;
        if(sum_ < 4)
        	return false;

    	for( int i=0; i<6; i++) {
    		// We sum over each row of the grid to verify if there is 4 aligned cells of the same calor
    		int row=7*i+1;
        	for ( int j=0; j<4; j++) {
        		int sum_row=state.get(row+j)+state.get(row+j+1) + state.get(row+j+2)+state.get(row+j+3);
        		if ( Math.abs(sum_row)==4 )
        			return true;
        	}
    	}
    	
    	for( int j=0; j<7; j++) {
    		// We sum over each column of the grid to verify if there is 4 aligned cells of the same color
    		int col=j+1;
        	for ( int i=0; i<3; i++) {
        		int sum_col=state.get(col+7*i)+state.get(col+7*(i+1))+state.get(col+7*(i+2))+state.get(col+7*(i+3));
        		if (  Math.abs(sum_col)==4  )
        			return true;
        	}
    	}
    	
    	// We sum over each diagonal of the grid to verify if there is 3 aligned cells
    	int k=0;
    	for(int i=0; i<3; i++) // diagonals of type: \ 
    		for( int j=0; j<4; j++) {
	    		k=i*7+j+1;
	    		int nbAligned=0;
	    		for (int l=1; l<=3; l++) {
	    			nbAligned+=state.get( k+(l-1)*8)*state.get( k+l*8) ;
	    		}
	    		if (nbAligned>=3) // at least four points of same color are aligned
	    			return true;
    	}
    	for(int i=0; i<3; i++) // diagonals of type: /
    		for( int j=3; j<7; j++) {
	    		k=i*7+j+1;
	    		int nbAligned=0;
	    		for (int l=1; l<=3; l++) {
	    			nbAligned+=state.get( k+(l-1)*6)*state.get( k+l*6) ;
	    		}
	    		if (nbAligned>=3) // at least four points of same color are aligned
	    			return true;
    	}
    	
    	return false;
    }

    @Override
    public double getUtility(List<Integer> state, Integer player) {
    	
    	int nbPlayerPawns=0;
    	int nbOppPawns=0; // number of opponent pawns (in a fixed row, in column or diagonal)
    	int nbPlayerPawnsTot=0;
    	int nbOppPawnsTot=0;
    	   
        
    	for( int i=0; i<6; i++) {
    		// We sum over each row of the grid to verify if there is 4 aligned cells of the same calor
    		int row=7*i+1;
        	for ( int j=0; j<4; j++) {
        		for(int k=0; k<3; k++) {
        			if( state.get(row+j+k)* state.get(row+j+k+1)==1 )
        				if (state.get(row+j+k)==player)
        					nbPlayerPawns++;
        				else
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
    	}
    	
    	for( int j=0; j<7; j++) {
    		// We sum over each column of the grid to verify if there is 4 aligned cells of the same color
    		int col=j+1;
        	for ( int i=0; i<3; i++) {
        		for(int k=0; k<3; k++) {
        			if( state.get(col+7*(i+k) )*state.get(col+7*(i+k+1) )==1 )
        				if ( state.get(col+7*(i+k))==player )
        					nbPlayerPawns++;
        				else
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
    	}
    	        
    	// We sum over each diagonal of the grid to verify if there is 3 aligned cells
    	int k=0;
    	for(int i=0; i<3; i++) // diagonals of type: \ 
    		for( int j=0; j<4; j++) {
	    		k=i*7+j+1;
	    		for(int l=0; l<3; l++) {
        			if( state.get(k+8*l)* state.get(k+8*(l+1))==1 )
        				if ( state.get(k+8*l)==player )
        					nbPlayerPawns++;
        				else
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
    	for(int i=0; i<3; i++) // diagonals of type: /
    		for( int j=3; j<7; j++) {
	    		k=i*7+j+1;
	    		
	    		for(int l=0; l<3; l++) {
        			if( state.get(k+6*l)* state.get(k+6*(l+1))==1 )
        				if ( state.get(k+8*l)==player )
        					nbPlayerPawns++;
        				else
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

    	return nbPlayerPawnsTot-nbOppPawnsTot;
    }
}
