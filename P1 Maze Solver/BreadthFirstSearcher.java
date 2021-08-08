//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Maze Solver
// Files: Maze.java, StateFValuePair.java, BreadthFirstSearcher.java, FindPath.java, IO.java,
// AStarSearcher.java, Searcher.java, State.java, Square.java
// Course: CS 540 Fall 2019
//
// Author: Yingjie Shen
// Email: shen92@wisc.edu
// Lecturer's Name: Chuck Dyer
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: N/A
// Partner Email: N/A
// Partner Lecturer's Name: N/A
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: (identify each person and describe their help in detail)
// Online Sources: (identify each URL and describe their assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

  /**
   * Calls the parent class constructor.
   * 
   * @see Searcher
   * @param maze initial maze.
   */
  public BreadthFirstSearcher(Maze maze) {
    // This method initializes the BFSeacher object with a Maze object
    super(maze);
    // test();
  }

  /**
   * Main breadth first search algorithm.
   * 
   * @return true if the search finds a solution, false otherwise.
   */
  public boolean search() {
    // Explored list is a 2D Boolean array that indicates if a state associated with a given
    // position in the maze has already been explored.
    boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
    LinkedList<State> queue = new LinkedList<State>(); // Queue implementing the Frontier list

    int numNodeExpand = 0;
    int maxQueueSize = 1;

    // Insert the start node to the queue
    queue.add(new State(maze.getPlayerSquare(), null, 0, 0));
    explored[maze.getPlayerSquare().X][maze.getPlayerSquare().Y] = true;

    // BFS iterative loop
    while (!queue.isEmpty()) {
      // Expand the current node
      State currentState = queue.pop(); // Get the node to expand
      numNodeExpand++;

      if (currentState.isGoal(this.maze)) {// return true if find a solution
        // Set Searcher class fields
        this.cost = currentState.getGValue();
        this.noOfNodesExpanded = numNodeExpand;
        this.maxDepthSearched = currentState.getDepth();
        this.maxSizeOfFrontier = maxQueueSize;

        // Draw path on maze from S -> G
        currentState = currentState.getParent();
        while (currentState.getParent() != null) {
          this.maze.setOneSquare(currentState.getSquare(), '.');
          currentState = currentState.getParent();
        }
        return true;
      }

      // Get successor states of current node
      ArrayList<State> currentStateSuccessors = currentState.getSuccessors(explored, this.maze);

      // Add all successor states to the queue
      for (int i = 0; i < currentStateSuccessors.size(); i++) {
        State newState = currentStateSuccessors.get(i);
        queue.add(newState);
      }

      // After add successor states of current state, check the max queue size
      if (queue.size() > maxQueueSize) {
        maxQueueSize = queue.size();
      }
    }
    return false;
  }

}
