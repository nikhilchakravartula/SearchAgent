package operators;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Captures an operator that moves the agent from one state to the successor state
 */
public interface Operator {
  State apply(State state);
}
