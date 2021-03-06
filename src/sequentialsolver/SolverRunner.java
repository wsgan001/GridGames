package sequentialsolver;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import props.Joint;

import sequentialgame.SequentialGame;
import sequentialgame.grid.Board;
import sequentialgame.grid.GridAction;
import sequentialgame.grid.GridGame;
import sequentialgame.grid.GridState;
import sequentialgame.grid.SimpleBoard;
import sequentialsimulator.Simulator;
import normalformsolver.BimatrixCocoSolver;
import normalformsolver.BimatrixCorrelatedEqmSolver;
import normalformsolver.BimatrixGambitSolver;
import normalformsolver.BimatrixHuSolver;
import normalformsolver.NormalFormSolver;

/**
 * A class for running algorithms to generate policies for grid games.
 * 
 * @author sodomka
 *
 */
public class SolverRunner {

	
	public static void main(String[] args) {
		int numIterations = 25;
		//NormalFormSolver<GridAction> normalFormSolver = new BimatrixHuSolver<GridAction>();
		//NormalFormSolver<GridAction> normalFormSolver = new BimatrixCocoSolver<GridAction>();
		//NormalFormSolver<GridAction> normalFormSolver = new BimatrixCorrelatedEqmSolver<GridAction>();
		NormalFormSolver<GridAction> normalFormSolver = new BimatrixGambitSolver<GridAction>();
		
		double gamma = .9;
		MultiAgentValueIteration<GridState,GridAction> valueIteration = new MultiAgentValueIteration<GridState,GridAction>(numIterations, normalFormSolver, gamma);

		String filename = "./input/grid1.txt";
		SimpleBoard board = new SimpleBoard(filename);
		//Board board = new SimpleBoard(2, 2);
		int numPlayers = 2;
		SequentialGame<GridState, GridAction> game = new GridGame(numPlayers, board);
		PolicyAndTransfers<GridState, GridAction> policyAndTransfers = valueIteration.generatePolicyAndTransfers(game);
		JointPolicy<GridState,GridAction> policy = policyAndTransfers.getPolicy();
		Map<GridState,Joint<Double>> transfers = policyAndTransfers.getTransfers();
		
		System.out.println("POLICY:\n" + policy.toString(.001));
		System.out.println("TRANSFERS:\n" + transfers.toString());
		
		// NOTE: transfer payments are now available for input to the simulator.
		// For each state, transfers gives a Joint<Double> representing the 
		// amount of additional utility received by each agent.
		
		//@betsy basic testing
		Simulator<GridState,GridAction> testSim = new Simulator<GridState,GridAction>(policy, transfers, game, 1);
//		Set<GridState> states = policy.keySet();
//		Iterator<GridState> iter = states.iterator();
//		GridState state = iter.next();
//		state = iter.next();
//		state = iter.next();
//		state = iter.next();
		//Joint<GridAction> action = testSim.samplePolicy(state);
//		Joint<Double>rewards = testSim.simulateAgents(100, 200);
//		System.out.println(rewards);
		
	}
	
	
}
