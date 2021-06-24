package costfunction;

import environment.Environment;
import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Accounts for the cost when the current position is muddy.
 */
public class MuddinessCostFunction implements CostFunction {
  private Environment env;

  public MuddinessCostFunction(Environment env) {
    this.env = env;
  }

  @Override
  public long cost(State currentState, State nextState) {
    return env.getMuddiness(nextState);
  }
}
