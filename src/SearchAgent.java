import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import costfunction.CostFunction;
import costfunction.EuclideanCostFunction;
import costfunction.IdentityCostFunction;
import costfunction.MoveCostFunction;
import costfunction.MuddinessCostFunction;
import costfunction.SteepnessCostFunction;
import environment.Environment;
import operators.EastOperator;
import operators.NorthEastOperator;
import operators.NorthOperator;
import operators.NorthWestOperator;
import operators.Operator;
import operators.SouthEastOperator;
import operators.SouthOperator;
import operators.SouthWestOperator;
import operators.WestOperator;
import search.BFSSearch;
import search.Search;
import search.UCSSearch;
import search.AStarSearch;
import state.State;
import strategy.SearchContext;

/**
 * @author Nikhil Chakravartula (nchakrav@usc.edu) (nikhilchakravartula@gmail.com)
 * SearchAgent class is the entry point to the application.
 * It is responsible for constructing the required params and then initiating the call to 
 * a search algorithm.
 *
 */
public class SearchAgent {

  public static final String BFS = "BFS";
  public static final String UCS = "UCS";
  public static final String ASTAR = "A*";
  int rows;
  int cols;
  int maxClimbHeight;
  int N;
  int[][] terrain;
  String algorithm;
  ArrayList<State> goalStates;
  State startState;


  public SearchAgent() throws IOException {
    goalStates = new ArrayList<State>();
  }

  /**
   * Reads the required parameters from the input file.
   * An example is given below
   * BFS   --> Algorithm
   * 2 2   --> #cols #rows
   * 0 0   --> Start state x and y coordinates respectively
   * 5     --> Max rock height that wagon can climb
   * 1     --> Number of goal states
   * 1 1   --> Goal state x and y coordinates respectively
   * 0 -10 --> Terrain starts here and continues till end of file
   *-10 -20
   */
  public void readInput() {
    
    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        new FileInputStream("./testcases/input/input2.txt")))) {
      //Read algorithm type
      algorithm = br.readLine();
      
      //Read number of columns and number of rows in the terrain
      String[] tokens = br.readLine().split(" ");
      cols = Integer.parseInt(tokens[0]);
      rows = Integer.parseInt(tokens[1]);
      terrain = new int[rows][cols];

      tokens = br.readLine().split(" ");
      int starty = Integer.parseInt(tokens[0]);
      int startx = Integer.parseInt(tokens[1]);
      startState = new State(startx, starty);
      maxClimbHeight = Integer.parseInt(br.readLine());
      N = Integer.parseInt(br.readLine());
      //Read goal states
      for (int i = 0; i < N; i++) {
        int x, y;
        tokens = br.readLine().split(" ");
        y = Integer.parseInt(tokens[0]);
        x = Integer.parseInt(tokens[1]);
        State c = new State(x, y);
        // System.out.println(c);
        goalStates.add(c);
      }

      //Read terrain entries
      for (int i = 0; i < rows; i++) {
        tokens = br.readLine().split("\\s+");
        for (int j = 0; j < cols; j++) {
          terrain[i][j] = Integer.parseInt(tokens[j]);
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }

  }

  /**
   * Given an ArrayList of search results, writes the result in terms of
   * xstart,ystart x1,y1 x2,y2 x3,y3 xgoal,ygoal into a output file
   * @param searchResult : Sequence of states that represent a path from start to goal.
   * @throws IOException
   */
  public void writeOutput(ArrayList<ArrayList<State>> searchResult) throws IOException {
    PrintStream ps = null;
    StringBuilder sb = null;
    try {
      //Fixed output file for now. Can be accepted as a param if required.
      ps = new PrintStream(new FileOutputStream("output.txt", false));
      sb = new StringBuilder();

      //Write the result into sb, and subsequently to a file.
      for (ArrayList<State> result : searchResult) {
        //No path found from start state to goal state.
        if (result.isEmpty()) {
          sb.append("FAIL\n");
        } else {
          //Path found. Iterate over the path and extract x,y coordinates.
          Iterator<State> itr = result.iterator();
          while (itr.hasNext()) {
            State c = itr.next();
            sb.append(c.getY() + "," + c.getX());
            if (itr.hasNext()) {
              sb.append(" ");
            } else
              sb.append("\n");
          }

        }

      }
      //Delete extra \n at the end.
      sb.deleteCharAt(sb.length() - 1);
      //Write the result to the output file.
      ps.print(sb);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void main(String... strings) throws IOException {
    
    SearchAgent agent = new SearchAgent();
    //Read input file and construct required params.
    agent.readInput();
    
    Environment env = new Environment(agent.terrain, agent.rows, agent.cols, agent.maxClimbHeight);
    
    //By default, include opertors for 8 directions.
    ArrayList<Operator> operators = new ArrayList<Operator>();
    operators.add(new NorthOperator());
    operators.add(new NorthEastOperator());
    operators.add(new EastOperator());
    operators.add(new SouthEastOperator());
    operators.add(new SouthOperator());
    operators.add(new SouthWestOperator());
    operators.add(new WestOperator());
    operators.add(new NorthWestOperator());

    Search search;
    if (agent.algorithm.equalsIgnoreCase(BFS)) {
      ArrayList<CostFunction> costFunctions = new ArrayList<CostFunction>();
      //Cost of moving from current state to next state is a constant. 
      costFunctions.add(new IdentityCostFunction());
      search = new BFSSearch(env, operators, costFunctions);
    } else if (agent.algorithm.equalsIgnoreCase(UCS)) {
      ArrayList<CostFunction> costFunctions = new ArrayList<CostFunction>();
      //For UCS, cost is calculated based on approximate move distance.
      costFunctions.add(new MoveCostFunction());
      search = new UCSSearch(env, operators, costFunctions);
    } else {
      ArrayList<CostFunction> costFunctions = new ArrayList<CostFunction>();
      ArrayList<CostFunction> heuristicCostFunctions = new ArrayList<CostFunction>();
      //For A*, cost is move cost + steepness cost + muddiness cost and heuristic is Euclidean.
      costFunctions.add(new MoveCostFunction());
      costFunctions.add(new SteepnessCostFunction(env));
      costFunctions.add(new MuddinessCostFunction(env));
      heuristicCostFunctions.add(new EuclideanCostFunction());
      search = new AStarSearch(env, operators, costFunctions, heuristicCostFunctions);
    }

    //Strategy pattern.
    SearchContext context = new SearchContext();
    context.setStrategy(search);

    //Search for the goal states.
    ArrayList<ArrayList<State>> searchResults = context.search(agent.startState, agent.goalStates);
    //Write path to a file (output.txt).
    agent.writeOutput(searchResults);

  }
}
