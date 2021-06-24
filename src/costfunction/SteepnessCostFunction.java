package costfunction;

import environment.Environment;
import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Accounts for the cost when moving from higher to lower altitude or vice-versa.
 */
public class SteepnessCostFunction implements CostFunction {
  private Environment env;

  public SteepnessCostFunction(Environment env) {
    this.env = env;
  }

  @Override
  public long cost(State currentState, State nextState) {
    return Math.abs(env.getSteepness(nextState) - env.getSteepness(currentState));
  }
}
