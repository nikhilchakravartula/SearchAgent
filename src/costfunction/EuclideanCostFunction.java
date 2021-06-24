package costfunction;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Euclidean cost function. Distance = sqrt( (x2-x1)^2 + (y2-y1)^2)
 */
public class EuclideanCostFunction implements CostFunction {

  @Override
  public long cost(State currentState, State nextState) {
    long dis = ((currentState.getX() - nextState.getX()) * (currentState.getX() - nextState.getX())
        + (currentState.getY() - nextState.getY()) * (currentState.getY() - nextState.getY()));
    dis = (long) Math.sqrt(dis);
    return dis;
  }
}
