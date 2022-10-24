package fr.emse.ai.adversarial.connectFour;
import fr.emse.ai.adversarial.AlphaBetaSearch;
import fr.emse.ai.adversarial.IterativeDeepeningAlphaBetaSearch;
import fr.emse.ai.adversarial.MinimaxSearch;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConnectFourplay {
	
    public static void printState(List<Integer> state) { // Print the 6*7 grid corresponding to the state
    	
        for (int k = 1; k <= 42; k++){
        	
        	if  (state.get(k)==1) 
        		System.out.print( " O "+ "\t");
        	else if  (state.get(k)==-1) 
        		System.out.print( " X "+ "\t"); 
        	else
        		System.out.print( " - "+ "\t");
        	
        	if (k%7==0)
        		System.out.println();
        }
	}
    
    public static void main(String[] args) throws InterruptedException {
        ConnectFourGame game = new ConnectFourGame();
        MinimaxSearch<List<Integer>, Integer, Integer> minimaxSearch = MinimaxSearch.createFor(game);
        AlphaBetaSearch<List<Integer>, Integer, Integer> alphabetaSearch = AlphaBetaSearch.createFor(game);
        IterativeDeepeningAlphaBetaSearch<List<Integer>, Integer, Integer> iterativeDeepeningAlphaBetaSearch = IterativeDeepeningAlphaBetaSearch.createFor(game, -1, 1, 7);
        List<Integer> state = game.getInitialState();
        //int kk=0;
       
        while (!game.isTerminal(state)) {
            System.out.println("\n======================");
            System.out.println("Current state: ");
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
                    	System.out.println("You must choose an integer k (from 1 to 7) corresponding "
                    			+ "to the column you want to put the next ball"
                    			+ "[\nPossible actions :"+actions);
                    k++;
                
                }
            } else {
                //machine
            	System.out.println("Machine player, what is your action?");
            	action=iterativeDeepeningAlphaBetaSearch.makeDecision(state);
                System.out.println("Metrics for IDAlphaBetaSearch : " + iterativeDeepeningAlphaBetaSearch.getMetrics());
            	
            	/*if (kk>10) {
            	//	action=(int)Math.floor(Math.random()*(7-6+1)+1);
            	//}else {
                action = minimaxSearch.makeDecision(state);
                System.out.println("Metrics for Minimax : " + minimaxSearch.getMetrics());
                action=alphabetaSearch.makeDecision(state);
                System.out.println("Metrics for AlphaBeta : " + alphabetaSearch.getMetrics());
            	}
            	*/
               
                try {
                    Thread.sleep(500);  // for a pause (to simulate machine thinking)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Chosen action is " + action);
            state = game.getResult(state, action);
            //kk++;
        }
        System.out.print("\n ----- <GAME OVER : ");
        
        int sum_=0;
        for( int k=1; k<=42; k++)
        	sum_+=Math.abs(state.get(k));
        
        if (sum_==42)
        	System.out.println("A draw: no winner !   > ------");
        else if (state.get(0) == -1)
            System.out.println("Human wins!   > ------");
        else 
            System.out.println("Machine wins!    > ------");
        
        System.out.println("\nFinal grid state: ");
        printState(state);

    }
}
