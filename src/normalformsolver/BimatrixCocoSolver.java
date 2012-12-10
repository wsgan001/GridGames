package normalformsolver;

import java.util.List;

import props.Joint;

import normalformgame.NormalFormGame;
import sequentialgame.AbstractAction;

public class BimatrixCocoSolver<A extends AbstractAction> implements NormalFormSolver<A> {

	static final int player1Idx = 0;
	static final int player2Idx = 1;

	@Override
	public GameSolution<A> solve(NormalFormGame<A> normalFormGame) {
		
		List<A> player1Actions = normalFormGame.getPossibleActionsForPlayer(player1Idx);
		List<A> player2Actions = normalFormGame.getPossibleActionsForPlayer(player2Idx);		
		int numPlayer1Actions = player1Actions.size();
		int numPlayer2Actions = player2Actions.size();
		
		// Extract the payoff matrices for players 1 and 2.
		double[][] player1Payoffs = BimatrixHuSolver.getPayoffMatrixForPlayer(normalFormGame, player1Idx);
		double[][] player2Payoffs = BimatrixHuSolver.getPayoffMatrixForPlayer(normalFormGame, player2Idx);
		
		// Create cooperative and competitive payoff matrices.
		double[][] player1CooperativePayoffs = new double[numPlayer1Actions][numPlayer2Actions];
		double[][] player2CooperativePayoffs = new double[numPlayer1Actions][numPlayer2Actions];
		double[][] player1CompetitivePayoffs = new double[numPlayer1Actions][numPlayer2Actions];
		double[][] player2CompetitivePayoffs = new double[numPlayer1Actions][numPlayer2Actions];
		for (int player1ActionIdx=0; player1ActionIdx<numPlayer1Actions; player1ActionIdx++) {
			for (int player2ActionIdx=0; player2ActionIdx<numPlayer2Actions; player2ActionIdx++) {
				double player1Payoff = player1Payoffs[player1ActionIdx][player2ActionIdx];
				double player2Payoff = player2Payoffs[player1ActionIdx][player2ActionIdx];
				double cooperativePayoff = (player1Payoff + player2Payoff) / 2.0;
				double competitivePayoff = (player1Payoff - player2Payoff) / 2.0;
				player1CooperativePayoffs[player1ActionIdx][player2ActionIdx] = cooperativePayoff;
				player2CooperativePayoffs[player1ActionIdx][player2ActionIdx] = cooperativePayoff;
				player1CompetitivePayoffs[player1ActionIdx][player2ActionIdx] = competitivePayoff;
				player2CompetitivePayoffs[player1ActionIdx][player2ActionIdx] = -competitivePayoff;
			}
		}
		
		// Solve the cooperative game
		Joint<double[]> cooperativeMixedStrategyPerPlayer = BimatrixHuSolver.solveForMixedStrategies(player1CooperativePayoffs, player2CooperativePayoffs);
		
		// Solve the competitive game.
		Joint<double[]> competitiveMixedStrategyPerPlayer = BimatrixHuSolver.solveForMixedStrategies(player1CompetitivePayoffs, player2CompetitivePayoffs);
		
		// Solution is to follow the cooperative strategy.
		// Coco values define the transfer utility.
		
		return null;
	}

}