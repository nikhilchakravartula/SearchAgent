package costfunction;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Approximate distance between two adjacent states. Diagonal : 14 Non-diagonal : 10
 */
public class MoveCostFunction implements CostFunction {
  static final int DIAGONALCOST = 14;
  static final int NONDIAGONALCOST = 10;

  @Override
  public long cost(State currentState, State nextState) {

    long cost = 0;
    
    if (Math.abs(nextState.getX() - currentState.getX()) == 1
        && Math.abs(nextState.getY() - currentState.getY()) == 1) {
      //currentState lies digoanally adjacent to nextState
      cost = DIAGONALCOST;
    } else
      cost = NONDIAGONALCOST;
    return cost;

  }
}
