package environment;

import state.State;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * Defines the environment for the agent. In this case, environment is a 2D terrain.
 */
public class Environment {
  private final int NROWS;
  private final int NCOLUMNS;
  private int[][] map;
  //maxClimbHeight defines the max altitude an agent can climb in this environment.
  private int maxClimbHeight;

  public Environment(int[][] map, int NROWS, int NCOLUMNS, int maxClimbHeight) {
    this.map = map;
    this.NROWS = NROWS;
    this.NCOLUMNS = NCOLUMNS;
    this.maxClimbHeight = maxClimbHeight;
  }

  public int getNRows() {
    return NROWS;
  }

  public int getNColumns() {
    return NCOLUMNS;
  }

  /**
   * @param state
   * @return true if state is present in the terrain, false otherwise
   */
  public boolean isValid(State state) {
    State terrainState = (State) state;
    return isValid(terrainState.getX(), terrainState.getY());
  }

  /**
   * @param x
   * @param y
   * @return true if (x,y) is in the terrain, false otherwise.
   */
  public boolean isValid(int x, int y) {
    return x >= 0 && y >= 0 && x < NROWS && y < NCOLUMNS;
  }

  /**
   * @param x
   * @param y
   * @param nextX
   * @param nextY
   * @return true if the agent can move from (x,y) to (nextX,nextY).
   */
  public boolean canMove(int x, int y, int nextX, int nextY) {
    int currentHeight = getSteepness(x, y);
    int nextHeight = getSteepness(nextX, nextY);
    return Math.abs(currentHeight - nextHeight) <= maxClimbHeight;

  }

  /**
   * @param currentState
   * @param nextState
   * @return true if the agent can move from currentState to nextState.
   */
  public boolean canMove(State currentState, State nextState) {
    State currentTerrainState = (State) currentState;
    State nextTerrainState = (State) nextState;
    return canMove(currentTerrainState.getX(), currentTerrainState.getY(), nextTerrainState.getX(),
        nextTerrainState.getY());
  }

  /**
   * @param x
   * @param y
   * @return true if (x,y) in the environment is muddy
   */
  public boolean isMuddy(int x, int y) {
    return map[x][y] >= 0;
  }

  /**
   * @param x
   * @param y
   * @return true if (x,y) in the environment is rocky.
   */
  public boolean isRocky(int x, int y) {
    return map[x][y] < 0;
  }

  /**
   * @param x
   * @param y
   * @return the altitude of (x,y) in the environment.
   */
  public int getSteepness(int x, int y) {
    if (isRocky(x, y)) {
      return Math.abs(map[x][y]);
    }
    return 0;
  }

  /**
   * @param x
   * @param y
   * @return the muddiness of (x,y) in the environment.
   */
  public int getMuddiness(int x, int y) {
    if (isMuddy(x, y)) {
      return map[x][y];
    }
    return 0;
  }

  /**
   * @param state
   * @return true is state is muddy, false otherwise.
   */
  public boolean isMuddy(State state) {
    return map[state.getX()][state.getY()] >= 0;
  }

  /**
   * @param state
   * @return true if state is rocky
   */
  public boolean isRocky(State state) {
    return map[state.getX()][state.getY()] < 0;
  }

  public int at(State state) {
    return map[state.getX()][state.getY()];
  }

  /**
   * @param state
   * @return steepness of state.
   */
  public int getSteepness(State state) {
    return getSteepness(state.getX(), state.getY());
  }

  /**
   * @param state
   * @return muddiness of state
   */
  public int getMuddiness(State state) {
    return getMuddiness(state.getX(), state.getY());
  }

  @Override
  public String toString() {
    String printString = "TERRAIN: \n NROWS " + NROWS + " NCOLUMNS " + NCOLUMNS + "\n";
    for (int i = 0; i < NROWS; i++) {
      for (int j = 0; j < NCOLUMNS; j++) {
        printString += map[i][j] + " ";
      }
      printString += "\n";
    }
    return printString;
  }

}
