package fr.emse.ai.adversarial.tictactoe;
import fr.emse.ai.adversarial.AlphaBetaSearch;
import fr.emse.ai.adversarial.IterativeDeepeningAlphaBetaSearch;
import fr.emse.ai.adversarial.MinimaxSearch;

import java.util.List;
import java.util.Scanner;

public class TicTacToeGameplay {
	
    public static void printState(List<Integer> state) { // Print the 3*3 grid corresponding to the state
    	System.out.println("Current state: ");
        for (int k = 1; k <= 9; k++){
        	
        	if  (state.get(k)==1) 
        		System.out.print( " O "+ "\t");
        	else if  (state.get(k)==-1) 
        		System.out.print( " X "+ "\t"); 
        	else
        		System.out.print( " - "+ "\t");
        	
        	if (k%3==0)
        		System.out.println();
        }
	}
    
    public static void main(String[] args) {
        TicTacToeGame game = new TicTacToeGame();
        MinimaxSearch<List<Integer>, Integer, Integer> minimaxSearch = MinimaxSearch.createFor(game);
        AlphaBetaSearch<List<Integer>, Integer, Integer> alphabetaSearch = AlphaBetaSearch.createFor(game);
        IterativeDeepeningAlphaBetaSearch<List<Integer>, Integer, Integer> iterativeDeepeningAlphaBetaSearch = IterativeDeepeningAlphaBetaSearch.createFor(game, -1, 1, 10);
        List<Integer> state = game.getInitialState();
        
        while (!game.isTerminal(state)) {
            System.out.println("\n======================");
            printState(state);
            int action = 10;
            if (state.get(0) == 1) {
                //human
                List<Integer> actions = game.getActions(state);
                Scanner in = new Scanner(System.in);
                int k=0;
                while (!actions.contains(action)) {
                    System.out.println("Human player, what is your action?");
                    action = in.nextInt();
                    //System.out.println(actions);
                    if (k>0)
                    	System.out.println("You must choose an integer k corresponding "
                    			+ "to an unoccupied position (i,j) in the 3*3 grid "
                    			+ "[ i=(k-1)/3 and j=(k-1)%3 ] \nPossible actions :"+actions);
                    k++;
                    
                }
            } else {
                //machine
                System.out.println("Machine player, what is your action?");
                action = minimaxSearch.makeDecision(state);
                System.out.println("Metrics for Minimax : " + minimaxSearch.getMetrics());
                alphabetaSearch.makeDecision(state);
                System.out.println("Metrics for AlphaBeta : " + alphabetaSearch.getMetrics());
                iterativeDeepeningAlphaBetaSearch.makeDecision(state);
                System.out.println("Metrics for IDAlphaBetaSearch : " + iterativeDeepeningAlphaBetaSearch.getMetrics());
            }
            System.out.println("Chosen action is " + action);
            state = game.getResult(state, action);
        }
        System.out.print("\n<--- GAME OVER:   ");
        
        int sum_=0;
        for( int k=1; k<=9; k++)
        	sum_+=Math.abs(state.get(k));
        
        if (sum_==9)
        	System.out.println("A draw: no winner ! --->");
        else if (state.get(0) == -1)
            System.out.println("Human wins! --->");
        else 
            System.out.println("Machine wins!  --->");
        
        System.out.println("\nFinal grid state: ");
        printState(state);

    }
}
