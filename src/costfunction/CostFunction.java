package costfunction;

import state.State;


/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * This is an interface that defines the contract to define a cost function.
 */
public interface CostFunction {
  /**
   * @param currentState
   * @param nextState
   * @return The cost incurred for a transition from currentState to nextState
   */
  long cost(State currentState, State nextState);
}
