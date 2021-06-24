package operators;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Applying this operator moves the agent one step north-east.
 */
public class NorthEastOperator implements Operator {
  @Override
  public State apply(State state) {
    return new State(state.getX() - 1, state.getY() + 1);
  }

}
