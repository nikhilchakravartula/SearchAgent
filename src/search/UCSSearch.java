package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import costfunction.CostFunction;
import environment.Environment;
import operators.Operator;
import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * UCSNode captures node information during UCS search.
 */
class UCSNode {
  private State state;
  private long cost;
  private long heuristic;

  public UCSNode(State state, long cost) {
    this.state = state;
    this.cost = cost;
    this.heuristic = 0;
  }

  public UCSNode(State state, long cost, long heuristic) {
    this.state = state;
    this.cost = cost;
    this.heuristic = heuristic;
  }

  public State getState() {
    return state;
  }

  public long getCost() {
    return cost;
  }

  public long getHeuristic() {
    return heuristic;
  }

  @Override
  public int hashCode() {
    return state.hashCode();
  }

  /**
   * Two nodes are equal if their states are equal, irrespective
   * of cost and heuristic cost.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof UCSNode)) {
      return false;
    }
    UCSNode otherObj = (UCSNode) obj;
    return state.equals(otherObj.state);
  }

  public String toString() {
    return state.toString() + "-" + cost;
  }
}

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * UCSSearch implements UCS search algorithm
 */
public class UCSSearch implements Search {
  Environment env;
  //The running queue
  PriorityQueue<UCSNode> queue;
  //Map that holds nodes that are already processed.
  HashMap<UCSNode, UCSNode> closed;
  //Map that holds nodes that are yet to be processed.
  HashMap<UCSNode, UCSNode> open;
  //Parent links
  HashMap<UCSNode, UCSNode> parent;
  ArrayList<Operator> operators;
  //The cost function f(n)
  ArrayList<CostFunction> costFunctions;

  class UCSComparator implements Comparator<UCSNode> {

    public int compare(UCSNode node1, UCSNode node2) {
      if (node1.getCost() <= node2.getCost()) {
        return -1;
      }
      if (node1.getCost() > node2.getCost()) {
        return 1;
      }
      return 0;
    }

  }

  public void reset() {
    open.clear();
    closed.clear();
    queue.clear();
    parent.clear();
  }

  /**
   * @param env : The environment in which a goal needs to be searched.
   * @param operators : Operators that enable movement of the agent from one state to another.
   * @param costFunctions : Cost functions that define f(n)
   */
  public UCSSearch(Environment env, ArrayList<Operator> operators,
      ArrayList<CostFunction> costFunctions) {
    this.env = env;
    this.operators = operators;
    this.costFunctions = costFunctions;
    queue = new PriorityQueue<UCSNode>(new UCSComparator());
    open = new HashMap<UCSNode, UCSNode>();
    closed = new HashMap<UCSNode, UCSNode>();
    parent = new HashMap<UCSNode, UCSNode>();
    reset();
  }

  /**
   * @param startState
   * @param goalState
   * @return A sequence of states that correspond to the path from
   * startState to endState
   */
  public ArrayList<State> search(State startState, State goalState) {
    UCSNode node = new UCSNode(startState, 0);
    queue.offer(node);
    parent.put(node, null);
    open.put(node, node);

    boolean solutionFound = false;
    UCSNode child = null;

    while (queue.isEmpty() == false) {
      node = queue.poll();
      //Note that startState will be updated every iteration.
      startState = node.getState();
      if (startState.equals(goalState)) {
        solutionFound = true;
        break;
      } else {
        //Explore next/adjacent/successor states 
        State nextState;
        for (Operator operator : operators) {
          nextState = operator.apply(startState);
          if (env.isValid(nextState) && env.canMove(startState, nextState)) {
            //Evaluate a state only if it is feasible
            long cost = node.getCost();
            
            //Calculate costs incurred to reach this state.
            for (CostFunction cf : costFunctions) {
              cost += cf.cost(startState, nextState);
            }
            //Generate child node.
            child = new UCSNode(nextState, cost);
            if (open.containsKey(child) == false && closed.containsKey(child) == false) {
              //child unexplored till now
              queue.offer(child);
              parent.put(child, node);
              open.put(child, child);
            } else if (open.containsKey(child)) {
              //child is ready for processing.
              Long prevCost = open.get(child).getCost();
              if (prevCost > child.getCost()) {
                //found a better path to child. Hence,update.
                open.put(child, child);
                queue.remove(child);
                queue.offer(child);
                parent.put(child, node);
              }

            } else if (closed.containsKey(child)) {
              //child already explored and processed.
              Long prevCost = closed.get(child).getCost();
              if (prevCost > child.getCost()) {
                //found a better path to child. Hence,update.
                closed.remove(child);
                open.put(child, child);
                queue.remove(child);
                queue.offer(child);
                parent.put(child, node);
              }
            }
          }
        }
      }
      //Done exploring this node. Hence, put it in the closed list.
      closed.put(node, node);
    }

    //Extract solution
    ArrayList<State> solution = new ArrayList<State>();
    long solutionCost = 0;
    if (solutionFound == true) {
      solutionCost = node.getCost();
      while (node != null) {
        solution.add(node.getState());
        node = parent.get(node);
      }

    }
    //solution contains nodes from goal to start. Hence, reverse before returning. 
    Collections.reverse(solution);

    System.out.println(solutionCost);
    return solution;

  }
}
