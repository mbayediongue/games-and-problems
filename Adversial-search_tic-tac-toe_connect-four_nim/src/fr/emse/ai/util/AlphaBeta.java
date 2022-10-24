package fr.emse.ai.util;

public class AlphaBeta extends MiniMax {
	
	public static Integer nbExploredNodes=0;
	
	public Integer alphaBetaAlgo( SimpleTwoPlyGameTree<Integer> node, Integer alpha, Integer beta ){
		
		(AlphaBeta.nbExploredNodes)++;
		if ( node.isLeaf( ) ) {
			return node.getValue();
		}else {
			node.alpha=Integer.MIN_VALUE;
			node.beta=Integer.MAX_VALUE;
			
			if ( !(node.isMax()) ){
				for ( SimpleTwoPlyGameTree<Integer> child: node.getChildren()) {
					Integer val= alphaBetaAlgo( child, alpha, Integer.min(beta, node.beta ));
					node.beta=Integer.min( node.beta, val);
					if ( alpha >= node.beta ) // la coupe
						return node.beta;
				}
				return node.beta;
			}
			else {
				for ( SimpleTwoPlyGameTree<Integer> child: node.getChildren()) {
					Integer val= alphaBetaAlgo( child, Integer.max(alpha, node.alpha ), beta);
					node.alpha=Integer.max( node.alpha, val);
					if ( node.alpha >= beta ) // la coupe sur alpha
						return node.alpha;
				}
				return node.alpha;
			}
		}
		
	}
}

	
