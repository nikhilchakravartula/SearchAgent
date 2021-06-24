package state;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * State represents a location in the terrain.
 */
public class State {
  private int x, y;

  public State(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    return 2 * x + 3 * y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof State)) {
      return false;
    }
    State otherObj = (State) obj;
    return this.x == otherObj.x && this.y == otherObj.y;
  }

  @Override
  public String toString() {
    return "(" + this.x + "," + this.y + ")";
  }
}
