package fr.emse.ai.util;

import java.util.ArrayList;

public class MiniMax {
	
	public static Integer nbExploredNodes=0;
	public Integer minimax( SimpleTwoPlyGameTree<Integer> node) {
		
		if( node.isMax())
			return maxVal( node);
		else
			return minVal(node);
	}
	
	public Integer maxVal( SimpleTwoPlyGameTree<Integer> node ){
		Integer max=Integer.MIN_VALUE;
		if (node.isLeaf() ){
			return node.getValue();
		}else {
		ArrayList<SimpleTwoPlyGameTree<Integer>> children=node.getChildren();
		for( SimpleTwoPlyGameTree<Integer> succ: children) {
			Integer minMaxVal=minVal(succ);
			(MiniMax.nbExploredNodes)++;
			if ( minMaxVal >= max)
				max=minMaxVal;
		}
		return max;
		}
	}
	
	public Integer minVal( SimpleTwoPlyGameTree<Integer> node ){
		Integer min=Integer.MAX_VALUE;
		if (node.isLeaf() ){
			return node.getValue();
		}else {
		ArrayList<SimpleTwoPlyGameTree<Integer>> children=node.getChildren();
		for( SimpleTwoPlyGameTree<Integer> succ: children) {
			(MiniMax.nbExploredNodes)++;
			Integer maxMinVal=maxVal(succ); 
			if (  maxMinVal <= min) 
				min=maxMinVal;
		}
		return min;
		}
	}
	
	
}
