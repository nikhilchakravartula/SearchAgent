package strategy;

import search.Search;
import state.State;
import java.util.ArrayList;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * A strategy pattern implementation for search
 */
public class SearchContext {

  private Search searchStrategy;

  /**
   * @param searchStrategy
   * Set the search strategy
   */
  public void setStrategy(Search searchStrategy) {
    this.searchStrategy = searchStrategy;
  }

  /**
   * @param startState
   * @param goalStates
   * @return list of paths from start state to each of the goal states 
   */
  public ArrayList<ArrayList<State>> search(State startState, ArrayList<State> goalStates) {
    ArrayList<ArrayList<State>> result = new ArrayList<ArrayList<State>>();;
    for (State goalState : goalStates) {
      searchStrategy.reset();
      result.add(searchStrategy.search(startState, goalState));
    }
    return result;
  }
}
