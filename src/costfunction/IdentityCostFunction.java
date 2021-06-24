package costfunction;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * The cost incurred to reach from any state to any other state is 1.
 */
public class IdentityCostFunction implements CostFunction {

  @Override
  public long cost(State currentState, State nextState) {
    return 1;
  }

}
