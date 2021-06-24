package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import costfunction.CostFunction;
import environment.Environment;
import operators.Operator;
import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * AStarNode captures node information during A* search.
 */
class AStarNode {
  //The current state
  private State state;
  //Cost to reach this state
  private long cost;
  //Heuristic from the current state to the goal state
  private long heuristic;

  public AStarNode(State state, long cost) {
    this.state = state;
    this.cost = cost;
    this.heuristic = 0;
  }

  public AStarNode(State state, long cost, long heuristic) {
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
    if (!(obj instanceof AStarNode)) {
      return false;
    }
    AStarNode otherObj = (AStarNode) obj;
    return state.equals(otherObj.state);
  }

  public String toString() {
    return state.toString() + "-" + cost;
  }
}


/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * AStarSearch implements A* search algorithm
 */
public class AStarSearch implements Search {
  Environment env;
  ArrayList<Operator> operators;
  //The running queue
  PriorityQueue<AStarNode> queue;
  //Map that holds nodes that are already processed.
  HashMap<AStarNode, AStarNode> closed;
  //Map that holds nodes that are yet to be processed.
  HashMap<AStarNode, AStarNode> open;
  //Parent links
  HashMap<AStarNode, AStarNode> parent;
  //The cost function f(n)
  ArrayList<CostFunction> costFunctions;
  //The hueristic g(n)
  ArrayList<CostFunction> heuristicCostFunctions;

  class AStarComparator implements Comparator<AStarNode> {

    public int compare(AStarNode node1, AStarNode node2) {
      if (node1.getCost() + node1.getHeuristic() <= node2.getCost() + node2.getHeuristic()) {
        return -1;
      } else
        return 1;
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
   * @param heuristicCostFunctions : Cost functions that define g(n)
   */
  public AStarSearch(Environment env, ArrayList<Operator> operators,
      ArrayList<CostFunction> costFunctions, ArrayList<CostFunction> heuristicCostFunctions) {
    this.env = env;
    this.operators = operators;
    this.costFunctions = costFunctions;
    this.heuristicCostFunctions = heuristicCostFunctions;
    queue = new PriorityQueue<AStarNode>(new AStarComparator());
    open = new HashMap<AStarNode, AStarNode>();
    closed = new HashMap<AStarNode, AStarNode>();
    parent = new HashMap<AStarNode, AStarNode>();
    reset();
  }

  /**
   * @param startState
   * @param goalState
   * @return A sequence of states that correspond to the path from
   * startState to endState
   */
  public ArrayList<State> search(State startState, State goalState) {
    AStarNode node = new AStarNode(startState, 0);
    queue.offer(node);
    parent.put(node, null);
    open.put(node, node);

    boolean solutionFound = false;
    AStarNode child = null;
    while (queue.isEmpty() == false) {
      node = queue.poll();
      //Note that startState will be updated every iteration.
      startState = node.getState();
      //If goal found, break
      if (startState.equals(goalState)) {
        solutionFound = true;
        break;
      } else {
        //Explore next/adjacent/successor states 
        State nextState;
        for (Operator operator : operators) {
          nextState = operator.apply(startState);
          //Evaluate a state only if it is feasible
          if (env.isValid(nextState) && env.canMove(startState, nextState)) {
            long cost = node.getCost();
            long heuristicCost = 0;
            
            //Calculate costs incurred to reach this state.
            for (CostFunction cf : costFunctions) {
              cost += cf.cost(startState, nextState);
            }
            //Calculate costs that will incur according to heuristics.
            for (CostFunction hcf : heuristicCostFunctions) {
              heuristicCost += hcf.cost(nextState, goalState);
            }
            
            //Generate child node.
            child = new AStarNode(nextState, cost, heuristicCost);

            
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
