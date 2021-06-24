package operators;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Applying this operator moves the agent one step west.
 */
public class WestOperator implements Operator {
  @Override
  public State apply(State state) {
    return new State(state.getX(), state.getY() - 1);
  }
}
