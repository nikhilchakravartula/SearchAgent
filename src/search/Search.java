package search;

import java.util.ArrayList;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Search enables implementors to define their own search algorithms from startState to goalState
 */
public interface Search {

  /**
   * Reset search params
   */
  void reset();

  /**
   * @param startState
   * @param goalState
   * @return a sequence of states that represent path from startState to goalState
   */
  ArrayList<State> search(State startState, State goalState);
}
