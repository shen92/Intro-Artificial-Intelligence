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

/**
 * A state in the search represented by the (x,y) coordinates of the square and the parent. In other
 * words a (square,parent) pair where square is a Square, parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

  private Square square; // Square of current State
  private State parent; // parent State generate current State

  // Maintain the gValue (the distance from start)
  // You may not need it for the BFS but you will
  // definitely need it for AStar
  private int gValue;

  // States are nodes in the search tree, therefore each has a depth.
  private int depth;

  /**
   * @param square current square
   * @param parent parent state
   * @param gValue total distance from start
   */
  public State(Square square, State parent, int gValue, int depth) {
    this.square = square;
    this.parent = parent;
    this.gValue = gValue;
    this.depth = depth;
  }

  /**
   * This method gets all the successors of the current state
   * 
   * @param visited explored[i][j] is true if (i,j) is already explored
   * @param maze initial maze to get find the neighbors
   * @return all the successors of the current state
   */
  public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
    ArrayList<State> successorsList = new ArrayList<>(); // successors list to return
    // get neighbors in LEFT: not explored, not wall
    if (explored[this.square.X][this.square.Y - 1] == false
        && maze.getSquareValue(this.square.X, this.square.Y - 1) != '%') {
      explored[this.square.X][this.square.Y - 1] = true;
      successorsList.add(new State(new Square(this.getX(), this.getY() - 1), this, this.gValue + 1,
          this.depth + 1));;
    }

    // get neighbors in DOWN: not explored, not wall
    if (explored[this.square.X + 1][this.square.Y] == false
        && maze.getSquareValue(this.square.X + 1, this.square.Y) != '%') {
      explored[this.square.X + 1][this.square.Y] = true;
      successorsList.add(new State(new Square(this.getX() + 1, this.getY()), this, this.gValue + 1,
          this.depth + 1));
    }

    // get neighbors in RIGHT: not explored, not wall
    if (explored[this.square.X][this.square.Y + 1] == false
        && maze.getSquareValue(this.square.X, this.square.Y + 1) != '%') {
      explored[this.square.X][this.square.Y + 1] = true;
      successorsList.add(new State(new Square(this.getX(), this.getY() + 1), this, this.gValue + 1,
          this.depth + 1));
    }

    // get neighbors in UP: not explored, not wall
    if (explored[this.square.X - 1][this.square.Y] == false
        && maze.getSquareValue(this.square.X - 1, this.square.Y) != '%') {
      explored[this.square.X - 1][this.square.Y] = true;
      successorsList.add(new State(new Square(this.getX() - 1, this.getY()), this, this.gValue + 1,
          this.depth + 1));
    }

    return successorsList;
  }

  /**
   * @return x coordinate of the current state
   */
  public int getX() {
    return square.X;
  }

  /**
   * @return y coordinate of the current state
   */
  public int getY() {
    return square.Y;
  }

  /**
   * @param maze initial maze
   * @return true is the current state is a goal state
   */
  public boolean isGoal(Maze maze) {
    if (square.X == maze.getGoalSquare().X && square.Y == maze.getGoalSquare().Y)
      return true;
    return false;
  }

  /**
   * @return the current state's square representation
   */
  public Square getSquare() {
    return square;
  }

  /**
   * @return parent of the current state
   */
  public State getParent() {
    return parent;
  }

  /**
   * You may not need g() value in the BFS but you will need it in A-star search.
   * 
   * @return g() value of the current state
   */
  public int getGValue() {
    return gValue;
  }

  /**
   * @return depth of the state (node)
   */
  public int getDepth() {
    return depth;
  }


}
