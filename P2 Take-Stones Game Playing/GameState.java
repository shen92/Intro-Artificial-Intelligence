//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Take Stones
// Files: Helper.java, AlphaBetaPruning.java, TakeStones.java, GameState.java
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
  private int size; // The number of stones
  private boolean[] stones; // Game state: true for available stones, false for taken ones
  private int lastMove; // The last move
  public boolean type; // true: maxPlayer, false: minPlayer

  /**
   * Class constructor specifying the number of stones.
   */
  public GameState(int size) {

    this.size = size;

    // For convenience, we use 1-based index, and set 0 to be unavailable
    this.stones = new boolean[this.size + 1];
    this.stones[0] = false;

    // Set default state of stones to available
    for (int i = 1; i <= this.size; ++i) {
      this.stones[i] = true;
    }

    // Set the last move be -1
    this.lastMove = -1;
  }

  /**
   * Copy constructor
   */
  public GameState(GameState other) {
    this.size = other.size;
    this.stones = Arrays.copyOf(other.stones, other.stones.length);
    this.lastMove = other.lastMove;
  }


  /**
   * This method is used to compute a list of legal moves
   *
   * @return This is the list of state's moves
   */
  public List<Integer> getMoves() {
    List<Integer> validMoves = new ArrayList<>();
    if (this.lastMove == -1) { // if this is the start state
      for (int i = 1; i < (double) size / 2; i++) {
        if (i % 2 == 1) {
          validMoves.add(new Integer(i));
        }
      }
      return validMoves;
    }

    for (int i = 1; i < this.size + 1; i++) {
      if (stones[i] == true && (isMultiple(i) || isFactor(i))) {
        validMoves.add(new Integer(i));
      }
    }
    return validMoves;
  }

  /**
   * This method checks if current number is a multiple of last move
   * 
   * @param int i current stone
   * @return boolean true if current number is a multiple of last move
   */
  private boolean isMultiple(int i) {
    return this.lastMove % i == 0;
  }

  /**
   * This method checks if current number is a factor of last move
   * 
   * @param int i current stone
   * @return boolean true if current number is a factor of last move
   */
  private boolean isFactor(int i) {
    return i % this.lastMove == 0;
  }


  /**
   * This method is used to generate a list of successors using the getMoves() method
   *
   * @return This is the list of state's successors
   */
  public List<GameState> getSuccessors() {
    return this.getMoves().stream().map(move -> {
      GameState state = new GameState(this);
      state.removeStone(move);
      return state;
    }).collect(Collectors.toList());
  }


  /**
   * This method is used to evaluate a game state based on the given heuristic function
   *
   * @return int This is the static score of given state
   */
  public double evaluate() {
    if (this.getSuccessors().size() == 0) { // At an end game state
      return 1.0;
    }
    if (this.stones[1] == true) { // if stone 1 is not taken yet
      return 0.0;
    }
    if (this.lastMove == 1) { // if the last move was 1
      if (this.getSuccessors().size() % 2 == 1) { // if the count is odd
        return 0.5;
      } else {// if the count is even
        return -0.5;
      }
    }
    if (Helper.isPrime(this.lastMove)) {// if last move is a prime
      if (this.getSuccessors().size() % 2 == 1) { // if the count is odd
        return 0.7;
      } else {// if the count is even
        return -0.7;
      }
    } else {
      int largestPrime = Helper.getLargestPrimeFactor(this.lastMove);
      int count = 0;
      List<Integer> successors = this.getMoves();
      for (Integer i : successors) {
        if (stones[i] == true && (largestPrime % i == 0)) {
          count++;
        }
      }
      if (count % 2 == 1) { // if the count is odd
        return 0.6;
      } else {// if the count is even
        return -0.6;
      }
    }
  }

  /**
   * This method is used to take a stone out
   *
   * @param idx Index of the taken stone
   */
  public void removeStone(int idx) {
    this.stones[idx] = false;
    this.lastMove = idx;
  }

  /**
   * These are get/set methods for a stone
   *
   * @param idx Index of the taken stone
   */
  public void setStone(int idx) {
    this.stones[idx] = true;
  }

  public boolean getStone(int idx) {
    return this.stones[idx];
  }

  /**
   * These are get/set methods for lastMove variable
   *
   * @param move Index of the taken stone
   */
  public void setLastMove(int move) {
    this.lastMove = move;
  }

  public int getLastMove() {
    return this.lastMove;
  }

  /**
   * This is get method for game size
   *
   * @return int the number of stones
   */
  public int getSize() {
    return this.size;
  }

  public void setType(int removed) {
    this.type = (removed % 2 == 0);
  }

  public boolean getType() {
    return this.type;
  }

}
