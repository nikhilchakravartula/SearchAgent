package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import state.State;
import environment.*;
import operators.*;
import costfunction.*;
/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * BFSNode captures node information during BFS search.
 */
class BFSNode {
  private State state;
  private long cost;

  public BFSNode(State state, long cost) {
    this.state = state;
    this.cost = cost;
  }

  public State getState() {
    return state;
  }

  public long getCost() {
    return cost;
  }

  @Override
  public int hashCode() {
    return state.hashCode();
  }

  /**
   * Two nodes are equal if their states are equal, irrespective
   * of cost.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof BFSNode)) {
      return false;
    }
    BFSNode otherObj = (BFSNode) obj;
    return state.equals(otherObj.state);
  }

  public String toString() {
    return state.toString() + "-" + cost;
  }
}


/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * BFSSearch implements BFS search algorithm
 */
public class BFSSearch implements Search {
  Environment env;
  ArrayList<Operator> operators;
  LinkedList<BFSNode> queue;
  HashSet<State> visited;
  HashMap<BFSNode, BFSNode> parent;
  ArrayList<CostFunction> costFunctions;

  public void reset() {
    visited.clear();
    queue.clear();
    parent.clear();
  }

  /**
   * @param env : The environment in which a goal needs to be searched.
   * @param operators : Operators that enable movement of the agent from one state to another.
   * @param costFunctions : Cost functions that define f(n)
   */
  public BFSSearch(Environment env, ArrayList<Operator> operators,
      ArrayList<CostFunction> costFunctions) {
    this.env = env;
    this.operators = operators;
    this.costFunctions = costFunctions;
    queue = new LinkedList<BFSNode>();
    parent = new HashMap<BFSNode, BFSNode>();
    visited = new HashSet<State>();
    reset();
  }

  /**
   *@param startState
   *@param goalState
   *@return a sequence of states that represent the path from startState to goalState
   */
  public ArrayList<State> search(State startState, State goalState) {
    BFSNode bfsNode = new BFSNode(startState, 0);
    queue.addLast(bfsNode);
    parent.put(bfsNode, null);
    visited.add(startState);

    boolean solutionFound = false;
    BFSNode child = null;

    while (queue.isEmpty() == false) {
      bfsNode = queue.removeFirst();
      //Note that startState will be updated every iteration.
      startState = bfsNode.getState();
      if (startState.equals(goalState)) {
        solutionFound = true;
        break;
      } else {
        //Explore next/adjacent/successor states 
        State nextState;
        for (Operator operator : operators) {
          nextState = operator.apply(startState);
          if (env.isValid(nextState) && env.canMove(startState, nextState)
              && !visited.contains(nextState)) {
            //Consider a state only if its valid and not visited.
            long cost = bfsNode.getCost();
            
            //Calculate the cost incurred to reach this state
            for (CostFunction cf : costFunctions) {
              cost += cf.cost(startState, nextState);
            }
            //Generate a child node and add in queue for processing.
            child = new BFSNode(nextState, cost);
            queue.addLast(child);
            //Parent references to extract path later.
            parent.put(child, bfsNode);
            visited.add(nextState);
          }
        }
      }
    }

    ArrayList<State> solution = new ArrayList<State>();
    long solutionCost = 0;
    if (solutionFound == true) {
      solutionCost = bfsNode.getCost();
      while (bfsNode != null) {
        solution.add(bfsNode.getState());
        bfsNode = parent.get(bfsNode);
      }
    }
    Collections.reverse(solution);

    System.out.println(solutionCost);
    return solution;
  }
}
